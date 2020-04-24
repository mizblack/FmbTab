package com.eye3.golfpay.fmb_tab.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShadeOrder implements Serializable {
    @SerializedName("shade_id")
    @Expose
    public String shade_id;

    @SerializedName("reserve_id")
    @Expose
    public String reserve_id ;
    @SerializedName("order_detail")
    @Expose
    List<OrderDetail> orderDetailArrayList = new ArrayList<>();

    public ShadeOrder(String shade_id, String reserve_id, List<OrderDetail> orderDetailArrayList){
        this.shade_id = shade_id;
        this.reserve_id = reserve_id;
        this.orderDetailArrayList = orderDetailArrayList;
    }
}
