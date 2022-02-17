
#include "../include/Studio.h"
#include "../include/Action.h"
#include "map"


void BaseAction::complete() {
    this->status = COMPLETED;
}
ActionStatus BaseAction::getStatus() const {
    return status;
}
void BaseAction::error(std::string errorMsg) {
    this->errorMsg = errorMsg;
}
std::string BaseAction::getErrorMsg() const {
    return errorMsg;
}
BaseAction::BaseAction():status(ERROR) {}
extern Studio* backup;


OpenTrainer::OpenTrainer(int id, std::vector<Customer *> &customersList):trainerId(id) {
    for(auto customer:customersList){
        this->customers.push_back(customer->clone());
    }
    error("Workout session does not exist or is already open.");
}
void OpenTrainer::act(Studio &studio) {
    try {
        if (studio.getNumOfTrainers() <= trainerId|| trainerId<0 || studio.getTrainer(trainerId)->isOpen() || trainerId<0) {
            throw 1;
        }
        studio.getTrainer(trainerId)->openTrainer();
        for (auto customer : customers){
            studio.getTrainer(trainerId)->addCustomer(customer->clone());
        }
        complete();
    }
    catch(int tid){ std:: cout<< getErrorMsg()<<std::endl;}
}
std::string OpenTrainer::toString() const {
    std::string str;
    if (this->getStatus() == COMPLETED){
        for (auto customer : customers) {
            str.append(customer->toString());
            str.push_back(' ');
        }
        str = "open " + std::to_string(trainerId) + ' ' + str + "Completed";
    }
    else str = "Error: " + getErrorMsg();
    return str;
}
OpenTrainer *OpenTrainer::clone() {
    return new OpenTrainer(*this);
}
OpenTrainer::~OpenTrainer() {
    for(auto customer:this->customers){
        delete customer;
    }
    customers.clear();
}
OpenTrainer::OpenTrainer(OpenTrainer &other):trainerId(other.trainerId) {
    if(other.getStatus()==COMPLETED){
        complete();
    }
    for(auto customer:customers){
        this->customers.push_back(customer->clone());
    }
    error("Workout session does not exist or is already open.");
}


Order::Order(int id):trainerId(id) {
    error("Trainer does not exist or is not open");
}
void Order::act(Studio &studio) {
    try {
        if ((trainerId >= studio.getNumOfTrainers()) | (trainerId < 0)
            || (studio.getTrainer(this->trainerId)->isOpen() == false)
               |!studio.getTrainer(this->trainerId)->getOrders().empty()) { //
            throw 1;
        }
        Trainer *trainer = studio.getTrainer(this->trainerId);
        for (auto customer: trainer->getCustomers()) {
            trainer->order(customer->getId(),
                           customer->order(studio.getWorkoutOptions()),
                           studio.getWorkoutOptions());
        }
        complete();
        for (auto order: trainer->getOrders()) {
            std::string name = trainer->getCustomer(order.first)->getName();
            std::string workout_name = order.second.getName();
            std::cout << name + " is doing " + workout_name << std::endl;
        }
    }
    catch (int i) {
        std::cout << this->getErrorMsg()<<std::endl;
    }
}
std::string Order::toString() const {
    std:: string str;
    if(this->getStatus()==COMPLETED){
        str ="Order " + std::to_string(trainerId) + " Completed";
    }
    else str ="Order " + std::to_string(trainerId) + "Error: " + this->getErrorMsg();
    return str;
}
Order *Order::clone() {
    return new Order(*this);
}

MoveCustomer::MoveCustomer(int src, int dst, int customerId):srcTrainer(src),dstTrainer(dst),id(customerId) {
    error("Cannot move customer");
}
void MoveCustomer::act(Studio &studio) {
    try{
        if((srcTrainer>=studio.getNumOfTrainers())|(srcTrainer<0)|
                (dstTrainer>=studio.getNumOfTrainers())|(dstTrainer<0)){
            throw 1;
        }
        Trainer* source = studio.getTrainer(srcTrainer);
        Trainer* dest = studio.getTrainer(dstTrainer);
        if(!dest->isOpen() | !source->isOpen() || (dest->getCustomers().size()>=(unsigned)dest->getCapacity())
                                                  |!source->isCustomer(id)){
            throw 1;
        }
        if(srcTrainer != dstTrainer) {
            dest->addOrders(source->toDelete(id));
//            Customer *customer = source->getCustomer(id);
            dest->addCustomer(source->getCustomer(id));
            source->removeCustomer(id);
            if(source->getCustomers().empty()){
                source->closeTrainer();
            }
            this->complete();
        }
//        source = nullptr;
//        dest = nullptr;
    }
    catch(int tid){
        std:: cout<< getErrorMsg()<<std::endl;
    }
}
std::string MoveCustomer::toString() const {
    std:: string str;
    if(this->getStatus()==COMPLETED){
        str = "Move "+ std::to_string(srcTrainer) + ' ' + std::to_string(dstTrainer) + ' '+ std::to_string(id) + " Completed" + ' ';
    }
    else str = "Move "+ std::to_string(srcTrainer) + ' ' + std::to_string(dstTrainer) + ' ' + std::to_string(id) + " Error: " + this->getErrorMsg();
    return str;
}
MoveCustomer *MoveCustomer::clone() {
    return new MoveCustomer(*this);
}

