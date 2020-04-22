package com.eye3.golfpay.fmb_tab.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {


    @SerializedName("category1_id")
    @Expose
    public String catergory1_id;

    @SerializedName("category1_name")
    @Expose

    public String catergory1_name;

    @SerializedName("category1_list")
    @Expose
    public ArrayList<Category2> category1_list = new ArrayList<>();

}
