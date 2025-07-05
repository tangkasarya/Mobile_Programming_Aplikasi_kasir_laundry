package com.example.uas_mobile;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailTransaksiActivity extends AppCompatActivity {

    TextView txtPelanggan, txtLayanan, txtHarga, txtBerat, txtTotal, txtTglTransaksi, txtTglSelesai, txtStatus, txtStatusPembayaran;
    DatabaseHelper dbHelper;
    int idTransaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaksi);

        txtPelanggan = findViewById(R.id.txtDetailPelanggan);
        txtLayanan = findViewById(R.id.txtDetailLayanan);
        txtHarga = findViewById(R.id.txtDetailHarga);
        txtBerat = findViewById(R.id.txtDetailBerat);
        txtTotal = findViewById(R.id.txtDetailTotal);
        txtTglTransaksi = findViewById(R.id.txtDetailTanggalTransaksi);
        txtTglSelesai = findViewById(R.id.txtDetailTanggalSelesai);
        txtStatus = findViewById(R.id.txtDetailStatus);
        txtStatusPembayaran = findViewById(R.id.txtDetailStatusPembayaran);

        dbHelper = new DatabaseHelper(this);
        idTransaksi = getIntent().getIntExtra("id_transaksi", -1);

        if (idTransaksi != -1) {
            tampilkanDetail(idTransaksi);
        }
    }

    private void tampilkanDetail(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT t.*, p.nama AS nama_pelanggan, l.nama_layanan " +
                "FROM transaksi t " +
                "JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan " +
                "JOIN layanan l ON t.id_layanan = l.id_layanan " +
                "WHERE t.id_transaksi = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            txtPelanggan.setText(cursor.getString(cursor.getColumnIndexOrThrow("nama_pelanggan")));
            txtLayanan.setText(cursor.getString(cursor.getColumnIndexOrThrow("nama_layanan")));
            txtHarga.setText("Rp " + cursor.getDouble(cursor.getColumnIndexOrThrow("harga")));
            txtBerat.setText(cursor.getDouble(cursor.getColumnIndexOrThrow("berat")) + " Kg");
            txtTotal.setText("Rp " + cursor.getDouble(cursor.getColumnIndexOrThrow("total_harga")));
            txtTglTransaksi.setText(cursor.getString(cursor.getColumnIndexOrThrow("tanggal_transaksi")));
            txtTglSelesai.setText(cursor.getString(cursor.getColumnIndexOrThrow("tanggal_selesai")));
            txtStatus.setText(cursor.getString(cursor.getColumnIndexOrThrow("status")));
            txtStatusPembayaran.setText(cursor.getString(cursor.getColumnIndexOrThrow("status_pembayaran")));
        }
        cursor.close();
        db.close();
    }
}
