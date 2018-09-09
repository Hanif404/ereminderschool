package org.karungkung.ereminderschool.guru.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.karungkung.ereminderschool.R;
import org.karungkung.ereminderschool.guru.models.Pengumuman;

import java.util.List;

/**
 * Created by hanif on 01/08/18.
 */

public class PengumumanAdapter extends RecyclerView.Adapter<PengumumanAdapter.MyViewHolder> {
    private List<Pengumuman> umumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView judul, tgl, isi;

        public MyViewHolder(View view) {
            super(view);
            judul = (TextView) view.findViewById(R.id.label_judul);
            tgl = (TextView) view.findViewById(R.id.label_tgl);
            isi = (TextView) view.findViewById(R.id.label_isi);
        }
    }

    public PengumumanAdapter(List<Pengumuman> umumList) {
        this.umumList = umumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_pengumuman, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Pengumuman umum = umumList.get(position);
        holder.judul.setText(umum.getJudul());
        holder.tgl.setText(umum.getTanggal());
        holder.isi.setText(umum.getIsi());
    }

    @Override
    public int getItemCount() {
        return umumList.size();
    }
}
