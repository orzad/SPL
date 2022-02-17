package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.CPU;
import bgu.spl.mics.application.objects.Cluster;

/**
 * CPU service is responsible for handling the {@link DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class CPUService extends MicroService {
    private CPU cpu;

    public CPUService(String name, CPU cpu) {
        super(name);
        this.cpu = cpu;
    }

    @Override
    protected void initialize() {
        MessageBusImpl.getInstance().register(this);

        Callback<TickBroadcast> tickCall = (TickBroadcast)->{
            if(cpu.getData().isEmpty()){
                cpu.process(Cluster.getInstance().getUnProcessed());
                cpu.notProcessed();
            }
            cpu.advanceTick();
            if (cpu.getData().isEmpty()){
                Cluster.getInstance().addProcessed(cpu.sendProcessedData());
            }
        };

        subscribeBroadcast(TickBroadcast.class, tickCall);


        Callback<TerminateBroadcast> terminateCall = (TerminateBroadcast ter)->{
            this.terminate();
        };

        subscribeBroadcast(TerminateBroadcast.class , terminateCall);
    }
}
