package com.example.uas_mobile;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PelangganActivity extends AppCompatActivity {

    ListView listView;
    EditText edtSearch;
    ArrayList<PelangganModel> dataList;
    DatabaseHelper dbHelper;
    PelangganAdapter adapter;

    Button btnNavDashboard, btnNavLayanan, btnNavTransaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelanggan);

        listView = findViewById(R.id.listPelanggan);
        edtSearch = findViewById(R.id.edtSearchPelanggan);
        dbHelper = new DatabaseHelper(this);

        findViewById(R.id.btnTambahPelanggan).setOnClickListener(v -> {
            Intent intent = new Intent(PelangganActivity.this, FormPelangganActivity.class);
            startActivity(intent);
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
            PelangganModel pelanggan = (PelangganModel) adapter.getItem(position);
            showAksiDialog(pelanggan.getId());
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
        Cursor cursor = db.rawQuery("SELECT * FROM pelanggan ORDER BY id_pelanggan DESC", null);

        dataList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String nama = cursor.getString(1);
                String noHp = cursor.getString(2);
                String alamat = cursor.getString(3);

                dataList.add(new PelangganModel(id, nama, noHp, alamat));
            } while (cursor.moveToNext());
        }

        adapter = new PelangganAdapter(this, dataList, pelanggan -> {
            showAksiDialog(pelanggan.getId());
        });

        listView.setAdapter(adapter);
        cursor.close();
        db.close();
    }

    private void showAksiDialog(int id) {
        CharSequence[] pilihan = {"Lihat", "Edit", "Hapus"};

        new AlertDialog.Builder(this)
                .setTitle("Pilih Aksi")
                .setItems(pilihan, (dialog, which) -> {
                    Intent intent;
                    switch (which) {
                        case 0:
                            intent = new Intent(this, DetailPelangganActivity.class);
                            intent.putExtra("id_pelanggan", id);
                            startActivity(intent);
                            break;
                        case 1:
                            intent = new Intent(this, FormEditPelangganActivity.class);
                            intent.putExtra("id_pelanggan", id);
                            startActivity(intent);
                            break;
                        case 2:
                            konfirmasiHapus(id);
                            break;
                    }
                }).show();
    }

    private void konfirmasiHapus(int id) {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Hapus")
                .setMessage("Yakin ingin menghapus pelanggan ini?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL("DELETE FROM pelanggan WHERE id_pelanggan = " + id);
                    db.close();
                    Toast.makeText(this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                    tampilkanData();
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    private void setupBottomButtons() {
        btnNavDashboard = findViewById(R.id.btnNavDashboard);
        btnNavLayanan = findViewById(R.id.btnNavLayanan);
        btnNavTransaksi = findViewById(R.id.btnNavTransaksi);

        btnNavDashboard.setOnClickListener(v -> {
            startActivity(new Intent(PelangganActivity.this, MainActivity.class));
        });

        btnNavLayanan.setOnClickListener(v -> {
            startActivity(new Intent(PelangganActivity.this, LayananActivity.class));
        });

        btnNavTransaksi.setOnClickListener(v -> {
            startActivity(new Intent(PelangganActivity.this, TransaksiActivity.class));
        });
    }
}
