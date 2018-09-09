package org.karungkung.ereminderschool.guru.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hanif on 01/07/18.
 */

public class Dashboard {
    @SerializedName("title")
    private String title;

    @SerializedName("konten")
    private String konten;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKonten() {
        return konten;
    }

    public void setKonten(String konten) {
        this.konten = konten;
    }
}
