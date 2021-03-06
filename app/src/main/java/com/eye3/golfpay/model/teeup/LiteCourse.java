package com.eye3.golfpay.model.teeup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiteCourse {
    @SerializedName("course_id")
    @Expose
    public String course_id;

    @SerializedName("courseName")
    @Expose
    public String courseName;

    @SerializedName("ctype")
    @Expose
    public String ctype;

    @SerializedName("hole_id")
    @Expose
    public String hole_id = "";

    @SerializedName("par")
    @Expose
    public String par = "";

    @SerializedName("hole_no")
    @Expose
    public String hole_no = "";

    @SerializedName("handiCap")
    @Expose
    public String handiCap = "";

    @SerializedName("hole_total_size")
    @Expose
    public String hole_total_size = "";
}
