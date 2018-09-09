package org.karungkung.ereminderschool.ortu.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.karungkung.ereminderschool.R;
import org.karungkung.ereminderschool.ortu.Models.Absensi;
import org.karungkung.ereminderschool.ortu.Models.PekerjaanRumah;

import java.util.List;

/**
 * Created by hanif on 10/08/18.
 */

public class PekerjaanRumahAdapter extends RecyclerView.Adapter<PekerjaanRumahAdapter.MyViewHolder> {
    private List<PekerjaanRumah> dataList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView judul, tgl, isi, pengajar;

        public MyViewHolder(View view) {
            super(view);
            judul = (TextView) view.findViewById(R.id.label_judul);
            tgl = (TextView) view.findViewById(R.id.label_tgl);
            isi = (TextView) view.findViewById(R.id.label_isi);
            pengajar = (TextView) view.findViewById(R.id.label_pengajar);
        }
    }

    public PekerjaanRumahAdapter(List<PekerjaanRumah> dataList) {
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_pekerjaan, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PekerjaanRumah data = dataList.get(position);
        holder.judul.setText(data.getMataPelajaran());
        holder.tgl.setText(data.getTglPr());
        holder.isi.setText(data.getIsi()+" dikumpulkan pada tanggal "+ data.getTglSelesai());
        holder.pengajar.setText(data.getPengajar());
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }
}
