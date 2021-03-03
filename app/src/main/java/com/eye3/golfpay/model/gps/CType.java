
package com.eye3.golfpay.model.gps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class CType {

    @SerializedName("ctype")
    @Expose
    private String  ctype = "";

    public String getCtype() {
        return ctype;
    }
}
