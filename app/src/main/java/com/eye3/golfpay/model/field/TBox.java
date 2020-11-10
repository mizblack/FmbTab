
package com.eye3.golfpay.model.field;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class TBox {

    @SerializedName("id")
    private Long mId;
    @SerializedName("tbox_name")
    private String mTboxName;
    @SerializedName("tbox_value")
    private String mTboxValue;

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getTboxName() {
        return mTboxName;
    }

    public void setTboxName(String tboxName) {
        mTboxName = tboxName;
    }

    public String getTboxValue() {
        return mTboxValue;
    }

    public void setTboxValue(String tboxValue) {
        mTboxValue = tboxValue;
    }

}
