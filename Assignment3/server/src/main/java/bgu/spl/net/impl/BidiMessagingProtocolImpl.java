package bgu.spl.net.impl;

import bgu.spl.net.api.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSserver.Database;

public class BidiMessagingProtocolImpl<T> implements BidiMessagingProtocol<Operation> {
    private int connectionId;
    private Connections connections;
    private boolean shouldTerminate;

    public BidiMessagingProtocolImpl(){
        shouldTerminate = false;
    }

    @Override
    public void start(int connectionId, Connections<Operation> connections) {
        this.connectionId = connectionId;
        this.connections = Database.getInstance().getConnections();
    }

    @Override
    public void process(Operation message) {
        message.setConnectionId(connectionId);
        Database.getInstance().getCallbacks().get(message.getOp()).call(message);
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
