package com.example.project.Domain;

public class CartDomain {
    private String mamonan;
    private String ten;
    private String gia;
    private String tong;
    private String soluong;
    private String hinhanh;
    private String iduser;

    public CartDomain(String mamonan, String ten, String gia, String tong, String soluong, String hinhanh, String iduser) {
        this.mamonan = mamonan;
        this.ten = ten;
        this.gia = gia;
        this.tong = tong;
        this.soluong = soluong;
        this.hinhanh = hinhanh;
        this.iduser = iduser;
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

    public String getTong() {
        return tong;
    }

    public void setTong(String tong) {
        this.tong = tong;
    }

    public String getSoluong() {
        return soluong;
    }

    public void setSoluong(String soluong) {
        this.soluong = soluong;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getMamonan() {
        return mamonan;
    }

    public void setMamonan(String mamonan) {
        this.mamonan = mamonan;
    }
}
