package com.eye3.golfpay.model.order;

import java.util.ArrayList;

public class OrderItemInvoice {
    public String mMenuId;
    public String mMenuName;
    public String mMenuPrice;
    public int mQty;
    public ArrayList<GuestNameOrder> mGuestNameOrders;

    public OrderItemInvoice() {
        this.mMenuId = "";
        this.mMenuName = "";
        this.mMenuPrice = "";
        this.mQty = 0;
        this.mGuestNameOrders = new ArrayList<GuestNameOrder>();
    }
}
