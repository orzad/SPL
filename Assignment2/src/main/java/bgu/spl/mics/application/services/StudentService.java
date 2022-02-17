package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.Student;


/**
 * Student is responsible for sending the {@link TrainModelEvent},
 * {@link TestModelEvent} and {@link PublishResultsEvent}.
 * In addition, it must sign up for the conference publication broadcasts.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class StudentService extends MicroService {

    private Student student;
    private int ModelToTrain;

    public StudentService(String name, Student student) {
        super(name);
        this.student = student;
    }

    @Override
    protected void initialize() {
        MessageBusImpl.getInstance().register(this);

        Callback<TerminateBroadcast> call4 = (TerminateBroadcast)->{
            this.terminate();
        };
        subscribeBroadcast(TerminateBroadcast.class,call4);
        Callback<PublishConferenceBroadcast> call1 = (PublishConferenceBroadcast Papers) -> {
            student.readPapers(Papers.getNumOfGoodPapers());
        };
        subscribeBroadcast(PublishConferenceBroadcast.class, call1);


        Callback<FinishedTrainBroadcast> call2 = (FinishedTrainBroadcast finished)->{
            if(finished.getName().equals(student.getModels().get(ModelToTrain).getName())) {
                TestModelEvent test = new TestModelEvent(student, student.getModels().get(ModelToTrain));
                sendEvent(test);
            }
        };
        subscribeBroadcast(FinishedTrainBroadcast.class,call2);


        Callback<FinishedTestBroadcast> call3 = (FinishedTestBroadcast finished)->{
            if (finished.getName().equals(student.getModels().get(ModelToTrain).getName())) {
                if (student.getModels().get(ModelToTrain).getResult().equals("Good")) {
                    PublishResultsEvent publish = new PublishResultsEvent(student, student.getModels().get(ModelToTrain));
                    sendEvent(publish);
                }
                ModelToTrain++;
                if (!student.getModels().isEmpty()) {
                    TrainModelEvent train = new TrainModelEvent(student, student.getModels().get(ModelToTrain));
                    sendEvent(train);
                }
            }
        };
        subscribeBroadcast(FinishedTestBroadcast.class,call3);
        sendEvent(new TrainModelEvent(student, student.getModels().get(0)));
    }
}
