package com.example.project.Domain;

public class CheckoutDomain {
    private String mamonan;
    private String ten;
    private String gia;
    private String soluong;
    private String tong;
    private String hinhanh;
    private String idUser;

    public CheckoutDomain(String mamonan, String ten, String gia, String soluong, String tong, String hinhanh, String idUser) {
        this.mamonan = mamonan;
        this.ten = ten;
        this.gia = gia;
        this.soluong = soluong;
        this.tong = tong;
        this.hinhanh = hinhanh;
        this.idUser = idUser;
    }

    public String getMamonan() {
        return mamonan;
    }

    public void setMamonan(String mamonan) {
        this.mamonan = mamonan;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getSoluong() {
        return soluong;
    }

    public void setSoluong(String soluong) {
        this.soluong = soluong;
    }

    public String getTong() {
        return tong;
    }

    public void setTong(String tong) {
        this.tong = tong;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
