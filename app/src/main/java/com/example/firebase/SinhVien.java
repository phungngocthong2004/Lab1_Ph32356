package com.example.firebase;

import com.google.gson.annotations.SerializedName;

public class SinhVien {
    @SerializedName("_id")
    private  String id;
    private String name;
    private String Masv;
    private  Double DiemTb;
    private byte[] avarta;

    public SinhVien(String name, String masv, Double diemTb, byte[] avarta) {
        this.name = name;
        Masv = masv;
        DiemTb = diemTb;
        this.avarta = avarta;
    }

    public SinhVien() {
    }

    public SinhVien(String name, String masv, Double diemTb) {
        this.name = name;
        Masv = masv;
        DiemTb = diemTb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMasv() {
        return Masv;
    }

    public void setMasv(String masv) {
        Masv = masv;
    }

    public Double getDiemTb() {
        return DiemTb;
    }

    public void setDiemTb(Double diemTb) {
        DiemTb = diemTb;
    }

    public byte[] getAvarta() {
        return avarta;
    }

    public void setAvarta(byte[] avarta) {
        this.avarta = avarta;
    }

    public String getId() {
        return id;
    }

}