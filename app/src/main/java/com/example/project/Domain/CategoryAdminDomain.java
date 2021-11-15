package com.example.project.Domain;

public class CategoryAdminDomain {
    private String tendm;
    private String madm;
    private String hinhanhdm;

    public CategoryAdminDomain(String tendm, String madm, String hinhanhdm) {
        this.tendm = tendm;
        this.madm = madm;
        this.hinhanhdm = hinhanhdm;
    }

    public String getTendm() {
        return tendm;
    }

    public void setTendm(String tendm) {
        this.tendm = tendm;
    }

    public String getMadm() {
        return madm;
    }

    public void setMadm(String madm) {
        this.madm = madm;
    }

    public String getHinhanhdm() {
        return hinhanhdm;
    }

    public void setHinhanhdm(String hinhanhdm) {
        this.hinhanhdm = hinhanhdm;
    }
}
