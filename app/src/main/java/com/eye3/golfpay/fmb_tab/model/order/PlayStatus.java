package com.eye3.golfpay.fmb_tab.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayStatus implements Serializable {
    @SerializedName("play_status")
    @Expose
    public String play_status;

    @SerializedName("reserve_id")
    @Expose
    public String reserve_id ;

    public PlayStatus(String reserve_id, String play_status){
        this.play_status = play_status;
        this.reserve_id = reserve_id;
    }
}
