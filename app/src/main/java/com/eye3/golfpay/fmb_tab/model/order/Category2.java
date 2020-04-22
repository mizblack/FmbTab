package com.eye3.golfpay.fmb_tab.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Category2 implements Serializable {
    @SerializedName("category2_id")
    @Expose
    public String catergory2_id;

    @SerializedName("category2_name")
    @Expose
    public String catergory2_name;

    @SerializedName("menu")
    @Expose
    public ArrayList<RestaurantMenu> Menus = new ArrayList<>();


}
