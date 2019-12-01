package com.example.myidol.model;

public class IdolHot {
    String idphoto;
    String linkphoto;
    String ididol;
    String nameidol;
    String iduser;

    public IdolHot() {
    }

    public IdolHot(String idphoto, String linkphoto, String ididol, String nameidol, String iduser) {
        this.idphoto = idphoto;
        this.linkphoto = linkphoto;
        this.ididol = ididol;
        this.nameidol = nameidol;
        this.iduser = iduser;
    }

    public String getIdphoto() {
        return idphoto;
    }

    public void setIdphoto(String idphoto) {
        this.idphoto = idphoto;
    }

    public String getLinkphoto() {
        return linkphoto;
    }

    public void setLinkphoto(String linkphoto) {
        this.linkphoto = linkphoto;
    }

    public String getIdidol() {
        return ididol;
    }

    public void setIdidol(String ididol) {
        this.ididol = ididol;
    }

    public String getNameidol() {
        return nameidol;
    }

    public void setNameidol(String nameidol) {
        this.nameidol = nameidol;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }
}
