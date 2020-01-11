package com.example.myidol.model;

public class Notification {
    String idpost;
    String iduser;
    String text;
    String type;
    String time;

    public Notification(String idpost, String iduser, String text, String type,String time) {
        this.idpost = idpost;
        this.iduser = iduser;
        this.text = text;
        this.type = type;
        this.time = time;
    }

    public Notification() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIdpost() {
        return idpost;
    }

    public void setIdpost(String idpost) {
        this.idpost = idpost;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
