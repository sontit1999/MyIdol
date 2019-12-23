package com.example.myidol.model;

public class Message {
    String content;
    String nameUser;
    String timeSend;
    public Message(String content, String nameUser, String timeSend) {
        this.content = content;
        this.nameUser = nameUser;
        this.timeSend = timeSend;
    }

    public Message() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getTimeSend() {
        return timeSend;
    }

    public void setTimeSend(String timeSend) {
        this.timeSend = timeSend;
    }
}
