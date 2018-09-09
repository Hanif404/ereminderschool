package org.karungkung.ereminderschool.guru;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import org.karungkung.ereminderschool.R;
import org.karungkung.ereminderschool.guru.Adapters.PengumumanAdapter;
import org.karungkung.ereminderschool.guru.Listeners.PengumumanListener;
import org.karungkung.ereminderschool.guru.models.Pengumuman;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarPengumumanActivity extends AppCompatActivity {

    private List<Pengumuman> pengumumanList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PengumumanAdapter mAdapter;
    private SessionManager sm;
    private Context context = this;
    private AlertDialog.Builder builder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_pengumuman);
        sm = new SessionManager(this);

        getDataEvent();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FormPengumumanActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getDataEvent(){
        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = ApiClient.getClient().create(GetDataService.class);
        Call<List<Pengumuman>> call = service.getDaftarEvent(sm.getPrefInteger("id_kelas"));
        call.enqueue(new Callback<List<Pengumuman>>() {
            @Override
            public void onResponse(Call<List<Pengumuman>> call, Response<List<Pengumuman>> response) {
                pengumumanList = response.body();
                prepareList(response.body());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Pengumuman>> call, Throwable t) {
                Toast.makeText(DaftarPengumumanActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prepareList(final List<Pengumuman> dataList){
        recyclerView = findViewById(R.id.recycler_pengumuman);
        mAdapter = new PengumumanAdapter(dataList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new PengumumanListener(getApplicationContext(), recyclerView, new PengumumanListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Pengumuman event = dataList.get(position);
                Intent intent = new Intent(getApplicationContext(), FormPengumumanActivity.class);
                intent.putExtra("id", event.getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, final int position) {
                if(builder == null){
                    builder = new AlertDialog.Builder(context);
                    builder.setTitle("Penghapusan Data");
                    builder.setMessage("Yakinkan anda akan menghapus data ini?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            Pengumuman umum = pengumumanList.get(position);

                            GetDataService service = ApiClient.getClient().create(GetDataService.class);
                            Call<ResponseBody> call = service.deleteEvent(umum.getId());
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    Toast.makeText(DaftarPengumumanActivity.this, "Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                                    getDataEvent();
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(DaftarPengumumanActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            builder = null;
                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // I do not need any action here you might
                            builder = null;
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        }));
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
