//
// Created by RUBIN on 11/10/21.
//

#include "../include/Studio.h"

Studio::Studio() {
    this->open = false;
    std::vector<Trainer*> trainers;
    std::vector<Workout> workout_options;
    std::vector<BaseAction*> actionsLog;
}
Studio::Studio (Studio &other){
    copy(other);
}
Studio::Studio(Studio &&other){
    steal(other);
}
Studio::Studio(const std::string &configFilePath) {
    this->open = false;
    int i=0;
    std::string num;

    while (configFilePath[i] != '\n') {
        num.push_back(configFilePath[i]);
        i++;
    }
    i++;

    while ( configFilePath[i] != '\n') {
        if (configFilePath[i] == ',') {
            trainers.push_back(new Trainer(stoi(num)));
            num = "";
            i++;
        }
        num.push_back(configFilePath[i]);
        i++;
    }
    i++;
    trainers.push_back(new Trainer(stoi(num)));

    int id = 0;
    std:: string name;
    std:: string price;
    std:: string workoutType;

    while((unsigned)i<configFilePath.length()){
        if(configFilePath[i]=='\n'){
            i++;
        }
        while ((unsigned)i<configFilePath.length() && configFilePath[i] != ',') {
            name.push_back(configFilePath[i]);
            i++;
        }
        i++;
        i++;
        while (configFilePath[i] != ',') {
            workoutType.push_back(configFilePath[i]);
            i++;
        }
        i++;
        i++;
        while ((unsigned)i<configFilePath.length() && configFilePath[i] != '\n') {
            price.push_back(configFilePath[i]);
            i++;
        }

        WorkoutType w;
        if(workoutType == "Cardio") w = CARDIO;
        else if(workoutType == "Mixed") w = MIXED ;
        else if(workoutType == "Anaerobic") w = ANAEROBIC ;

        workout_options.emplace_back(id,name,stoi(price),w);

        id++;
        name = "";
        price = "";
        workoutType = "";

    }
}
Studio &Studio::operator=(Studio &other){
    if(&other != this){
        clean();
        copy(other);
    }
    return *this;
}
Studio &Studio::operator=(Studio &&other) {
    clean();
    steal(other);
    return *this;
}
Studio::~Studio(){
    clean();
}

void Studio::start() {
    this->open = true;
    std:: cout <<"Studio is now open!" << std:: endl;
}
int Studio::getNumOfTrainers() const {
    return trainers.size();
}
Trainer *Studio::getTrainer(int tid) {
    return trainers[tid];
}
const std::vector<BaseAction *> &Studio::getActionsLog() const {
    return actionsLog;
}
std::vector<Workout> &Studio::getWorkoutOptions() {
    return workout_options;
}
void Studio::addActionToLog(BaseAction* action) {
    actionsLog.push_back(action);
}
bool Studio::getStatus() {
    return this->open;
}
void Studio::close(){
    this->open = false;
}

void Studio::clean() {

    for(auto *trainer:trainers){
        delete trainer;
    }
    for(auto *action:actionsLog){
        delete action;
    }
    trainers.clear();
    actionsLog.clear();
    workout_options.clear();
}
void Studio::copy(Studio &other) {
    this->open = other.open;
    this->workout_options = other.workout_options;
    for(int i=0; (unsigned)i < other.trainers.size(); i++){
        trainers.push_back(new Trainer(*other.trainers[i]));
    }
    for(int i=0;(unsigned)i<other.actionsLog.size();i++){
        actionsLog.push_back((other.actionsLog[i])->clone());
    }
//    for(auto action:other.actionsLog){
//        this->actionsLog.push_back(action->clone());
//    }
}
void Studio::steal(Studio &other) {
    copy(other);
    other.clean();
}









