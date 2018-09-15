package org.karungkung.ereminderschool.ortu;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.karungkung.ereminderschool.R;
import org.karungkung.ereminderschool.ortu.Adapters.DropdownAdapter;
import org.karungkung.ereminderschool.ortu.Models.Dropdown;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AkunBaruActivity extends AppCompatActivity {

    private DropdownAdapter ddAdapter;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun_baru);
        prepareForm();
    }

    private void prepareForm(){
        final MaterialBetterSpinner spSekolah = (MaterialBetterSpinner) findViewById(R.id.sp_sekolah);
        final EditText nisn = (EditText) findViewById(R.id.txt_nisn);
        final EditText nama = (EditText) findViewById(R.id.txt_nama);
        final EditText noTelp = (EditText) findViewById(R.id.txt_no_telp);
        final EditText alamat = (EditText) findViewById(R.id.txt_alamat);
        final EditText username = (EditText) findViewById(R.id.txt_username);
        final EditText password = (EditText) findViewById(R.id.txt_password);
        Button btnSubmit = (Button) findViewById(R.id.btn_submit);

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

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> paramCheck = new HashMap<>();
                paramCheck.put("username", username.getText().toString());

                final GetDataService service = ApiClient.getClient().create(GetDataService.class);
                Call<Boolean> callCheck = service.checkUsername(paramCheck);
                callCheck.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.body()){
                            Toast.makeText(AkunBaruActivity.this, "Pengguna Sudah terdaftar", Toast.LENGTH_SHORT).show();
                        }else{
                            HashMap<String, String> paramCheck = new HashMap<>();
                            paramCheck.put("nisn", nisn.getText().toString());
                            paramCheck.put("sekolah", getIdDropdown(spSekolah.getText().toString()));

                            final GetDataService service = ApiClient.getClient().create(GetDataService.class);
                            Call<Boolean> callCheck = service.checkSiswa(paramCheck);
                            callCheck.enqueue(new Callback<Boolean>() {
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    if(response.body()){
                                        HashMap<String, String> params = new HashMap<>();
                                        params.put("nisn", nisn.getText().toString());
                                        params.put("nama", nama.getText().toString());
                                        params.put("noTelp", noTelp.getText().toString());
                                        params.put("alamat", alamat.getText().toString());
                                        params.put("idSekolah", getIdDropdown(spSekolah.getText().toString()));
                                        params.put("username", username.getText().toString());
                                        params.put("password", password.getText().toString());


                                        Call<ResponseBody> callsubmit = service.addOrtu(params);
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
                                    }else{
                                        Toast.makeText(AkunBaruActivity.this, "Nisn siswa tidak terdaftar", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Boolean> call, Throwable t) {
                                    Toast.makeText(AkunBaruActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
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
}
