package com.eye3.golfpay.model.teeup;

import com.eye3.golfpay.model.field.Course;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GuestLight implements Serializable {

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("guestName")
    @Expose
    public String guestName;

    @SerializedName("putting")
    @Expose
    public String putting;

    @SerializedName("tee_shot")
    @Expose
    public String tee_shot = "";

    @SerializedName("par")
    @Expose
    public String par = "";

    @SerializedName("tar")
    @Expose
    public String tar = "";

    @SerializedName("hole_no")
    @Expose
    public String hole_no = "";

    @SerializedName("handiCap")
    @Expose
    public String handiCap = "";

    @SerializedName("hole_total_size")
    @Expose
    public String hole_total_size = "";

    @SerializedName("lastHoleNo")
    @Expose
    public int lastHoleNo = 0;

    @SerializedName("course")
    @Expose
    public List<Course> course;

    @SerializedName("totalPar")
    @Expose
    public int totalPar;

    @SerializedName("totalTar")
    @Expose
    public int totalTar;

    @SerializedName("totalPutting")
    @Expose
    public int totalPutting;

    @SerializedName("team_no")
    @Expose
    public int team_no;

    @SerializedName("Ranking")
    @Expose
    public int ranking;

    @SerializedName("totalRankingPutting")
    @Expose
    public int totalRankingPutting;

}
