package com.example.uas_mobile;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class FormTransaksiActivity extends AppCompatActivity {

    Spinner spinnerPelanggan, spinnerLayanan, spinnerStatus, spinnerStatusPembayaran;
    EditText edtBerat, edtHarga, edtTotal, edtTanggalTransaksi, edtTanggalSelesai;
    Button btnSimpan;
    DatabaseHelper dbHelper;

    ArrayList<Integer> idPelangganList, idLayananList;
    double hargaPerKg = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_transaksi);

        dbHelper = new DatabaseHelper(this);

        spinnerPelanggan = findViewById(R.id.spinnerPelanggan);
        spinnerLayanan = findViewById(R.id.spinnerLayanan);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        spinnerStatusPembayaran = findViewById(R.id.spinnerStatusPembayaran);
        edtBerat = findViewById(R.id.edtBerat);
        edtHarga = findViewById(R.id.edtHarga);
        edtTotal = findViewById(R.id.edtTotal);
        edtTanggalTransaksi = findViewById(R.id.edtTanggalTransaksi);
        edtTanggalSelesai = findViewById(R.id.edtTanggalSelesai);
        btnSimpan = findViewById(R.id.btnSimpanTransaksi);

        edtHarga.setEnabled(false);
        edtTotal.setEnabled(false);

        loadPelanggan();
        loadLayanan();
        loadStatus();
        loadStatusPembayaran();


        spinnerLayanan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ambilHargaLayanan(position);
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });

        edtBerat.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(android.text.Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hitungTotal();
            }
        });

        edtTanggalTransaksi.setOnClickListener(v -> pilihTanggal(edtTanggalTransaksi));
        edtTanggalSelesai.setOnClickListener(v -> pilihTanggal(edtTanggalSelesai));

        btnSimpan.setOnClickListener(v -> simpanTransaksi());
    }

    private void pilihTanggal(EditText field) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            field.setText(year + "-" + (month + 1) + "-" + day);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

//    mengambil data pelanggan
    private void loadPelanggan() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id_pelanggan, nama FROM pelanggan", null);

        ArrayList<String> namaList = new ArrayList<>();
        idPelangganList = new ArrayList<>();

        while (cursor.moveToNext()) {
            idPelangganList.add(cursor.getInt(0));
            namaList.add(cursor.getString(1));
        }

        spinnerPelanggan.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, namaList));
        cursor.close();
    }

//    mengambil data layanan
    private void loadLayanan() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id_layanan, nama_layanan FROM layanan", null);

        ArrayList<String> layananList = new ArrayList<>();
        idLayananList = new ArrayList<>();

        while (cursor.moveToNext()) {
            idLayananList.add(cursor.getInt(0));
            layananList.add(cursor.getString(1));
        }

        spinnerLayanan.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, layananList));
        cursor.close();
    }

    private void loadStatus() {
        String[] statusOptions = {"Proses", "Selesai", "Diambil"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusOptions);
        spinnerStatus.setAdapter(statusAdapter);
    }

    private void loadStatusPembayaran() {
        String[] pembayaranOptions = {"Belum Lunas", "Lunas"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, pembayaranOptions);
        spinnerStatusPembayaran.setAdapter(adapter);
    }

//    mengambil data harga perkg dari layanan
    private void ambilHargaLayanan(int position) {
        int idLayanan = idLayananList.get(position);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT harga_per_kg FROM layanan WHERE id_layanan = ?", new String[]{String.valueOf(idLayanan)});
        if (cursor.moveToFirst()) {
            hargaPerKg = cursor.getDouble(0);
            edtHarga.setText(String.valueOf(hargaPerKg));
            hitungTotal();
        }
        cursor.close();
    }

    private void hitungTotal() {
        String beratStr = edtBerat.getText().toString().trim();
        if (!beratStr.isEmpty()) {
            try {
                double berat = Double.parseDouble(beratStr);
                if (hargaPerKg > 0) {
                    double total = berat * hargaPerKg;
                    edtTotal.setText(String.valueOf(total));
                } else {
                    edtTotal.setText("0");
                }
            } catch (NumberFormatException e) {
                edtTotal.setText("0");
            }
        } else {
            edtTotal.setText("0");
        }
    }

    private void simpanTransaksi() {
        if (edtBerat.getText().toString().trim().isEmpty()
                || edtTanggalTransaksi.getText().toString().trim().isEmpty()
                || edtTanggalSelesai.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Semua field wajib diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        int idPelanggan = idPelangganList.get(spinnerPelanggan.getSelectedItemPosition());
        int idLayanan = idLayananList.get(spinnerLayanan.getSelectedItemPosition());
        double berat = Double.parseDouble(edtBerat.getText().toString());
        double total = berat * hargaPerKg;
        String tglTransaksi = edtTanggalTransaksi.getText().toString().trim();
        String tglSelesai = edtTanggalSelesai.getText().toString().trim();
        String status = spinnerStatus.getSelectedItem().toString();
        String statusPembayaran = spinnerStatusPembayaran.getSelectedItem().toString();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_pelanggan", idPelanggan);
        values.put("id_layanan", idLayanan);
        values.put("berat", berat);
        values.put("harga", hargaPerKg);
        values.put("total_harga", total);
        values.put("tanggal_transaksi", tglTransaksi);
        values.put("tanggal_selesai", tglSelesai);
        values.put("status", status);
        values.put("status_pembayaran", statusPembayaran);


        long result = db.insert("transaksi", null, values);
        if (result != -1) {
            Toast.makeText(this, "Transaksi berhasil ditambahkan", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Gagal menyimpan transaksi", Toast.LENGTH_SHORT).show();
        }
    }
}
