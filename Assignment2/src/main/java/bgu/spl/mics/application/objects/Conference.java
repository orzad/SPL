package bgu.spl.mics.application.objects;

import java.util.LinkedList;

public class Conference {
    String name;
    String[] models;

    public Conference(String name, LinkedList<Model> models) {
        this.name = name;
        this.models = new String[models.size()];
        for(int i =0; i<models.size(); i++){
            this.models[i] = models.get(i).getName();
        }
    }
}
