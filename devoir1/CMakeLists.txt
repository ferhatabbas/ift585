cmake_minimum_required(VERSION 3.1)
PROJECT(tp1-ift585)
#SET(CMAKE_CXX_COMPILER "clang++")
SET(CMAKE_CXX_FLAGS "-Wall -std=c++14 -g")
FIND_PACKAGE(Threads REQUIRED)
file(GLOB srv_tp1 
    "src/*.h"
    "src/*.cpp"
    )
ADD_EXECUTABLE(srv_cpy ${srv_tp1})
TARGET_LINK_LIBRARIES(srv_cpy ${CMAKE_THREAD_LIBS_INIT})
