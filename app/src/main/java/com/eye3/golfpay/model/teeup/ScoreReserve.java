package com.eye3.golfpay.model.teeup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ScoreReserve implements Serializable {

    @SerializedName("reserve_id")
    @Expose
    public String reserve_id;

    @SerializedName("guest")
    @Expose
    public List<GuestLight> guest;
}
