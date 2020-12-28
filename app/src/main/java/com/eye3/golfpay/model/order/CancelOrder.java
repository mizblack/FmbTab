package com.eye3.golfpay.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CancelOrder implements Serializable {

    @SerializedName("reserve_id")
    @Expose
    public String reserve_id;

    @SerializedName("shade_id")
    @Expose
    public String shade_id;

    @SerializedName("cancel_order_detail")
    @Expose
    public List<CancelOrderDetail>  cancel_order_detail = new ArrayList<>();

    public static class CancelOrderDetail {

        @SerializedName("order_bills_id")
        @Expose
        public String order_bills_id;
    }
}
