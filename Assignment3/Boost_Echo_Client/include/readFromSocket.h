//
// Created by RUBIN on 1/3/22.
//

#ifndef BOOST_ECHO_CLIENT_READFROMSOCKET_H
#define BOOST_ECHO_CLIENT_READFROMSOCKET_H

#include "connectionHandler.h"
#include <mutex>

class readFromSocket{

private:
    ConnectionHandler& CH;
    std::mutex &mutex;
public:
    readFromSocket(ConnectionHandler &CH,std::mutex &mutex);
    void operator()();
    void disconnect();
    std::string decode();
    short bytesToShorts(char* bytes);
};

#endif //BOOST_ECHO_CLIENT_READFROMSOCKET_H
