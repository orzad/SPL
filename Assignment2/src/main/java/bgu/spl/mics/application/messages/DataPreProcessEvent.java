package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class DataPreProcessEvent implements Event<String> {
    @Override
    public boolean getFinished() {
        return false;
    }
}
