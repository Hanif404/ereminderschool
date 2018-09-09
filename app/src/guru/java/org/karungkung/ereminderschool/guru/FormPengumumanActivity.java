package org.karungkung.ereminderschool.guru;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import org.karungkung.ereminderschool.R;
import org.karungkung.ereminderschool.guru.models.Pengumuman;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormPengumumanActivity extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Context context = this;
    private int idEvent;
    private SessionManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pengumuman);

        idEvent = getIntent().getIntExtra("id",0);
        sm = new SessionManager(this);

        prepareForm();
    }

    private void prepareForm(){
        final EditText txtJudul = (EditText) findViewById(R.id.txt_judul);
        final EditText txtIsi = (EditText) findViewById(R.id.txt_isi);

        final EditText txtTglMulai = (EditText) findViewById(R.id.txt_tgl_mulai);
        txtTglMulai.setOnClickListener(new View.OnClickListener(){

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
                        txtTglMulai.setText(year + "-" + monthStr+ "-" + dayStr);

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        final EditText txtTglAkhir = (EditText) findViewById(R.id.txt_tgl_akhir);
        txtTglAkhir.setOnClickListener(new View.OnClickListener(){

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

                        txtTglAkhir.setText(year + "-" + monthStr+ "-" + dayStr);

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        final EditText txtWktMulai = (EditText) findViewById(R.id.txt_wkt_mulai);
        txtWktMulai.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                /// Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String min = "";
                        String hour = "";
                        if(hourOfDay <= 9){
                            hour = "0"+hourOfDay;
                        }else{
                            hour = ""+hourOfDay;
                        }

                        if(minute <= 9){
                            min = "0"+minute;
                        }else{
                            min = ""+minute;
                        }
                        txtWktMulai.setText(hour + ":" + min + ":00");
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        final EditText txtWktAkhir = (EditText) findViewById(R.id.txt_wkt_akhir);
        txtWktAkhir.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                /// Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hour = "";
                        String min = "";
                        if(hourOfDay <= 9){
                            hour = "0"+hourOfDay;
                        }else{
                            hour = ""+hourOfDay;
                        }

                        if(minute <= 9){
                            min = "0"+minute;
                        }else{
                            min = ""+minute;
                        }

                        txtWktAkhir.setText(hour + ":" + min + ":00");
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        Button btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(idEvent));
                params.put("judul", txtJudul.getText().toString());
                params.put("isi", txtIsi.getText().toString());
                params.put("tglMulai", txtTglMulai.getText().toString());
                params.put("wktMulai", txtWktMulai.getText().toString());
                params.put("tglSelesai", txtTglAkhir.getText().toString());
                params.put("wktSelesai", txtWktAkhir.getText().toString());
                params.put("kelas", String.valueOf(sm.getPrefInteger("id_kelas")));

                GetDataService service = ApiClient.getClient().create(GetDataService.class);
                Call<ResponseBody> call = service.saveEvent(params);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(FormPengumumanActivity.this, "Berhasil Disubmit", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(FormPengumumanActivity.this, DaftarPengumumanActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(FormPengumumanActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        if(idEvent != 0){
          /*Create handle for the RetrofitInstance interface*/
            GetDataService service = ApiClient.getClient().create(GetDataService.class);
            Call<List<Pengumuman>> call = service.getDataEvent(idEvent);
            call.enqueue(new Callback<List<Pengumuman>>() {
                @Override
                public void onResponse(Call<List<Pengumuman>> call, Response<List<Pengumuman>> response) {
                    txtJudul.setText(response.body().get(0).getJudul());
                    txtIsi.setText(response.body().get(0).getIsi());
                    txtTglMulai.setText(response.body().get(0).getTglMulai());
                    txtWktMulai.setText(response.body().get(0).getWktMulai());
                    txtTglAkhir.setText(response.body().get(0).getTglSelesai());
                    txtWktAkhir.setText(response.body().get(0).getWktSelesai());
                }

                @Override
                public void onFailure(Call<List<Pengumuman>> call, Throwable t) {
                    Toast.makeText(FormPengumumanActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
