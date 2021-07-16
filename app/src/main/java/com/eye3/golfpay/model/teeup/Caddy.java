
package com.eye3.golfpay.model.teeup;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class Caddy implements Serializable {

    @SerializedName("caddy_id")
    public String caddy_id;
    @SerializedName("name")
    public String name;
    @SerializedName("email")
    public String email;
    @SerializedName("ret_code")
    public String cart_no;
}
