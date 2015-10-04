#pragma once

#include <vector>
#include <agents.h>

#include "Config.h"
#include "Signal.h"
#include "Packet.h"
#include "Frame.h"
#include "FrameUtilities.h"
#include "Hamming.h"

using namespace std;
using namespace concurrency;

class DataLinkLayer : public agent
{
private:
	ISource<Packet>& fromNetwork;
	ITarget<Packet>& toNetwork;
	ISource<vector<bool>>& fromPhysical;
	ITarget<vector<bool>>& toPhysical;
	ITarget<Signal>& ready;

	Config *cfg;
	
	enum class Event
	{
		cksumErr,
		timeout1,
		timeout2,
		timeout3,
		timeout4,
		ackTimeout
	};

	unbounded_buffer<Event> eventQueue;

	int timerInterval;
	
	static const int MAX_SEQ = 7;
	static const int NR_BUFS_OUT = (MAX_SEQ + 1) / 2;
	int NR_BUFS_IN;
	
	int ackExpected;
	int nextFrameToSend;
	int frameExpected;
	int tooFar;
	Frame r;
	Packet outBuf[NR_BUFS_OUT];
	Packet* inBuf;
	bool* arrived;
	int nbuffered;
	bool noNak;
	int oldestFrame;

	int frameBeingTimedByTimer1;
	int frameBeingTimedByTimer2;
	int frameBeingTimedByTimer3;
	int frameBeingTimedByTimer4;

	vector<bool> v;

	void inc(int& k)
	{
		if (k < MAX_SEQ)
			++k;
		else
			k = 0;
	}

	bool between(int a, int b, int c)
	{
		if (((a <= b) && (b < c)) || ((c < a) && (a <= b)) || ((b < c) && (c < a)))
			return true;
		else
			return false;
	}

	void sendFrame(FrameKind fk, int frameNr, int frameExpected, Packet buffer[], timer<Event>& timer1, timer<Event>& timer2, timer<Event>& timer3, timer<Event>& timer4, timer<Event>& ackTimer)
	{
		Frame s;
		s.kind = fk;
		if (fk == FrameKind::data)
			s.info = buffer[frameNr % NR_BUFS_OUT];
		s.seq = frameNr;
		s.ack = (frameExpected + MAX_SEQ) % (MAX_SEQ + 1);
		if (fk == FrameKind::nak)
			noNak = false;

		v = FrameUtilities::toBits(s);
		Hamming::encode(v);
		asend(toPhysical, v);

		if (fk == FrameKind::data)
		{
			switch (frameNr % NR_BUFS_OUT)
			{
			case 0:
				timer1.start();
				frameBeingTimedByTimer1 = frameNr;
				break;
			case 1:
				timer2.start();
				frameBeingTimedByTimer2 = frameNr;
				break;
			case 2:
				timer3.start();
				frameBeingTimedByTimer3 = frameNr;
				break;
			case 3:
				timer4.start();
				frameBeingTimedByTimer4 = frameNr;
				break;
			}
		}
		ackTimer.pause();
	}

	bool isFrameValid(Frame f)
	{
		if ((f.ack >= 0) && (f.ack <= MAX_SEQ) &&
			(f.seq >= 0) && (f.seq <= MAX_SEQ) &&
			((f.kind == FrameKind::data) || (f.kind == FrameKind::ack) || (f.kind == FrameKind::nak)))
			return true;
		else
			return false;
	}

public:
	DataLinkLayer(ISource<Packet>& fromNetwork, ITarget<Packet>& toNetwork, ISource<vector<bool>>& fromPhysical, ITarget<vector<bool>>& toPhysical, ITarget<Signal>& ready, int timerInterval, Config *c) :
		fromNetwork(fromNetwork),
		toNetwork(toNetwork),
		fromPhysical(fromPhysical),
		toPhysical(toPhysical),
		ready(ready),
		cfg(c)
	{	
		this->timerInterval = timerInterval;

		if (cfg->GetProp<int>(Config::ConfVal::SREP))
			NR_BUFS_IN = (MAX_SEQ + 1) / 2;
		else
			NR_BUFS_IN = 1;

		inBuf = new Packet[NR_BUFS_IN];
		arrived = new bool[NR_BUFS_IN];
		
		for (int i = 0; i < NR_BUFS_OUT; ++i)
			asend(ready, Signal());
		ackExpected = 0;
		nextFrameToSend = 0;
		frameExpected = 0;
		tooFar = NR_BUFS_IN;
		nbuffered = 0;
		for (int i = 0; i < NR_BUFS_IN; ++i)
			arrived[i] = false;
		noNak = true;
		oldestFrame = MAX_SEQ + 1;
	}