Close::Close(int id):trainerId(id) {
    error("Trainer does not exist or is not open");
}
void Close::act(Studio &studio) {
    try {
        if(trainerId>studio.getNumOfTrainers()) {throw trainerId;}
        Trainer *trainer = studio.getTrainer(trainerId);
        if(!(trainer->isOpen())) {throw trainerId;}
        trainer->terminateSession();
        int salary = trainer->getSalary();
        std::cout << "Trainer " + std::to_string(trainerId) + " closed. Salary: " + std::to_string(salary) << std::endl;
        complete();
    }
    catch(int id){std:: cout <<getErrorMsg()<< std::endl;}
}
std::string Close::toString() const {
    std:: string str;
    if(this->getStatus()==COMPLETED){
        str = "Close " + std::to_string(trainerId) + " Completed";
    }
    else str = "Close " + std::to_string(trainerId) + " Error: " + this->getErrorMsg();
    return str;
}
Close *Close::clone() {
    return new Close(*this);
}

CloseAll::CloseAll() = default;
void CloseAll::act(Studio &studio) {
    std:: string str;
    for (int i=0; i<studio.getNumOfTrainers();i++){
        str = str + "Trainer " + std::to_string(i) + ' ' + std::to_string(studio.getTrainer(i)->getSalary()) + '\n';
    }
    studio.close();
    complete();
    std:: cout <<str<<std::endl;
}
std::string CloseAll::toString() const {
    return "CloseAll " + this->getStatus();
}
CloseAll *CloseAll::clone() {
    return new CloseAll(*this);
}

PrintWorkoutOptions::PrintWorkoutOptions() = default;
void PrintWorkoutOptions::act(Studio &studio) {
    std:: vector<Workout> workouts = studio.getWorkoutOptions();
    std:: map <WorkoutType, std:: string> type_map = {
            {MIXED, "Mixed"},
            {ANAEROBIC, "Anaerobic"},
            {CARDIO, "Cardio"}
    };
    for (auto & workout : workouts){
        std:: cout << workout.getName();
        std:: cout << ", "<<type_map.at(workout.getType());
        std:: cout << ", "<< workout.getPrice() << std::endl;
    }
    complete();
}
std::string PrintWorkoutOptions::toString() const {
    std:: string status;
    if(this->getStatus()==0){status = "Completed";}
    else status = "Error";
    return "workout_options " + status;
}
PrintWorkoutOptions *PrintWorkoutOptions::clone() {
    return new PrintWorkoutOptions(*this);
}

PrintTrainerStatus::PrintTrainerStatus(int id):trainerId(id) {}
void PrintTrainerStatus::act(Studio &studio) {
    Trainer* trainer = studio.getTrainer(trainerId);
    std:: string str;
    std:: string status;
    bool open = trainer->isOpen();
    if(open){
        status = "open ";
    }
    else status = "close ";
    std:: vector<Customer*> customers = trainer->getCustomers();
    std:: vector<OrderPair> orders = trainer->getOrders();

    str = "Trainer " + std::to_string(trainerId) + " status: " + status + '\n' + "Customers:" + '\n';
    for (auto & customer : customers){
        str = str + std:: to_string(customer->getId()) +' '+ customer->getName() + '\n';
    }
    str = str + "Orders:" + '\n';
    for (auto & order : orders){
        str = str + order.second.getName() +' '+ std::to_string(order.second.getPrice()) + "NIS "  + std::to_string(order.first) + '\n';
    }
    str = str + "Current Trainer's Salary: " +std::to_string(trainer->getSalary()) +"NIS";
    complete();
    std:: cout << str<<std::endl;
}
std::string PrintTrainerStatus::toString() const {
    std:: string str;
    if(this->getStatus()==COMPLETED){
        str = "status " + std::to_string(trainerId) + " Completed";
    }
    else str = "status " + std::to_string(trainerId) + " Error: " + this->getErrorMsg();
    return str;
}
PrintTrainerStatus *PrintTrainerStatus::clone() {
    return new PrintTrainerStatus(*this);
}

PrintActionsLog::PrintActionsLog() = default;
void PrintActionsLog::act(Studio &studio) {
    complete();
    for(auto * action: studio.getActionsLog() ){
        std:: cout<< action->toString() << std::endl;
    }

}
std::string PrintActionsLog::toString() const {
    return "log Completed";
}
PrintActionsLog *PrintActionsLog::clone() {
    return new PrintActionsLog(*this);
}

extern Studio* backup;
BackupStudio::BackupStudio() {}
void BackupStudio::act(Studio &studio) {
    delete backup;
    backup = new Studio(studio);
    complete();
}
std::string BackupStudio::toString() const {
    return "backup Completed";
}
BackupStudio *BackupStudio::clone() {
    return new BackupStudio(*this);
}

RestoreStudio::RestoreStudio() {
    error("No backup available");
}
void RestoreStudio::act(Studio &studio) {
    try{
        if(backup == nullptr){
            throw 1;
        }
        studio = *backup;
    }
    catch (int i){
        std::cout<< getErrorMsg();
    }
    complete();
}
std::string RestoreStudio::toString() const {
    std:: string str;
    if (this->getStatus()==COMPLETED){
        str = "Completed";
    }
    else str = "No backup available";
    return str;
}
RestoreStudio *RestoreStudio::clone() {
    return new RestoreStudio(*this);
}

