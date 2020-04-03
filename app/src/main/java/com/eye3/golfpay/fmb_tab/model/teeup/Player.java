package com.eye3.golfpay.fmb_tab.model.teeup;

import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Player implements Serializable{

    @SerializedName("guestName")
    @Expose
    public String name;

    @SerializedName("id")
    @Expose
    public String guest_id;

    @SerializedName("totalPar")
    @Expose
    public String totalPar;

    @SerializedName("totalTar")
    @Expose
    public String totalTar;

    @SerializedName("totalRankingTar")
    @Expose
    public String totalRankingTar;

    @SerializedName("totalRankingPutting")
    @Expose
    public String totalRankingPutting;

    @SerializedName("lastHoleNo")
    @Expose
    public String lastHoleNo;

    @SerializedName("Ranking")
    @Expose
    public String Ranking;


    @SerializedName("course")
    @Expose
    public List<Course> playingCourse ;

//    @Override
//    public int compareTo(Player o) {
//        if (Integer.valueOf(this.Ranking) < Integer.valueOf(o.Ranking)) {
//            return -1;
//        } else if (Integer.valueOf(this.Ranking) > Integer.valueOf(o.Ranking)) {
//            return 1;
//        }
//
//        return 0;
//    }
}
