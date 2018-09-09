package org.karungkung.ereminderschool.ortu.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.karungkung.ereminderschool.R;
import org.karungkung.ereminderschool.ortu.Models.Absensi;
import org.karungkung.ereminderschool.ortu.Models.Pengumuman;

import java.util.List;

/**
 * Created by hanif on 10/08/18.
 */

public class PengumumanAdapter extends RecyclerView.Adapter<PengumumanAdapter.MyViewHolder> {
    private List<Pengumuman> dataList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView judul, tgl, isi;

        public MyViewHolder(View view) {
            super(view);
            judul = (TextView) view.findViewById(R.id.label_judul);
            tgl = (TextView) view.findViewById(R.id.label_tgl);
            isi = (TextView) view.findViewById(R.id.label_isi);
        }
    }

    public PengumumanAdapter(List<Pengumuman> dataList) {
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_pengumuman, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Pengumuman abs = dataList.get(position);
        holder.judul.setText(abs.getJudul());
        holder.tgl.setText(abs.getTglPengumuman());
        String tglSelesai = "", wktPengumuman = "";
        if(abs.getTglSelesai() != null){
            tglSelesai += " sampai "+ abs.getTglSelesai();
        }
        if(abs.getWktMulai() != null){
            wktPengumuman += ", jam "+ abs.getWktMulai();
            if(abs.getWktSelesai() != null){
                wktPengumuman += " sampai jam "+ abs.getWktSelesai();
            }
        }

        holder.isi.setText(abs.getIsiPengumuman()+" pada tanggal "+abs.getTglMulai() +""+ tglSelesai +""+ wktPengumuman);
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }
}
