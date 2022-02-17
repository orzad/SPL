package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.messages.TestModelEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.objects.Model;

/**
 * GPU service is responsible for handling the
 * {@link TrainModelEvent} and {@link TestModelEvent},
 * in addition to sending the {@link DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class GPUService extends MicroService {
    private GPU gpu;

    public GPUService(String name, GPU gpu) {
        super(name);
        this.gpu = gpu;
    }

    @Override
    protected void initialize() {
        MessageBusImpl.getInstance().register(this);
        Callback<TrainModelEvent> trainCall = (TrainModelEvent train)->{
            gpu.setModel(train.getModel());
            gpu.setEvent(train);
            train.getModel().setStatus(Model.Status.Training);
            Cluster.getInstance().addUnProcessed(gpu.sendChunks());
        };

        subscribeEvent(TrainModelEvent.class, trainCall);

        Callback<TestModelEvent> testCall = (TestModelEvent test)->{
            test.getModel().setStudent(test.getStudent());
            gpu.testModel(test.getModel());
            complete(test, gpu.getModel().getResult());
            test.finish();
            sendBroadcast(new FinishedTestBroadcast(gpu.getModel().getName()));
        };

        subscribeEvent(TestModelEvent.class, testCall);

        Callback<TickBroadcast> tickCall = (TickBroadcast tick)->{
            if(!Cluster.getInstance().getProcessedData().get(gpu.getModel().getName()).isEmpty()){
                // obtaining processed data
                while(gpu.getProcessWaitQueue().size()< gpu.getCapacity()) {
                    gpu.trainModel(Cluster.getInstance().getProcessedData().get(gpu.getModel().getName()).remove());
                }
            }
            if(gpu.getModel().getStatus()== "Training"){
                gpu.advanceTick();
                if (gpu.getModel().getStatus() == "Trained"){
                    complete(gpu.getEvent(), gpu.getModel().getStatus());
                    gpu.getEvent().finish();
                    sendBroadcast(new FinishedTrainBroadcast(gpu.getModel().getName()));
                }
            }
            else{
                gpu.advanceTick();
            }

        };

        subscribeBroadcast(TickBroadcast.class, tickCall);

        Callback<TerminateBroadcast> terminateCall = (TerminateBroadcast ter)->{
            this.terminate();
        };

        subscribeBroadcast(TerminateBroadcast.class , terminateCall);
    }
}
