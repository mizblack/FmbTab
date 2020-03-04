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
    public String qty;
    @SerializedName("price")
    @Expose
    public String price;
    @SerializedName("total")
    @Expose
    public String total;
    //메뉴이름
    public String name;
     public OrderedMenuItem(){
         this.id ="";
         this.qty = "0";
         this.price = "0";
         this.total = "0";
         this.name = "";

     }

    public OrderedMenuItem(String id, String qty, String price, String name) {
        super();
         this.id = id;
        this.qty = qty;
        this.price = price;
        setTotal();
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal() {
        this.total = String.valueOf(Integer.parseInt(this.qty) * Integer.parseInt(this.price));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
