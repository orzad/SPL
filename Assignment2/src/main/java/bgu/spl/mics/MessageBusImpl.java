package bgu.spl.mics;

import java.util.concurrent.*;
import java.util.LinkedList;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private ConcurrentHashMap <String,ConcurrentLinkedQueue<Message>> Services;
	private ConcurrentHashMap <Class,LinkedList<MicroService>> Events;
	private ConcurrentHashMap <Class,LinkedList<MicroService>> Broadcast;
	private ConcurrentHashMap <Event,Future> futures;
	private int counter = 0;

	private MessageBusImpl () {
		futures = new ConcurrentHashMap<>();
		Events = new ConcurrentHashMap<>();
		Broadcast = new ConcurrentHashMap<>();
		Services = new ConcurrentHashMap<>();
	}

	public static MessageBusImpl getInstance(){
		return MessageBusInstance.instance;
	}

	private static class MessageBusInstance{
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	/**
	 * @pre: for: Service in EventHashMap(type):Service !=m.
	 * @post: m belongs to EventHashMap(type).
	 */

	@Override
	public synchronized  <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		synchronized(this) {
			if (!Events.containsKey(type)) {
				createList(type, Events);
			}
			Events.get(type).add(m);
			notifyAll();
		}
	}

	/**
	 * @pre: for: Service in BroadcastHashMap(type):Service !=m.
	 * @post: m belongs to BroadcastHashMap(type).
	 */

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		synchronized(this) {
			if (!Broadcast.containsKey(type)) {
				createList(type, Broadcast);
			}
			Broadcast.get(type).add(m);
			notifyAll();
		}

	}

	/**
	 * @pre: for some s: ServiceHashMap(s) = e.
	 * @post: for same s ServiceHashMap(s) = null.
	 */

	@Override
	public <T> void complete(Event<T> e, T result) {//prob npo need for sync

		synchronized (Services) {
			if (futures.containsKey(e)) {
				futures.get(e).resolve(result);
				futures.remove(e);
			}
		}
	}

	/**
	 * @pre: none
	 * @post: broadcast was sent to all subscribers.
	 */

	@Override
	public synchronized void sendBroadcast(Broadcast b) {
		if (!Broadcast.containsKey(b.getClass())){
			Broadcast.put(b.getClass(),new LinkedList<MicroService>());
		}
		for (int i=0;i<Broadcast.get(b.getClass()).size() ;i++){
			Services.get(Broadcast.get(b.getClass()).get(i).getName()).add(b);
		}
		notifyAll();
	}

	/**
	 * @pre: EventsHashMap(e) !=null.
	 * @post: ServiceHashMap(s) != null for some s that belongs to EventsHashMap(e)
	 * (the s that handles the event).
	 */
	
	@Override
	public synchronized <T> Future<T> sendEvent(Event<T> e) {
		while (Events.get(e.getClass())==null){
			try {
				wait();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		LinkedList<MicroService> l = Events.get(e.getClass());
		MicroService m = l.get(counter % l.size());
		counter++;
		Services.get(m.getName()).add(e);
		notifyAll();
		Future<T> f = new Future<>();
		futures.put(e,f);
		return f;
	}

	/**
	 * @pre: ServicesHashMap("m") = null.
	 * @post: ServicesHashMap("m") = List.
	 */

	@Override
	public void register(MicroService m) {
		Services.put(m.getName(),new ConcurrentLinkedQueue<>());
	}

	/**
	 * @pre: m belongs to ServiceHashMap.
	 * @post: m does not belong to ServiceHashMap.
	 * @post: m does not belong to any List of EventsHashMap.
	 * @post: m does not belong to any List of BroadcastHashMap.
	 */

	@Override
	public synchronized void unregister(MicroService m) {
		for (LinkedList linkedList: Events.values() ){
			linkedList.remove(m);
			}
		for (LinkedList linkedList: Broadcast.values()){
			linkedList.remove(m);
		}
		while(!Services.get(m.getName()).isEmpty()){
			sendEvent((Event)Services.get(m.getName()).remove());
		}
	}

	/**
	 * @pre: message in ServiceHashMap(m) = 0 or message in ServiceHashMap(m) = n.
	 * @post: message in ServiceHashMap(m) = 0 or message in ServiceHashMap(m) = n-1.
	 */

	@Override
	public synchronized Message awaitMessage(MicroService m) throws InterruptedException {
		try {
			while (Services.isEmpty() || Services.get(m.getName()).isEmpty()) {
					wait();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return Services.get(m.getName()).remove();
	}

	private synchronized   <T> void createList (Class<? extends Message> type,ConcurrentHashMap message){
		LinkedList subscribers = new LinkedList();
		message.put(type,subscribers);
	}
}
