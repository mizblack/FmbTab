package com.eye3.golfpay.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReserveGameType implements Serializable {
    @SerializedName("ret_code")
    @Expose
    public String ret_code;

    @SerializedName("ret_msg")
    @Expose
    public String ret_msg;
    @SerializedName("res_id")
    @Expose
    public int res_id;

    @SerializedName("course_long")
    @Expose
    public String course_long;

    @SerializedName("hole_no_long")
    @Expose
    public int hole_no_long;

    @SerializedName("course_near")
    @Expose
    public String course_near;

    @SerializedName("hole_no_near")
    @Expose
    public int hole_no_near;
}
