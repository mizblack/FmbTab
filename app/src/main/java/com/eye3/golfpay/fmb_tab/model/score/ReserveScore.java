package com.eye3.golfpay.fmb_tab.model.score;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.eye3.golfpay.fmb_tab.model.teeup.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class ReserveScore implements Serializable {
    @SerializedName("ret_code")
    @Expose
    public String ret_code;

    @SerializedName("ret_msg")
    @Expose
    public String ret_msg;

    @SerializedName("reserve_id")
    @Expose
    public String reserve_id;

    @SerializedName("hole_id")
    @Expose
    public String hole_id;

    @SerializedName("guest_score")
    @Expose
    public ArrayList<ScoreSend> guest_score_list = new ArrayList<>();

    public ReserveScore(int size) {
        for (int i = 0; size > i; i++)
            guest_score_list.add(new ScoreSend());
    }

    public void setReserve_id(String reserve_id) {
        this.reserve_id = reserve_id;
    }

    public void setHole_id(String hole_id) {
        this.hole_id = hole_id;
    }

    //guest = player
    public void setGuest_score_list(ArrayList<ScoreSend> guest_score_list) {
        this.guest_score_list = guest_score_list;
    }
}
