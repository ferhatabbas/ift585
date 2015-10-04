#include <iostream>
#include <agents.h>

using namespace std;
using namespace concurrency;

const int MAX_PKT = 1024;

enum class FrameKind
{ 
	data,
	ack,
	nak
};

struct Packet
{
	char data[MAX_PKT];
};

struct Frame
{
	FrameKind kind;
	int seq;
	int ack;
	Packet info;
};

class DataLinkLayer : public agent
{
private:
	ISource<Packet>& fromNetwork;
	ITarget<Packet>& toNetwork;
	ISource<Frame>& fromPhysical;
	ITarget<Frame>& toPhysical;

public:
	DataLinkLayer(ISource<Packet>& fromNetwork, ITarget<Packet>& toNetwork, ISource<Frame>& fromPhysical, ITarget<Frame>& toPhysical) :
		fromNetwork(fromNetwork),
		toNetwork(toNetwork),
		fromPhysical(fromPhysical),
		toPhysical(toPhysical)
	{
	}

	void run()
	{
		while (true)
		{
			Packet packet;
			Frame frame;
			auto selector = make_choice(&fromNetwork, &fromPhysical);
			int index = receive(selector);
			switch (index)
			{
			case 0:
				packet = selector.value<Packet>();
				frame.info = packet;
				asend(toPhysical, frame);
				break;
			case 1:
				frame = selector.value<Frame>();
				packet = frame.info;
				asend(toNetwork, packet);
				break;
			}
		}
		done();
	}
};

class PhysicalLayer : public agent
{
private:
	ISource<Frame>& fromDataLink1;
	ITarget<Frame>& toDataLink1;
	ISource<Frame>& fromDataLink2;
	ITarget<Frame>& toDataLink2;

public:
	PhysicalLayer(ISource<Frame>& fromDataLink1, ITarget<Frame>& toDataLink1, ISource<Frame>& fromDataLink2, ITarget<Frame>& toDataLink2) :
		fromDataLink1(fromDataLink1),
		toDataLink1(toDataLink1),
		fromDataLink2(fromDataLink2),
		toDataLink2(toDataLink2)
	{
	}

	void run()
	{
		while (true)
		{
			Frame frame;
			auto selector = make_choice(&fromDataLink1, &fromDataLink2);
			int index = receive(selector);
			switch (index)
			{
			case 0:
				frame = selector.value<Frame>();
				asend(toDataLink2, frame);
				break;
			case 1:
				frame = selector.value<Frame>();
				asend(toDataLink1, frame);
				break;
			}
		}
		done();
	}
};

int main()
{
	unbounded_buffer<Packet> toStation1;
	unbounded_buffer<Packet> fromStation1;
	unbounded_buffer<Packet> toStation2;
	unbounded_buffer<Packet> fromStation2;

	unbounded_buffer<Frame> buffer1;
	unbounded_buffer<Frame> buffer2;
	unbounded_buffer<Frame> buffer3;
	unbounded_buffer<Frame> buffer4;

	DataLinkLayer station1(toStation1, fromStation1, buffer1, buffer2);
	DataLinkLayer station2(toStation2, fromStation2, buffer3, buffer4);
	PhysicalLayer transmissionMedia(buffer2, buffer1, buffer4, buffer3);
	
	station1.start();
	station2.start();
	transmissionMedia.start();

	for (int message = 0; message < 10; ++message)
	{
		Packet packet;
		packet.data[0] = message;
		asend(toStation1, packet);
	}

	while (true)
	{
		Packet packet;
		packet = receive(fromStation2);
		int message = packet.data[0];
		cout << message << '\n';
	}

	agent* agents[3] = { &station1, &station2, &transmissionMedia };
	agent::wait_for_all(3, agents);
}
