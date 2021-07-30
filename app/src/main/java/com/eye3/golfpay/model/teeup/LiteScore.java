package com.eye3.golfpay.model.teeup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LiteScore implements Serializable {

    @SerializedName("course")
    @Expose
    public List<LiteCourse> course = new ArrayList<>();

   @SerializedName("reserve")
   @Expose
   public List<ScoreReserve> reserves = new ArrayList<>();
}
