package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.services.ConferenceService;
import java.util.Vector;

public class Statistics {
    private int cpuTime;
    private int gpuTime;
    private int DataBatches;
    private student[] students;
    private Conference[] conferences;

    public Statistics(Vector<CPU> cpus, Vector<GPU> gpus, Vector<Student> students, Vector<ConferenceService> conferenceServices) {
        int gpuWorkTime = 0;
        int cpuWorkTime = 0;
        int DBCounter = 0;
        for (int i = 0; i < gpus.size(); i++) {
            gpuWorkTime += gpus.get(i).getEffectiveTime();
        }
        for (int j = 0; j < cpus.size(); j++) {
            DBCounter += cpus.get(j).getDataBatchCounter();
            cpuWorkTime += cpus.get(j).getEffectiveTime();
        }
        this.cpuTime = cpuWorkTime;
        this.gpuTime = gpuWorkTime;
        this.DataBatches = DBCounter;

        Conference[] Conference = new Conference[conferenceServices.size()];
        for (int i = 0; i < Conference.length; i++) {
            Conference[i] = new Conference(conferenceServices.get(i).getName(), conferenceServices.get(i).getSuccessful_models());
        }
        conferences = Conference;

        this.students = new student[students.size()];
        for (int i = 0; i < students.size(); i++) {
            String name = students.get(i).getName();
            models[] models = new models[0];
            for (int j = 0; j < students.get(i).getModels().size(); j++) {
                if (students.get(i).getModels().get(j).getStatus() == "Trained") {
                    models = addArray(models,new models(students.get(i).getModels().get(j).getName(),
                            students.get(i).getModels().get(j).isPublished()));
                }
                this.students[i] = new student(name, models);
            }
        }

        }
    public models[] addArray (models[] arr,models toAdd){
        models[] newArray = new models[arr.length+1];
        for(int i=0;i<arr.length;i++){
            newArray[i]=arr[i];
        }
        newArray[newArray.length-1]=toAdd;
        return newArray;
    }

}
