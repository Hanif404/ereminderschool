package org.karungkung.ereminderschool.guru;

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
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import org.karungkung.ereminderschool.R;
import org.karungkung.ereminderschool.guru.Adapters.PekerjaanAdapter;
import org.karungkung.ereminderschool.guru.Listeners.PekerjaanListener;
import org.karungkung.ereminderschool.guru.models.Pekerjaan;
import org.karungkung.ereminderschool.guru.models.Pengumuman;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarPekerjaanActivity extends AppCompatActivity {

    private List<Pekerjaan> pekerjaanList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PekerjaanAdapter mAdapter;
    private SessionManager sm;
    private AlertDialog.Builder builder = null;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_pekerjaan);
        sm = new SessionManager(this);

        getDataList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FormPekerjaanActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getDataList(){
        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = ApiClient.getClient().create(GetDataService.class);
        Call<List<Pekerjaan>> call = service.getDaftaPr(sm.getPrefInteger("id_guru"));
        call.enqueue(new Callback<List<Pekerjaan>>() {
            @Override
            public void onResponse(Call<List<Pekerjaan>> call, Response<List<Pekerjaan>> response) {
                pekerjaanList = response.body();
                prepareList(response.body());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Pekerjaan>> call, Throwable t) {
                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prepareList(final List<Pekerjaan> dataList){
        recyclerView = findViewById(R.id.recycler_pekerjaan);
        mAdapter = new PekerjaanAdapter(dataList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new PekerjaanListener(getApplicationContext(), recyclerView, new PekerjaanListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Pekerjaan pr = pekerjaanList.get(position);
                Intent intent = new Intent(context, FormPekerjaanActivity.class);
                intent.putExtra("id", pr.getId());
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
                            Pekerjaan data = pekerjaanList.get(position);
                            Log.d("teststs",data.getId()+"");
                            GetDataService service = ApiClient.getClient().create(GetDataService.class);
                            Call<ResponseBody> call = service.deletePr(data.getId());
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    Toast.makeText(context, "Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                                    getDataList();
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
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
