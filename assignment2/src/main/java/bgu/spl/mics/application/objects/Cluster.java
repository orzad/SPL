package bgu.spl.mics.application.objects;


import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {
	private HashMap <String,Queue<DataBatch>> processedData;
	private LinkedList<Queue<DataBatch>> unProcessedData;
	int counter;

	private Cluster(){
		this.unProcessedData = new LinkedList<>();
		this.processedData = new HashMap<>();
		this.counter = 0;
	}
	private static class ClusterInstance{
		private static Cluster instance = new Cluster();
	}
	/**
	 * Retrieves the single instance of this class.
	 */
	public static Cluster getInstance() {
		return ClusterInstance.instance;
	}



	public void addProcessed(DataBatch DB) {
		if(!processedData.containsKey(DB.getModelName())) {
			processedData.put(DB.getModelName(), new ConcurrentLinkedQueue<>());
		}
		processedData.get(DB.getModelName()).add(DB);
	}

	public void addUnProcessed(Queue<DataBatch> DB){

		unProcessedData.add(DB);

	}

	public synchronized DataBatch getUnProcessed(){
		DataBatch dataBatch = null;
		if (!unProcessedData.isEmpty()) {
			dataBatch = unProcessedData.get(counter % unProcessedData.size()).remove();
		}
		if(unProcessedData.get(counter % unProcessedData.size()).isEmpty()){
			unProcessedData.remove(unProcessedData.get(counter % unProcessedData.size()));
		}
		counter++;
		return dataBatch;
	}

	public HashMap<String, Queue<DataBatch>> getProcessedData() {
		return processedData;
	}

}
