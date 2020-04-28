package com.eye3.golfpay.fmb_tab.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PosPersonalOrder implements Serializable {
    @SerializedName("order_name")
    @Expose
    public String order_name ="홍길동";

    @SerializedName("guest_id")
    @Expose
    public String guest_id;

    @SerializedName("totalPrice")
    @Expose
    public String total_price;

    @SerializedName("menu")
    @Expose
    public List<Menu> menuList = new ArrayList<>();
}
