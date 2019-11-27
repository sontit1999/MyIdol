package com.example.myidol.model;

public class Post {
    String nameAuthor;
    String linkImage;
    String decribe;
    String numberLike;
    String numberComment;
    String numberDownload;

    public Post(String nameAuthor, String linkImage, String decribe,  String numberLike,  String numberComment,  String numberDownload) {
        this.nameAuthor = nameAuthor;
        this.linkImage = linkImage;
        this.decribe = decribe;
        this.numberLike = numberLike;
        this.numberComment = numberComment;
        this.numberDownload = numberDownload;
    }

    public Post() {
    }

    public String getNameAuthor() {
        return nameAuthor;
    }

    public void setNameAuthor(String nameAuthor) {
        this.nameAuthor = nameAuthor;
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

    public  String getNumberLike() {
        return numberLike;
    }

    public void setNumberLike( String numberLike) {
        this.numberLike = numberLike;
    }

    public  String getNumberComment() {
        return numberComment;
    }

    public void setNumberComment( String numberComment) {
        this.numberComment = numberComment;
    }

    public  String getNumberDownload() {
        return numberDownload;
    }

    public void setNumberDownload(String numberDownload) {
        this.numberDownload = numberDownload;
    }
}
