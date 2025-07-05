package com.example.uas_mobile;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TransaksiActivity extends AppCompatActivity {

    ListView listView;
    EditText edtSearch;
    ArrayList<TransaksiModel> listTransaksi;
    DatabaseHelper dbHelper;
    TransaksiAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        listView = findViewById(R.id.listTransaksi);
        edtSearch = findViewById(R.id.edtSearchTransaksi);
        dbHelper = new DatabaseHelper(this);

        findViewById(R.id.btnTambahTransaksi).setOnClickListener(v -> {
            startActivity(new Intent(this, FormTransaksiActivity.class));
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    adapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            TransaksiModel transaksi = (TransaksiModel) adapter.getItem(position);
            showAksiDialog(transaksi.getId());
        });

        setupBottomButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tampilkanData();
    }

    private void tampilkanData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT t.id_transaksi, p.nama, l.nama_layanan, t.berat, t.total_harga, t.status, t.status_pembayaran " +
                "FROM transaksi t " +
                "JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan " +
                "JOIN layanan l ON t.id_layanan = l.id_layanan " +
                "ORDER BY t.id_transaksi DESC";

        Cursor cursor = db.rawQuery(query, null);
        listTransaksi = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String namaPelanggan = cursor.getString(1);
                String layanan = cursor.getString(2);
                double berat = cursor.getDouble(3);
                double total = cursor.getDouble(4);
                String status = cursor.getString(5);
                String statusBayar = cursor.getString(6);

                TransaksiModel transaksi = new TransaksiModel(
                        id, namaPelanggan, layanan, berat, total, status, statusBayar
                );

                listTransaksi.add(transaksi);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        adapter = new TransaksiAdapter(this, listTransaksi);
        listView.setAdapter(adapter);

        // klik data untuk aksi
        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            int selectedId = ((TransaksiModel) adapter.getItem(position)).getId();
            showAksiDialog(selectedId);
        });
    }

    private void showAksiDialog(int idTransaksi) {
        CharSequence[] pilihan = {"Lihat", "Ubah Status", "Ubah Status Pembayaran"};

        new AlertDialog.Builder(this)
                .setTitle("Pilih Aksi")
                .setItems(pilihan, (dialog, which) -> {
                    Intent intent;
                    switch (which) {
                        case 0:
                            intent = new Intent(this, DetailTransaksiActivity.class);
                            intent.putExtra("id_transaksi", idTransaksi);
                            startActivity(intent);
                            break;
//                        case 1:
//                            intent = new Intent(this, FormEditTransaksiActivity.class);
//                            intent.putExtra("id_transaksi", idTransaksi);
//                            startActivity(intent);
//                            break;
                        case 1:
                            ubahStatus(idTransaksi);
                            break;
                        case 2:
                            ubahStatusPembayaran(idTransaksi);
                            break;
//                        case 3:
//                            hapusTransaksi(idTransaksi);
//                            break;
                    }
                }).show();
    }

    private void ubahStatus(int idTransaksi) {
        CharSequence[] status = {"Proses", "Selesai", "Diambil"};
        new AlertDialog.Builder(this)
                .setTitle("Ubah Status")
                .setItems(status, (dialog, which) -> {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL("UPDATE transaksi SET status = ? WHERE id_transaksi = ?",
                            new Object[]{status[which], idTransaksi});
                    db.close();
                    Toast.makeText(this, "Status diperbarui", Toast.LENGTH_SHORT).show();
                    tampilkanData();
                }).show();
    }

    private void ubahStatusPembayaran(int idTransaksi) {
        CharSequence[] status = {"Belum Lunas", "Lunas"};
        new AlertDialog.Builder(this)
                .setTitle("Ubah Status Pembayaran")
                .setItems(status, (dialog, which) -> {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL("UPDATE transaksi SET status_pembayaran = ? WHERE id_transaksi = ?",
                            new Object[]{status[which], idTransaksi});
                    db.close();
                    Toast.makeText(this, "Status pembayaran diperbarui", Toast.LENGTH_SHORT).show();
                    tampilkanData();
                }).show();
    }

    private void setupBottomButtons() {
        findViewById(R.id.btnNavDashboard).setOnClickListener(v -> {
            startActivity(new Intent(TransaksiActivity.this, MainActivity.class));
        });

        findViewById(R.id.btnNavPelanggan).setOnClickListener(v -> {
            startActivity(new Intent(TransaksiActivity.this, PelangganActivity.class));
        });

        findViewById(R.id.btnNavLayanan).setOnClickListener(v -> {
            startActivity(new Intent(TransaksiActivity.this, LayananActivity.class));
        });

        findViewById(R.id.btnNavTransaksi).setOnClickListener(v -> {
            // Sedang di halaman ini, jadi tidak perlu pindah
            Toast.makeText(this, "Sudah di halaman Transaksi", Toast.LENGTH_SHORT).show();
        });
    }

//    private void hapusTransaksi(int idTransaksi) {
//        new AlertDialog.Builder(this)
//                .setTitle("Konfirmasi Hapus")
//                .setMessage("Yakin ingin menghapus transaksi ini?")
//                .setPositiveButton("Ya", (dialog, which) -> {
//                    SQLiteDatabase db = dbHelper.getWritableDatabase();
//                    db.execSQL("DELETE FROM transaksi WHERE id_transaksi = " + idTransaksi);
//                    db.close();
//                    Toast.makeText(this, "Transaksi dihapus", Toast.LENGTH_SHORT).show();
//                    tampilkanData();
//                })
//                .setNegativeButton("Batal", null)
//                .show();
//    }
}
