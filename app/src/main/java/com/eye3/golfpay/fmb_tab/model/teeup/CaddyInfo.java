package com.eye3.golfpay.fmb_tab.model.teeup;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CaddyInfo implements Serializable {
    @SerializedName("admins_id")
    private String admins_id;
    @SerializedName("name")
    private String name;

    public String getAdmins_id() {
        return admins_id;
    }

    public void setAdmins_id(String admins_id) {
        this.admins_id = admins_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
