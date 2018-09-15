package org.karungkung.ereminderschool.guru;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.karungkung.ereminderschool.R;
import org.karungkung.ereminderschool.guru.Adapters.AbsensiAdapter;
import org.karungkung.ereminderschool.guru.Adapters.DropdownAdapter;
import org.karungkung.ereminderschool.guru.Adapters.SiswaAdapter;
import org.karungkung.ereminderschool.guru.Listeners.AbsensiListener;
import org.karungkung.ereminderschool.guru.models.Dropdown;
import org.karungkung.ereminderschool.guru.models.MataPelajaran;
import org.karungkung.ereminderschool.guru.models.OldSiswa;
import org.karungkung.ereminderschool.guru.models.Siswa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbsensiActivity extends AppCompatActivity {

    private List<Siswa> siswaList = new ArrayList<>();
    private List<Siswa> oldSiswaList;
    private RecyclerView recyclerView;
    private AbsensiAdapter mAdapter;
    private final Context context = this;
    private SessionManager sm;
    private DropdownAdapter ddAdapter;
    private int idMp;
    private MaterialBetterSpinner spKelas;
    private AlertDialog.Builder builder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absensi);
        sm = new SessionManager(this);

        prepareForm();
    }

    private void getDataSiswa(){
        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = ApiClient.getClient().create(GetDataService.class);
        Call<List<Siswa>> call = service.getDaftarSiswa(getIdDropdown(spKelas.getText().toString()));
        call.enqueue(new Callback<List<Siswa>>() {
            @Override
            public void onResponse(Call<List<Siswa>> call, Response<List<Siswa>> response) {
                siswaList = response.body();
                prepareList(response.body());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Siswa>> call, Throwable t) {
                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prepareForm(){
        final MaterialBetterSpinner spMatapelajaran = (MaterialBetterSpinner) findViewById(R.id.sp_matapelajaran);
        spKelas = (MaterialBetterSpinner) findViewById(R.id.sp_kelas);

        String[] mpPart = sm.getPrefString("mata_pelajaran").split(",");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, mpPart);
        spMatapelajaran.setAdapter(adapter);

        GetDataService service = ApiClient.getClient().create(GetDataService.class);
        Call<List<Dropdown>> call = service.getKelas(String.valueOf(sm.getPrefInteger("id_sekolah")));
        call.enqueue(new Callback<List<Dropdown>>() {
            @Override
            public void onResponse(Call<List<Dropdown>> call, Response<List<Dropdown>> response) {
                ddAdapter = new DropdownAdapter(context, android.R.layout.simple_spinner_item, response.body());
                spKelas.setAdapter(ddAdapter);
            }

            @Override
            public void onFailure(Call<List<Dropdown>> call, Throwable t) {
                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        spMatapelajaran.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String txtMp = spMatapelajaran.getText().toString();
                getIdMataPelajaran(txtMp);
            }
        });

        Button btnCari = (Button) findViewById(R.id.btn_cari);
        btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataSiswa();
            }
        });

        Button btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(idMp != 0){
                    for (Siswa siswa : siswaList){
                        HashMap<String, String> params = new HashMap<>();
                        params.put("siswa", String.valueOf(siswa.getId()));
                        params.put("guru", String.valueOf(sm.getPrefInteger("id_guru")));
                        params.put("pelajaran", String.valueOf(idMp));

                        int status = 0;
                        if(siswa.getStatus().equals("hadir")){
                            status = 1;
                        }else if(siswa.getStatus().equals("izin")){
                            status = 2;
                        }else if(siswa.getStatus().equals("sakit")){
                            status = 3;
                        }else if(siswa.getStatus().equals("tdk")){
                            status = 4;
                        }
                        params.put("absen", String.valueOf(status));

                        GetDataService service = ApiClient.getClient().create(GetDataService.class);
                        Call<ResponseBody> call = service.saveAbsen(params);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Toast.makeText(context, "Berhasil Disubmit", Toast.LENGTH_SHORT).show();
                                siswaList.clear();
                                mAdapter.notifyDataSetChanged();

                                Intent intent = new Intent(AbsensiActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }else{
                    Toast.makeText(context, "Mata Pelajaran Belum Dipilih", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void prepareList(List<Siswa> dataList){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_absensi);

        mAdapter = new AbsensiAdapter(dataList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new AbsensiListener(getApplicationContext(), recyclerView, new AbsensiListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {
                final Siswa siswa = siswaList.get(position);

                if(builder == null){
                    builder = new AlertDialog.Builder(context);
                    builder.setTitle("Pilih Absensi");

                    // add a list
                    String[] absensi = {"Hadir", "Izin", "Sakit", "Tdk Hadir"};
                    builder.setItems(absensi, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            switch (which) {
                                case 0:
                                    siswa.setStatus("hadir");
                                    break;
                                case 1:
                                    siswa.setStatus("izin");
                                    break;
                                case 2:
                                    siswa.setStatus("sakit");
                                    break;
                                case 3:
                                    siswa.setStatus("tdk");
                                    break;
                            }
                            listReloadTask();
                            builder = null;
                        }
                    });

                    // create and show the alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private int getIdDropdown(String data){
        int id = 0;
        if(data.length() > 0){
            String[] parts = data.split("-");
            id = Integer.valueOf(parts[0]);
        }
        return id;
    }

    private void getIdMataPelajaran(String data){
        GetDataService service = ApiClient.getClient().create(GetDataService.class);
        Call<List<MataPelajaran>> call = service.getIdMataPelajaran(data,sm.getPrefInteger("id_sekolah"));
        call.enqueue(new Callback<List<MataPelajaran>>() {
            @Override
            public void onResponse(Call<List<MataPelajaran>> call, Response<List<MataPelajaran>> response) {
                idMp = response.body().get(0).getId();
            }

            @Override
            public void onFailure(Call<List<MataPelajaran>> call, Throwable t) {
                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void listReloadTask(){
        GetDataService service = ApiClient.getClient().create(GetDataService.class);
        Call<List<Siswa>> call = service.getDaftarSiswa(getIdDropdown(spKelas.getText().toString()));
        call.enqueue(new Callback<List<Siswa>>() {
            @Override
            public void onResponse(Call<List<Siswa>> call, Response<List<Siswa>> response) {
                mAdapter.updateAbsensiListItems(response.body());
            }

            @Override
            public void onFailure(Call<List<Siswa>> call, Throwable t) {
                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
