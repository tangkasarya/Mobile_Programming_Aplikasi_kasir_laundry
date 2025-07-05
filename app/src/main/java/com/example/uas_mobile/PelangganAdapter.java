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

public class PelangganAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private ArrayList<PelangganModel> originalList; // list penuh
    private ArrayList<PelangganModel> filteredList; // list hasil filter
    private LayoutInflater inflater;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(PelangganModel pelanggan);
    }

    public PelangganAdapter(Context context, ArrayList<PelangganModel> pelangganList, OnItemClickListener listener) {
        this.context = context;
        this.originalList = pelangganList;
        this.filteredList = new ArrayList<>(pelangganList);
        this.listener = listener;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return filteredList.get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.item_pelanggan_card, parent, false);
            holder = new ViewHolder();
            holder.txtNama = view.findViewById(R.id.txtNama);
            holder.txtNoHp = view.findViewById(R.id.txtNoHp);
            holder.txtAlamat = view.findViewById(R.id.txtAlamat);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        PelangganModel pelanggan = filteredList.get(position);
        holder.txtNama.setText(pelanggan.getNama());
        holder.txtNoHp.setText("No HP: " + pelanggan.getNoHp());
        holder.txtAlamat.setText("Alamat: " + pelanggan.getAlamat());

        view.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(pelanggan);
            }
        });

        return view;
    }

    static class ViewHolder {
        TextView txtNama, txtNoHp, txtAlamat;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence keyword) {
                FilterResults results = new FilterResults();
                ArrayList<PelangganModel> filtered = new ArrayList<>();

                if (keyword == null || keyword.length() == 0) {
                    filtered.addAll(originalList);
                } else {
                    String filterPattern = keyword.toString().toLowerCase().trim();
                    for (PelangganModel pelanggan : originalList) {
                        if (pelanggan.getNama().toLowerCase().contains(filterPattern) ||
                                pelanggan.getNoHp().toLowerCase().contains(filterPattern) ||
                                pelanggan.getAlamat().toLowerCase().contains(filterPattern)) {
                            filtered.add(pelanggan);
                        }
                    }
                }

                results.values = filtered;
                results.count = filtered.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence keyword, FilterResults results) {
                filteredList.clear();
                filteredList.addAll((ArrayList<PelangganModel>) results.values);
                notifyDataSetChanged();
            }
        };
    }
}
