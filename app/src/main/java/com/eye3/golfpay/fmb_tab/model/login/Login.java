
package com.eye3.golfpay.fmb_tab.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Login implements Serializable {

    @SerializedName("caddy_no")
    @Expose
    private int mCaddyNo;
    @SerializedName("ret_code")
    @Expose
    private String mRetCode;
    @SerializedName("ret_msg")
    @Expose
    private String mRetMsg;

    public int getCaddyNo() {
        return mCaddyNo;
    }

    public void setCaddyNo(int caddyNo) {
        mCaddyNo = caddyNo;
    }

    public String getRetCode() {
        return mRetCode;
    }

    public void setRetCode(String retCode) {
        mRetCode = retCode;
    }

    public String getRetMsg() {
        return mRetMsg;
    }

    public void setRetMsg(String retMsg) {
        mRetMsg = retMsg;
    }

}
