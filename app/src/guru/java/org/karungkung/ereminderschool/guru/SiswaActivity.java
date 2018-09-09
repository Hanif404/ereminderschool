package org.karungkung.ereminderschool.guru;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.karungkung.ereminderschool.R;
import org.karungkung.ereminderschool.guru.Adapters.SiswaAdapter;
import org.karungkung.ereminderschool.guru.Listeners.SiswaListener;
import org.karungkung.ereminderschool.guru.models.Dashboard;
import org.karungkung.ereminderschool.guru.models.Siswa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiswaActivity extends AppCompatActivity {

    private List<Siswa> siswaList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SiswaAdapter mAdapter;
    private final Context context = this;
    private SessionManager sm;
    private AlertDialog.Builder builder = null;
    private Dialog mdialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siswa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sm = new SessionManager(this);

        getDataSiswa();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mdialog == null){
                    mdialog = new Dialog(context);
                    mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    mdialog.setContentView(R.layout.dialog_form_siswa);

                    prepareForm(0, 0);
                    mdialog.show();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getDataSiswa(){
        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = ApiClient.getClient().create(GetDataService.class);
        Call<List<Siswa>> call = service.getDaftarSiswa(sm.getPrefInteger("id_kelas"));
        call.enqueue(new Callback<List<Siswa>>() {
            @Override
            public void onResponse(Call<List<Siswa>> call, Response<List<Siswa>> response) {
                prepareList(response.body());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Siswa>> call, Throwable t) {
                Toast.makeText(SiswaActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prepareList(List<Siswa> dataList){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_siswa);
        siswaList = new ArrayList<>();
        siswaList = dataList;

        mAdapter = new SiswaAdapter(dataList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new SiswaListener(getApplicationContext(), recyclerView, new SiswaListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(mdialog == null){
                    mdialog = new Dialog(context);
                    mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    mdialog.setContentView(R.layout.dialog_form_siswa);

                    prepareForm(1, position);
                    mdialog.show();
                }
            }

            @Override
            public void onLongClick(View view, final int position) {
                if(builder == null){
                    builder = new AlertDialog.Builder(context);

                    builder.setTitle("Penghapusan Data");
                    builder.setMessage("Yakinkan anda akan menghapus data ini?");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            Siswa sis = siswaList.get(position);

                            GetDataService service = ApiClient.getClient().create(GetDataService.class);
                            Call<ResponseBody> call = service.deleteSiswa(sis.getId());
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    Toast.makeText(SiswaActivity.this, "Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                                    getDataSiswa();
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(SiswaActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
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

    private void prepareForm(final int type, final int pos){
        final EditText txtNis = (EditText) mdialog.findViewById(R.id.txt_nis);
        final EditText txtNama = (EditText) mdialog.findViewById(R.id.txt_nama);
        Button btnSubmit = (Button) mdialog.findViewById(R.id.btn_submit);
        Button btnBatal = (Button) mdialog.findViewById(R.id.btn_cancel);

        if(type == 1){
            Siswa sis = siswaList.get(pos);

            GetDataService service = ApiClient.getClient().create(GetDataService.class);
            Call<List<Siswa>> call = service.getDataSiswa(sis.getId());
            call.enqueue(new Callback<List<Siswa>>() {
                @Override
                public void onResponse(Call<List<Siswa>> call, Response<List<Siswa>> response) {
                    List<Siswa> arrSis = response.body();
                    txtNis.setText(arrSis.get(0).getNis());
                    txtNama.setText(arrSis.get(0).getNama());
                }

                @Override
                public void onFailure(Call<List<Siswa>> call, Throwable t) {
                    Toast.makeText(SiswaActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        btnSubmit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(type == 1){
                    Siswa sis = siswaList.get(pos);

                    HashMap<String, String> params = new HashMap<>();
                    params.put("nisn", txtNis.getText().toString());
                    params.put("name", txtNama.getText().toString());
                    params.put("id", String.valueOf(sis.getId()));

                    GetDataService service = ApiClient.getClient().create(GetDataService.class);
                    Call<ResponseBody> callsubmit = service.editSiswa(params);
                    callsubmit.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Toast.makeText(SiswaActivity.this, "Berhasil Disubmit", Toast.LENGTH_SHORT).show();
                            mdialog.dismiss();
                            mdialog = null;
                            getDataSiswa();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(SiswaActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    HashMap<String, String> params = new HashMap<>();
                    params.put("nisn", txtNis.getText().toString());
                    params.put("name", txtNama.getText().toString());
                    params.put("kelas", String.valueOf(sm.getPrefInteger("id_kelas")));

                    GetDataService service = ApiClient.getClient().create(GetDataService.class);
                    Call<ResponseBody> callsubmit = service.addSiswa(params);
                    callsubmit.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Toast.makeText(SiswaActivity.this, "Berhasil Disubmit", Toast.LENGTH_SHORT).show();
                            mdialog.dismiss();
                            mdialog = null;
                            getDataSiswa();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(SiswaActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog.dismiss();
                mdialog = null;
            }
        });
    }

}
