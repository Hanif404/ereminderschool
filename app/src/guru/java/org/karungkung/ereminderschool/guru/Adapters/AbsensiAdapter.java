package org.karungkung.ereminderschool.guru.Adapters;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.karungkung.ereminderschool.R;
import org.karungkung.ereminderschool.guru.AbsensiDiffCallBack;
import org.karungkung.ereminderschool.guru.models.Siswa;

import java.util.List;

/**
 * Created by hanif on 01/07/18.
 */

public class AbsensiAdapter extends RecyclerView.Adapter<AbsensiAdapter.MyViewHolder>{
    private List<Siswa> siswaList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nis, nama, status;

        public MyViewHolder(View view) {
            super(view);
            nis = (TextView) view.findViewById(R.id.label_nis);
            nama = (TextView) view.findViewById(R.id.label_nm);
            status = (TextView) view.findViewById(R.id.label_status);
        }
    }

    public AbsensiAdapter(List<Siswa> siswaList) {
        this.siswaList = siswaList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_absensi, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Siswa siswa = siswaList.get(position);
        holder.nis.setText(siswa.getNis());
        holder.nama.setText(siswa.getNama());

        if(siswa.getStatus() != "-"){
            if(siswa.getStatus() == "hadir"){
                holder.status.setText("hadir");
            }else if(siswa.getStatus().equals("izin")){
                holder.status.setText("izin");
            }else if(siswa.getStatus().equals("sakit")){
                holder.status.setText("sakit");
            }else if(siswa.getStatus().equals("tdk")){
                holder.status.setText("tidak hadir");
            }
        }else{
            holder.status.setText("blm absen");
        }
    }

    @Override
    public int getItemCount() {
        return siswaList.size();
    }

    public void updateAbsensiListItems(List<Siswa> newsiswa) {
        final AbsensiDiffCallBack diffCallback = new AbsensiDiffCallBack(newsiswa, this.siswaList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        diffResult.dispatchUpdatesTo(this);

        newsiswa.clear();
        newsiswa.addAll(this.siswaList);
    }
}
