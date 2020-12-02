package com.eye3.golfpay.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RestaurantMenu implements Serializable {

    @SerializedName("item_id")
    @Expose
    public String id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("price")
    @Expose
    public String price;

    @SerializedName("image")
    @Expose
    public String image;
    //ui용 변수
    public boolean isSelected = false;

    public String category1Idx;
    public String category2Idx;
    public int SubCateZeroIndex;

    public RestaurantMenu(String id, String name, String price, String image, String category1Idx, String category2Idx, int SubCateZeroIndex){
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.category1Idx = category1Idx;
        this.category2Idx = category2Idx;
        this.SubCateZeroIndex = SubCateZeroIndex;
    }
}
