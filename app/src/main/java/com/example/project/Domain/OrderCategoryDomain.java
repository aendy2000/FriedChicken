package com.example.project.Domain;

public class OrderCategoryDomain {
    private String trangthai;
    private String hinhanh;

    public OrderCategoryDomain(String trangthai, String hinhanh) {
        this.trangthai = trangthai;
        this.hinhanh = hinhanh;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }
}