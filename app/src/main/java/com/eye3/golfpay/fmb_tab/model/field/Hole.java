package com.eye3.golfpay.fmb_tab.model.field;

import com.eye3.golfpay.fmb_tab.model.order.teeup.Score;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Hole  implements Serializable {

    @SerializedName("holeId")
    @Expose
    public String holeId;

    @SerializedName("par")
    @Expose
    public int par;

    @SerializedName("distance")
    @Expose
    public int distance;

    //player가 홀에서 친 타수
    @SerializedName("hit")
    @Expose
    public int hit;

    //player가 홀에서 친 퍼트수
    @SerializedName("putt")
    @Expose
    public int putt;

}
