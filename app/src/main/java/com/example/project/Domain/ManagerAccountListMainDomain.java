package com.example.project.Domain;

public class ManagerAccountListMainDomain {
    private String ten;
    private String hinhanh;
    private String id;

    public ManagerAccountListMainDomain(String ten, String hinhanh, String id) {
        this.ten = ten;
        this.hinhanh = hinhanh;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }
}
