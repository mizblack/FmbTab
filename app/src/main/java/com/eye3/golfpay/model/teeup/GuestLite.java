package com.eye3.golfpay.model.teeup;

import com.eye3.golfpay.model.field.Course;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GuestLite implements Serializable {

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("guestName")
    @Expose
    public String guestName;

    @SerializedName("putting_a")
    @Expose
    public String putting_a;

    @SerializedName("tee_shot_a")
    @Expose
    public String tee_shot_a = "";

    @SerializedName("par_a")
    @Expose
    public String par_a = "";

    @SerializedName("tar_a")
    @Expose
    public String tar_a = "";

    @SerializedName("putting_b")
    @Expose
    public String putting_b;

    @SerializedName("tee_shot_b")
    @Expose
    public String tee_shot_b = "";

    @SerializedName("par_b")
    @Expose
    public String par_b = "";

    @SerializedName("tar_b")
    @Expose
    public String tar_b = "";

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

    @SerializedName("ranking")
    @Expose
    public int ranking;

    @SerializedName("totalRankingPutting")
    @Expose
    public int totalRankingPutting;

}
