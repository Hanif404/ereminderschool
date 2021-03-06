package org.karungkung.ereminderschool.ortu.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hanif on 11/08/18.
 */

public class Pengumuman {
    @SerializedName("id")
    private int id;

    @SerializedName("judul")
    private String judul;

    @SerializedName("tgl_pengumuman")
    private String tglPengumuman;

    @SerializedName("isi_pengumuman")
    private String isiPengumuman;

    @SerializedName("tgl_mulai")
    private String tglMulai;

    @SerializedName("tgl_selesai")
    private String tglSelesai;

    @SerializedName("wkt_mulai")
    private String wktMulai;

    @SerializedName("wkt_selesai")
    private String wktSelesai;

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getTglPengumuman() {
        return tglPengumuman;
    }

    public void setTglPengumuman(String tglPengumuman) {
        this.tglPengumuman = tglPengumuman;
    }

    public String getIsiPengumuman() {
        return isiPengumuman;
    }

    public void setIsiPengumuman(String isiPengumuman) {
        this.isiPengumuman = isiPengumuman;
    }

    public String getTglMulai() {
        return tglMulai;
    }

    public void setTglMulai(String tglMulai) {
        this.tglMulai = tglMulai;
    }

    public String getTglSelesai() {
        return tglSelesai;
    }

    public void setTglSelesai(String tglSelesai) {
        this.tglSelesai = tglSelesai;
    }

    public String getWktMulai() {
        return wktMulai;
    }

    public void setWktMulai(String wktMulai) {
        this.wktMulai = wktMulai;
    }

    public String getWktSelesai() {
        return wktSelesai;
    }

    public void setWktSelesai(String wktSelesai) {
        this.wktSelesai = wktSelesai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
