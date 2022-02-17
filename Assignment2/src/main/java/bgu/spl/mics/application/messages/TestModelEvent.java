package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

public class TestModelEvent implements Event<String> {
    private Student student;
    private Model model;
    private boolean finished;

    public TestModelEvent(Student student, Model model) {
        this.student = student;
        this.model = model;
        this.finished = false;
    }

    public boolean getFinished(){
        return finished;
    }

    public void finish(){
        finished = true;
    }

    public Model getModel(){
        return model;
    }

    public Student getStudent(){
        return student;
    }
}
