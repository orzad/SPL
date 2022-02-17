#include "../include/Studio.h"
#include <iostream>
#include <fstream>

using namespace std;

Studio* backup = nullptr;
std:: string findWord(std:: string input,int* i){
    std:: string str;
    while ((unsigned)*i<input.size() && input[*i]!=' ' && input[*i]!=',' && input[*i]!='\n') {
        str.push_back(input[*i]);
        (*i)++;
    }
    if((unsigned)*i<input.size()) {
        (*i)++;
    }
    return str;
}

int main(int argc, char** argv){

    if(argc!=2){
        std::cout << "usage: studio <config_path>" << std::endl;
        return 0;
    }
    string configurationFile = argv[1];

    fstream file;
    file.open(configurationFile,ios::in);
    std:: string str;
    std:: string configFile;
    while(getline(file,str)){
        if((str[0]!= '#') & (str != "")) {
            configFile.append(str);
            configFile.append("\n");
        }
    }
    configFile.erase(configFile.size()-1);
    file.close();

    Studio studio(configFile);
    studio.start();

    std:: string input;
    str = "";
    int id=0;
    while(studio.getStatus()){
        int v=0;
        int *i = &v;
        getline(std:: cin,input);
        str = findWord(input,i);

        if(str == "open") {
            int numOfCustomer = 0;
            std::string t_id = findWord(input,i);
            std::vector<Customer *> customerList;
            while (((unsigned) *i < input.size()) ) {
                std::string name = findWord(input, i);
                std::string type = findWord(input, i);

                if (type == "swt") {
                    customerList.push_back(new SweatyCustomer(name, id));
                } else if (type == "chp") {
                    customerList.push_back(new CheapCustomer(name, id));
                } else if (type == "mcl") {
                    customerList.push_back(new HeavyMuscleCustomer(name, id));
                } else if (type == "fbd") {
                    customerList.push_back(new FullBodyCustomer(name, id));
                }id++;
                numOfCustomer++;
            }
            OpenTrainer* OTA = new OpenTrainer(stoi(t_id), customerList);
            OTA->act(studio);
            studio.addActionToLog(OTA);
            for (auto customer:customerList){
                delete customer;
            }
            customerList.clear();
        }
        else if(str=="order"){
            std::string t_id = findWord(input,i);
            Order* OA =  new Order(stoi(t_id));
            OA->act(studio);
            studio.addActionToLog(OA);
        }
        else if(str=="move"){
            std::string src = findWord(input,i);
            std::string dst = findWord(input,i);
            std::string c_id = findWord(input,i);
            MoveCustomer* move = new MoveCustomer(stoi(src), stoi(dst), stoi(c_id));
            move->act(studio);
            studio.addActionToLog(move);
        }
        else if(str=="close"){
            std:: string toClose = findWord(input,i);
            Close* close = new Close(stoi(toClose));
            close->act(studio);
            studio.addActionToLog(close);
        }
        else if(str=="workout_options"){
            PrintWorkoutOptions* PWO = new PrintWorkoutOptions();
            PWO->act(studio);
            studio.addActionToLog(PWO);
        }
        else if(str=="status"){
            std:: string id = findWord(input,i);
            PrintTrainerStatus* TS = new PrintTrainerStatus(stoi(id));
            TS->act(studio);
            studio.addActionToLog(TS);
        }
        else if(str=="log"){
            PrintActionsLog* AL = new PrintActionsLog();
            AL->act(studio);
            studio.addActionToLog(AL);
        }
        else if(str=="backup"){
            BackupStudio* BS = new BackupStudio();
            BS->act(studio);
            studio.addActionToLog(BS);
        }
        else if(str=="restore"){
            RestoreStudio* RS = new RestoreStudio();
            RS->act(studio);
            delete RS;
        }
        else if(str =="closeall"){
            CloseAll CA = CloseAll();
            CA.act(studio);
        }
    }

    if(backup!=nullptr){
    	delete backup;
    	backup = nullptr;
    }
    return 0;
}
