
package com.eye3.golfpay.fmb_tab.model.order;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class OrderData implements Serializable {

    @SerializedName("order_detail")
    private ArrayList<OrderDetail> mOrderDetail;
    @SerializedName("reserve_id")
    private Long mReserveId;
    @SerializedName("shade_id")
    private Long mShadeId;

    public ArrayList<OrderDetail> getOrderDetail() {
        return mOrderDetail;
    }

    public void setOrderDetail(ArrayList<OrderDetail> orderDetail) {
        mOrderDetail = orderDetail;
    }

    public Long getReserveId() {
        return mReserveId;
    }

    public void setReserveId(Long reserveId) {
        mReserveId = reserveId;
    }

    public Long getShadeId() {
        return mShadeId;
    }

    public void setShadeId(Long shadeId) {
        mShadeId = shadeId;
    }

}
