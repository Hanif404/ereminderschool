package org.karungkung.ereminderschool.ortu.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.karungkung.ereminderschool.R;
import org.karungkung.ereminderschool.ortu.Models.Absensi;

import java.util.List;

/**
 * Created by hanif on 10/08/18.
 */

public class AbsensiAdapter extends RecyclerView.Adapter<AbsensiAdapter.MyViewHolder> {
    private List<Absensi> dataList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView judul, nmGuru, isi;

        public MyViewHolder(View view) {
            super(view);
            judul = (TextView) view.findViewById(R.id.label_judul);
            nmGuru = (TextView) view.findViewById(R.id.label_guru);
            isi = (TextView) view.findViewById(R.id.label_isi);
        }
    }

    public AbsensiAdapter(List<Absensi> dataList) {
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_absensi, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Absensi abs = dataList.get(position);
        holder.judul.setText(abs.getJudul());
        holder.nmGuru.setText(abs.getPengajar());

        String absen = "";
        if(abs.getIsAbsen() == 1){
            absen = "Hadir";
        }else if(abs.getIsAbsen() == 2){
            absen = "Izin";
        }else if(abs.getIsAbsen() == 3){
            absen = "Sakit";
        }else if(abs.getIsAbsen() == 4){
            absen = "Tidak Hadir";
        }
        holder.isi.setText("Tgl. "+ abs.getTgl() + " dengan keterangan "+ absen);
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }
}
