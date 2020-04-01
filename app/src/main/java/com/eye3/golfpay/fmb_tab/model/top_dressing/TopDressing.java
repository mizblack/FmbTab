package com.eye3.golfpay.fmb_tab.model.top_dressing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopDressing {

    public TopDressing(String start_time, String finish_time, String course, String hole){
        this.start_time = start_time;
        this.finish_time = finish_time;
        this.course = course;
        this.hole = hole;
    }
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
