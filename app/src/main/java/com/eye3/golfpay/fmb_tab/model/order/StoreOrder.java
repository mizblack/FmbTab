package com.eye3.golfpay.fmb_tab.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StoreOrder implements Serializable {

    @SerializedName("store_no")
    @Expose
    public String store_no;

//    @SerializedName("order_state")
//    @Expose
//    public String order_state = "주문완료";

    @SerializedName("store_name")
    @Expose
    public String store_name;

    @SerializedName("tablet_order_list")
    @Expose
    public List<ReceiptUnit>  tablet_order_list = new ArrayList<ReceiptUnit>();

    @SerializedName("pos_order_list")
    @Expose
    public List<PosPersonalOrder>  pos_order_list = new ArrayList<PosPersonalOrder>();



}
