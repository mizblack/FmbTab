package com.eye3.golfpay.fmb_tab.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RestaurantMenu implements Serializable {

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("price")
    @Expose
    String price;

    @SerializedName("image")
    @Expose
    String image;

}
