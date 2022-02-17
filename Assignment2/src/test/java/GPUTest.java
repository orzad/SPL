//package bgu.spl.mics.application.objects;
//
//import org.junit.Before;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.Assert.*;
//
//class GPUTest {
//
//    private static GPU gpu;
//    private static Model model;
//    private static Cluster cluster;
//    private static GPU.Type type;
//
//
//    @Before
//    public void setUp() throws Exception{
//        gpu = new GPU(type, model, cluster);
//    }
//
//    @org.junit.Test
//    void getType() {
//        assertEquals(type,gpu.getType());
//    }
//
//    @org.junit.Test
//    void getModel() {
//        assertEquals(model, gpu.getModel());
//        Model m2 = new Model();
//        gpu.setModel(m2);
//        assertEquals(m2, gpu.getModel());
//    }
//
//    @org.junit.Test
//    void getCluster() {
//        assertEquals(cluster, gpu.getCluster());
//    }
//
//    @org.junit.Test
//    void setModel() {
//        Model m2 = new Model();
//        gpu.setModel(m2);
//        assertEquals(m2, gpu.getModel());
//    }
//
//    @org.junit.Test
//    void isReceived() {
//        assertEquals(false, gpu.isReceived());
//        gpu.sendChunks();
//        assertEquals(true, gpu.isReceived());
//    }
//
//    @org.junit.Test
//    void sendChunks() {
//        //indication from cluster
//        gpu.sendChunks();
//        assertEquals(true, gpu.isReceived());
//    }
//
//    @org.junit.Test
//    void trainModel(DataBatch DB) {
//        while(gpu.getDBCounter()<gpu.getTotDB()) {
//            int i = gpu.getDBCounter();
//            gpu.trainModel(DB);
//            assertEquals(i + 1, gpu.getDBCounter());
//        }
//        // now they are equal, we'll check model status
//        assertEquals("Trained", this.getModel().status());
//    }
//
//    @org.junit.Test
//    void getResults() {
//    }
//
//    @org.junit.Test
//    void setTicks(int t){
//        gpu.setTicks(t);
//        assertEquals(t,gpu.getTicks());
//    }
//
//    @org.junit.Test
//    void getTicks(){
//        assertEquals(0,gpu.getTicks());
//        gpu.setTicks(5);
//        assertEquals(5,gpu.getTicks());
//    }
//
//    @org.junit.Test
//    void advanceTick() {
//        int i = gpu.getTicks();
//        gpu.advanceTick();
//        assertEquals(i + 1,gpu.getTicks());
//    }
//}
//}