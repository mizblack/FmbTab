package com.eye3.golfpay.fmb_tab.model.notice;

import java.io.Serializable;

public class NoticeDataTemp implements Serializable {

    private String title;

    private String content;

    private String time;

    private String imageUri;

    public NoticeDataTemp(String title, String content, String time) {
        this.title = title;
        this.content = content;
        this.time = time;
    }

    public NoticeDataTemp(String title, String content, String time, String imageUri) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.imageUri = imageUri;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public String getImage() {
        return imageUri;
    }
}