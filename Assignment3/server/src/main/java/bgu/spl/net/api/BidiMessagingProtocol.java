package bgu.spl.net.api;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSserver.Database;
import bgu.spl.net.impl.Operation;

public interface BidiMessagingProtocol<T> {
 
    /**
     * process the given message 
     * @param msg the received message
     * @return the response to send or null if no response is expected by the client
     */
    void process(T msg);
 
    /**
     * @return true if the connection should be terminated
     */
    boolean shouldTerminate();

    public void start(int connectionId, Connections<Operation> connections);
 
}