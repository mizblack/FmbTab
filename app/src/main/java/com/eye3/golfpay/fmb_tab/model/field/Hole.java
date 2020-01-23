package com.eye3.golfpay.fmb_tab.model.field;

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

    public Hole(String holeId, int par, int distance){
        this.holeId = holeId;
        this.par = par;
        this.distance = distance;
    }


}
