package org.karungkung.ereminderschool.guru;

import android.app.Dialog;
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
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.karungkung.ereminderschool.R;
import org.karungkung.ereminderschool.guru.Adapters.DashboardAdapter;
import org.karungkung.ereminderschool.guru.Adapters.DropdownAdapter;
import org.karungkung.ereminderschool.guru.Adapters.MatapelajaranAdapter;
import org.karungkung.ereminderschool.guru.models.Dropdown;
import org.karungkung.ereminderschool.guru.models.Kelas;
import org.karungkung.ereminderschool.guru.models.MataPelajaran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AkunBaruActivity extends AppCompatActivity {

    private List<MataPelajaran> mpList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MatapelajaranAdapter mAdapter;
    private Context context = this;
    private DropdownAdapter ddAdapter;
    private int jnsGuru = 1; //default : guru pengajar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun_baru);
        prepareForm();
    }

    private void prepareForm(){
        final MaterialBetterSpinner spSekolah = (MaterialBetterSpinner) findViewById(R.id.sp_sekolah);
        final MaterialBetterSpinner spKelas = (MaterialBetterSpinner) findViewById(R.id.sp_kelas);
        final MaterialBetterSpinner spJnsGuru = (MaterialBetterSpinner) findViewById(R.id.sp_jns_guru);
        final EditText noIdentitas = (EditText) findViewById(R.id.txt_noidentitas);
        final EditText nama = (EditText) findViewById(R.id.txt_nama);
        final EditText username = (EditText) findViewById(R.id.txt_username);
        final EditText password = (EditText) findViewById(R.id.txt_password);

        spKelas.setVisibility(View.GONE);
        GetDataService service = ApiClient.getClient().create(GetDataService.class);
        Call<List<Dropdown>> call = service.getSekolah();
        call.enqueue(new Callback<List<Dropdown>>() {
            @Override
            public void onResponse(Call<List<Dropdown>> call, Response<List<Dropdown>> response) {
                ddAdapter = new DropdownAdapter(context, android.R.layout.simple_spinner_item, response.body());
                spSekolah.setAdapter(ddAdapter);
            }

            @Override
            public void onFailure(Call<List<Dropdown>> call, Throwable t) {
                Toast.makeText(AkunBaruActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        String[] arrJnsGuru = {"Wali Kelas", "Guru Pengajar"};
        ArrayAdapter<String> adapterJnsGuru = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, arrJnsGuru);
        spJnsGuru.setAdapter(adapterJnsGuru);

        spJnsGuru.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String txtjnsGuru = spJnsGuru.getText().toString();
                if(txtjnsGuru.equals("Guru Pengajar")){
                    spKelas.setVisibility(View.GONE);
                    spKelas.setText("");
                    jnsGuru = 1;
                }else{
                    spKelas.setVisibility(View.VISIBLE);
                    jnsGuru = 2;

                    GetDataService service = ApiClient.getClient().create(GetDataService.class);
                    Call<List<Dropdown>> call = service.getKelas(getIdDropdown(spSekolah.getText().toString()));
                    call.enqueue(new Callback<List<Dropdown>>() {
                        @Override
                        public void onResponse(Call<List<Dropdown>> call, Response<List<Dropdown>> response) {
                            ddAdapter = new DropdownAdapter(context, android.R.layout.simple_spinner_item, response.body());
                            spKelas.setAdapter(ddAdapter);
                        }

                        @Override
                        public void onFailure(Call<List<Dropdown>> call, Throwable t) {
                            Toast.makeText(AkunBaruActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        final EditText pilihMp = (EditText) findViewById(R.id.txt_mp);
        pilihMp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(spSekolah.length() > 0){
                    GetDataService service = ApiClient.getClient().create(GetDataService.class);
                    Call<List<MataPelajaran>> call = service.getMataPelajaran(getIdDropdown(spSekolah.getText().toString()));
                    call.enqueue(new Callback<List<MataPelajaran>>() {
                        @Override
                        public void onResponse(Call<List<MataPelajaran>> call, Response<List<MataPelajaran>> response) {
                            Dialog dialog = new Dialog(context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_mp);

                            prepareFormMpDialog(dialog, response.body());
                            mAdapter.notifyDataSetChanged();
                            dialog.show();
                        }

                        @Override
                        public void onFailure(Call<List<MataPelajaran>> call, Throwable t) {
                            Toast.makeText(AkunBaruActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(AkunBaruActivity.this, "Pilih Sekolah Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> params = new HashMap<>();
                params.put("noIdentitas", noIdentitas.getText().toString());
                params.put("nama", nama.getText().toString());
                params.put("jnsGuru", String.valueOf(jnsGuru));
                params.put("foto", "");
                params.put("idSekolah", getIdDropdown(spSekolah.getText().toString()));
                params.put("kelas", getIdDropdown(spKelas.getText().toString()));
                params.put("mp", pilihMp.getText().toString());
                params.put("username", username.getText().toString());
                params.put("password", password.getText().toString());

                GetDataService service = ApiClient.getClient().create(GetDataService.class);
                Call<ResponseBody> callsubmit = service.addGuru(params);
                callsubmit.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(AkunBaruActivity.this, "Berhasil Disubmit", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AkunBaruActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(AkunBaruActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private String getIdDropdown(String data){
        String id = "";
        if(data.length() > 0){
            String[] parts = data.split("-");
            id = parts[0];
        }
        return id;
    }

    private void prepareFormMpDialog(final Dialog dialog, List<MataPelajaran> mpList){
        recyclerView = (RecyclerView) dialog.findViewById(R.id.recycler_mp);
        mAdapter = new MatapelajaranAdapter(this, mpList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        Button btnSimpan = (Button) dialog.findViewById(R.id.btn_submit);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = "";
                int j = 0;
                List<MataPelajaran> mprList = ((MatapelajaranAdapter) mAdapter).getMpList();
                for (int i = 0; i < mprList.size(); i++) {
                    MataPelajaran mp = mprList.get(i);
                    if (mp.isSelected() == true) {
                        if(j == 0){
                            data = data + mp.getNama().toString();
                        }else{
                            data = data + ", " + mp.getNama().toString();
                        }
                        j++;
                    }
                }
                EditText pilihMp = (EditText) findViewById(R.id.txt_mp);
                pilihMp.setText(data);

                dialog.dismiss();
            }
        });

        Button btnBatal = (Button) dialog.findViewById(R.id.btn_cancel);
        btnBatal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
