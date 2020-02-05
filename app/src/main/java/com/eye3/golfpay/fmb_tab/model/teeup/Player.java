package com.eye3.golfpay.fmb_tab.model.teeup;

import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {

    @SerializedName("guestName")
    @Expose
    public String name;

    @SerializedName("id")
    @Expose
    public String guest_id;

    @SerializedName("course")
    @Expose
    public ArrayList<Course> playingCourse = new ArrayList<Course>();

}
