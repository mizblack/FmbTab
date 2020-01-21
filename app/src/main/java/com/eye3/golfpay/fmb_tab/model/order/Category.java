
package com.eye3.golfpay.fmb_tab.model.order;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class Category implements Serializable {

    @SerializedName("category_id")
    private int mCategoryId;
    @SerializedName("category_name")
    private String mCategoryName;
    @SerializedName("menu")
    private ArrayList<RestaurantMenu> mMenu;

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(int categoryId) {
        mCategoryId = categoryId;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String categoryName) {
        mCategoryName = categoryName;
    }

    public ArrayList<RestaurantMenu> getMenu() {
        return mMenu;
    }

    public void setMenu(ArrayList<RestaurantMenu> menu) {
        mMenu = menu;
    }

}
