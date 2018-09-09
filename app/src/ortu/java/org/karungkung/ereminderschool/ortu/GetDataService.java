package org.karungkung.ereminderschool.ortu;

import org.karungkung.ereminderschool.ortu.Models.Absensi;
import org.karungkung.ereminderschool.ortu.Models.Dropdown;
import org.karungkung.ereminderschool.ortu.Models.Ortu;
import org.karungkung.ereminderschool.ortu.Models.PekerjaanRumah;
import org.karungkung.ereminderschool.ortu.Models.Pengumuman;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by hanif on 12/08/18.
 */

public interface GetDataService {
    @GET("sekolah")
    Call<List<Dropdown>> getSekolah();

    @POST("akunortu")
    @FormUrlEncoded
    Call<ResponseBody> addOrtu(@FieldMap HashMap<String, String> params);

    @POST("loginortu")
    @FormUrlEncoded
    Call<List<Ortu>> loginOrtu(@FieldMap HashMap<String, String> params);

    @POST("checksiswa")
    @FormUrlEncoded
    Call<Boolean> checkSiswa(@FieldMap HashMap<String, String> params);

    @GET("notifAbsensi/{id}")
    Call<List<Absensi>> getNotifAbsensi(@Path("id") String nisn);

    @GET("notifPr/{id}")
    Call<List<PekerjaanRumah>> getNotifPr(@Path("id") String nisn);

    @GET("notifPengumuman/{id}")
    Call<List<Pengumuman>> getNotifPengumuman(@Path("id") String nisn);
}
