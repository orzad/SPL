package bgu.spl.net.impl.BGSserver;

import bgu.spl.net.impl.ConnectionsImpl;
import bgu.spl.net.impl.Operation;
import bgu.spl.net.srv.ConnectionHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Database {
    private List<callback> callbacks;
    private List<String> messages;
    private List<String> wordsToFilter;
    private ConcurrentHashMap<String,User> users;
    private ConcurrentHashMap<Integer,User> usersById;
    private ConnectionsImpl connections;
    private int idGenerator;

    private Database() {
        this.callbacks = new LinkedList<>();
        this.messages = new LinkedList<>();
        this.wordsToFilter = new LinkedList<>();
        wordsToFilter.add("balls");
        this.users = new ConcurrentHashMap();
        this.usersById = new ConcurrentHashMap<>();
        this.connections = new ConnectionsImpl();
        this.idGenerator = 0;       // might change
        initialize();
    }

    private static class DatabaseInstance{
        private static Database instance = new Database();
    }

    public static Database getInstance() {
        return DatabaseInstance.instance;
    }

    public void initialize(){
        callback call0 = (Operation op1)->{};
        callbacks.add(0,call0);
        callback call1 = (Operation op1)->{
            if (users.containsKey(op1.getUsername())){
                sendError(op1);
            }
            else {
                User user = new User();
                user.setName(op1.getUsername());
                user.setPassword(op1.getPassword());
                user.setBirthday(op1.getBirthday());
                usersById.put(op1.getConnectionId(), user);
                user.setConnectionId(op1.getConnectionId());
                users.put(user.getName(), user);
                sendAck(op1);
            }
        };
        callbacks.add(1,call1);

        callback call2 = (Operation op2)->{
            if(!users.containsKey(op2.getUsername()) ||
            !users.get(op2.getUsername()).getPassword().equals(op2.getPassword()) ||
            users.get(op2.getUsername()).isLoggedIn() |
            op2.getCaptcha() == 0){
                sendError(op2);
            }
            else{
                User user = users.get(op2.getUsername());
                user.setLoggedIn(true);
                usersById.put(op2.getConnectionId(), user);
                user.setConnectionId(op2.getConnectionId());
                Queue queue = user.getAwaitingMessages();
                while(!queue.isEmpty()){
                    sendNotification((LinkedList) queue.poll());
                }
            }
        };
        callbacks.add(2, call2);

        callback call3 = (Operation op3)->{  // this is wrong i have no way of knowing name
            // using op3, but the structure is probably like this
            if (!usersById.containsKey(op3.getConnectionId()) || !usersById.get(op3.getConnectionId()).isLoggedIn()){
                sendError(op3);
            }else {
                sendAck(op3);
                usersById.get(op3.getConnectionId()).setLoggedIn(false);
                usersById.remove(op3.getConnectionId());
                connections.getIdMap().remove(op3.getConnectionId());
            }
        };
        callbacks.add(3, call3);

        callback call4 = (Operation op4)->{
            if (!usersById.containsKey(op4.getConnectionId())){
                sendError(op4);
            }else {
                String name = usersById.get(op4.getConnectionId()).getName();
                boolean follow = op4.getFollow() == 0;
                User user = users.get(name);
                User fUser = users.get(op4.getUsername());
                if (!user.getBlockers().contains(fUser) & !fUser.getBlockers().contains(user)) {
                    if (user.getFollowing().contains(fUser) ==  follow) {
                        sendError(op4);
                    } else if (follow) {
                        user.getFollowing().add(fUser);
                        fUser.getFollowers().add(user);
                        sendAck(op4, fUser.getName());
                    }else if(!follow){
                        user.getFollowing().remove(fUser);
                        fUser.getFollowers().remove(user);
                        sendAck(op4, fUser.getName());
                    }
                }
            }
        };
        callbacks.add(4, call4);

        callback call5 = (Operation op5)->{
            if(!usersById.containsKey(op5.getConnectionId())){
                sendError(op5);
            }
            else {
                String username = usersById.get(op5.getConnectionId()).getName();
                if (!users.get(username).isLoggedIn()) {
                    sendError(op5);
                } else {
                    LinkedList names = new LinkedList();
                    String content = op5.getContent();
                    int i = 0;
                    while (i < content.length()) {
                        if (content.charAt(i) == '@') {
                            i++;
                            int startIndex = i;
                            int finalIndex = i;
                            while (i < content.length() && content.charAt(i) != ' ') {
                                i++;
                                finalIndex = i;
                            }
                            names.add(content.subSequence(startIndex, finalIndex));
                        }
                        i++;
                    }
                    User user = users.get(username);
                    user.incrementPosts();
                    messages.add(content);
                    sendAck(op5);
                    List followers = users.get(username).getFollowers();

                    for (int j = 0; j < followers.size() ; j++) {
                        if ( !user.getBlockers().contains(((User)followers.get(j)))) {
                            String name = ((User) followers.get(j)).getName();
                            if (!names.contains(name)) {
                                if (users.get(name).isLoggedIn()) {
                                    sendNotification(username, name, (byte) 0, content);

                                } else {
                                    saveNotification(username, name, "0", content);
                                }
                            }
                        }
                    }
                    for (int k = 0; k < names.size(); k++) {
                        if(  !(user.getBlockers().contains(users.get(names.get(k))))) {
                            if (users.get((String) names.get(k)).isLoggedIn()) {
                                sendNotification(username, (String) names.get(k), (byte) 0, content);
                            } else {
                                saveNotification(username, (String) names.get(k), "0", content);
                            }
                        }
                    }
                }
            }
        };
        callbacks.add(5,call5);

        callback call6 = (Operation op6)->{  // what if not connected
            if(!usersById.containsKey(op6.getConnectionId())){
                sendError(op6);
            }
            else {
                String senderName = usersById.get(op6.getConnectionId()).getName();
                String receiverName = op6.getUsername();

                if (!users.get(senderName).isLoggedIn() | !users.containsKey(receiverName)) {
                    sendError(op6);
                } else {
                    String content = op6.getContent()+' ';
                    content = filter(content);
                    messages.add(content);
                    if (!users.get(senderName).getBlockers().contains(users.get(receiverName))) {
                        if (users.get(receiverName).isLoggedIn()) {
                            sendNotification(senderName, receiverName, (byte) 1, content);
                        } else {
                            saveNotification(senderName, receiverName, "1", content);
                        }
                    }
                }
            }
        };
        callbacks.add(6,call6);

        callback call7 = (Operation op7)->{
            if (!usersById.containsKey(op7.getConnectionId())){
                sendError(op7);
            }
            else {
                String name = usersById.get(op7.getConnectionId()).getName();
                if (!users.get(name).isLoggedIn()) {
                    sendError(op7);
                } else {
                    Iterator iterator = users.values().iterator();
                    while (iterator.hasNext()){
                        User user  = (User) iterator.next();
                        if (user.getName() != name & !users.get(name).getBlockers().contains(user)) {
                            String content = user.getAge() + " " + user.getNumPosts() + " " + user.getFollowers().size() + " " + user.getFollowing().size();
                            sendAck(op7, content);
                        }
                    }
                }
            }
        };
        callbacks.add(7, call7);

        callback call8 = (Operation op8)->{
            if (!usersById.containsKey(op8.getConnectionId())){
                sendError(op8);
            }
            else {
                String name = usersById.get(op8.getConnectionId()).getName();
                if (!users.get(name).isLoggedIn()) {
                    sendError(op8);
                } else {
                    List names = op8.getUsernames();
                    for(int i = 0; i<names.size() ; i++) {
                        if(users.containsKey(names.get(i)) && !users.get(name).getBlockers().contains(users.get(names.get(i)))) {
                            User user = users.get(names.get(i));
                            String content = user.getAge() + " " + user.getNumPosts() + " " + user.getFollowers().size() + " " + user.getFollowing().size();
                            sendAck(op8, content);
                        }
                    }
                }
            }

        };
        callbacks.add(8, call8);
        callbacks.add(9,call0);
        callbacks.add(10,call0);
        callbacks.add(11,call0);

        callback call12 = (Operation op12)->{
            if (!users.containsKey(op12.getUsername()) |
                    (users.containsKey(op12.getUsername()) &
                            users.get(op12.getUsername()).getBlockers().contains(usersById.get(op12.getConnectionId())))){
                sendError(op12);
            }
            else {
                User blocking = usersById.get(op12.getConnectionId());
                User blocked = users.get(op12.getUsername());
                blocked.getBlockers().add(blocking);
                if(blocking.getFollowing().contains(blocked)){
                    blocking.getFollowing().remove(blocked);
                    blocked.getFollowers().remove(blocking);
                }
                if(blocking.getFollowers().contains(blocked)){
                    blocking.getFollowers().remove(blocked);
                    blocked.getFollowing().remove(blocking);
                }
            }
        };
        callbacks.add(12, call12);
    }

    public ConcurrentHashMap<Integer, User> getUsersById() {
        return usersById;
    }

    private void sendError(Operation op){
        Operation operation = new Operation();
        operation.setOp((short)11);
        operation.setMessage(op.getOp());
        connections.send(op.getConnectionId(), operation);
    }

    private void sendAck(Operation op){
        Operation operation = new Operation();
        operation.setOp((short)10);
        operation.setMessage(op.getOp());
        connections.send(op.getConnectionId(), operation);
    }

    private void sendAck(Operation op, String content){
        Operation operation = new Operation();
        operation.setOp((short)10);
        operation.setMessage(op.getOp());
        operation.setContent(content);
        connections.send(op.getConnectionId(), operation);
    }

    private void sendNotification(String senderName, String receiverName, byte type, String content){
        Operation operation = new Operation();
        operation.setOp((short) 9);
        operation.setNotification(type);
        operation.setPostingUser(senderName);
        operation.setUsername(receiverName);
        operation.setContent(content);
        int id = users.get(receiverName).getConnectionId();
        connections.send(id, operation);
    }

    private void sendNotification(LinkedList list){
        sendNotification((String)list.get(0),(String) list.get(1), (byte) Integer.parseInt((String) list.get(2)), (String)list.get(3));
    }

    private String filter(String content){
        int startIndex = 0;
        int finalIndex = 0;
        String str ="";
        while(finalIndex<content.length()){
            if (content.charAt(finalIndex) == ' '){
                if(wordsToFilter.contains(content.subSequence(startIndex, finalIndex))){
                    content = content.substring(0,startIndex)+"filtered " +content.substring(finalIndex);
                    finalIndex = startIndex +9;
                    startIndex = finalIndex+1;
                }else{
                    startIndex =finalIndex+1;
                }
            }
            finalIndex++;
        }
        return content;
    }

    public void saveNotification(String username, String name , String type, String content ){
        LinkedList notification = new LinkedList();
        notification.add(username);
        notification.add(name);
        notification.add(type);
        notification.add(content);
        users.get(name).getAwaitingMessages().add(notification);
    }

    public ConnectionsImpl getConnections() {
        return connections;
    }

    public List<callback> getCallbacks() {
        return callbacks;
    }

    public int getIdGenerator() {
        return idGenerator;
    }

    public void setIdGenerator(int idGenerator) {
        this.idGenerator = idGenerator;
    }
}
