
package com.eye3.golfpay.fmb_tab.model.order;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class Datum implements Serializable {

    @SerializedName("category")
    private ArrayList<Category> mCategory;
    @SerializedName("id")
    private int mId;
    @SerializedName("name")
    private String mName;

    public ArrayList<Category> getCategory() {
        return mCategory;
    }

    public void setCategory(ArrayList<Category> category) {
        mCategory = category;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

}
