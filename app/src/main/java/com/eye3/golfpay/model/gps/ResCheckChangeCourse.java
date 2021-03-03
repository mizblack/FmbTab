
package com.eye3.golfpay.model.gps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class ResCheckChangeCourse {

    @SerializedName("ret_code")
    @Expose
    private String  resultCode = "";

    @SerializedName("ret_msg")
    @Expose
    private String resultMessage = "";

    @SerializedName("change_yn")
    @Expose
    private String change_yn = "";

    public String getResultCode() {
        return resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public String getChange_yn() {
        return change_yn;
    }
}
