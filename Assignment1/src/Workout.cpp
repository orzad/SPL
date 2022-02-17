//
// Created by RUBIN on 11/10/21.
//

#include "../include/Workout.h"

Workout::Workout(int w_id, std::string w_name, int w_price, WorkoutType w_type):id(w_id),name(w_name),price(w_price),type(w_type) {}
Workout::Workout(const Workout &workout):id(workout.id),name(workout.name),price(workout.price),type(workout.type){}
Workout::~Workout()= default;
Workout &Workout:: operator=(const Workout &other){
    if (&other!=this){
        *this = Workout(other);
    }
    return *this;
}

int Workout::getId() const {
    return id;
}
std::string Workout::getName() const {
    return std::string(name);
}
int Workout::getPrice() const {
    return price;
}
WorkoutType Workout::getType() const {
    return type;
}

