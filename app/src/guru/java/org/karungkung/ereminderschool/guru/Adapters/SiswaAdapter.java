package org.karungkung.ereminderschool.guru.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.karungkung.ereminderschool.R;
import org.karungkung.ereminderschool.guru.models.Dashboard;
import org.karungkung.ereminderschool.guru.models.Siswa;

import java.util.List;

/**
 * Created by hanif on 01/07/18.
 */

public class SiswaAdapter extends RecyclerView.Adapter<SiswaAdapter.MyViewHolder>{
    private List<Siswa> siswaList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nis, nama;

        public MyViewHolder(View view) {
            super(view);
            nis = (TextView) view.findViewById(R.id.label_nis);
            nama = (TextView) view.findViewById(R.id.label_nm);
        }
    }

    public SiswaAdapter(List<Siswa> siswaList) {
        this.siswaList = siswaList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_siswa, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Siswa siswa = siswaList.get(position);
        holder.nis.setText(siswa.getNis());
        holder.nama.setText(siswa.getNama());
    }

    @Override
    public int getItemCount() {
        return siswaList.size();
    }
}
