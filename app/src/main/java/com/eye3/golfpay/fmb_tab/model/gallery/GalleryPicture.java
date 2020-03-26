package com.eye3.golfpay.fmb_tab.model.gallery;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GalleryPicture {

    public GalleryPicture(String id, String group_id, String url, Object created_at){
        this.id = id;
        this.group_id = group_id;
        this.url = url;
        this.created_at = created_at;
        this.file_path = file_path;
    }

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("group_id")
    @Expose
    private String group_id;


    @SerializedName("url")
    @Expose
    public String url;

    @SerializedName("file_path")
    @Expose
    public String file_path;

    @SerializedName("created_at")
    @Expose
    private Object created_at;

}
