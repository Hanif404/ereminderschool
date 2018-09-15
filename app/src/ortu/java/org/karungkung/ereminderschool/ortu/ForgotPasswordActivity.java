package org.karungkung.ereminderschool.ortu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.karungkung.ereminderschool.R;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        final EditText username = (EditText) findViewById(R.id.txt_username);
        final EditText newPassword = (EditText) findViewById(R.id.txt_password_new);

        Button btnSimpan = (Button) findViewById(R.id.btn_submit);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
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
                            HashMap<String, String> params = new HashMap<>();
                            params.put("username", username.getText().toString());
                            params.put("password", newPassword.getText().toString());

                            Call<ResponseBody> callsubmit = service.resetPassword(params);
                            callsubmit.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    Toast.makeText(ForgotPasswordActivity.this, "Berhasil Direset", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(ForgotPasswordActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(ForgotPasswordActivity.this, "Pengguna tidak terdaftar", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(ForgotPasswordActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
