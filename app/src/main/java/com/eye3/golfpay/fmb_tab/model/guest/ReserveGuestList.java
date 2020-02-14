
package com.eye3.golfpay.fmb_tab.model.guest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("unused")
public class ReserveGuestList implements Serializable {

    @Expose
    private List<Guest> list;
    @SerializedName("ret_code")
    private String retCode;
    @SerializedName("ret_msg")
    private String retMsg;

    public List<Guest> getList() {
        return list;
    }

    public void setList(List<Guest> list) {
        this.list = list;
    }

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
