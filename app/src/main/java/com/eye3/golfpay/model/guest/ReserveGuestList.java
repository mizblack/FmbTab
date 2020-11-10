
package com.eye3.golfpay.model.guest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class ReserveGuestList implements Serializable {

    @Expose
    private ArrayList<Guest> list;
    @SerializedName("ret_code")
    private String retCode;
    @SerializedName("ret_msg")
    private String retMsg;

    public ArrayList<Guest> getList() {
        return list;
    }

    public void setList(ArrayList<Guest> list) {
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
