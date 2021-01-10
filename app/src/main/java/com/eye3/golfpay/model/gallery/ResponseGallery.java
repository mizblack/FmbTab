package com.eye3.golfpay.model.gallery;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseGallery {
    @SerializedName("event_photo")
    @Expose
    public List<PhotoData> event_photo;

    @SerializedName("normal_photo")
    @Expose
    public List<PhotoData>normal_photo;

    @SerializedName("club_photo_team")
    @Expose
    public List<PhotoData>club_photo_team;

    @SerializedName("club_photo_personal")
    @Expose
    public List<PersnoalPhotoData>club_photo_personal;
}
