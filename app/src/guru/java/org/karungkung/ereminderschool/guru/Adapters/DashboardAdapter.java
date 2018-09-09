package org.karungkung.ereminderschool.guru.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.karungkung.ereminderschool.R;
import org.karungkung.ereminderschool.guru.models.Dashboard;

import java.util.List;

/**
 * Created by hanif on 01/07/18.
 */

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.MyViewHolder>{
    private List<Dashboard> infosList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, konten;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            konten = (TextView) view.findViewById(R.id.konten);
        }
    }

    public DashboardAdapter(List<Dashboard> infosList) {
        this.infosList = infosList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_dashboard, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Dashboard dashboard = infosList.get(position);
        holder.title.setText(dashboard.getTitle());
        holder.konten.setText(dashboard.getKonten());
    }

    @Override
    public int getItemCount() {
        return infosList.size();
    }
}
