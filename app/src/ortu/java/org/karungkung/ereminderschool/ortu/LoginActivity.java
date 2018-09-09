package org.karungkung.ereminderschool.ortu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.karungkung.ereminderschool.R;
import org.karungkung.ereminderschool.ortu.Models.Ortu;

import java.util.HashMap;
import java.util.List;

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
                Call<List<Ortu>> callsubmit = service.loginOrtu(params);
                callsubmit.enqueue(new Callback<List<Ortu>>() {
                    @Override
                    public void onResponse(Call<List<Ortu>> call, Response<List<Ortu>> response) {
                        SessionManager sm = new SessionManager(getApplicationContext());
                        sm.setPrefInteger("id", response.body().get(0).getId());
                        sm.setPrefInteger("id_sekolah", response.body().get(0).getIdSekolah());
                        sm.setPrefString("name", response.body().get(0).getName());
                        sm.setPrefString("nisn", response.body().get(0).getNisn());

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<List<Ortu>> call, Throwable t) {
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
    }
}
