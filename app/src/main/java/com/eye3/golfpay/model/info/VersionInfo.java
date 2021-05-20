
package com.eye3.golfpay.model.info;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


@SuppressWarnings("unused")
public class VersionInfo implements Serializable {

    @SerializedName("code")
    private String retCode;
    @SerializedName("version")
    private String version;
    @SerializedName("filename")
    private String filename;
    @SerializedName("msg")
    private String msg;

    public String getRetCode() {
        return retCode;
    }

    public String getVersion() {
        return version;
    }

    public String getFilename() {
        return filename;
    }

    public String getMsg() {
        return msg;
    }
}
