package org.karungkung.ereminderschool.guru.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hanif on 02/08/18.
 */

public class Pekerjaan {
    @SerializedName("id")
    private int id;

    @SerializedName("id_pelajaran")
    private int idMp;

    @SerializedName("nm_mp")
    private String namaMp;

    @SerializedName("id_kelas")
    private int idKelas;

    @SerializedName("nm_kls")
    private String namaKelas;

    @SerializedName("tgl_pr")
    private String tglBuat;

    @SerializedName("isi_pr")
    private String isi;

    @SerializedName("tgl_selesai")
    private String tglSelesai;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMp() {
        return idMp;
    }

    public void setIdMp(int idMp) {
        this.idMp = idMp;
    }

    public String getNamaMp() {
        return namaMp;
    }

    public void setNamaMp(String namaMp) {
        this.namaMp = namaMp;
    }

    public int getIdKelas() {
        return idKelas;
    }

    public void setIdKelas(int idKelas) {
        this.idKelas = idKelas;
    }

    public String getNamaKelas() {
        return namaKelas;
    }

    public void setNamaKelas(String namaKelas) {
        this.namaKelas = namaKelas;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getTglSelesai() {
        return tglSelesai;
    }

    public void setTglSelesai(String tglSelesai) {
        this.tglSelesai = tglSelesai;
    }

    public String getTglBuat() {
        return tglBuat;
    }

    public void setTglBuat(String tglBuat) {
        this.tglBuat = tglBuat;
    }
}
