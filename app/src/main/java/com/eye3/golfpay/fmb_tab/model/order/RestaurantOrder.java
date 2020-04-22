package com.eye3.golfpay.fmb_tab.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RestaurantOrder {
    //서버에서 assgin받아야함.
    @SerializedName("receipt_id")
    @Expose
    String receipt_no;//영수증아이디

    @SerializedName("restaurant_id")
    @Expose
    String restaurant_id;

    @SerializedName("restaurant_name")
    @Expose
    String restaurant_name;
    //주문 일시
    @SerializedName("ordered_at")
    @Expose
    String ordered_at;

    List<OrderDetail> mOrderDetailList = new ArrayList<OrderDetail>();
}
