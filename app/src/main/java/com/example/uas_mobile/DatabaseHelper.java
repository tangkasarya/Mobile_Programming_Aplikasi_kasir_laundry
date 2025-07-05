package com.example.uas_mobile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "laundry.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // pelanggan
        db.execSQL("CREATE TABLE pelanggan (" +
                "id_pelanggan INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nama TEXT NOT NULL, " +
                "no_hp TEXT NOT NULL, " +
                "alamat TEXT NOT NULL)");

        // layanan
        db.execSQL("CREATE TABLE layanan (" +
                "id_layanan INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nama_layanan TEXT NOT NULL, " +
                "harga_per_kg REAL NOT NULL)");

        // transaksi
        db.execSQL("CREATE TABLE transaksi (" +
                "id_transaksi INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_pelanggan INTEGER NOT NULL, " +
                "id_layanan INTEGER NOT NULL, " +
                "berat REAL NOT NULL, " +
                "harga REAL NOT NULL, " +
                "total_harga REAL NOT NULL, " +
                "tanggal_transaksi TEXT NOT NULL, " +
                "tanggal_selesai TEXT NOT NULL, " +
                "status TEXT NOT NULL DEFAULT 'Proses', " +
                "status_pembayaran TEXT NOT NULL DEFAULT 'Belum Lunas', " +
                "FOREIGN KEY(id_pelanggan) REFERENCES pelanggan(id_pelanggan) ON DELETE CASCADE, " +
                "FOREIGN KEY(id_layanan) REFERENCES layanan(id_layanan))");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS transaksi");
        db.execSQL("DROP TABLE IF EXISTS layanan");
        db.execSQL("DROP TABLE IF EXISTS pelanggan");
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
    }
}
