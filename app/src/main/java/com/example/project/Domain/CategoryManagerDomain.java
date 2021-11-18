package com.example.project.Domain;

public class CategoryManagerDomain {
    private String hinhanh;
    private String ten;
    private String id;

    public CategoryManagerDomain(String hinhanh, String ten, String id) {
        this.hinhanh = hinhanh;
        this.ten = ten;
        this.id = id;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
