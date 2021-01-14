package com.eye3.golfpay.model.photo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhotoResponse {

    @SerializedName("ret_code")
    @Expose
    private final String  resultCode = "";

    @SerializedName("ret_msg")
    @Expose
    private final String resultMessage = "";

    @SerializedName("file_data")
    @Expose
    private final FileData file_data = null;

    static class FileData {
        @SerializedName("file_id")
        @Expose
        private final String file_id = "";
        @SerializedName("file_name")
        @Expose
        private final String file_name = "";
        @SerializedName("file_url")
        @Expose
        private final String file_url = "";
    }
}
