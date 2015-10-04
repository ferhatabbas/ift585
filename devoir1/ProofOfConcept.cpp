#include <iostream>
#include <vector>
using namespace std;

void encode(vector<bool>&);
void decode(vector<bool>&);
bool isPow2(int);
bool hasPow2(int, int);
int pos(vector<bool>&, vector<bool>::iterator);
int pos(vector<bool>&, vector<bool>::reverse_iterator);
int toInt(vector<bool>&);

int main() {
	vector<bool> message{ 1, 0, 0, 0, 0, 0, 1 };
	encode(message);
	message[4].flip();
	decode(message);
}

void encode(vector<bool>& v) {
	for (auto p = v.begin(); p != v.end(); ++p) {
		if (isPow2(pos(v, p)))
			p = v.insert(p, 0);
	}
	for (auto p = v.begin(); p != v.end(); ++p) {
		if (isPow2(pos(v, p))) {
			for (auto q = v.begin(); q != v.end(); ++q) {
				if (hasPow2(pos(v, q), (pos(v, p))))
					*p = *p ^ *q;
			}
		}
	}
}

void decode(vector<bool>& v) {
	vector<bool> syndrome;
	for (auto p = v.begin(); p != v.end(); ++p) {
		if (isPow2(pos(v, p))) {
			bool check = 0;
			for (auto q = v.begin(); q != v.end(); ++q) {
				if (hasPow2(pos(v, q), (pos(v, p))))
					check = check ^ *q;
			}
			syndrome.push_back(check);
		}
	}
	if (toInt(syndrome))
		v[toInt(syndrome) - 1].flip();
	for (auto p = v.rbegin(); p != v.rend();) {
		if (isPow2(pos(v, p)))
			p = vector<bool>::reverse_iterator(v.erase(--p.base()));
		else ++p;
	}
}

bool isPow2(int n) {
	return n && ((n & (n - 1)) == 0);
}

bool hasPow2(int n, int pow2) {
	return (n & pow2) == pow2;
}

int pos(vector<bool>& v, vector<bool>::iterator p) {
	return distance(v.begin(), p) + 1;
}

int pos(vector<bool>& v, vector<bool>::reverse_iterator p) {
	return distance(p, v.rend());
}

int toInt(vector<bool>& v) {
	int n = 0;
	for (auto p = v.begin(); p != v.end(); ++p) {
		if (*p) n += pow(2, pos(v, p) - 1);
	}
	return n;
}
