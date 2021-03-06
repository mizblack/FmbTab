package com.eye3.golfpay.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//주문된 메뉴아이템, 주문 전송시 사용
public class OrderedMenuItem implements Serializable {
    @SerializedName("id")
    @Expose
    public String id = "";

    @SerializedName("qty")
    @Expose
    public String qty = "";

    @SerializedName("price")
    @Expose
    public String price = "";

    @SerializedName("total")
    @Expose
    public String total = "";

    //메뉴이름
    @SerializedName("name")
    @Expose
    public String menuName = "";

    @SerializedName("caddy_id")
    @Expose
    public String caddy_id = "";

    public OrderedMenuItem(String trim, String s, String trim1, String name) {
        this.id = "";
        this.qty = "0";
        this.price = "0";
        this.total = "0";
        this.menuName = "";
    }

    public OrderedMenuItem(String id, String qty, String price, String name, String caddyId) {
        super();
        this.id = id;
        this.qty = qty;
        this.price = price;
        setTotal();
        this.menuName = name;
        this.caddy_id = caddyId;
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
        return menuName;
    }

    public void setName(String name) {
        this.menuName = name;
    }
}
