
package com.eye3.golfpay.fmb_tab.model.memo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class MemoResponse implements Serializable {

    @SerializedName("ret_code")
    private String retCode;
    @SerializedName("ret_msg")
    private String retMsg;

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

}
