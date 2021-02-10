
package com.eye3.golfpay.model.gps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class GpsInfo implements Serializable {

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("cc_id")
    @Expose
    int cc_id;

    @SerializedName("reserve_id")
    @Expose
    String reserve_id;

    @SerializedName("guestName")
    @Expose
    String guestName;

    @SerializedName("caddie_id")
    @Expose
    String caddie_id;

    @SerializedName("cart_status")
    @Expose
    String cart_status;

    @SerializedName("hole_point_id")
    @Expose
    String hole_point_id;

    @SerializedName("course_id")
    @Expose
    String course_id;

    @SerializedName("hole_id")
    @Expose
    String hole_id;

    @SerializedName("hole")
    @Expose
    String hole;

    @SerializedName("lat")
    @Expose
    double lat;

    @SerializedName("lng")
    @Expose
    double lng;

    @SerializedName("cart_no")
    @Expose
    int cart_no;

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

    public String getId() {
        return id;
    }

    public int getCc_id() {
        return cc_id;
    }

    public String getReserve_id() {
        return reserve_id;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getCaddie_id() {
        return caddie_id;
    }

    public String getCart_status() {
        return cart_status;
    }

    public String getHole_point_id() {
        return hole_point_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public String getHole_id() {
        return hole_id;
    }

    public String getHole() {
        return hole;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public int getCart_no() {
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
}
