package com.example.uas_mobile;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailPelangganActivity extends AppCompatActivity {

    TextView txtNama, txtNoHp, txtAlamat;
    DatabaseHelper dbHelper;
    int idPelanggan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pelanggan);

        txtNama = findViewById(R.id.txtDetailNama);
        txtNoHp = findViewById(R.id.txtDetailNoHp);
        txtAlamat = findViewById(R.id.txtDetailAlamat);
        dbHelper = new DatabaseHelper(this);

        // ambil id pelanggan
        idPelanggan = getIntent().getIntExtra("id_pelanggan", -1);

        tampilkanDetail(idPelanggan);
    }

    private void tampilkanDetail(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM pelanggan WHERE id_pelanggan = ?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            txtNama.setText(cursor.getString(1));
            txtNoHp.setText(cursor.getString(2));
            txtAlamat.setText(cursor.getString(3));
        }
        cursor.close();
    }
}
