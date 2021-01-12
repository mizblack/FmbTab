package com.eye3.golfpay.model.gallery;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PersnoalPhotoData {
    @SerializedName("reserve_guest_id")
    @Expose
    public int reserve_guest_id;

    @SerializedName("list")
    @Expose
    public List<PhotoData> list;
}
