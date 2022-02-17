//
// Created by spl211 on 09/11/2021.
//

#ifndef UNTITLED_TRAINER_H
#define UNTITLED_TRAINER_H


#ifndef TRAINER_H_
#define TRAINER_H_

#include <vector>
#include "Customer.h"
#include "Workout.h"

typedef std::pair<int, Workout> OrderPair;

class Trainer{
public:
    Trainer(int t_capacity);
    Trainer(Trainer& other);
    Trainer(Trainer&& other);
    Trainer& operator=(Trainer& other);
    Trainer& operator=(Trainer&& other);
    ~Trainer();
    int getCapacity() const;
    void addCustomer(Customer* customer);
    void removeCustomer(int id);
    Customer* getCustomer(int id);
    std::vector<Customer*>& getCustomers();
    std::vector<OrderPair>& getOrders();
    void order(const int customer_id, const std::vector<int> workout_ids, const std::vector<Workout>& workout_options);
    void openTrainer();
    void closeTrainer();
    int getSalary();
    bool isOpen();
    void clean();
    void copy(const Trainer &other);
    void steal(Trainer &other);
    std::vector<OrderPair> toDelete(int id);
    void addOrders(std::vector<OrderPair> OP);
    bool isCustomer(int id);
    void terminateSession();
private:
    int capacity;
    bool open;
    int salary;
    std::vector<Customer*> customersList;
    std::vector<OrderPair> orderList; //A list of pairs for each order for the trainer - (customer_id, Workout)
};


#endif


#endif //UNTITLED_TRAINER_H
