package com.eye3.golfpay.fmb_tab.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PersonalOrder implements Serializable {

    @SerializedName("order_name")
    @Expose
    public String guest_name ="홍길동";

    @SerializedName("pos_bill_id")
    @Expose
    public String pos_bill_id;

    @SerializedName("statement_no")
    @Expose
    public String statement_no;

    @SerializedName("total_price")
    @Expose
    public String total_price;

    @SerializedName("order_status")
    @Expose
    public String order_status = "";

    @SerializedName("menu")
    @Expose
    public List<Menu> menuList = new ArrayList<>();

}
