package org.karungkung.ereminderschool.guru.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hanif on 17/08/18.
 */

public class Guru {
    @SerializedName("id")
    private int idGuru;

    @SerializedName("id_kelas")
    private int idKelas;

    @SerializedName("id_sekolah")
    private int idSekolah;

    @SerializedName("jns_guru")
    private int jnsGuru;

    @SerializedName("no_identitas")
    private String noidentitas;

    @SerializedName("name")
    private String name;

    @SerializedName("mata_pelajaran")
    private String mataPelajaran;

    public int getIdGuru() {
        return idGuru;
    }

    public void setIdGuru(int idGuru) {
        this.idGuru = idGuru;
    }

    public int getIdKelas() {
        return idKelas;
    }

    public void setIdKelas(int idKelas) {
        this.idKelas = idKelas;
    }

    public int getJnsGuru() {
        return jnsGuru;
    }

    public void setJnsGuru(int jnsGuru) {
        this.jnsGuru = jnsGuru;
    }

    public String getNoidentitas() {
        return noidentitas;
    }

    public void setNoidentitas(String noidentitas) {
        this.noidentitas = noidentitas;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMataPelajaran() {
        return mataPelajaran;
    }

    public void setMataPelajaran(String mataPelajaran) {
        this.mataPelajaran = mataPelajaran;
    }

    public int getIdSekolah() {
        return idSekolah;
    }

    public void setIdSekolah(int idSekolah) {
        this.idSekolah = idSekolah;
    }
}
