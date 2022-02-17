package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class FinishedTrainBroadcast implements Broadcast {

    private Boolean finished;
    private String name;

    public FinishedTrainBroadcast(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Boolean getFinished() {
        return finished;
    }
}
