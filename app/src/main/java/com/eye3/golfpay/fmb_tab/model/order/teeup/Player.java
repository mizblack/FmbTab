package com.eye3.golfpay.fmb_tab.model.order.teeup;

import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("rank")
    @Expose
    public String rank;

    @SerializedName("total_rank")
    @Expose
    public String totalRank;

    public ArrayList<Course> playedCourseList = new ArrayList<>();
}
