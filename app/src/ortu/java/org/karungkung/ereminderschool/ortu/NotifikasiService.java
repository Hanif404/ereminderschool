package org.karungkung.ereminderschool.ortu;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import org.karungkung.ereminderschool.R;
import org.karungkung.ereminderschool.ortu.Models.Absensi;
import org.karungkung.ereminderschool.ortu.Models.PekerjaanRumah;
import org.karungkung.ereminderschool.ortu.Models.Pengumuman;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by blackflag on 10/05/18.
 */

public class NotifikasiService extends Service {
    private SessionManager sm;
    private List<Integer> arrNotifAbsensi;
    private List<Integer> arrNotifPr;
    private List<Integer> arrNotifPengumuman;

    @Override
    public void onCreate() {
        super.onCreate();
        sm = new SessionManager(this);
        arrNotifAbsensi = new ArrayList<Integer>();
        arrNotifPr = new ArrayList<Integer>();
        arrNotifPengumuman = new ArrayList<Integer>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.postDelayed(runnable, 5000);
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(sm.getPrefInteger("id") != 0){
                myNotif();
            }
            handler.postDelayed(this, 5000);
        }
    };

    private void myNotif(){
        if(getTime() == 24){
            arrNotifAbsensi.clear();
            arrNotifPr.clear();
            arrNotifPengumuman.clear();
        }

        //Absensi
        GetDataService service = ApiClient.getClient().create(GetDataService.class);
        Call<List<Absensi>> callAbsensi = service.getNotifAbsensi(sm.getPrefString("nisn"));
        callAbsensi.enqueue(new Callback<List<Absensi>>() {
            @Override
            public void onResponse(Call<List<Absensi>> call, Response<List<Absensi>> response) {
                for (Absensi absensi : response.body()) {

                    String absen = "";
                    if(absensi.getIsAbsen() == 1){
                        absen = "Hadir";
                    }else if(absensi.getIsAbsen() == 2){
                        absen = "Izin";
                    }else if(absensi.getIsAbsen() == 3){
                        absen = "Sakit";
                    }else if(absensi.getIsAbsen() == 4){
                        absen = "Tidak Hadir";
                    }
                    if (arrNotifAbsensi.contains(absensi.getId())) {
                        //data notif sudah ada
                    } else {
                        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.ic_school)
                                .setContentTitle("Absensi")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true);

                        mBuilder.setContentText(absensi.getJudul()+", Keterangan : " + absen).setNumber(arrNotifAbsensi.size());
                        arrNotifAbsensi.add(absensi.getId());

                        manager.notify(absensi.getId(), mBuilder.build());
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Absensi>> call, Throwable t) {

            }
        });

        Call<List<PekerjaanRumah>> callPR = service.getNotifPr(sm.getPrefString("nisn"));
        callPR.enqueue(new Callback<List<PekerjaanRumah>>() {
            @Override
            public void onResponse(Call<List<PekerjaanRumah>> call, Response<List<PekerjaanRumah>> response) {
                for (PekerjaanRumah pr : response.body()) {

                    if (arrNotifPr.contains(pr.getId())) {
                        //data notif sudah ada
                    } else {
                        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.ic_school)
                                .setContentTitle("Pekerjaan Rumah")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true);

                        mBuilder.setContentText(pr.getMataPelajaran()+ " PR : " + pr.getIsi()).setNumber(arrNotifPr.size());
                        arrNotifPr.add(pr.getId());

                        manager.notify(pr.getId(), mBuilder.build());
                    }
                }

            }

            @Override
            public void onFailure(Call<List<PekerjaanRumah>> call, Throwable t) {

            }
        });

        Call<List<Pengumuman>> callPengumuman = service.getNotifPengumuman(sm.getPrefString("nisn"));
        callPengumuman.enqueue(new Callback<List<Pengumuman>>() {
            @Override
            public void onResponse(Call<List<Pengumuman>> call, Response<List<Pengumuman>> response) {
                for (Pengumuman pengumuman : response.body()) {

                    if (arrNotifPengumuman.contains(pengumuman.getId())) {
                        //data notif sudah ada
                    } else {
                        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.ic_school)
                                .setContentTitle("Pengumuman")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true);

                        mBuilder.setContentText(pengumuman.getJudul()+ " Pelaksanaan tgl. " + pengumuman.getTglMulai()).setNumber(arrNotifPengumuman.size());
                        arrNotifPengumuman.add(pengumuman.getId());

                        manager.notify(pengumuman.getId(), mBuilder.build());
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Pengumuman>> call, Throwable t) {

            }
        });
    }

    private int getTime(){
        Calendar cal = Calendar.getInstance();
        Date date=cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH");
        String formattedDate = dateFormat.format(date);
        return Integer.valueOf(formattedDate);
    }
}
