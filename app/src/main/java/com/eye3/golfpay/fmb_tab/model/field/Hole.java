package com.eye3.golfpay.fmb_tab.model.field;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Hole  implements Serializable {

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
    public String hole_no;

    @SerializedName("par")
    @Expose
    public String par;

    @SerializedName("handiCap")
    @Expose
    public String handiCap;

    @SerializedName("gps_lat")
    @Expose
    public String gps_lat;

    @SerializedName("gps_lon")
    @Expose
    public String gps_lon;

    @SerializedName("ctype")
    @Expose
    public String ctype;

    @SerializedName("game_calc")
    @Expose
    public String game_calc;

    @SerializedName("hole_total_size")
    @Expose
    public String hole_total_size;

    @SerializedName("bunker")
    @Expose
    public String bunker;

    @SerializedName("ob_point")
    @Expose
    public String ob_point;

    @SerializedName("holecup_section")
    @Expose
    public String holecup_section;

    @SerializedName("holecup_area")
    @Expose
    public String holecup_area;


    @SerializedName("hole_remark")
    @Expose
    public String hole_remark;

    @SerializedName("gameType")
    @Expose
    public String gameType;

    @SerializedName("hole_image_1")
    @Expose
    public String hole_image_1;

    @SerializedName("hole_image_2")
    @Expose
    public String hole_image_2;

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

//
//    public Hole(String id, String par, String distance){
//        this.id = id;
//        this.par = par;
//        this.distance = distance;
//    }


}
