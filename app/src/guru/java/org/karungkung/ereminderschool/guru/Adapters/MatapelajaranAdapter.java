package org.karungkung.ereminderschool.guru.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import org.karungkung.ereminderschool.R;
import org.karungkung.ereminderschool.guru.models.MataPelajaran;
import org.karungkung.ereminderschool.guru.models.Siswa;

import java.util.List;

/**
 * Created by hanif on 01/07/18.
 */

public class MatapelajaranAdapter extends RecyclerView.Adapter<MatapelajaranAdapter.MyViewHolder>{
    private List<MataPelajaran> mpList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nama;
        public CheckBox select;

        public MyViewHolder(View view) {
            super(view);
            nama = (TextView) view.findViewById(R.id.label_nm);
            select = (CheckBox) view.findViewById(R.id.checkbox_mp);
        }
    }

    public MatapelajaranAdapter(Context mContext, List<MataPelajaran> mpList) {
        this.mContext = mContext;
        this.mpList = mpList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_mp, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        MataPelajaran mp = mpList.get(position);
        holder.nama.setText(mp.getNama());
        holder.select.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mpList.get(position).setSelected(holder.select.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mpList.size();
    }

    public List<MataPelajaran> getMpList() {
        return mpList;
    }
}
