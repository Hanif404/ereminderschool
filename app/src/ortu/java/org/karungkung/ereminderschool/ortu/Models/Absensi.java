package org.karungkung.ereminderschool.ortu.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hanif on 10/08/18.
 */

public class Absensi {
    @SerializedName("id")
    private int id;

    @SerializedName("nm_mp")
    private String judul;

    @SerializedName("tgl_absen")
    private String tgl;

    @SerializedName("is_absen")
    private int isAbsen;

    @SerializedName("nm_gr")
    private String pengajar;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getPengajar() {
        return pengajar;
    }

    public void setPengajar(String pengajar) {
        this.pengajar = pengajar;
    }

    public int getIsAbsen() {
        return isAbsen;
    }

    public void setIsAbsen(int isAbsen) {
        this.isAbsen = isAbsen;
    }
}
