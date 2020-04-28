package com.eye3.golfpay.fmb_tab.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReceiptUnit implements Serializable {

    @SerializedName("order_time")
    @Expose
    public String order_time ;

    @SerializedName("recept_list")
    @Expose
    public List<PersonalOrder> recept_list = new ArrayList<PersonalOrder>();
}
