package com.eye3.golfpay.fmb_tab.model.field;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Club implements Serializable {

    @SerializedName("wood_set")
    @Expose
    public List<String> woodList;

    @SerializedName("iron_set")
    @Expose
    public List<String> IronList;

    @SerializedName("wedge_set")
    @Expose
    public List<String> wedgeList;

    @SerializedName("wedge_cover")
    @Expose
    public List<String> wedgeCoverList;

}
