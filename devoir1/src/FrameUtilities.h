#pragma once

#include <cstring>
#include <vector>

#include "Frame.h"

using namespace std;

namespace FrameUtilities
{
	bool isFrameSize(vector<bool>& v)
	{
		int frameSize = sizeof(Frame) * 8;
		int bitsSize = v.size();
		if (bitsSize == frameSize)
			return true;
		else
			return false;
	}

	void toChar(Frame frame, char*& buffer)
	{
		buffer[sizeof(frame)];
		memcpy(buffer, &frame, sizeof(frame));
	}

	void toBits(vector<bool>& data, char mychar)
	{
		for (int i = 7; i >= 0; --i)
		{
			data.push_back((1 << i) & mychar);
		}
	}

	void toBits(vector<bool>& data, char*& arrayOfchar, int frameSize)
	{
		for (int i = 0; i < frameSize; ++i)
		{
			toBits(data, arrayOfchar[i]);
		}
	}

	char toChar(vector<bool>& data)
	{
		int charValue = 0, j = 0, size = data.size();
		for (int i = 7; i >= 0; --i)
		{
			if (data[j] == 1)
				charValue = charValue | (1u << i);
			//charValue += data[i] * pow(2, j);
			j++;
		}
		char myChar = charValue;
		data.erase(data.begin(), data.begin() + 8);
		return myChar;
	}

	vector<char> toVectorChar(vector<bool>& data)
	{
		vector <char> charVector;
		for (int i = (data.size() / 8); i > 0; --i)
		{
			charVector.push_back(toChar(data));
		}
		return charVector;
	}

	Frame toFrame(vector<char> v)
	{
		Frame frame;
		memcpy(&frame, &v[0], sizeof(char) * v.size());
		return frame;
	}

	vector<bool> toBits(Frame frame)
	{
		int frameSize = sizeof(frame);
		char* arrayOfchar = new char[frameSize];
		vector<bool> data;
		toChar(frame, arrayOfchar);
		toBits(data, arrayOfchar, frameSize);
		delete[] arrayOfchar;
		arrayOfchar = NULL;
		return data;
	}

	Frame toFrame(vector<bool> data)
	{
		Frame frame = toFrame(toVectorChar(data));
		return frame;
	}
}
