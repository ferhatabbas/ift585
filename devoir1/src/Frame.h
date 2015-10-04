#pragma once

#include "Packet.h"

enum class FrameKind
{
	data,
	ack,
	nak
};

struct Frame
{
	FrameKind kind;
	int seq;
	int ack;
	Packet info;
};
