package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.Model;

import java.util.LinkedList;

public class PublishConferenceBroadcast implements Broadcast {

    private LinkedList<Model> models;
    private String name;

    public PublishConferenceBroadcast(String name, LinkedList<Model> models){
        this.name = name;
        this.models = models;
    }

    public int getNumOfGoodPapers(){return this.models.size();}
    public void setNumOfGoodPapers(LinkedList models){this.models = models;}
    public String getName() {return name;}
}
