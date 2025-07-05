package com.example.uas_mobile;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LayananActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> dataList;
    ArrayList<Integer> idList;
    DatabaseHelper dbHelper;
    Button btnNavDashboard, btnNavPelanggan, btnNavTransaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layanan);

        listView = findViewById(R.id.listLayanan);
        dbHelper = new DatabaseHelper(this);

        findViewById(R.id.btnTambahLayanan).setOnClickListener(v -> {
            Intent intent = new Intent(LayananActivity.this, FormLayananActivity.class);
            startActivity(intent);
        });

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            int selectedId = idList.get(position);
            showAksiDialog(selectedId);
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
        Cursor cursor = db.rawQuery("SELECT * FROM layanan ORDER BY id_layanan DESC", null);

        dataList = new ArrayList<>();
        idList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String nama = cursor.getString(1);
                double harga = cursor.getDouble(2);

                dataList.add(nama + " - Rp " + harga + " /kg");
                idList.add(id);
            } while (cursor.moveToNext());
        }

        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList));
        cursor.close();
        db.close();
    }

    private void showAksiDialog(int id) {
        CharSequence[] pilihan = {"Edit", "Hapus"};

        new AlertDialog.Builder(this)
                .setTitle("Pilih Aksi")
                .setItems(pilihan, (dialog, which) -> {
                    Intent intent;
                    switch (which) {
                        case 0:
                            intent = new Intent(this, FormEditLayananActivity.class);
                            intent.putExtra("id_layanan", id);
                            startActivity(intent);
                            break;
                        case 1:
                            konfirmasiHapus(id);
                            break;
                    }
                }).show();
    }

    private void konfirmasiHapus(int id) {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Hapus")
                .setMessage("Yakin ingin menghapus layanan ini?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL("DELETE FROM layanan WHERE id_layanan = " + id);
                    db.close();
                    Toast.makeText(this, "Layanan berhasil dihapus", Toast.LENGTH_SHORT).show();
                    tampilkanData();
                })
                .setNegativeButton("Batal", null)
                .show();
    }
    private void setupBottomButtons() {
        btnNavDashboard = findViewById(R.id.btnNavDashboard);
        btnNavPelanggan = findViewById(R.id.btnNavPelanggan);
        btnNavTransaksi = findViewById(R.id.btnNavTransaksi);

        btnNavDashboard.setOnClickListener(v -> {
            startActivity(new Intent(LayananActivity.this, MainActivity.class));
        });

        btnNavPelanggan.setOnClickListener(v -> {
            startActivity(new Intent(LayananActivity.this, PelangganActivity.class));
        });

        btnNavTransaksi.setOnClickListener(v -> {
            startActivity(new Intent(LayananActivity.this, TransaksiActivity.class));
        });
    }
}
