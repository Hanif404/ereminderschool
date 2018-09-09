package org.karungkung.ereminderschool.guru;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.karungkung.ereminderschool.R;
import org.karungkung.ereminderschool.guru.Adapters.DropdownAdapter;
import org.karungkung.ereminderschool.guru.models.Dropdown;
import org.karungkung.ereminderschool.guru.models.MataPelajaran;
import org.karungkung.ereminderschool.guru.models.Pekerjaan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormPekerjaanActivity extends AppCompatActivity {
    private int mYear, mMonth, mDay;
    private Context context = this;
    private SessionManager sm;
    private DropdownAdapter ddAdapter;
    private int id, idMp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pekerjaan);
        sm = new SessionManager(this);
        id = getIntent().getIntExtra("id",0);

        prepareForm();
    }

    private void prepareForm() {
        final MaterialBetterSpinner spMatapelajaran = (MaterialBetterSpinner) findViewById(R.id.sp_matapelajaran);
        final MaterialBetterSpinner spKelas = (MaterialBetterSpinner) findViewById(R.id.sp_kelas);
        final EditText txtIsi = (EditText) findViewById(R.id.txt_isi);

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

        final EditText txtTglPr = (EditText) findViewById(R.id.txt_tgl_selesai);
        txtTglPr.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        int day = dayOfMonth;
                        String dayStr = "";
                        if(day <= 9){
                            dayStr = "0"+day;
                        }else{
                            dayStr = ""+day;
                        }

                        int month = (monthOfYear + 1);
                        String monthStr = "";
                        if(month <= 9){
                            monthStr = "0"+month;
                        }else{
                            monthStr = ""+month;
                        }
                        txtTglPr.setText(year + "-" + monthStr+ "-" + dayStr);

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        Button btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(idMp != 0){
                    if(txtTglPr.getText().length() > 0){
                        HashMap<String, String> params = new HashMap<>();
                        params.put("id", String.valueOf(id));
                        params.put("mataPelajaran", String.valueOf(idMp));
                        params.put("kelas", getIdDropdown(spKelas.getText().toString()));
                        params.put("idGuru", String.valueOf(sm.getPrefInteger("id_guru")));
                        params.put("isi", txtIsi.getText().toString());
                        params.put("tglSelesai", txtTglPr.getText().toString());
                        params.put("idSekolah", String.valueOf(sm.getPrefInteger("id_sekolah")));

                        GetDataService service = ApiClient.getClient().create(GetDataService.class);
                        Call<ResponseBody> call = service.savePr(params);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Toast.makeText(context, "Berhasil Disubmit", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(context, DaftarPekerjaanActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(context, "Tanggal Selesai Harus diisi", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "Mata Pelajaran Masih Kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(id != 0){
            Call<List<Pekerjaan>> callData = service.getDataPr(id);
            callData.enqueue(new Callback<List<Pekerjaan>>() {
                @Override
                public void onResponse(Call<List<Pekerjaan>> call, Response<List<Pekerjaan>> response) {
                    spMatapelajaran.setText(response.body().get(0).getNamaMp());
                    spKelas.setText(response.body().get(0).getNamaKelas());
                    txtIsi.setText(response.body().get(0).getIsi());
                    txtTglPr.setText(response.body().get(0).getTglSelesai());
                }

                @Override
                public void onFailure(Call<List<Pekerjaan>> call, Throwable t) {
                    Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private String getIdDropdown(String data){
        String id = "";
        if(data.length() > 0){
            String[] parts = data.split("-");
            id = parts[0];
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
}
