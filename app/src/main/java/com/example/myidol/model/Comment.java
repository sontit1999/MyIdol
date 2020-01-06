package com.example.myidol.model;

public class Comment {
    String iduser;
    String content;
    String timecomment;

    public Comment() {
    }

    public Comment(String iduser, String content, String timecomment) {
        this.iduser = iduser;
        this.content = content;
        this.timecomment = timecomment;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimecomment() {
        return timecomment;
    }

    public void setTimecomment(String timecomment) {
        this.timecomment = timecomment;
    }
}
