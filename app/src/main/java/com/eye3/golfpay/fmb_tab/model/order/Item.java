
package com.eye3.golfpay.fmb_tab.model.order;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Item implements Serializable {

    @SerializedName("id")
    private int mId;
    @SerializedName("price")
    private int mPrice;
    @SerializedName("qty")
    private int mQty;
    @SerializedName("total")
    private int mTotal;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public int getQty() {
        return mQty;
    }

    public void setQty(int qty) {
        mQty = qty;
    }

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int total) {
        mTotal = total;
    }

}
