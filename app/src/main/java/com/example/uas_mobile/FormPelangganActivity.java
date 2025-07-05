package com.example.uas_mobile;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FormPelangganActivity extends AppCompatActivity {

    EditText edtNama, edtNoHp, edtAlamat;
    Button btnSimpan;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pelanggan);

        edtNama = findViewById(R.id.edtNama);
        edtNoHp = findViewById(R.id.edtNoHp);
        edtAlamat = findViewById(R.id.edtAlamat);
        btnSimpan = findViewById(R.id.btnSimpan);
        dbHelper = new DatabaseHelper(this);

        btnSimpan.setOnClickListener(v -> {
            String nama = edtNama.getText().toString().trim();
            String noHp = edtNoHp.getText().toString().trim();
            String alamat = edtAlamat.getText().toString().trim();

            if (nama.isEmpty() || noHp.isEmpty() || alamat.isEmpty()) {
                Toast.makeText(this, "Semua field wajib diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("nama", nama);
            values.put("no_hp", noHp);
            values.put("alamat", alamat);

            long result = db.insert("pelanggan", null, values);

            if (result != -1) {
                Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
            }

            db.close();
        });
    }
}
