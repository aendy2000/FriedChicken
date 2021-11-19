package com.example.project.Domain;

public class ProductManagerDomain {
    private String madm;
    private String masp;
    private String ten;
    private String gia;
    private String mota;
    private String hinhanh;

    public ProductManagerDomain(String madm, String masp, String ten, String gia, String mota, String hinhanh) {
        this.madm = madm;
        this.masp = masp;
        this.ten = ten;
        this.gia = gia;
        this.mota = mota;
        this.hinhanh = hinhanh;
    }

    public String getMadm() {
        return madm;
    }

    public void setMadm(String madm) {
        this.madm = madm;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getMasp() {
        return masp;
    }

    public void setMasp(String masp) {
        this.masp = masp;
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

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }
}
