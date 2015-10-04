#pragma once

#include <vector>
#include <agents.h>

using namespace std;
using namespace concurrency;

class PhysicalLayer : public agent
{
private:
	ISource<vector<bool>>& fromDataLink1;
	ITarget<vector<bool>>& toDataLink1;
	ISource<vector<bool>>& fromDataLink2;
	ITarget<vector<bool>>& toDataLink2;

	vector<bool> v;

public:
	PhysicalLayer(ISource<vector<bool>>& fromDataLink1, ITarget<vector<bool>>& toDataLink1, ISource<vector<bool>>& fromDataLink2, ITarget<vector<bool>>& toDataLink2) :
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
			auto selector = make_choice(&fromDataLink1, &fromDataLink2);
			int index = receive(selector);
			switch (index)
			{
			case 0:
				v = selector.value<vector<bool>>();
				asend(toDataLink2, v);
				break;
			case 1:
				v = selector.value<vector<bool>>();
				asend(toDataLink1, v);
				break;
			}
		}
		done();
	}
};
