package com.eye3.golfpay.fmb_tab.model.field;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Hole  implements Serializable {

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("cc_id")
    @Expose
    public String cc_id;

    @SerializedName("hole_no")
    @Expose
    public String hole_no;

    @SerializedName("par")
    @Expose
    public String par;

    @SerializedName("distance")
    @Expose
    public String distance;

    public Hole(String id, String par, String distance){
        this.id = id;
        this.par = par;
        this.distance = distance;
    }


}
