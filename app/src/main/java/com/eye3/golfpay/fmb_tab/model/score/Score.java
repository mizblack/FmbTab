package com.eye3.golfpay.fmb_tab.model.score;

import com.eye3.golfpay.fmb_tab.model.field.Hole;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Score  implements Serializable {
    //파점수 계산시 사용( -2 under par)
    @SerializedName("par")
    @Expose
    public String par = "-";

    @SerializedName("putting")
    @Expose
    public String putting = "-";

    //putting + hit
    @SerializedName("tar")
    @Expose
    public String tar = "-";

    public Score(String par, String putting, String tar){
        this.par = par;
        this.putting = putting ;
        this.tar = tar;
    }

}
