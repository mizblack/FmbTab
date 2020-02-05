package com.eye3.golfpay.fmb_tab.model.score;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScoreSend extends Score {
    @SerializedName("guest_id")
    @Expose
    public String guest_id;
}
