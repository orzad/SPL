package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSserver.Database;
import bgu.spl.net.srv.ConnectionHandler;

import java.util.HashMap;
import java.util.Iterator;

public class ConnectionsImpl<T> implements Connections<T> {

    private HashMap<Integer, ConnectionHandler> idMap;

    public ConnectionsImpl() {
        this.idMap = new HashMap<>();
    }

    public HashMap<Integer, ConnectionHandler> getIdMap() {
        return idMap;
    }

    public void setIdMap(HashMap<Integer, ConnectionHandler> idMap) {
        this.idMap = idMap;
    }

    @Override
    public boolean send(int connectionId, T msg) {
        if(!idMap.containsKey(connectionId)) {
            return false;
        }
        idMap.get(connectionId).send(msg);
        return true;
    }

    @Override
    public void broadcast(T msg) {
        Iterator iterator = idMap.values().iterator();
        while(iterator.hasNext()){
            ConnectionHandler handler = (ConnectionHandler) iterator.next();
            handler.send(msg);
        }
    }

    @Override
    public void disconnect(int connectionId) {
        idMap.remove(connectionId);
    }

    public void connect(ConnectionHandler handler){
       int id = Database.getInstance().getIdGenerator();
       idMap.put(id, handler);
       handler.connectProtocol(id);
       Database.getInstance().setIdGenerator(id+1);
    }
}
