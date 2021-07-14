package com.eye3.golfpay.model.field;

import com.eye3.golfpay.model.score.Score;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Hole implements Serializable {

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("cc_id")
    @Expose
    public String cc_id;

    @SerializedName("course_id")
    @Expose
    public String course_id;

    @SerializedName("hole_no")
    @Expose
    public String hole_no = "";

    @SerializedName("par")
    @Expose
    public String par = "";

    @SerializedName("handiCap")
    @Expose
    public String handiCap = "";

    @SerializedName("ctype")
    @Expose
    public String ctype;

    @SerializedName("hole_total_size")
    @Expose
    public String hole_total_size;

    @SerializedName("bunker")
    @Expose
    public String bunker;

    @SerializedName("gameType")
    @Expose
    public String gameType;

    @SerializedName("img_1_file_url")
    @Expose
    public String img_1_file_url;

    @SerializedName("img_2_file_url")
    @Expose
    public String img_2_file_url;

    @SerializedName("tbox")
    @Expose
    public ArrayList<TBox> tBox = new ArrayList<>();

    @SerializedName("course_hole_width_per")
    @Expose
    public int course_hole_width_per;

    @SerializedName("course_hole_height_per")
    @Expose
    public int course_hole_height_per;

    @SerializedName("course_start_width_per")
    @Expose
    public int course_start_width_per;

    @SerializedName("course_start_height_per")
    @Expose
    public int course_start_height_per;

    @SerializedName("hole_pin_width_per")
    @Expose
    public int hole_pin_width_per;

    @SerializedName("hole_pin_height_per")
    @Expose
    public int hole_pin_height_per;

    @SerializedName("hole_image_meter")
    @Expose
    public int hole_image_meter;

    @SerializedName("score")
    @Expose
    public Score playedScore;

    @SerializedName("hole_length")
    @Expose
    public int hole_length;

    @SerializedName("distance_between_hole")
    @Expose
    public int distance_between_hole;
}
