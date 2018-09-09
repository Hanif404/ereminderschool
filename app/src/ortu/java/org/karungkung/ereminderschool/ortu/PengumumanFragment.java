package org.karungkung.ereminderschool.ortu;


import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.karungkung.ereminderschool.R;
import org.karungkung.ereminderschool.ortu.Adapters.PengumumanAdapter;
import org.karungkung.ereminderschool.ortu.Models.PekerjaanRumah;
import org.karungkung.ereminderschool.ortu.Models.Pengumuman;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hanif on 10/08/18.
 */

public class PengumumanFragment extends Fragment {
    private List<Pengumuman> dataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PengumumanAdapter mAdapter;
    private SessionManager sm;

    public static PengumumanFragment newInstance() {
        PengumumanFragment fragment = new PengumumanFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pengumuman, container, false);
        sm = new SessionManager(getActivity());
        getDataEvent(view);
        return view;
    }

    private void getDataEvent(final View view){
        GetDataService service = ApiClient.getClient().create(GetDataService.class);
        Call<List<Pengumuman>> call = service.getNotifPengumuman(sm.getPrefString("nisn"));
        call.enqueue(new Callback<List<Pengumuman>>() {
            @Override
            public void onResponse(Call<List<Pengumuman>> call, Response<List<Pengumuman>> response) {
                prepareList(response.body(), view);
            }

            @Override
            public void onFailure(Call<List<Pengumuman>> call, Throwable t) {
//                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prepareList(final List<Pengumuman> dataList, View view){
        recyclerView = view.findViewById(R.id.recycler_pengumuman);
        TextView txtData = view.findViewById(R.id.txt_nodata);
        if(dataList.size() > 0){
            mAdapter = new PengumumanAdapter(dataList);
            txtData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(view.getContext(), 1);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), false));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);

            mAdapter.notifyDataSetChanged();
        }else{
            txtData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
