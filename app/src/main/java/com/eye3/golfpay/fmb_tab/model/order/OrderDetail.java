
package com.eye3.golfpay.fmb_tab.model.order;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class OrderDetail implements Serializable {

    @SerializedName("items")
    private ArrayList<Item> mItems;
    @SerializedName("paid_total_amount")
    private int mPaidTotalAmount;
    @SerializedName("reserve_guest_id")
    private int mReserveGuestId;

    public ArrayList<Item> getItems() {
        return mItems;
    }

    public void setItems(ArrayList<Item> items) {
        mItems = items;
    }

    public int getPaidTotalAmount() {
        return mPaidTotalAmount;
    }

    public void setPaidTotalAmount(int paidTotalAmount) {
        mPaidTotalAmount = paidTotalAmount;
    }

    public int getReserveGuestId() {
        return mReserveGuestId;
    }

    public void setReserveGuestId(int reserveGuestId) {
        mReserveGuestId = reserveGuestId;
    }

}
