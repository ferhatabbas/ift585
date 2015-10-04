#pragma once

#include <vector>

using namespace std;

namespace Hamming
{
	bool isPow2(int n)
	{
		return n && ((n & (n - 1)) == 0);
	}

	bool hasPow2(int n, int pow2)
	{
		return (n & pow2) == pow2;
	}

	/////////////////////////////////////////////////////
	// init section	
	void initIndex(int*& dataIndex, const int oldDataSize)
	{
		for (int i = 0; i < oldDataSize; ++i)
			dataIndex[i] = 0;
	}

	//////////////////////////////////////////////////////////
	// main operation functions
	void parityBit(int oldDataSize, vector<int>& parityBitIndex, int*& dataIndex, int& paritySize)
	{
		int res = 2, compData = 0, cur = 1;
		paritySize = 0;
		while (compData < oldDataSize)
		{
			if (isPow2(cur))
			{
				parityBitIndex.push_back(cur);
				++paritySize;
			}
			else
			{
				dataIndex[compData] = cur;
				++compData;
			}
			++cur;
		}
	}

	void setParityVal(vector<bool>& v, int*& dataIndex, const vector<int>& parityBitIndex, const int paritySize, const int oldDataSize)
	{
		int res, indexD, indexP;
		for (int i = 0; i < paritySize; ++i)
		{
			res = 0;
			for (int j = 0; j < oldDataSize; ++j)
			{
				indexD = dataIndex[j] - 1;
				if (hasPow2(v[j], parityBitIndex[i]))
					res = res + v[indexD];
			}
			indexP = parityBitIndex[i] - 1;
			if (res % 2 == 0)
				v[indexP] = 0;
			else if (res % 2 == 1)
				v[indexP] = 1;
		}
	}

	// resize data and set data bit to real index
	void resizeV(vector<bool>& v, int* const& dataIndex, const int paritySize, const int oldDataSize)
	{
		vector<bool> dataValCopy(v);
		int index;
		v.resize(paritySize + oldDataSize);
		for (int j = 0; j < oldDataSize; ++j)
		{
			index = dataIndex[j] - 1;
			v[index] = dataValCopy[j];
		}
	}

	void removeParity(vector<bool>& v)
	{
		vector<bool>::iterator it = v.begin();
		int k = 1;
		int finalDataSize = v.size();
		while (k <= finalDataSize)
		{
			if (isPow2(k))
				it = v.erase(it);
			else
				++it;
			++k;
		}
	}

	void encode(vector<bool>& v)
	{
		int paritySize;
		int oldDataSize = v.size();
		vector<int> parityBitIndex;
		int* dataIndex = new int[oldDataSize];
		initIndex(dataIndex, oldDataSize);

		// find parity bit and data bit index 
		parityBit(oldDataSize, parityBitIndex, dataIndex, paritySize);

		// resize data vector and put data bits to their final place(index) in the final encoded data
		resizeV(v, dataIndex, paritySize, oldDataSize);

		// calculate all parity bit values by checking for each parity bit, if the sum of all of data bit related, is even or not
		setParityVal(v, dataIndex, parityBitIndex, paritySize, oldDataSize);
		delete[] dataIndex;
	}

	void decode(vector<bool>& v)
	{
		vector<bool> dataCopy = v; // une copie de la donnee recue
		int errorSyndrom = 0;
		int k = 0; // k est le numero du bit de parite, ex: si c'est le bit de parite 1 ou 2
		removeParity(v);
		encode(v);

		for (int j = 0; j < v.size(); j++)
		{
			if (isPow2(j + 1))
			{
				if (dataCopy[j] != v[j]) errorSyndrom = errorSyndrom + pow(2, k);
				++k;
			}
		}
		if (errorSyndrom == 0)
		{
			v = dataCopy;
			removeParity(v);
		}
		else
		{
			dataCopy[errorSyndrom - 1] = !dataCopy[errorSyndrom - 1];
			v = dataCopy;
			removeParity(v);
		}
	}
}
