package com.example.uas_mobile;

public class TransaksiModel {
    private int id;
    private String namaPelanggan, layanan, status, statusPembayaran;
    private double berat, total;

    public TransaksiModel(int id, String namaPelanggan, String layanan, double berat, double total, String status, String statusPembayaran) {
        this.id = id;
        this.namaPelanggan = namaPelanggan;
        this.layanan = layanan;
        this.berat = berat;
        this.total = total;
        this.status = status;
        this.statusPembayaran = statusPembayaran;
    }

    public int getId() {
        return id;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public String getLayanan() {
        return layanan;
    }

    public double getBerat() {
        return berat;
    }

    public double getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusPembayaran() {
        return statusPembayaran;
    }
}
