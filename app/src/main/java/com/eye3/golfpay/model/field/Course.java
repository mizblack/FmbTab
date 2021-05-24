package com.eye3.golfpay.model.field;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Course implements Serializable {

    @SerializedName("course_id")
    @Expose
    public String course_id;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("cc_id")
    @Expose
    public String cc_id;
    @SerializedName("courseName")
    @Expose
    public String courseName;
    @SerializedName("ctype")
    @Expose
    public String ctype;
    @SerializedName("created_at")
    @Expose
    public String created_at;
    @SerializedName("holes")
    @Expose
    public List<Hole> holes;
}
