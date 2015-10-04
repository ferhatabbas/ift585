#!/bin/bash

if [ ! -d "cmake" ];
then
    mkdir cmake
fi

if [ "$1" == "clean" ];
then
    rm -rf cmake/*
else    
    cd cmake
    cmake ../
    make
fi

cp srv_cpy ../
