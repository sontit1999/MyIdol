package com.example.myidol.model;

public class Message {
    String content;
    String idsendUser;
    String idreciveUser;
    String timeSend;

    public Message(String content, String idsendUser, String idreciveUser, String timeSend) {
        this.content = content;
        this.idsendUser = idsendUser;
        this.idreciveUser = idreciveUser;
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

    public String getIdsendUser() {
        return idsendUser;
    }

    public void setIdsendUser(String idsendUser) {
        this.idsendUser = idsendUser;
    }

    public String getIdreciveUser() {
        return idreciveUser;
    }

    public void setIdreciveUser(String idreciveUser) {
        this.idreciveUser = idreciveUser;
    }

    public String getTimeSend() {
        return timeSend;
    }

    public void setTimeSend(String timeSend) {
        this.timeSend = timeSend;
    }
}