	~DataLinkLayer()
	{
		delete inBuf;
		delete arrived;
	}

	void run()
	{	
		timer<Event> timer1(timerInterval, Event::timeout1, &eventQueue, true);
		timer<Event> timer2(timerInterval, Event::timeout2, &eventQueue, true);
		timer<Event> timer3(timerInterval, Event::timeout3, &eventQueue, true);
		timer<Event> timer4(timerInterval, Event::timeout4, &eventQueue, true);

		timer<Event> ackTimer(timerInterval, Event::ackTimeout, &eventQueue, true);
		
		while (true)
		{
			auto selector = make_choice(&fromNetwork, &fromPhysical, &eventQueue);
			int index = receive(selector);
			switch (index)
			{
			case 0:
				++nbuffered;
				outBuf[nextFrameToSend % NR_BUFS_OUT] = selector.value<Packet>();
				sendFrame(FrameKind::data, nextFrameToSend, frameExpected, outBuf, timer1, timer2, timer3, timer4, ackTimer);
				inc(nextFrameToSend);
				break;
			case 1:
				v = selector.value<vector<bool>>();
				Hamming::decode(v);
				r = FrameUtilities::toFrame(v);

				if (r.kind == FrameKind::data)
				{
					if ((r.seq != frameExpected) && noNak)
						sendFrame(FrameKind::nak, 0, frameExpected, outBuf, timer1, timer2, timer3, timer4, ackTimer);
					else
						ackTimer.start();
					if (between(frameExpected, r.seq, tooFar) && (arrived[r.seq % NR_BUFS_IN] == false))
					{
						arrived[r.seq % NR_BUFS_IN] = true;
						inBuf[r.seq % NR_BUFS_IN] = r.info;
						while (arrived[frameExpected % NR_BUFS_IN])
						{
							asend(toNetwork, inBuf[frameExpected % NR_BUFS_IN]);
							noNak = true;
							arrived[frameExpected % NR_BUFS_IN] = false;
							inc(frameExpected);
							inc(tooFar);
							ackTimer.start();
						}
					}
				}

				if ((r.kind == FrameKind::nak) && between(ackExpected, (r.ack + 1) % (MAX_SEQ + 1), nextFrameToSend))
					sendFrame(FrameKind::data, (r.ack + 1) % (MAX_SEQ + 1), frameExpected, outBuf, timer1, timer2, timer3, timer4, ackTimer);

				while (between(ackExpected, r.ack, nextFrameToSend))
				{
					--nbuffered;
					switch (ackExpected % NR_BUFS_OUT)
					{
					case 0:
						timer1.pause();
						break;
					case 1:
						timer2.pause();
						break;
					case 2:
						timer3.pause();
						break;
					case 3:
						timer4.pause();
						break;
					}
					inc(ackExpected);
					asend(ready, Signal());
				}
				break;
			case 2:
				Event event = selector.value<Event>();
				switch (event)
				{
				case Event::cksumErr:
					if (noNak)
						sendFrame(FrameKind::nak, 0, frameExpected, outBuf, timer1, timer2, timer3, timer4, ackTimer);
					break;
				case Event::timeout1:
					oldestFrame = frameBeingTimedByTimer1;
					sendFrame(FrameKind::data, oldestFrame, frameExpected, outBuf, timer1, timer2, timer3, timer4, ackTimer);
					break;
				case Event::timeout2:
					oldestFrame = frameBeingTimedByTimer2;
					sendFrame(FrameKind::data, oldestFrame, frameExpected, outBuf, timer1, timer2, timer3, timer4, ackTimer);
					break;
				case Event::timeout3:
					oldestFrame = frameBeingTimedByTimer3;
					sendFrame(FrameKind::data, oldestFrame, frameExpected, outBuf, timer1, timer2, timer3, timer4, ackTimer);
					break;
				case Event::timeout4:
					oldestFrame = frameBeingTimedByTimer4;
					sendFrame(FrameKind::data, oldestFrame, frameExpected, outBuf, timer1, timer2, timer3, timer4, ackTimer);
					break;
				case Event::ackTimeout:
					sendFrame(FrameKind::ack, 0, frameExpected, outBuf, timer1, timer2, timer3, timer4, ackTimer);
					break;
				}
				break;
			}
		}
		done();
	}
};
