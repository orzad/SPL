#ifndef STUDIO_H_
#define STUDIO_H_

#include <vector>
#include <string>
#include "Workout.h"
#include "Trainer.h"
#include "Action.h"


class Studio{
public:
    Studio();
    Studio(Studio &other);
    Studio(Studio &&other);
    Studio(const std::string &configFilePath);
    Studio& operator =(Studio& other);
    Studio& operator =(Studio&& other);
    ~Studio();

    void clean();
    void copy(Studio &other);
    void steal(Studio &other);
    void start();
    int getNumOfTrainers() const;
    Trainer* getTrainer(int tid);
    const std::vector<BaseAction*>& getActionsLog() const; // Return a reference to the history of actions
    std::vector<Workout>& getWorkoutOptions();
    void addActionToLog (BaseAction* action);
    bool getStatus();
    void close();


private:
    bool open;
    std::vector<Trainer*> trainers;
    std::vector<Workout> workout_options;
    std::vector<BaseAction*> actionsLog;
};

#endif