package com.eye3.golfpay.fmb_tab.model.field;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Course implements Serializable {
    //*********************************************
    @SerializedName("course_id")
    @Expose
    public String course_id;
    //********************************************

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("cc_id")
    @Expose
    public String cc_id;

    @SerializedName("part_id")
    @Expose
    public String part_id;

    @SerializedName("courseName")
    @Expose
    public String courseName;

    @SerializedName("public_use")
    @Expose
    public String public_use;

    @SerializedName("ctype")
    @Expose
    public String ctype;

    @SerializedName("is_use")
    @Expose
    public String is_use;

    @SerializedName("del_use")
    @Expose
    public String del_use;

    @SerializedName("course_img")
    @Expose
    public String course_img;

    @SerializedName("created_user")
    @Expose
    public String created_user;

    @SerializedName("created_at")
    @Expose
    public String created_at;

    @SerializedName("updated_user")
    @Expose
    public String updated_user;

    @SerializedName("updated_at")
    @Expose
    public String updated_at;

    @SerializedName("holes")
    @Expose
    public ArrayList<Hole> holes = new ArrayList<>();

//    public Course(Course copyCourse){
//        this.course_id = copyCourse.course_id;
//        this.id = copyCourse.id;
//        this.cc_id =
//                this.part_id =
//    }
//


}
