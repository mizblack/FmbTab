package com.eye3.golfpay.model.teeup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ScoreReserve implements Serializable {

    @SerializedName("reserve_id")
    @Expose
    public String reserve_id;

    @SerializedName("lastHoleNo")
    @Expose
    public int lastHoleNo = 0;

    @SerializedName("course_a_id")
    @Expose
    public String course_a_id = "";

    @SerializedName("course_b_id")
    @Expose
    public String course_b_id = "";

    @SerializedName("guest")
    @Expose
    public List<GuestLite> guest;
}
