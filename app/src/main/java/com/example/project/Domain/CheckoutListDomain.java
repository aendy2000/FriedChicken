package com.example.project.Domain;

public class CheckoutListDomain {
    private String mamonan;
    private String idUser;
    private String somon;
    private String ngaymua;
    private String trangthai;
    private String tongcong;

    public CheckoutListDomain(String mamonan, String idUser, String somon, String ngaymua, String trangthai, String tongcong) {
        this.mamonan = mamonan;
        this.idUser = idUser;
        this.somon = somon;
        this.ngaymua = ngaymua;
        this.trangthai = trangthai;
        this.tongcong = tongcong;
    }

    public String getSomon() {
        return somon;
    }

    public void setSomon(String somon) {
        this.somon = somon;
    }

    public String getNgaymua() {
        return ngaymua;
    }

    public void setNgaymua(String ngaymua) {
        this.ngaymua = ngaymua;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }

    public String getTongcong() {
        return tongcong;
    }

    public void setTongcong(String tongcong) {
        this.tongcong = tongcong;
    }

    public String getMamonan() {
        return mamonan;
    }

    public void setMamonan(String mamonan) {
        this.mamonan = mamonan;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
