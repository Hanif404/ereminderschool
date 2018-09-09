package org.karungkung.ereminderschool.guru.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.karungkung.ereminderschool.R;
import org.karungkung.ereminderschool.guru.models.Pekerjaan;
import org.karungkung.ereminderschool.guru.models.Pengumuman;

import java.util.List;

/**
 * Created by hanif on 01/08/18.
 */

public class PekerjaanAdapter extends RecyclerView.Adapter<PekerjaanAdapter.MyViewHolder> {
    private List<Pekerjaan> pekerjaanList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView judul, tgl, isi;

        public MyViewHolder(View view) {
            super(view);
            judul = (TextView) view.findViewById(R.id.label_judul);
            tgl = (TextView) view.findViewById(R.id.label_tgl);
            isi = (TextView) view.findViewById(R.id.label_isi);
        }
    }

    public PekerjaanAdapter(List<Pekerjaan> pekerjaanList) {
        this.pekerjaanList = pekerjaanList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_pekerjaan, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Pekerjaan pekerjaan = pekerjaanList.get(position);
        holder.judul.setText(pekerjaan.getNamaMp());
        holder.tgl.setText(pekerjaan.getTglBuat());
        holder.isi.setText("Untuk kelas "+ pekerjaan.getNamaKelas() +" ada tugas "+pekerjaan.getIsi()+", pekerjaan dikumpulkan tgl. "+pekerjaan.getTglSelesai());
    }

    @Override
    public int getItemCount() {
        return pekerjaanList.size();
    }
}
