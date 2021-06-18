
package com.eye3.golfpay.model.gps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class GpsInfo implements Serializable {

    public GpsInfo(String name, String gubun, int percent, String cart_no) {
        this.guestName = name;
        this.gubun = gubun;
        this.percent = percent;
        this.cart_no = cart_no;
        cart_status = "G";
    }
    @SerializedName("guestName")
    @Expose
    String guestName;

    @SerializedName("cart_status")
    @Expose
    String cart_status;

    @SerializedName("hole_id")
    @Expose
    String hole_id;

    @SerializedName("lat")
    @Expose
    double lat;

    @SerializedName("lng")
    @Expose
    double lng;

    @SerializedName("cart_no")
    @Expose
    String cart_no;

    @SerializedName("gubun")
    @Expose
    String gubun;

    @SerializedName("ctype")
    @Expose
    String ctype;

    @SerializedName("time_before")
    @Expose
    String time_before;

    @SerializedName("time_after")
    @Expose
    String time_after;

    @SerializedName("distance")
    @Expose
    int distance;

    @SerializedName("percent")
    @Expose
    int percent;

    public String getGuestName() {
        return guestName;
    }

    public String getCart_status() {
        return cart_status;
    }

    public String getHole_id() {
        return hole_id;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getCart_no() {
        return cart_no;
    }

    public String getGubun() {
        return gubun;
    }

    public String getCtype() {
        return ctype;
    }

    public String getTime_before() {
        return time_before;
    }

    public String getTime_after() {
        return time_after;
    }

    public int getDistance() {
        return distance;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
