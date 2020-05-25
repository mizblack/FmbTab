package com.eye3.golfpay.fmb_tab.model.teeup;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class CaddyInfo implements Serializable {
    @SerializedName("admins_id")
    private String admins_id;

    @SerializedName("name")
    private String name;

    @SerializedName("cart_no")
    private String cart_no;

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

    public String getCart_no() {
        return cart_no;
    }

    public void setCart_no(String cart_no) {
        this.cart_no = cart_no;
    }
}
