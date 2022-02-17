//
// Created by RUBIN on 1/1/22.
//

#ifndef BOOST_ECHO_CLIENT_READFROMKEYBOARD_H
#define BOOST_ECHO_CLIENT_READFROMKEYBOARD_H
#include "connectionHandler.h"
#include "readFromKeyboard.h"
#include <mutex>

class readFromKeyboard {

private:
    ConnectionHandler& CH;
    std::mutex &mutex;
public:
    readFromKeyboard(ConnectionHandler &CH,std::mutex &mutex);
    void operator()();
};


#endif //BOOST_ECHO_CLIENT_READFROMKEYBOARD_H
