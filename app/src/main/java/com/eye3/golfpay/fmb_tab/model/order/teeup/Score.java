package com.eye3.golfpay.fmb_tab.model.order.teeup;

import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Score implements Serializable {

    @SerializedName("scoreList")
    @Expose
    int[] scores = new int[9];

}
