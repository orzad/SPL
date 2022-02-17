CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -lboost_thread -pthread

all: EchoClient
	g++ -o bin/echoExample bin/connectionHandler.o bin/echoClient.o bin/readFromKeyboard.o bin/readFromSocket.o $(LDFLAGS)

EchoClient: bin/connectionHandler.o bin/echoClient.o bin/readFromKeyboard.o bin/readFromSocket.o
	
bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/echoClient.o: src/echoClient.cpp
	g++ $(CFLAGS) -o bin/echoClient.o src/echoClient.cpp

bin/readFromKeyboard.o: src/readFromKeyboard.cpp
	g++ $(CFLAGS) -o bin/readFromKeyboard.o src/readFromKeyboard.cpp

bin/readFromSocket.o: src/readFromSocket.cpp
	g++ $(CFLAGS) -o bin/readFromSocket.o src/readFromSocket.cpp
.PHONY: clean
clean:
	rm -f bin/*
