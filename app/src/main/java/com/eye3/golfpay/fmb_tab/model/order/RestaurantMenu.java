package com.eye3.golfpay.fmb_tab.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RestaurantMenu implements Serializable {

    @SerializedName("id")
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

    public String category1 ;
    public String category2 ;
    public int SubCateZeroIndex ;

    public RestaurantMenu(String id, String name, String price, String image, String category1, String category2, int SubCateZeroIndex){
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.category1 = category1;
        this.category2 = category2;
        this.SubCateZeroIndex = SubCateZeroIndex;
    }



}
