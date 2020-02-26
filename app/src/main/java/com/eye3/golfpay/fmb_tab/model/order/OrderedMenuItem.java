package com.eye3.golfpay.fmb_tab.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//주문된 메뉴아이템, 주문 전송시 사용
public class OrderedMenuItem implements Serializable {
    @SerializedName("id")
    @Expose
    public String id = "-1";
    @SerializedName("qty")
    @Expose
    public String qty = "0";
    @SerializedName("price")
    @Expose
    public String price = "0";
    @SerializedName("total")
    @Expose
    public String total = "0";

 //   public String name ="";

    public OrderedMenuItem(String id, String qty, String price, String name ){
        this.id = id;
        this.qty =  qty;
        this.price = price;
        setTotal();
      //  this.total = setTotal();
   //     this.name = name;
    }

    public void setTotal(){
        this.total = String.valueOf(Integer.valueOf(this.qty) * Integer.valueOf(this.price));
    }
}
