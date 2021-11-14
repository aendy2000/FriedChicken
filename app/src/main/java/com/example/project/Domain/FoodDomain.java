package com.example.project.Domain;

public class FoodDomain {
    private String mamonan;
    private String name;
    private String mota;
    private String gia;
    private String imgFood;
    private String UserID;

    public FoodDomain(String mamonan, String name, String mota, String gia, String imgFood, String UserID) {
        this.name = name;
        this.mota = mota;
        this.gia = gia;
        this.imgFood = imgFood;
        this.mamonan = mamonan;
        this.UserID = UserID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getImgFood() {
        return imgFood;
    }

    public void setImgFood(String idFood) {
        this.imgFood = imgFood;
    }

    public String getMamonan() {
        return mamonan;
    }

    public void setMamonan(String mamonan) {
        this.mamonan = mamonan;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

}
