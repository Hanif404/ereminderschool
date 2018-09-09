package org.karungkung.ereminderschool.guru;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.karungkung.ereminderschool.R;
import org.karungkung.ereminderschool.guru.Adapters.DashboardAdapter;
import org.karungkung.ereminderschool.guru.models.Dashboard;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IPickResult {

    private List<Dashboard> dashboardList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DashboardAdapter mAdapter;
    private SessionManager sm;
    private Context context = this;
    private CircleImageView foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        sm = new SessionManager(this);

        TextView txtNama = (TextView) findViewById(R.id.txt_nama);
        txtNama.setText(sm.getPrefString("name"));

        TextView txtNoIdentitas = (TextView) findViewById(R.id.txt_noidentitas);
        txtNoIdentitas.setText(sm.getPrefString("no_identitas"));

        foto = (CircleImageView) findViewById(R.id.foto_profile);
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(new PickSetup()).show(MainActivity.this);
            }
        });

        getFoto();
        prepareList();
    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            final File file = new File(r.getPath());
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("fileprofile", file.getName(), requestFile);

            GetDataService service = ApiClient.getClient().create(GetDataService.class);
            Call<ResponseBody> call = service.upload(body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    modifyFoto(file);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(context, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.absensi) {
            Intent intent = new Intent(getApplicationContext(), AbsensiActivity.class);
            startActivity(intent);
        } else if (id == R.id.pekerjaan_rumah) {
            Intent intent = new Intent(getApplicationContext(), DaftarPekerjaanActivity.class);
            startActivity(intent);
        } else if (id == R.id.pengumuman) {
            Intent intent = new Intent(getApplicationContext(), DaftarPengumumanActivity.class);
            startActivity(intent);
        } else if (id == R.id.daftar_siswa) {
            if(sm.getPrefInteger("jns_guru").equals(2)){
                Intent intent = new Intent(getApplicationContext(), SiswaActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(context, "Tidak Ada Akses", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.pindah_siswa) {
            if(sm.getPrefInteger("jns_guru").equals(2)){
                Intent intent = new Intent(getApplicationContext(), TransferSiswaActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(context, "Tidak Ada Akses", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.keluar) {
            sm.clearPreferences();
            Intent intent = new Intent(getApplicationContext(), SplashscreenActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void prepareList(){
        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = ApiClient.getClient().create(GetDataService.class);
        Call<List<Dashboard>> call = service.getKeahlian(sm.getPrefInteger("id_guru"));
        call.enqueue(new Callback<List<Dashboard>>() {
            @Override
            public void onResponse(Call<List<Dashboard>> call, Response<List<Dashboard>> response) {
                generateDataList(response.body());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Dashboard>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFoto()
    {
        GetDataService service = ApiClient.getClient().create(GetDataService.class);
        Call<ResponseBody> callFoto = service.ambilFoto(sm.getPrefInteger("id_guru"));
        callFoto.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Bitmap bm = BitmapFactory.decodeStream(response.body().byteStream());
                foto.setImageBitmap(bm);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void modifyFoto(final File file)
    {
        GetDataService service = ApiClient.getClient().create(GetDataService.class);
        Call<ResponseBody> call = service.hapusFoto(sm.getPrefInteger("id_guru"));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                HashMap<String, String> params = new HashMap<>();
                params.put("foto", file.getName());
                params.put("id", String.valueOf(sm.getPrefInteger("id_guru")));

                GetDataService service = ApiClient.getClient().create(GetDataService.class);
                Call<ResponseBody> callFoto = service.saveFoto(params);
                callFoto.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(context, "Berhasil Disubmit", Toast.LENGTH_SHORT).show();
                        getFoto();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataList(final List<Dashboard> dataList) {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new DashboardAdapter(dataList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
}
