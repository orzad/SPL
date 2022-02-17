//package bgu.spl.mics.application.objects;
//
//import org.junit.Before;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.Assert.*;
//
//class CPUTest {
//
//    private CPU cpu;
//    private static Cluster cluster;
//    private static int cores;
//    private static DataBatch DB;
//
//    @Before
//    public void setUp() throws Exception{
//        cpu = new CPU(cores,cluster);
//    }
//
//    @org.junit.Test
//    void getCores() {
//        assertEquals(cores,(cpu.getCores()));
//    }
//
//    @org.junit.Test
//    void getData() {
//        assertEquals(null,cpu.getData());
//        cpu.setData(DB);
//        assertEquals(DB,cpu.getData());
//    }
//
//    @org.junit.Test
//    void getCluster() {
//        assertEquals(cluster, cpu.getCluster());
//    }
//
//    @org.junit.Test
//    void isProcessed() {
//        assertEquals(false, cpu.isProcessed());
//        cpu.processed();
//        assertEquals(true, cpu.isProcessed());
//        cpu.notProcessed();
//        assertEquals(false, cpu.isProcessed());
//    }
//
//    @org.junit.Test
//    void setData(DataBatch DB) {
//        assertEquals(null, cpu.getData());
//        cpu.setData(DB);
//        assertEquals(DB, cpu.getData());
//    }
//
//    @org.junit.Test
//    void processed() {
//        cpu.notProcessed();
//        assertEquals(false, cpu.isProcessed());
//        cpu.processed();
//        assertEquals(true,cpu.isProcessed());
//    }
//
//    @org.junit.Test
//    void notProcessed() {
//        cpu.processed();
//        assertEquals(true,cpu.isProcessed());
//        cpu.notProcessed();
//        assertEquals(false, cpu.isProcessed());
//    }
//
//    @org.junit.Test
//    void process() {
//        int initTicks = cpu.getTicks();
//        assertEquals(false,cpu.isProcessed());
//        int finalTicks = 4; // defends on type of data
//        cpu.setTicks(finalTicks);
//        assertEquals(true, cpu.isProcessed());
//    }
//
//    @org.junit.Test
//    void sendProcessedData() {
//        cpu.sendProcessedData(DB);
//        assertEquals(true, cpu.isReceived());
//    }
//
//    @org.junit.Test
//    void isReceived() {
//        assertEquals(false, cpu.isReceived());
//        cpu.sendProcessedData(DB);
//        assertEquals(true, cpu.isReceived());
//    }
//
//    @org.junit.Test
//    void setTicks(int t){
//        cpu.setTicks(t);
//        assertEquals(t,cpu.getTicks());
//    }
//
//    @org.junit.Test
//    void getTicks(){
//        assertEquals(0,cpu.getTicks());
//        cpu.setTicks(5);
//        assertEquals(5,cpu.getTicks());
//    }
//
//    @org.junit.Test
//    void advanceTick() {
//        int i = cpu.getTicks();
//        cpu.advanceTick();
//        assertEquals(i + 1,cpu.getTicks());
//    }
//}