package com.example.uas_mobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class TransaksiAdapter extends BaseAdapter implements Filterable {
    Context context;
    ArrayList<TransaksiModel> listTransaksi;
    ArrayList<TransaksiModel> listTransaksiFull;
    LayoutInflater inflater;

    public TransaksiAdapter(Context context, ArrayList<TransaksiModel> listTransaksi) {
        this.context = context;
        this.listTransaksi = new ArrayList<>(listTransaksi);
        this.listTransaksiFull = new ArrayList<>(listTransaksi); // backup untuk search
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listTransaksi.size();
    }

    @Override
    public Object getItem(int position) {
        return listTransaksi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listTransaksi.get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.item_transaksi_card, parent, false);
            holder = new ViewHolder();
            holder.nama = view.findViewById(R.id.txtNama);
            holder.layanan = view.findViewById(R.id.txtLayanan);
            holder.berat = view.findViewById(R.id.txtBerat);
            holder.total = view.findViewById(R.id.txtTotal);
            holder.status = view.findViewById(R.id.txtStatus);
            holder.pembayaran = view.findViewById(R.id.txtPembayaran);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        TransaksiModel transaksi = listTransaksi.get(position);

        holder.nama.setText("Nama: " + transaksi.getNamaPelanggan());
        holder.layanan.setText("Layanan: " + transaksi.getLayanan());
        holder.berat.setText("Berat: " + transaksi.getBerat() + " kg");
        holder.total.setText("Total Harga: Rp " + transaksi.getTotal());
        holder.status.setText("Status: " + transaksi.getStatus());
        holder.pembayaran.setText("Pembayaran: " + transaksi.getStatusPembayaran());

        return view;
    }

    static class ViewHolder {
        TextView nama, layanan, berat, total, status, pembayaran;
    }

    @Override
    public Filter getFilter() {
        return transaksiFilter;
    }

    private final Filter transaksiFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<TransaksiModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listTransaksiFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (TransaksiModel transaksi : listTransaksiFull) {
                    if (transaksi.getNamaPelanggan().toLowerCase().contains(filterPattern) ||
                            transaksi.getLayanan().toLowerCase().contains(filterPattern) ||
                            transaksi.getStatus().toLowerCase().contains(filterPattern) ||
                            transaksi.getStatusPembayaran().toLowerCase().contains(filterPattern)) {
                        filteredList.add(transaksi);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listTransaksi.clear();
            listTransaksi.addAll((ArrayList<TransaksiModel>) results.values);
            notifyDataSetChanged();
        }
    };
}
