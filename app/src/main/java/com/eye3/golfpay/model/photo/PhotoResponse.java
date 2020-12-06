package com.eye3.golfpay.model.photo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhotoResponse {

    @SerializedName("ret_code")
    @Expose
    private String  resultCode = "";

    @SerializedName("ret_msg")
    @Expose
    private String resultMessage = "";

    @SerializedName("file_data")
    @Expose
    private FileData file_data = null;

    static class FileData {
        @SerializedName("file_id")
        @Expose
        private String file_id = "";
        @SerializedName("file_name")
        @Expose
        private String file_name = "";
        @SerializedName("file_url")
        @Expose
        private String file_url = "";
    }
}
