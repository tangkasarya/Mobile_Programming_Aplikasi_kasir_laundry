package com.example.uas_mobile;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView txtTotalTransaksi, txtBelumLunas, txtSudahLunas, txtTotalPelanggan;
    ListView listTransaksiTerakhir;
    DatabaseHelper dbHelper;
    ArrayList<String> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTotalTransaksi = findViewById(R.id.txtTotalTransaksi);
        txtBelumLunas = findViewById(R.id.txtBelumLunas);
        txtSudahLunas = findViewById(R.id.txtSudahLunas);
        txtTotalPelanggan = findViewById(R.id.txtTotalPelanggan);
        listTransaksiTerakhir = findViewById(R.id.listTransaksiTerakhir);

        dbHelper = new DatabaseHelper(this);

        tampilkanStatistik();
        tampilkanTransaksiTerakhir();
        setupBottomNavigation();
    }

//    method menampilkan 4 card
    private void tampilkanStatistik() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursorTotal = db.rawQuery("SELECT COUNT(*) FROM transaksi", null);
        if (cursorTotal.moveToFirst()) {
            txtTotalTransaksi.setText(cursorTotal.getString(0));
        }
        cursorTotal.close();

        Cursor cursorBelum = db.rawQuery("SELECT COUNT(*) FROM transaksi WHERE status_pembayaran = 'Belum Lunas'", null);
        if (cursorBelum.moveToFirst()) {
            txtBelumLunas.setText(cursorBelum.getString(0));
        }
        cursorBelum.close();

        Cursor cursorLunas = db.rawQuery("SELECT COUNT(*) FROM transaksi WHERE status_pembayaran = 'Lunas'", null);
        if (cursorLunas.moveToFirst()) {
            txtSudahLunas.setText(cursorLunas.getString(0));
        }
        cursorLunas.close();

        Cursor cursorPelanggan = db.rawQuery("SELECT COUNT(*) FROM pelanggan", null);
        if (cursorPelanggan.moveToFirst()) {
            txtTotalPelanggan.setText(cursorPelanggan.getString(0));
        }
        cursorPelanggan.close();

        db.close();
    }

//    transaksi terakhir
    private void tampilkanTransaksiTerakhir() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT p.nama, l.nama_layanan, t.total_harga, t.status_pembayaran FROM transaksi t JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan JOIN layanan l ON t.id_layanan = l.id_layanan ORDER BY t.id_transaksi DESC LIMIT 5", null);

        dataList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String nama = cursor.getString(0);
                String layanan = cursor.getString(1);
                double total = cursor.getDouble(2);
                String status = cursor.getString(3);

                String info = nama + " - " + layanan + "\nRp " + total + " | " + status;
                dataList.add(info);
            } while (cursor.moveToNext());
        }

        listTransaksiTerakhir.setAdapter(new android.widget.ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList));
        cursor.close();
        db.close();
    }

    private void setupBottomNavigation() {
        findViewById(R.id.btnNavDashboard).setOnClickListener(v -> {
        });

        findViewById(R.id.btnNavPelanggan).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, PelangganActivity.class));
        });

        findViewById(R.id.btnNavLayanan).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LayananActivity.class));
        });

        findViewById(R.id.btnNavTransaksi).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, TransaksiActivity.class));
        });
    }


}
