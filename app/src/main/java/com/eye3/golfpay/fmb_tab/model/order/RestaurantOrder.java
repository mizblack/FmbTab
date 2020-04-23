package com.eye3.golfpay.fmb_tab.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RestaurantOrder {
    //서버에서 assgin받아야함.
//    @SerializedName("order_id")
//    @Expose
    public String order_id="xxxxxxx";//영수증아이디

    @SerializedName("restaurant_id")
    @Expose
    public String restaurant_id = "01234";

    @SerializedName("restaurant_name")
    @Expose
    public String restaurant_name="대식당";
    //주문 일시
    @SerializedName("ordered_at")
    @Expose
    public String ordered_at="20200422445";

    @SerializedName("whole_total_amount")
    @Expose
    public String wholeTotalAmount = "0";

    //주문완료, 주문예약, 주문취소
//    @SerializedName("order_state")
//    @Expose
    public String orderState="" ;

    @SerializedName("order_detail_list")
    @Expose
    public List<OrderDetail> mOrderDetailList = new ArrayList<OrderDetail>();


}
