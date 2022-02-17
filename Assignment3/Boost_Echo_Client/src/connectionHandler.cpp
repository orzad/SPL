#include "../include/connectionHandler.h"
#include "../include/connectionHandler.h"


using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;
 
ConnectionHandler::ConnectionHandler(string host, short port): host_(host), port_(port), io_service_(), socket_(io_service_),connected(false){
}
    
ConnectionHandler::~ConnectionHandler() {
    close();
}

bool ConnectionHandler::isConnected(){
    return connected;
}

void ConnectionHandler::disconnect (){
    connected = false;
}
 
bool ConnectionHandler::connect() {
    std::cout << "Starting connect to " 
        << host_ << ":" << port_ << std::endl;
    try {
		tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_); // the server endpoint
		boost::system::error_code error;
		socket_.connect(endpoint, error);
		if (error)
			throw boost::system::system_error(error);
    }
    catch (std::exception& e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    connected = true;
    return true;
}
 
bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp ) {
                tmp += socket_.read_some(boost::asio::buffer(bytes + tmp, bytesToRead - tmp), error);
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp ) {
			tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getLine(std::string& line) {

    return getFrameAscii(line, ';');
}

bool ConnectionHandler::sendLine(std::string& line) {
    return sendFrameAscii(line, ';');
}

bool ConnectionHandler::getFrameAscii(std::string& frame, char delimiter) {

    int i = 0;
    char c;
    // Stop when we encounter the null character.
    // Notice that the null character is not appended to the frame string.
    try {
        do{
            if(i<2){
                char d[2];
                getBytes(d, 2);
                //string  num = std::to_string(bytesToShort(d));
                frame.append(d);
            }
            else {
                getBytes(&c, 1);
                char str[1];
                memcpy(str,&c, 1);
                frame.append(&c,1);
            }
            i++;
        }while (delimiter != c);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendFrameAscii(const std::string& frame, char delimiter) {
    char op[3];
    char charsArr[frame.length()-1];
    char toSend[frame.length()];
    std::string opString = frame.substr(frame.length()-2,frame.length()-1);
    std::string message = frame.substr(0,frame.length()-2);
    strcpy(charsArr, message.c_str());
    strcpy(op, opString.c_str());

    int first = 0;
    int last =0;
    if(opString == "06"){
        first = message.find_first_of(' ');
        last = message.find_last_of(' ');
    }

    for (int i=0;(unsigned)i<frame.length();i++){
        if(opString=="13"){
            if(i<3){toSend[i]=op[i];}
            else{toSend[i]=charsArr[i-3];}
        }
        else {
            if (i < 2) { toSend[i] = op[i];}
            else {
                if(((opString != "05") & (opString!="06") & (charsArr[i-2] == ' ')) |
                ((opString == "06") & ((i - 2 == first) | (i - 2 == last)) & (charsArr[i-2] == ' '))) {
                    toSend[i] = '\u0000';
                }
                else if(((opString == "01") | (opString == "05") | (opString == "06") | (opString == "08") | (opString == "12")) & (charsArr[i-2] == ';')){
                    toSend[i]='\u0000';
                }
                else{
                    toSend[i] = charsArr[i - 2];
                    }
                }
               }
        }

	bool result = sendBytes(toSend,frame.length());
	if(!result) return false;
	return sendBytes(&delimiter,1);
}
 
// Close down the connection properly.
void ConnectionHandler::close() {
    try{
        socket_.close();
    } catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl;
    }
}

short ConnectionHandler::bytesToShort(char* bytesArr) {
    short result = (short)((bytesArr[0] & 0xFF) << 8);
    result += (short)(bytesArr[1] & 0xFF);
    return result;
}

void ConnectionHandler::shortToBytes(short num, char* bytesArr) {
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}


