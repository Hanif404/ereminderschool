package org.karungkung.ereminderschool.guru;

import org.karungkung.ereminderschool.guru.models.Dashboard;
import org.karungkung.ereminderschool.guru.models.Dropdown;
import org.karungkung.ereminderschool.guru.models.Guru;
import org.karungkung.ereminderschool.guru.models.Kelas;
import org.karungkung.ereminderschool.guru.models.MataPelajaran;
import org.karungkung.ereminderschool.guru.models.Pekerjaan;
import org.karungkung.ereminderschool.guru.models.Pengumuman;
import org.karungkung.ereminderschool.guru.models.Siswa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hanif on 12/08/18.
 */

public interface GetDataService {
    @GET("sekolah")
    Call<List<Dropdown>> getSekolah();

    @GET("kelas/{id}")
    Call<List<Dropdown>> getKelas(@Path("id") String idsekolah);

    @GET("outerkelas/{id}/{kls}")
    Call<List<Dropdown>> getOutKelas(@Path("id") int idsekolah, @Path("kls") int idkelas);

    @GET("matapelajaran/{id}")
    Call<List<MataPelajaran>> getMataPelajaran(@Path("id") String idsekolah);

    @GET("idmatapelajaran/{mp}/{sk}")
    Call<List<MataPelajaran>> getIdMataPelajaran(@Path("mp") String mp, @Path("sk") int idsk);

    @POST("akunguru")
    @FormUrlEncoded
    Call<ResponseBody> addGuru(@FieldMap HashMap<String, String> params);

    @POST("loginguru")
    @FormUrlEncoded
    Call<List<Guru>> loginGuru(@FieldMap HashMap<String, String> params);

    @GET("ahliguru/{id}")
    Call<List<Dashboard>> getKeahlian(@Path("id") int idguru);

    @GET("siswa/{id}")
    Call<List<Siswa>> getDaftarSiswa(@Path("id") int idkelas);

    @POST("savesiswa")
    @FormUrlEncoded
    Call<ResponseBody> addSiswa(@FieldMap HashMap<String, String> params);

    @POST("editsiswa")
    @FormUrlEncoded
    Call<ResponseBody> editSiswa(@FieldMap HashMap<String, String> params);

    @GET("datasiswa/{id}")
    Call<List<Siswa>> getDataSiswa(@Path("id") int idsiswa);

    @GET("deletesiswa/{id}")
    Call<ResponseBody> deleteSiswa(@Path("id") int idsiswa);

    @GET("pindahsiswa/{id}/{kls}")
    Call<ResponseBody> pindahSiswa(@Path("id") int idsiswa, @Path("kls") int idkelas);

    @GET("event/{id}")
    Call<List<Pengumuman>> getDaftarEvent(@Path("id") int idevent);

    @GET("dataevent/{id}")
    Call<List<Pengumuman>> getDataEvent(@Path("id") int idevent);

    @POST("saveevent")
    @FormUrlEncoded
    Call<ResponseBody> saveEvent(@FieldMap HashMap<String, String> params);

    @GET("deleteevent/{id}")
    Call<ResponseBody> deleteEvent(@Path("id") int idevent);

    @GET("pr/{id}")
    Call<List<Pekerjaan>> getDaftaPr(@Path("id") int idguru);

    @GET("deletepr/{id}")
    Call<ResponseBody> deletePr(@Path("id") int idpr);

    @POST("savepr")
    @FormUrlEncoded
    Call<ResponseBody> savePr(@FieldMap HashMap<String, String> params);

    @GET("datapr/{id}")
    Call<List<Pekerjaan>> getDataPr(@Path("id") int idpr);

    @POST("absensi")
    @FormUrlEncoded
    Call<ResponseBody> saveAbsen(@FieldMap HashMap<String, String> params);

    @POST("upload")
    @Multipart
    Call<ResponseBody> upload(@Part MultipartBody.Part file);

    @POST("setfoto")
    @FormUrlEncoded
    Call<ResponseBody> saveFoto(@FieldMap HashMap<String, String> params);

    @GET("getfoto/{id}")
    Call<ResponseBody> ambilFoto(@Path("id") int idguru);

    @GET("delfoto/{id}")
    Call<ResponseBody> hapusFoto(@Path("id") int idguru);

    @POST("checkguru")
    @FormUrlEncoded
    Call<Boolean> checkUsername(@FieldMap HashMap<String, String> params);

    @POST("resetguru")
    @FormUrlEncoded
    Call<ResponseBody> resetPassword(@FieldMap HashMap<String, String> params);
}
