package com.eye3.golfpay.fmb_tab.model.field;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Course implements Serializable {

    @SerializedName("course_id")
    @Expose
    public String CourseId;

    @SerializedName("course_name")
    @Expose
    public String CourseName;

    @SerializedName("hole_list")
    @Expose
   public  ArrayList<Hole> holeList = new ArrayList<>();

}
