package com.example.myidol.model;

public class Comment {
    String linkAvatar;
    String nameAuthor;
    String contentComment;
    String timeComment;
    public Comment(String linkAvatar, String nameAuthor, String contentComment, String timeComment) {
        this.linkAvatar = linkAvatar;
        this.nameAuthor = nameAuthor;
        this.contentComment = contentComment;
        this.timeComment = timeComment;
    }

    public Comment() {
    }

    public String getLinkAvatar() {
        return linkAvatar;
    }

    public void setLinkAvatar(String linkAvatar) {
        this.linkAvatar = linkAvatar;
    }

    public String getNameAuthor() {
        return nameAuthor;
    }

    public void setNameAuthor(String nameAuthor) {
        this.nameAuthor = nameAuthor;
    }

    public String getContentComment() {
        return contentComment;
    }

    public void setContentComment(String contentComment) {
        this.contentComment = contentComment;
    }

    public String getTimeComment() {
        return timeComment;
    }

    public void setTimeComment(String timeComment) {
        this.timeComment = timeComment;
    }
}
