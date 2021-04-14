
package com.eye3.golfpay.model.gps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class TimeData {

    @SerializedName("before_start_time")
    @Expose
    public String  before_start_time = "";

    @SerializedName("before_end_time")
    @Expose
    public String  before_end_time = "";

    @SerializedName("after_start_time")
    @Expose
    public String  after_start_time = "";

    @SerializedName("after_end_time")
    @Expose
    public String  after_end_time = "";
}
