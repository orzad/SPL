package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.PublishConferenceBroadcast;
import bgu.spl.mics.application.messages.PublishResultsEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.Model;
import java.util.LinkedList;

/**
 * Conference service is in charge of
 * aggregating good results and publishing them via the {@link PublishConfrenceBroadcast},
 * after publishing results the conference will unregister from the system.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ConferenceService extends MicroService {

    private LinkedList<Model> successful_models = new LinkedList<>();
    private int time;
    private final int date;
    private final int fdate;

    public ConferenceService(String name,int date, int fdate) {
        super(name);
        this.date=date;
        this.fdate = fdate;
    }

    public LinkedList<Model> getSuccessful_models() {
        return successful_models;
    }

    @Override
    protected void initialize() {
        MessageBusImpl.getInstance().register(this);

        Callback<PublishResultsEvent> call1 = (PublishResultsEvent publish)->{
            publish.getModel().published();
            successful_models.add(publish.getModel());
            publish.finish();
        };
        Callback<TickBroadcast> call2 = (TickBroadcast tick)->{
            time++;
            if (time ==date){
                PublishConferenceBroadcast p = new PublishConferenceBroadcast(this.getName(),successful_models);
                sendConference(p);
                MessageBusImpl.getInstance().unregister(this);
                this.terminate();
            }
            if(time==fdate+5){
                subscribeEvent(PublishResultsEvent.class,call1);
            }

        };
        Callback<TerminateBroadcast> call3 = (TerminateBroadcast)->{this.terminate();};

        subscribeBroadcast(TerminateBroadcast.class,call3);
        subscribeBroadcast(TickBroadcast.class,call2);
    }

    public void sendConference (PublishConferenceBroadcast conference){
        sendBroadcast(conference);
    }

}
