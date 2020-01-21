
package com.eye3.golfpay.fmb_tab.model.order;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class Data implements Serializable {

    @SerializedName("data")
    private ArrayList<Datum> mData;
    @SerializedName("ret_code")
    private String mRetCode;

    public ArrayList<Datum> getData() {
        return mData;
    }

    public void setData(ArrayList<Datum> data) {
        mData = data;
    }

    public String getRetCode() {
        return mRetCode;
    }

    public void setRetCode(String retCode) {
        mRetCode = retCode;
    }

}
