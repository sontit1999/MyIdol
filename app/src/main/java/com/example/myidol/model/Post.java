package com.example.myidol.model;

public class Post {
    String idpost;
    String linkImage;
    String decribe;
    String publisher;
    String timepost;

    public Post(String idpost, String linkImage, String decribe,String publisher,String timepost) {
        this.idpost = idpost;
        this.linkImage = linkImage;
        this.decribe = decribe;
        this.publisher = publisher;
        this.timepost = timepost;
    }

    public Post() {
    }

    public String getTimepost() {
        return timepost;
    }

    public void setTimepost(String timepost) {
        this.timepost = timepost;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public String getDecribe() {
        return decribe;
    }

    public void setDecribe(String decribe) {
        this.decribe = decribe;
    }


    public String getIdpost() {
        return idpost;
    }

    public void setIdpost(String idpost) {
        this.idpost = idpost;
    }
}
