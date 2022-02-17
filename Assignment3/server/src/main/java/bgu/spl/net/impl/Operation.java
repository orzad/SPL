package bgu.spl.net.impl;

import java.util.List;

public class Operation {
    private short op;
    private String username;
    private String password;
    private String birthday;
    private byte captcha;
    private byte follow;
    private String content;
    private String sendTime;
    private List<String> usernames;
    private byte notification;
    private String postingUser;
    private short message;
    private int connectionId;

    public Operation() {
        this.op = 0;
        this.username = null;
        this.password = null;
        this.birthday = null;
        this.captcha = 0;
        this.follow = 0;
        this.content = "";
        this.sendTime = null;
        this.usernames = null;
        this.notification = 0;
        this.postingUser = null;
        this.message = 0;
    }

    public short getOp() {
        return op;
    }

    public void setOp(short op) {
        this.op = op;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public byte getCaptcha() {
        return captcha;
    }

    public void setCaptcha(byte captcha) {
        this.captcha = captcha;
    }

    public byte getFollow() {
        return follow;
    }

    public void setFollow(byte follow) {
        this.follow = follow;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }

    public byte getNotification() {
        return notification;
    }

    public void setNotification(byte notification) {
        this.notification = notification;
    }

    public String getPostingUser() {
        return postingUser;
    }

    public void setPostingUser(String postingUser) {
        this.postingUser = postingUser;
    }

    public short getMessage() {
        return message;
    }

    public void setMessage(short message) {
        this.message = message;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }
}
