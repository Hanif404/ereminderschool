package org.karungkung.ereminderschool.guru;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.karungkung.ereminderschool.R;
import org.karungkung.ereminderschool.guru.models.Guru;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText txtUsername = (EditText) findViewById(R.id.txt_username);
                EditText txtPassword = (EditText) findViewById(R.id.txt_password);

                HashMap<String, String> params = new HashMap<>();
                params.put("user", txtUsername.getText().toString());
                params.put("pass", txtPassword.getText().toString());

                GetDataService service = ApiClient.getClient().create(GetDataService.class);
                Call<List<Guru>> callsubmit = service.loginGuru(params);
                callsubmit.enqueue(new Callback<List<Guru>>() {
                    @Override
                    public void onResponse(Call<List<Guru>> call, Response<List<Guru>> response) {
                        if(response.body().size() > 0){
                            SessionManager sm = new SessionManager(getApplicationContext());
                            sm.setPrefInteger("id_guru", response.body().get(0).getIdGuru());
                            sm.setPrefInteger("id_kelas", response.body().get(0).getIdKelas());
                            sm.setPrefInteger("id_sekolah", response.body().get(0).getIdSekolah());
                            sm.setPrefInteger("jns_guru", response.body().get(0).getJnsGuru());
                            sm.setPrefString("no_identitas", response.body().get(0).getNoidentitas());
                            sm.setPrefString("name", response.body().get(0).getName());
                            sm.setPrefString("mata_pelajaran", response.body().get(0).getMataPelajaran());

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "Username atau password salah", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Guru>> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        final TextView btnAkunBaru = findViewById(R.id.btn_akun_baru);
        btnAkunBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, AkunBaruActivity.class);
                startActivity(intent);
            }
        });

        TextView btnForgot = (TextView) findViewById(R.id.btn_forgot);
        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}
