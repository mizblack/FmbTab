package com.eye3.golfpay.fmb_tab.model.teeup;

import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.eye3.golfpay.fmb_tab.model.score.HoleScoreSet;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("rank")
    @Expose
    public String rank;

    @SerializedName("total_rank")
    @Expose
    public String totalRank;

    public ArrayList<Course> playedCourseList = new ArrayList<>();
    public ArrayList<HoleScoreSet> courseScores = new ArrayList<>(); //코스 전체 점수

}
