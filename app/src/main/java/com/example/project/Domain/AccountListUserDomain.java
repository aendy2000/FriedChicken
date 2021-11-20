package com.example.project.Domain;

public class AccountListUserDomain {
    private String matk;
    private String avatar;
    private String ten;
    private String role;
    public AccountListUserDomain(String matk, String ten, String avatar, String role) {
        this.matk = matk;
        this.ten = ten;
        this.avatar = avatar;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getMatk() {
        return matk;
    }

    public void setMatk(String matk) {
        this.matk = matk;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
