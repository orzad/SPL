package bgu.spl.net.impl.BGSserver;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class User {
    private String name;
    private String password;
    private String birthday;
    private List<User> followers;
    private List<User> following;
    private List<User> blockers;
    private int numPosts;
    private int loginTime;
    private Queue<LinkedList<String>> awaitingMessages;
    private boolean loggedIn;
    private int connectionId;

    public User() {
        this.name = null;
        this.password = null;
        this.birthday = null;
        this.followers = new LinkedList<>();
        this.following = new LinkedList<>();
        this.blockers = new LinkedList<>();
        this.numPosts = 0;
        this.loginTime = 0;
        this.awaitingMessages = new ConcurrentLinkedQueue<>();
        this.loggedIn = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public List<User> getFollowing() {
        return following;
    }

    public void setFollowing(List<User> following) {
        this.following = following;
    }

    public int getNumPosts() {
        return numPosts;
    }

    public void setNumPosts(int numPosts) {
        this.numPosts = numPosts;
    }

    public Queue<LinkedList<String>> getAwaitingMessages() {
        return awaitingMessages;
    }

    public void setAwaitingMessages(Queue<LinkedList<String>> awaitingMessages) {
        this.awaitingMessages = awaitingMessages;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public List<User> getBlockers() {
        return blockers;
    }

    public void setBlockers(List<User> blockers) {
        this.blockers = blockers;
    }

    public int getAge(){
        int year = Integer.parseInt(getBirthday().substring(getBirthday().length() - 4));
        return 2022 - year;
    }

    public void incrementPosts(){
        this.numPosts++;
    }
}
