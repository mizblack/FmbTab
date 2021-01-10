package com.eye3.golfpay.model.gallery;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhotoData {
    @SerializedName("photo_id")
    @Expose
    public int photo_id;

    @SerializedName("photo_url")
    @Expose
    public String photo_url;
}
