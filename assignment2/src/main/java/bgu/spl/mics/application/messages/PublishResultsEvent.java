package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

public class PublishResultsEvent implements Event<String> {

    private Student student;
    private Model model;
    private Boolean finished;

    public PublishResultsEvent(Student s,Model m){
        this.model=m;
        this.student=s;
        this.finished = false;
    }
    public Model getModel(){return model;}
    public boolean getFinished() {
        return finished;
    }
    public void finish(){
        finished = true;
    }
}
