
package com.eye3.golfpay.fmb_tab.model.teeup;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class TeeUpTime implements Serializable {

    @SerializedName("ret_code")
    private String retCode;
    @SerializedName("ret_msg")
    private String retMsg;
    @SerializedName("caddy_info")
    private CaddyInfo caddy_info;
//    "caddy_info": {
//        "admins_id": 33,
//                "name": "아이유1"
//    },

    @SerializedName("today_reserve_list")
    private ArrayList<TodayReserveList> todayReserveList;

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

    public CaddyInfo getCaddyInfo() {
        return caddy_info;
    }

    public void setCaddyInfo(CaddyInfo caddyInfo) {
        this.caddy_info = caddyInfo;
    }

    public ArrayList<TodayReserveList> getTodayReserveList() {
        return todayReserveList;
    }
    //**********************************************************
    public ArrayList<GuestDatum> getGuests(int position) {
        return todayReserveList.get(position).getGuestData();
    }

    public void setTodayReserveList(ArrayList<TodayReserveList> todayReserveList) {
        this.todayReserveList = todayReserveList;
    }


}
