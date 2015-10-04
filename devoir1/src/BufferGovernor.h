#pragma once

#include <agents.h>

#include "Signal.h"

using namespace concurrency;

class BufferGovernor
{
private:
	int capacity;
	int phase;
	unbounded_buffer<Signal> completedItems;

public:
	BufferGovernor(int capacity) :
		phase(0),
		capacity(capacity)
	{
	}

	void freeBufferSlot()
	{
		send(completedItems, Signal());
	}

	void waitForAvailableBufferSlot()
	{
		if (phase < capacity)
			++phase;
		else
			receive(completedItems);
	}

	void WaitForEmptyBuffer()
	{
		while (phase > 0)
		{
			--phase;
			receive(completedItems);
		}
	}

private:
	// Disable copy constructor and assignment.
	BufferGovernor(const BufferGovernor&);
	BufferGovernor const & operator=(BufferGovernor const&);
};
