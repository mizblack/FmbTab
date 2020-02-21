package com.eye3.golfpay.fmb_tab.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {


    @SerializedName("category_id")
    @Expose
    public String catergory_id;

    @SerializedName("category_name")
    @Expose
    public String catergory_name;

    @SerializedName("menu")
    @Expose
    public ArrayList<RestaurantMenu> Menus = new ArrayList<>();


}
