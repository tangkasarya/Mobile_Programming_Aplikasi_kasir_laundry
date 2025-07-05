package com.example.uas_mobile;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class FormEditPelangganActivity extends AppCompatActivity {

    EditText edtNama, edtNoHp, edtAlamat;
    Button btnSimpan;
    DatabaseHelper dbHelper;
    int idPelanggan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pelanggan);

        edtNama = findViewById(R.id.edtNama);
        edtNoHp = findViewById(R.id.edtNoHp);
        edtAlamat = findViewById(R.id.edtAlamat);
        btnSimpan = findViewById(R.id.btnSimpan);

        dbHelper = new DatabaseHelper(this);

        // ambil id pelanggan
        idPelanggan = getIntent().getIntExtra("id_pelanggan", -1);

        // mengubah judul form
        ((android.widget.TextView) findViewById(R.id.txtJudulForm)).setText("Edit Data Pelanggan");

        // menampilkan data
        tampilkanDataLama(idPelanggan);

        btnSimpan.setOnClickListener(v -> updateData());
    }

    private void tampilkanDataLama(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM pelanggan WHERE id_pelanggan = ?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            edtNama.setText(cursor.getString(1));
            edtNoHp.setText(cursor.getString(2));
            edtAlamat.setText(cursor.getString(3));
        }
        cursor.close();
    }

    private void updateData() {
        String namaBaru = edtNama.getText().toString().trim();
        String noHp = edtNoHp.getText().toString().trim();
        String alamat = edtAlamat.getText().toString().trim();

        if (namaBaru.isEmpty() || noHp.isEmpty() || alamat.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nama", namaBaru);
        values.put("no_hp", noHp);
        values.put("alamat", alamat);

        int result = db.update("pelanggan", values, "id_pelanggan = ?", new String[]{String.valueOf(idPelanggan)});
        if (result > 0) {
            Toast.makeText(this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Gagal update data", Toast.LENGTH_SHORT).show();
        }
    }
}
