//package main.java.bgu.spl.mics;
//
//import main.java.bgu.spl.mics.example.messages.ExampleBroadcast;
//import main.java.bgu.spl.mics.example.messages.ExampleEvent;
//import org.junit.Test;
//import org.junit.Before;
//
//import static org.junit.Assert.*;
//
//public class MessageBusTest {
//
//    private MessageBus messageBus;
//    private ExampleEvent event;
//    private ExampleBroadcast broadcast;
//    private MicroService service;
//
//    @Before
//    public void setUp() throws Exception {
//        this.messageBus = new MessageBusImpl();
//        this.event = new ExampleEvent("service");
//        this.broadcast = new ExampleBroadcast("service");
//        this.service = new MicroService("service") {
//            @Override
//            protected void initialize() {}
//        };
//    }
//
//    @Test
//    public void subscribeEvent() {
//        messageBus.register(service);
//        assertEquals(messageBus.sendEvent(event),null);
//        try {
//            assertEquals(messageBus.awaitMessage(service), event);
//            messageBus.subscribeEvent(event.getClass(),service);
//            assertNotNull(messageBus.sendEvent(event));
//        }
//        catch (InterruptedException e){}
//
//    }
//
//    @Test
//    public void subscribeBroadcast() {
//        messageBus.register(service);
//        try {
//            messageBus.subscribeBroadcast(broadcast.getClass(),service);
//            messageBus.sendBroadcast(broadcast);
//            assertEquals(messageBus.awaitMessage(service), broadcast);
//        }
//        catch (InterruptedException e){}
//    }
//
//    @Test
//    public void complete() {
//        messageBus.register(service);
//        assertNull(messageBus.sendEvent(event));
//        try {
//            messageBus.awaitMessage(service);
//            messageBus.subscribeEvent(event.getClass(),service);
//            Future<String> O = messageBus.sendEvent(event);
//            Thread.sleep(1000);
//            assertNotNull(O);
//        }
//        catch(InterruptedException e){}
//    }
//
//    @Test
//    public void sendBroadcast() {
//        try{
//            messageBus.register(service);
//            assertEquals(messageBus.awaitMessage(service),broadcast);
//            messageBus.subscribeBroadcast(broadcast.getClass(),service);
//            messageBus.sendBroadcast(broadcast);
//
//        }
//        catch(InterruptedException e){}
//    }
//
//    @Test
//    public void sendEvent() {
//        try{
//            messageBus.register(service);
//            assertEquals(messageBus.awaitMessage(service),event);
//            messageBus.subscribeEvent(event.getClass(),service);
//            messageBus.sendEvent(event);
//        }
//        catch(InterruptedException e){}
//    }
//
//    @Test
//    public void register() {
//        try{
//            assertNull(messageBus.awaitMessage(service));
//            messageBus.subscribeEvent(event.getClass(),service);
//            messageBus.sendEvent(event);
//
//            messageBus.register(service);
//            assertEquals(messageBus.awaitMessage(service),event);
//            messageBus.subscribeEvent(event.getClass(),service);
//            messageBus.sendEvent(event);
//        }
//        catch(InterruptedException e){}
//    }
//
//    @Test
//    public void unregister() {
//        try{
//            messageBus.unregister(service);
//            assertNull(messageBus.awaitMessage(service));
//            messageBus.subscribeEvent(event.getClass(),service);
//            messageBus.sendEvent(event);
//        }
//        catch(InterruptedException e){}
//    }
//
//    @Test
//    public void awaitMessage() {
//        try{
//            assertNotNull(messageBus.awaitMessage(service));
//            messageBus.register(service);
//            messageBus.subscribeEvent(event.getClass(),service);
//            messageBus.sendEvent(event);
//        }
//        catch(InterruptedException e){}
//    }
//}
//
