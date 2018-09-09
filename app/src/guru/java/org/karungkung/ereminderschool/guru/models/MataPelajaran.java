package org.karungkung.ereminderschool.guru.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hanif on 05/08/18.
 */

public class MataPelajaran {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String nama;
    private boolean selected;

    public MataPelajaran(int id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
