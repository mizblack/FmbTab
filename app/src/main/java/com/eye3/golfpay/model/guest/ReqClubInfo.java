package com.eye3.golfpay.model.guest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReqClubInfo {

    @SerializedName("wood")
    @Expose
    public String wood = "";

    @SerializedName("utility")
    @Expose
    public String utility = "";

    @SerializedName("iron")
    @Expose
    public String iron = "";

    @SerializedName("putter")
    @Expose
    public String putter = "";

    @SerializedName("wedge")
    @Expose
    public String wedge = "";

    @SerializedName("wood_cover")
    @Expose
    public String wood_cover = "";

    @SerializedName("putter_cover")
    @Expose
    public String putter_cover = "";

    @SerializedName("wedge_cover")
    @Expose
    public String wedge_cover = "";

    @SerializedName("putter_cover")
    @Expose
    public String iron_cover = "";

    @SerializedName("utility_cover")
    @Expose
    public String utility_cover = "";

    @SerializedName("wood_memo")
    @Expose
    public String wood_memo = "";

    @SerializedName("utility_memo")
    @Expose
    public String utility_memo = "";

    @SerializedName("iron_memo")
    @Expose
    public String iron_memo = "";

    @SerializedName("wedge_memo")
    @Expose
    public String wedge_memo = "";

    @SerializedName("putter_memo")
    @Expose
    public String putter_memo = "";
}
