package org.karungkung.ereminderschool.guru.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hanif on 30/07/18.
 */

public class OldSiswa {

    @SerializedName("id")
    private int id;

    @SerializedName("nisn")
    private String nis;

    @SerializedName("name")
    private String nama;

    private String status;
    private boolean selected;

    public OldSiswa(int id, String nis, String nama, String status) {
        this.setId(id);
        this.setNis(nis);
        this.setNama(nama);
        this.setStatus(status);
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
