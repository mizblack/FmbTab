
package com.eye3.golfpay.model.info;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


@SuppressWarnings("unused")
public class BasicInfo implements Serializable {

    public BasicInfo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
