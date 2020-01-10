package com.example.myidol.model;

public class GroupChat {
    String id;
    String namegroup;
    String timeCreate;
    String idAdmin;
    String imageGroup;

    public String getImageGroup() {
        return imageGroup;
    }

    public void setImageGroup(String imageGroup) {
        this.imageGroup = imageGroup;
    }

    public GroupChat(String id, String namegroup, String timeCreate, String idAdmin, String imageGroup) {
        this.id = id;
        this.namegroup = namegroup;
        this.timeCreate = timeCreate;
        this.idAdmin = idAdmin;
        this.imageGroup = imageGroup;
    }

    public GroupChat() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamegroup() {
        return namegroup;
    }

    public void setNamegroup(String namegroup) {
        this.namegroup = namegroup;
    }

    public String getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(String timeCreate) {
        this.timeCreate = timeCreate;
    }

    public String getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(String idAdmin) {
        this.idAdmin = idAdmin;
    }
}
