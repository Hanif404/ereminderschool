package org.karungkung.ereminderschool.ortu.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hanif on 12/08/18.
 */

public class Dropdown {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getId() +"-"+ getName();
    }
}
