package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class FinishedTestBroadcast implements Broadcast {

    private Boolean finished;
    private String name;

    public FinishedTestBroadcast(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Boolean getFinished() {
        return finished;
    }

}
