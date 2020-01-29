package com.eye3.golfpay.fmb_tab.model.score;

import com.eye3.golfpay.fmb_tab.model.field.Hole;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public abstract class Score  implements Serializable {
    //파점수 계산시 사용( -2 under par)
    @SerializedName("par")
    public String par;

    @SerializedName("putting")
    public String putting;

    //putting + hit
    @SerializedName("tar")
    public String tar;

}
