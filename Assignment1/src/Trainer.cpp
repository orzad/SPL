//
// Created by spl211 on 09/11/2021.
//

#include "../include/Trainer.h"
Trainer::Trainer(int t_capacity) {
    capacity = t_capacity;
    open = false;
    salary = 0;
    std::vector<Customer*> customersList;
    std::vector<OrderPair> orderList;
}

int Trainer::getCapacity() const {
    return this->capacity;
}
void Trainer::addCustomer(Customer *customer) {
    if (customersList.size() < (unsigned)capacity){
        customersList.push_back(customer);
    }
}
void Trainer::removeCustomer(int id) { // i want to do a for each loop find the customer with id and pop him
    int i = 0;
    for(auto tmp:customersList){  // auto allows access by value
        if(tmp->getId()==id){
            customersList.erase(customersList.begin()+i);
        }
        i++;
    }
    this->toDelete(id);
}

Customer *Trainer::getCustomer(int id) {
    for(auto tmp:customersList){
        if(tmp->getId()==id){
            return tmp;
        }
    }
    return nullptr;
}

std::vector<Customer *> &Trainer::getCustomers() {
    return this->customersList;
}

std::vector<OrderPair> &Trainer::getOrders() {
    return this->orderList;
}

void Trainer::order(const int customer_id, const std::vector<int> workout_ids, const std::vector<Workout> &workout_options) {
    for(auto id:workout_ids){
        Workout workout = workout_options.at(id);
        salary = salary +workout.getPrice();
        OrderPair op = OrderPair(customer_id,workout);
        this->orderList.push_back(op);
    }
}

void Trainer::openTrainer() {
    open = true;
}

void Trainer::closeTrainer() {
    open = false;
}

int Trainer::getSalary() {
    return salary;
}

bool Trainer::isOpen() {
    return this->open;
}

void Trainer::clean() {  // should i also put nullptr?
    capacity = -1;
    for(auto *customer:customersList){
        delete customer;
    }
    this->customersList.clear();
    this->orderList.clear();
}
void Trainer::copy(const Trainer &other) {
    for(auto customer:other.customersList){
        this->customersList.push_back(customer->clone());
    }
}
void Trainer::steal(Trainer &other) {
    for(auto customer:other.customersList){
        this->customersList.push_back(customer->clone());
    }
    other.clean();
};

Trainer::Trainer(Trainer &other):capacity(other.capacity) ,open(other.open), salary(other.salary),
                                 orderList(other.orderList){
    copy(other);
}
Trainer::Trainer(Trainer &&other):capacity(other.capacity),  open(other.open),salary(other.salary),
                                  orderList(other.orderList) {
    steal(other);
}
Trainer &Trainer::operator=(Trainer &other) {
    if (this != &other){ // not sure thats correct
        clean();
        copy(other);
    }
    return *this;
}
Trainer &Trainer::operator=(Trainer &&other) {
    clean();
    steal(other);
    return *this;
}
Trainer::~Trainer() {
    clean();
}

std::vector<OrderPair> Trainer::toDelete(int id) { // i need to make sure that this method is stable
    std::vector<OrderPair> output;
    std::vector<OrderPair> newOrderList;
    for(auto order: this->orderList) {
        if (id == order.first) {
            output.push_back(order);
        }
        else{
            newOrderList.push_back(order);
        }
        this->orderList.clear();
        this->salary = 0;
        for(auto newOP:newOrderList){
            this->orderList.push_back(newOP);
            salary = salary + newOP.second.getPrice();
        }
    }
    return output;
}

void Trainer::addOrders(std::vector<OrderPair> OP) {
    for(auto order:OP){
        this->orderList.push_back(order);
        salary =salary+order.second.getPrice();
    }
}

bool Trainer::isCustomer(int id) {
    for(auto customer:customersList){
        if(customer->getId() == id){
            return true;
        }
    }
    return false;
}

void Trainer::terminateSession() {
    open = false;
    for(auto customer:customersList){
        delete customer;
    }
    this->customersList.clear();
    this->orderList.clear();
}



