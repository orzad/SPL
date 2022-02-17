cmake_minimum_required(VERSION 3.20)
project(Assignment1)

set(CMAKE_CXX_STANDARD 14)

add_executable(Assignment1 src/main.cpp src/Customer.cpp include/Customer.h src/Studio.cpp include/Studio.h src/Trainer.cpp include/Trainer.h src/Workout.cpp include/Workout.h src/Action.cpp include/Action.h)
