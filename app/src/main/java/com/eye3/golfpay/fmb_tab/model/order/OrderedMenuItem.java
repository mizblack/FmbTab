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

    public String name;

    public OrderedMenuItem(String id, String qty, String price, String name) {
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
