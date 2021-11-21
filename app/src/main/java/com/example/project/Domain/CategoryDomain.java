package com.example.project.Domain;

import java.io.Serializable;

public class CategoryDomain implements Serializable {
    private String title;
    private String pic;
    private String UserID;
    private String idDm;
    public CategoryDomain(String title, String pic, String UserID, String idDm) {
        this.title = title;
        this.pic = pic;
        this.UserID = UserID;
        this.idDm = idDm;
    }

    public String getIdDm() {
        return idDm;
    }

    public void setIdDm(String idDm) {
        this.idDm = idDm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }
}
