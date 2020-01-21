
package com.eye3.golfpay.fmb_tab.model.notice;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class Notice implements Serializable {

    @SerializedName("data")
    private ArrayList<Datum> mData;

    public ArrayList<Datum> getData() {
        return mData;
    }

    public void setData(ArrayList<Datum> data) {
        mData = data;
    }

}
