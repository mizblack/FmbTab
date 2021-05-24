package com.eye3.golfpay.model.teeup;

import com.eye3.golfpay.model.field.Course;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Player implements Serializable, Comparable<Player>{

    @SerializedName("id")
    @Expose
    public String guest_id;

    @SerializedName("guestName")
    @Expose
    public String guestName;

    @SerializedName("reserve_id")
    @Expose
    public String reserve_id;

    @SerializedName("teeoff")
    @Expose
    public String teeoff = "";

    @SerializedName("team_no")
    @Expose
    public int team_no = 0;

    @SerializedName("course")
    @Expose
    public List<Course> course;

    @SerializedName("totalPar")
    @Expose
    public int totalPar;

    @SerializedName("totalTar")
    @Expose
    public int totalTar;

    @SerializedName("lastHoleNo")
    @Expose
    public int lastHoleNo;

    @SerializedName("Ranking")
    @Expose
    public int ranking;

    @SerializedName("totalRankingPutting")
    @Expose
    public int totalRankingPutting;


    @Override
    public int compareTo(Player o) {
        if (ranking < o.ranking) {
            return -1;
        } else if (ranking > o.ranking) {
            return 1;
        }
        return 0;
    }
}
