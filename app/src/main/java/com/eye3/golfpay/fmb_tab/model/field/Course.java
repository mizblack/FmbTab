package com.eye3.golfpay.fmb_tab.model.field;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Course implements Serializable {

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

   public Hole[] arrHole = new Hole[9];

}
