package com.eye3.golfpay.model.order;

import java.util.ArrayList;

public class OrderItemInvoice {
  public  String mMenunName ;
  public  int mQty ;
  public  ArrayList<GuestNameOrder> mGuestNameOrders;

  public OrderItemInvoice(){
    this.mMenunName = "";
    this.mQty = 0;
    this.mGuestNameOrders = new ArrayList<GuestNameOrder>();
  }

}
