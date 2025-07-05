package com.example.uas_mobile;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FormLayananActivity extends AppCompatActivity {

    EditText edtNama, edtHarga;
    Button btnSimpan;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_layanan);

        edtNama = findViewById(R.id.edtNamaLayanan);
        edtHarga = findViewById(R.id.edtHargaLayanan);
        btnSimpan = findViewById(R.id.btnSimpanLayanan);

        dbHelper = new DatabaseHelper(this);

        btnSimpan.setOnClickListener(v -> simpanData());
    }

    private void simpanData() {
        String nama = edtNama.getText().toString().trim();
        String hargaStr = edtHarga.getText().toString().trim();

        if (nama.isEmpty() || hargaStr.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        double harga;
        try {
            harga = Double.parseDouble(hargaStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Harga harus berupa angka!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nama_layanan", nama);
        values.put("harga_per_kg", harga);

        long result = db.insert("layanan", null, values);
        if (result != -1) {
            Toast.makeText(this, "Layanan berhasil ditambahkan", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Gagal menambahkan layanan", Toast.LENGTH_SHORT).show();
        }
    }
}
