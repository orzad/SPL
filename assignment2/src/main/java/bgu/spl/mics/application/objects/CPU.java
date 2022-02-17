package bgu.spl.mics.application.objects;


import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {
    private  int cores;
    private Queue<DataBatch> data;
    private Queue<DataBatch> processedData;
    private int initTime;
    private Cluster cluster;
    private boolean processed;
    private int ticks;
    private int dataBatchCounter;
    private int effectiveTime;


    public CPU(int cores){
        this.cores = cores;
        this.data = new ConcurrentLinkedQueue<>();
        this.processedData = new ConcurrentLinkedQueue<>();
        this.cluster = Cluster.getInstance();
        this.processed = false;
        this.ticks = 0;
        this.initTime = 0;
        this.dataBatchCounter = 0;
        this.effectiveTime = 0;
    }

    public int getCores(){
        return cores;
    }

    public Queue<DataBatch> getData(){
        return data;
    }

    public Cluster getCluster(){
        return cluster;
    }

    public boolean isProcessed(){
        return processed;
    }

    public void setTicks(int t){
        this.ticks = t;
    }

    public int getTicks(){
        return ticks;
    }

    public Queue<DataBatch> getProcessedData() {
        return processedData;
    }

    public void processed(){}// not sure if needed

    public void notProcessed(){
        processed = false;
    }

    public void process(DataBatch DB){
        data.add(DB);
        initTime = ticks;

    }

    public DataBatch sendProcessedData(){
        return processedData.remove();
    }

    public void addData(DataBatch dataBatch){
        this.getData().add(dataBatch);
    }

    public void advanceTick(){
        ticks++;
        if(data.peek().getData().getType() == Data.Type.Images & !processed){
            effectiveTime++;
            if(ticks - initTime>= (32/cores)*4){
                processedData.add(data.remove());
                processed = true;
                dataBatchCounter++;
            }
        }
        else if(data.peek().getData().getType() == Data.Type.Text & !processed){
            effectiveTime++;
            if(ticks - initTime>= (32/cores)*2){
                processedData.add(data.remove());
                processed = true;
                dataBatchCounter++;
            }
        }
        else if(data.peek().getData().getType() == Data.Type.Tabular & !processed){
            effectiveTime++;
            if(ticks - initTime>= (32/cores)*1){
                processedData.add(data.remove());
                processed = true;
                dataBatchCounter++;
            }
        }
    }

    public int getEffectiveTime() {
        return effectiveTime;
    }

    public int getDataBatchCounter() {
        return dataBatchCounter;
    }
}
