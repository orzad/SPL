//package main.java.bgu.spl.mics;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.Assert.*;
//
//public class FutureTest {
//
//    private Future<Object> O;
//
//    @Before
//    public void setUp() throws Exception {
//        Future<Object> O = new Future<Object>();
//        this.O = O;
//    }
//
//    @Test
//    public void get1() {
//        assertNull(O.get());
//        O.resolve(O);
//        assertNotNull(O.get());
//    }
//
//    @Test
//    public void resolve() {
//        assertFalse(O.isDone());
//        O.resolve(0);
//        assertTrue(O.isDone());
//    }
//
//    @Test
//    public void Get2() {
//        try {
//            assertEquals(O.get(10, TimeUnit.MILLISECONDS), null);
//            assertNull(O.get(500, TimeUnit.MILLISECONDS));
//            Thread.sleep(1000);
//            O.resolve(1);
//            assertEquals(O.get(1000, TimeUnit.MILLISECONDS),1);
//            Thread.sleep(500);
//            O.resolve(1);
//
//        }
//        catch (InterruptedException i){
//            System.out.println("Failed in Get2 test");
//        }
//    }
//}