package com.example.uas_mobile;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FormEditLayananActivity extends AppCompatActivity {

    EditText edtNama, edtHarga;
    Button btnSimpan;
    DatabaseHelper dbHelper;
    int idLayanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_layanan);

        edtNama = findViewById(R.id.edtNamaLayanan);
        edtHarga = findViewById(R.id.edtHargaLayanan);
        btnSimpan = findViewById(R.id.btnSimpanLayanan);

        dbHelper = new DatabaseHelper(this);

        idLayanan = getIntent().getIntExtra("id_layanan", -1);
        if (idLayanan != -1) {
            tampilkanDataLama(idLayanan);
        }

        btnSimpan.setOnClickListener(v -> updateData());
    }

    private void tampilkanDataLama(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM layanan WHERE id_layanan = ?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            edtNama.setText(cursor.getString(1));
            edtHarga.setText(cursor.getString(2));
        }
        cursor.close();
    }

    private void updateData() {
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

        int result = db.update("layanan", values, "id_layanan = ?", new String[]{String.valueOf(idLayanan)});
        if (result > 0) {
            Toast.makeText(this, "Layanan berhasil diupdate", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Gagal update layanan", Toast.LENGTH_SHORT).show();
        }
    }
}
