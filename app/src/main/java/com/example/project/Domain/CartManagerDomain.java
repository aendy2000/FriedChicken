package com.example.project.Domain;

public class CartManagerDomain {
    private String taikhoan;
    private String somon;

    public CartManagerDomain(String taikhoan, String somon) {
        this.taikhoan = taikhoan;
        this.somon = somon;
    }

    public String getTaikhoan() {
        return taikhoan;
    }

    public void setTaikhoan(String taikhoan) {
        this.taikhoan = taikhoan;
    }

    public String getSomon() {
        return somon;
    }

    public void setSomon(String somon) {
        this.somon = somon;
    }
}
