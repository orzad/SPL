package bgu.spl.mics.application.objects;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import bgu.spl.mics.application.messages.*;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU {
    /**
     * Enum representing the type of the GPU.
     */
    enum Type {RTX3090, RTX2080, GTX1080}

    private Type type;
    private Model model;
    private  Cluster cluster;
    private int ticks;
    private int capacity;
    private Queue<DataBatch> processWaitQueue;
    private Queue<Integer> initTimeQueue;
    int processTime;
    private TrainModelEvent event;
    private int effectiveTime;

    public GPU(String t){
        this.model = null;
        this.cluster = Cluster.getInstance();
        this.ticks = 0;
        this.processWaitQueue = new ConcurrentLinkedQueue<>();
        this.initTimeQueue = new ConcurrentLinkedQueue<>();
        this.effectiveTime = 0;
        if (t == "GTX1080"){
            processTime = 4;
            type = Type.GTX1080;
            capacity = 8;
        }
        else if (t == "RTX2080"){
            processTime = 2;
            type = Type.RTX2080;
            capacity = 16;
        }
        else{
            processTime = 1;
            type = Type.RTX3090;
            capacity = 32;
        }

    }

    public TrainModelEvent getEvent() {
        return event;
    }

    public void setEvent(TrainModelEvent event) {
        this.event = event;
    }

    public Type getType() {
        return type;
    }

    public Model getModel() {
        return model;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setModel(Model m){
        this.model = m;
    }

    public Queue<DataBatch> sendChunks(){
        Queue<DataBatch> toSend = new ConcurrentLinkedQueue<>();
        int DBIndex = 0;
        while(DBIndex < model.getData().getSize()){
            DataBatch db = new DataBatch(model.getData(), DBIndex, model.getName());
            toSend.add(db);
            DBIndex = DBIndex + 1000;
        }
        return toSend;
    }

    public void trainModel(DataBatch DB){   // checking the capacity will probably take place at the service
        this.processWaitQueue.add(DB);
        this.initTimeQueue.add((Integer)ticks);
    }

    public Queue<DataBatch> getProcessWaitQueue() {
        return processWaitQueue;
    }

    public int getCapacity() {
        return capacity;
    }

    public void advanceTick(){
        ticks++;
        if(!initTimeQueue.isEmpty()){
            effectiveTime++;
        }
        while(!initTimeQueue.isEmpty() && (Integer)ticks - initTimeQueue.peek() >= processTime ){
            getModel().getData().increment();
            initTimeQueue.remove();
            processWaitQueue.remove();
        }
        if (model != null && getModel().getData().getProcessed()== getModel().getData().getSize()){
            model.setStatus(Model.Status.Trained);
        }
    }

    public void setTicks(int t){
        ticks = t;
    }

    public int getTicks(){
        return ticks;
    }

    public void testModel(Model model){
        setModel(model);
        Student.Degree type = model.getStudent().getStatus();
        Model.Result result ;
        Random r = new Random(11);
        int i = r.nextInt();
        if(type == Student.Degree.MSc) {
            if (i < 6) {
                result = Model.Result.Good;
            } else {
                result = Model.Result.Bad;
            }
        }
        else {
            if (i<8){
                result = Model.Result.Good;
            } else {
                result = Model.Result.Bad;
            }
        }
        model.setResult(result);
        model.setStatus(Model.Status.Tested);
    }


    public int getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(int effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

}
