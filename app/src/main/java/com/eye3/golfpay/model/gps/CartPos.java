
package com.eye3.golfpay.model.gps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class CartPos implements Serializable {

    public CartPos(String name, String cart_status, double lat, double lng, int cart_no, String gubun, int distance, int percent) {
        this.name = name;
        this.cart_status = cart_status;
        this.lat = lat;
        this.lng = lng;
        this.cart_no = cart_no;
        this.gubun = gubun;
        this.distance = distance;
        this.percent = percent;
    }

    String name;
    String cart_status;
    double lat;
    double lng;
    int cart_no;
    String gubun;
    int distance;
    int percent;

    public String getName() {
        return name;
    }

    public String getCart_status() {
        return cart_status;
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

    public int getDistance() {
        return distance;
    }

    public int getPercent() {
        return percent;
    }
}
