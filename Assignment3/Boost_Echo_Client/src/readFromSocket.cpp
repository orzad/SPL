//
// Created by RUBIN on 1/3/22.
//

#include <vector>
#include "../include/readFromSocket.h"
#include "../include/connectionHandler.h"
#include <mutex>

using std::string;
using std::cout;
using std::endl;

readFromSocket::readFromSocket(ConnectionHandler &CH, std::mutex &mutex):CH(CH),mutex(mutex) {}

void readFromSocket::operator()() {
    while (CH.isConnected()) {
        decode();
    }
}

short readFromSocket::bytesToShorts(char* bytes){
    short result = (short)((bytes[0] & 0xff) << 8);
    result += (short)(bytes[1] & 0xff);
    return result;
}

void readFromSocket::disconnect() {
    CH.disconnect();
}

std::string readFromSocket::decode() {
    string line = "";
    CH.getLine(line);
   std::cout<<"Received message: " +line<<std::endl;

    if(line.substr(0,2) =="09") {
        if(line.substr(2,2)=="00"){
            cout<<"NOTIFICATION public "<<line.substr(4,line.size()-5)<<endl;
        }
        else cout<<"NOTIFICATION PM "<<line.substr(4,line.size()-5)<<endl;
    }
    else if(line.substr(0,2).compare("10")==0){
        if(line.substr(2,2).compare("12")==0){
            cout<<"ACK "<<line.substr(4,line.size()-5)<<endl;
        }
        else if((line.substr(2,2) == "07") | (line.substr(2,2) == "08")  ){
            cout<<"ACK "<<line.substr(4,line.size()-5)<<endl;
        }
        else {
            cout << "ACK "  << line.substr(2, 2) << endl;
        }
        if(line.substr(2, 2).compare("03")==0) {
            disconnect();
            std::cout<<"disconnected"<<std::endl;
        }
    }
    else cout<<"ERROR "<<line.substr(2,line.size()-3)<<endl;

    return line;
}

