#include <iostream>
#include <vector>
#include <agents.h>

#include "Config.h"
#include "Signal.h"
#include "Packet.h"
#include "PhysicalLayer.h"
#include "DataLinkLayer.h"

using namespace std;
using namespace concurrency;

int main()
{
	Config cfg("tp1.conf");
	cfg.ReadFile();
	unbounded_buffer<Packet> toStation1;
	unbounded_buffer<Packet> fromStation1;
	unbounded_buffer<Packet> toStation2;
	unbounded_buffer<Packet> fromStation2;

	unbounded_buffer<Signal> station1Ready;
	unbounded_buffer<Signal> station2Ready;
	
	unbounded_buffer<vector<bool>> buffer1;
	unbounded_buffer<vector<bool>> buffer2;
	unbounded_buffer<vector<bool>> buffer3;
	unbounded_buffer<vector<bool>> buffer4;

	/*BufferGovernor governor1(Config::transmissionMediaLimit);
	BufferGovernor governor2(Config::transmissionMediaLimit);
	BufferGovernor governor3(Config::transmissionMediaLimit);
	BufferGovernor governor4(Config::transmissionMediaLimit);*/

	DataLinkLayer station1(toStation1, fromStation1, buffer1, buffer2, station1Ready, cfg.GetProp<int>(Config::ConfVal::TST1)/*Config::station1TimerInterval*/, &cfg);
	DataLinkLayer station2(toStation2, fromStation2, buffer3, buffer4, station2Ready, cfg.GetProp<int>(Config::ConfVal::TST2)/*Config::station2TimerInterval*/, &cfg);
	PhysicalLayer transmissionMedia(buffer2, buffer1, buffer4, buffer3);

	station1.start();
	station2.start();
	transmissionMedia.start();

	for (int message = 0; message < 100; ++message)
	{
		receive(station1Ready);
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
