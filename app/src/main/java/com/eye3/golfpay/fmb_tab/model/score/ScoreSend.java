package com.eye3.golfpay.fmb_tab.model.score;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/*
*   스코어를 서버로 보낼때 사용, ReserveScore에 리스트로 포함됨
 */
public class ScoreSend extends Score {
    @SerializedName("guest_id")
    @Expose
    public String guest_id = "";

    public ScoreSend(String guest_id, String par, String putting, String tar){
        super(par, putting, tar);
        this.guest_id = guest_id;

    }
}
