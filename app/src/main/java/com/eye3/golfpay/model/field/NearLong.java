package com.eye3.golfpay.model.field;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class NearLong implements Serializable {

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("guestName")
    @Expose
    public String guestName;

    @SerializedName("near_ranking")
    @Expose
    public int near_ranking;

    @SerializedName("long_ranking")
    @Expose
    public int long_ranking;

    @SerializedName("near")
    @Expose
    public String nearest = "";

    @SerializedName("long")
    @Expose
    public String longest = "";
}
