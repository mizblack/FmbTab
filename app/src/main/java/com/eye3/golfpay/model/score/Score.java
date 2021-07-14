package com.eye3.golfpay.model.score;

import com.eye3.golfpay.model.field.Hole;
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

    @SerializedName("tee_shot")
    @Expose
    public String teeShot = "bunker";

    public Score(String par, String putting, String tar, String teeShot){
        this.par = par;
        this.putting = putting ;
        this.tar = tar;
        this.teeShot = teeShot;
    }

    public Score() {

    }
}
