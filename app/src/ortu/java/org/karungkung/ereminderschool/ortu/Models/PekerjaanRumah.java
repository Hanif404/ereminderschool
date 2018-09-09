package org.karungkung.ereminderschool.ortu.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hanif on 10/08/18.
 */

public class PekerjaanRumah {
    @SerializedName("id")
    private int id;

    @SerializedName("nm_mp")
    private String mataPelajaran;

    @SerializedName("tgl_selesai")
    private String tglSelesai;

    @SerializedName("tgl_pr")
    private String tglPr;

    @SerializedName("isi_pr")
    private String isi;

    @SerializedName("nm_gr")
    private String pengajar;

    public String getMataPelajaran() {
        return mataPelajaran;
    }

    public void setMataPelajaran(String mataPelajaran) {
        this.mataPelajaran = mataPelajaran;
    }

    public String getTglSelesai() {
        return tglSelesai;
    }

    public void setTglSelesai(String tglSelesai) {
        this.tglSelesai = tglSelesai;
    }

    public String getTglPr() {
        return tglPr;
    }

    public void setTglPr(String tglPr) {
        this.tglPr = tglPr;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getPengajar() {
        return pengajar;
    }

    public void setPengajar(String pengajar) {
        this.pengajar = pengajar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
