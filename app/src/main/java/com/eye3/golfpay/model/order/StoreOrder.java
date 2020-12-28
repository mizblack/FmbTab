package com.eye3.golfpay.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StoreOrder implements Serializable {

    @SerializedName("store_no")
    @Expose
    public String store_no;

    @SerializedName("store_name")
    @Expose
    public String store_name;

    @SerializedName("tablet_order_list")
    @Expose
    public List<ReceiptUnit>  tablet_order_list = new ArrayList<>();

    @SerializedName("pos_order_list")
    @Expose
    public List<PersonalOrder>  pos_order_list ;



}
