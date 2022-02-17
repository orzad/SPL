//
// Created by spl211 on 09/11/2021.
//

#include "../include/Customer.h"

Customer::Customer(std::string c_name, int c_id):name(c_name), id(c_id) {};

std::string Customer::getName() const {
    return this->name;
}

int Customer::getId() const {
    return this->id;
}


SweatyCustomer::SweatyCustomer(std::string name, int id) : Customer(name, id) {

}

std::vector<int> SweatyCustomer::order(const std::vector<Workout> &workout_options) {
    std::vector<int> swt_workout;
    for (auto workout: workout_options) {
        if (workout.getType() == CARDIO) {
            swt_workout.push_back(workout.getId());
        }
    }

    return swt_workout;
}

std::string SweatyCustomer::toString() const {
    return this->getName()+",swt";
}

SweatyCustomer *SweatyCustomer::clone() {
    return new SweatyCustomer(*this);
}

CheapCustomer::CheapCustomer(std::string name, int id) : Customer(name, id) {

}

std::vector<int> CheapCustomer::order(const std::vector<Workout> &workout_options) {
    std::vector<int> chp_workout;
    if (!workout_options.empty()) {
        Workout ans = workout_options[0];
        int i = ans.getPrice();
        int id = ans.getId();

        for (auto workout: workout_options) {
            if ((workout.getPrice() < i) |((workout.getPrice() == i) & (workout.getId() < id))) {
                i = workout.getPrice();
                id = workout.getId();
            }
        }
        chp_workout.push_back(id);
    }
    return chp_workout;
}


std::string CheapCustomer::toString() const {
    return this->getName()+",chp";;
}

CheapCustomer *CheapCustomer::clone() {
    return new CheapCustomer(*this);
}

HeavyMuscleCustomer::HeavyMuscleCustomer(std::string name, int id) : Customer(name, id) {

}

std::vector<int> HeavyMuscleCustomer::order(const std::vector<Workout> &workout_options) {
    std::vector<int> mcl_workout;
    int i = 0;
    for(auto workout:workout_options){
        if (workout.getType() == ANAEROBIC){
            if(mcl_workout.empty()){
                mcl_workout.push_back(workout.getId());
            }
            else{
                while((unsigned)i<mcl_workout.size() &&
                workout_options[mcl_workout[i]].getPrice()<= workout.getPrice()){
                    i++;
                }
                mcl_workout.insert(mcl_workout.begin()+i,workout.getId());
                i = 0;
            }
        }
    }
    return mcl_workout;
}

std::string HeavyMuscleCustomer::toString() const {
    return this->getName()+",mcl";;
}

HeavyMuscleCustomer *HeavyMuscleCustomer::clone() {
    return new HeavyMuscleCustomer(*this);
}

FullBodyCustomer::FullBodyCustomer(std::string name, int id) : Customer(name, id) {

}

std::vector<int> FullBodyCustomer::order(const std::vector<Workout> &workout_options) {
    std::vector<int> fbd_workout;
    std::vector<Workout> cardio;
    std::vector<Workout> mix;
    std::vector<Workout> anAerobic;
     for (auto workout: workout_options) {
         if (workout.getType() == CARDIO) {
             if (cardio.empty()) {
                 cardio.push_back(workout);
             } else if ((workout.getPrice() < cardio[0].getPrice()) ||
                     (workout.getPrice() == cardio[0].getPrice()) & (workout.getId() < cardio[0].getId())) {
                 cardio.pop_back();
                 cardio.push_back(workout);
             }
         }
         if (workout.getType() == MIXED) {
             if (mix.empty()) {
                 mix.push_back(workout);
             } else if ((workout.getPrice() > mix[0].getPrice() )||
                        (workout.getPrice() == mix[0].getPrice()) & (workout.getId() < mix[0].getId())) {
                 mix.pop_back();
                 mix.push_back(workout);
             }
         }
         if (workout.getType() == ANAEROBIC) {
             if (anAerobic.empty()) {
                 anAerobic.push_back(workout);
             } else if ((workout.getPrice() < anAerobic[0].getPrice()) ||
                        ((workout.getPrice() == anAerobic[0].getPrice()) & (workout.getId() < anAerobic[0].getId()))) {
                 anAerobic.pop_back();
                 anAerobic.push_back(workout);
             }
         }
     }
     // old requirement
//    if (!cardio.empty() && !mix.empty() && !anAerobic.empty()){
//        fbd_workout.push_back(cardio[0].getId());
//        fbd_workout.push_back(mix[0].getId());
//        fbd_workout.push_back(anAerobic[0].getId());
//    }
    if(!cardio.empty()){
        fbd_workout.push_back(cardio[0].getId());
    }
    if(!mix.empty()){
        fbd_workout.push_back(mix[0].getId());
    }
    if(!anAerobic.empty()){
        fbd_workout.push_back(anAerobic[0].getId());
    }
    return fbd_workout;
}
std::string FullBodyCustomer::toString() const {
    return this->getName()+",fbd";;
}

FullBodyCustomer *FullBodyCustomer::clone() {
    return new FullBodyCustomer(*this);
}
