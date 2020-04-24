package com.eye3.golfpay.fmb_tab.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Menu implements Serializable {

    @SerializedName("pos_item_id")
    @Expose
    public String pos_item_id;

    @SerializedName("item_name")
    @Expose
    public String item_name;

    @SerializedName("qty")
    @Expose
    public String qty;

    @SerializedName("unit_price")
    @Expose
    public String unit_price;

    @SerializedName("total_amount")
    @Expose
    public String total_amount;

}
