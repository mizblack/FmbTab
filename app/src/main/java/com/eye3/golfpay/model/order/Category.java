package com.eye3.golfpay.model.order;

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
    public ArrayList<Category2> subCategoryList = new ArrayList<>();

    public boolean isSelected;

}
