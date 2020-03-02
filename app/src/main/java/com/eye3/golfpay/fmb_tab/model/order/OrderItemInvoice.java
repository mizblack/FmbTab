package com.eye3.golfpay.fmb_tab.model.order;

import java.util.ArrayList;

public class OrderItemInvoice {
  public  String mMenunName ;
  public  int mQty ;
  public  ArrayList<NameOrder> mNameOrders ;

  public OrderItemInvoice(){
    this.mMenunName = "";
    this.mQty = 0;
    this.mNameOrders = new ArrayList<NameOrder>();
  }

}
