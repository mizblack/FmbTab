
package com.eye3.golfpay.model.control;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class ChatHotKeyOption implements Serializable {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("add_text")
    @Expose
    private String add_text;
    @SerializedName("position")
    @Expose
    private String position;
    @SerializedName("detail")
    @Expose
    private ArrayList<Detail> detail;

    public class Detail {
        public String text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdd_text() {
        return add_text;
    }

    public void setAdd_text(String add_text) {
        this.add_text = add_text;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public ArrayList<Detail> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<Detail> detail) {
        this.detail = detail;
    }
}
