package com.example.uas_mobile;

public class PelangganModel {
    private int id;
    private String nama, noHp, alamat;

    public PelangganModel(int id, String nama, String noHp, String alamat) {
        this.id = id;
        this.nama = nama;
        this.noHp = noHp;
        this.alamat = alamat;
    }

    public int getId() { return id; }
    public String getNama() { return nama; }
    public String getNoHp() { return noHp; }
    public String getAlamat() { return alamat; }
}