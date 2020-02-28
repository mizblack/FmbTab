package com.eye3.golfpay.fmb_tab.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//주문된 메뉴아이템, 주문 전송시 사용
public class OrderedMenuItem implements Serializable {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("qty")
    @Expose
    String qty;
    @SerializedName("price")
    @Expose
    String price;
    @SerializedName("total")
    @Expose
    private String total;

 //   public String name;

    public OrderedMenuItem(String id, String qty, String price, String name) {
        this.id = id;
        this.qty = qty;
        this.price = price;
        setTotal();
  //      this.name = name;
    }

    void setTotal() {
        this.total = String.valueOf(Integer.valueOf(this.qty) * Integer.valueOf(this.price));
    }
}
