package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.time.Clock;


/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService{
	long currentTime;
	long duration;
	long speed;
	boolean terminated;

	public TimeService() {
		super("timeService");
		// TODO Implement this
	}


	public TimeService(long duration, long speed){
		super("timeService");
		this.currentTime = 1;
		this.duration = duration;
		this.speed = speed;
		if(duration -1 <=0){
			this.terminated = true;
		}
		else {
			this.terminated = false;
		}
	}

	@Override
	protected void initialize() {
		MessageBusImpl.getInstance().register(this);
		int i = 0;
		while(currentTime<=duration){
			TickBroadcast tick = new TickBroadcast();
			sendBroadcast(tick);
			currentTime++;
			try {
				Thread.sleep(speed);
				}
			catch (InterruptedException e) {
				e.printStackTrace();
			}

//			System.out.println(currentTime);
		}
		sendBroadcast(new TerminateBroadcast());
		terminate();
//		System.out.println("Terminated gracefully");
		}



}