
package com.eye3.golfpay.model.gps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class GpsInfo implements Serializable {

    @SerializedName("cc_id")
    @Expose
    int cc_id;

    @SerializedName("reserve_id")
    @Expose
    String reserve_id;

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

    @SerializedName("orderNo")
    @Expose
    String orderNo;

    @SerializedName("isHall")
    @Expose
    String isHall;

    @SerializedName("startOrEndPoint")
    @Expose
    String startOrEndPoint;

    @SerializedName("lat")
    @Expose
    double lat;

    @SerializedName("lng")
    @Expose
    double lng;


}
