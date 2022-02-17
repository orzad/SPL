all: bin/main

check: bin/main
	valgrind --leak-check=yes bin/main

run: bin/main
	bin/main

assembly: src/compileTimeStack.cpp
	g++ -S -O0 src/compileTimeStack.cpp

bin/main: bin/main.o
	@echo 'Linking'
	g++ -o bin/main bin/main.o


bin/main.o: src/main.cpp
	g++ -g -Wall -O0 -Weffc++ -std=c++11 -c -Iinclude  -o  bin/main.o src/main.cpp
	# g++ -g -Wall -O0 -Weffc++ -std=c++11 -c -fno-stdarg-opt -fno-ipa-icf -fstack-reuse=none -Iinclude -fno-stack-protector -o  bin/main.o src/main.cpp

clean:
	@echo 'Cleaning'
	rm -f bin/*

