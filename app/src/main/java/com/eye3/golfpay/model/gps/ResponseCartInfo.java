
package com.eye3.golfpay.model.gps;

import com.eye3.golfpay.model.gallery.PhotoData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import retrofit2.Response;

@SuppressWarnings("unused")
public class ResponseCartInfo {

    @SerializedName("nearby_hole_list")
    @Expose
    public List<GpsInfo> nearby_hole_list;

    @SerializedName("here_to_hole")
    @Expose
    public int here_to_hole;
}
