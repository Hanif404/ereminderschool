package org.karungkung.ereminderschool.guru;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.karungkung.ereminderschool.R;
import org.karungkung.ereminderschool.guru.Adapters.DropdownAdapter;
import org.karungkung.ereminderschool.guru.Adapters.TransferSiswaAdapter;
import org.karungkung.ereminderschool.guru.models.Dropdown;
import org.karungkung.ereminderschool.guru.models.Siswa;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransferSiswaActivity extends AppCompatActivity {

    private List<Dropdown> kelasList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TransferSiswaAdapter mAdapter;
    private final Context context = this;
    private DropdownAdapter ddAdapter;
    private SessionManager sm;
    private Dialog dialog = null;
    private int idKelas;
    private AlertDialog.Builder builder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_siswa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sm = new SessionManager(this);
        getDataSiswa();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialog == null){
                    dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_form_tfsiswa);

                    prepareForm();
                    dialog.show();
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
                Toast.makeText(TransferSiswaActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prepareForm(){
        final EditText pilihKelas = (EditText) dialog.findViewById(R.id.txt_kelas);
        pilihKelas.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                // add a list
                GetDataService service = ApiClient.getClient().create(GetDataService.class);
                Call<List<Dropdown>> call = service.getOutKelas(sm.getPrefInteger("id_sekolah"), sm.getPrefInteger("id_kelas"));
                call.enqueue(new Callback<List<Dropdown>>() {
                    @Override
                    public void onResponse(Call<List<Dropdown>> call, Response<List<Dropdown>> response) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Pilih Kelas");

                        kelasList = response.body();
                        ddAdapter = new DropdownAdapter(context, android.R.layout.simple_spinner_item, response.body());

                        builder.setAdapter(ddAdapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int pos) {
                                Dropdown dd = kelasList.get(pos);
                                pilihKelas.setText(dd.getName());
                                idKelas = dd.getId();
                            }
                        });

                        // create and show the alert dialog
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                    @Override
                    public void onFailure(Call<List<Dropdown>> call, Throwable t) {
                        Toast.makeText(TransferSiswaActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Button btnSimpan = (Button) dialog.findViewById(R.id.btn_submit);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Siswa> stList = ((TransferSiswaAdapter) mAdapter).getSiswaList();
                for (int i = 0; i < stList.size(); i++) {
                    final Siswa siswa = stList.get(i);
                    if (siswa.getSelected() == true) {
                        GetDataService service = ApiClient.getClient().create(GetDataService.class);
                        Call<ResponseBody> call = service.pindahSiswa(siswa.getId(), idKelas);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Toast.makeText(TransferSiswaActivity.this, siswa.getNama()+" Berhasil Pindah", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(TransferSiswaActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                dialog.dismiss();
                dialog = null;
                getDataSiswa();
            }
        });

        Button btnBatal = (Button) dialog.findViewById(R.id.btn_cancel);
        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog = null;
            }
        });
    }

    private void prepareList(List<Siswa> dataList){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_tfsiswa);

        mAdapter = new TransferSiswaAdapter(dataList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

}
