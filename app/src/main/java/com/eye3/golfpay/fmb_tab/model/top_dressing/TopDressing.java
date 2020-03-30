package com.eye3.golfpay.fmb_tab.model.top_dressing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopDressing {
    @SerializedName("start_time")
    @Expose
    public String start_time;

    @SerializedName("finish_time")
    @Expose
    public String finish_time;

    @SerializedName("course")
    @Expose
    public String course;

    @SerializedName("hole")
    @Expose
    public String hole;

}
