package com.eye3.golfpay.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {


    @SerializedName("id")
    @Expose
    public String catergory1_id;

    @SerializedName("category")
    @Expose
    public String catergory1_name;

    @SerializedName("menu")
    @Expose
    public ArrayList<RestaurantMenu> Menus = new ArrayList<>();

    public boolean isSelected;
}
