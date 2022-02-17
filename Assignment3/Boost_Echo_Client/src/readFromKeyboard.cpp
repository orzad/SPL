//
// Created by RUBIN on 1/1/22.
//

#include <mutex>
#include <vector>
#include "../include/connectionHandler.h"
#include "../include/readFromKeyboard.h"

#include <thread>

using namespace std;

readFromKeyboard::readFromKeyboard(ConnectionHandler &CH, std::mutex &mutex): CH(CH),mutex(mutex) {}

void readFromKeyboard::operator()() {

    while(CH.isConnected()) {
        const short SIZE_OF_BUFFER = 1024;
        std::vector<std::string> input= std::vector<std::string>();
        char buffer[SIZE_OF_BUFFER];
        std::cin.getline(buffer, SIZE_OF_BUFFER);
        std::string line(buffer);
        int i = line.find_first_of(' ');
        input.push_back(line.substr(0, i));
        input.push_back(line.substr(i + 1, line.length()));

        std::vector<std::string> cases = {"REGISTER"
                                          ,"LOGIN"
                                          , "LOGOUT"
                                          , "FOLLOW"
                                          ,"POST"
                                          , "PM"
                                          , "LOGSTAT"
                                          , "STAT"
                                          , "BLOCK"};
        int index = -1;
        int c=0;
        while(index == -1 && (unsigned) c < cases.size()){
            if(cases[c]==input[0]){
                index = c;
            }
            c++;
        }

        std::string output;

        switch (index) {
            case 0: {
                output = input[1];
                output+=";";
                output+="01";
                CH.sendLine(output);
                break;
            }
            case 1:{
                output = input[1] +";";
                output+="02";
                CH.sendLine(output);
                break;
            }
            case 2:{
                output = input[1] +";";
                output+="03";
                CH.sendLine(output);
                break;
            }
            case 3:{
                output = input[1];
                output = output +";";
                output+="04";
                CH.sendLine(output);
                break;
            }
            case 4:{
                output = input[1];
                output = output + ";";
                output+="05";
                CH.sendLine(output);
                break;
            }
            case 5:{
                output = input[1];
                output += " 070120221200";
                output = output + ";";
                output+="06";
                CH.sendLine(output);
                break;
            }
            case 6:{
                output = input[1];
                output+=";07";
                CH.sendLine(output);
                break;
            }
            case 7:{
                int i = 0;
                while (i != -1) {
                    i = input[1].find_first_of(' ');
                    output = output + input[1].substr(0, i) + '|';
                    input[1] = input[1].substr(i + 1, input[1].length());
                }
                output = output+ '|'+ ";";
                output+="08";
                CH.sendLine(output);
                break;
            }
            case 8:{
                output= input[1]+ ";"+"12";
                CH.sendLine(output);
                break;
            }
        }
    }
}


