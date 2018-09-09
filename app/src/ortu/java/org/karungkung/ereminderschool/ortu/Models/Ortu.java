package org.karungkung.ereminderschool.ortu.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hanif on 05/09/18.
 */

public class Ortu {
    @SerializedName("id")
    private int id;

    @SerializedName("nisn")
    private String nisn;

    @SerializedName("id_sekolah")
    private int idSekolah;

    @SerializedName("name")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNisn() {
        return nisn;
    }

    public void setNisn(String nisn) {
        this.nisn = nisn;
    }

    public int getIdSekolah() {
        return idSekolah;
    }

    public void setIdSekolah(int idSekolah) {
        this.idSekolah = idSekolah;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
