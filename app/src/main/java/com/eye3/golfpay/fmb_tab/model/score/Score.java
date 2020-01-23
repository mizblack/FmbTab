package com.eye3.golfpay.fmb_tab.model.score;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Score implements Serializable {

    @SerializedName("putt")
    public int putt;

    @SerializedName("hit")
    public int hit;

}
