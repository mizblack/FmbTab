
package com.eye3.golfpay.model.control;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class ChatHotKey implements Serializable {

    @SerializedName("ret_code")
    @Expose
    private String mRetCode;
    @SerializedName("ret_msg")
    @Expose
    private String mRetMsg;

    @SerializedName("header")
    @Expose
    private ArrayList<ChatHotKeyItem> header;

    @SerializedName("body")
    @Expose
    private ArrayList<ChatHotKeyItem> body;

    @SerializedName("options")
    @Expose
    private ArrayList<ChatHotKeyOption> options;

    public String getmRetCode() {
        return mRetCode;
    }

    public void setmRetCode(String mRetCode) {
        this.mRetCode = mRetCode;
    }

    public String getmRetMsg() {
        return mRetMsg;
    }

    public void setmRetMsg(String mRetMsg) {
        this.mRetMsg = mRetMsg;
    }

    public ArrayList<ChatHotKeyItem> getHeader() {
        return header;
    }

    public void setHeader(ArrayList<ChatHotKeyItem> header) {
        this.header = header;
    }

    public ArrayList<ChatHotKeyItem> getBody() {
        return body;
    }

    public void setBody(ArrayList<ChatHotKeyItem> body) {
        this.body = body;
    }

    public ArrayList<ChatHotKeyOption> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<ChatHotKeyOption> options) {
        this.options = options;
    }
}
