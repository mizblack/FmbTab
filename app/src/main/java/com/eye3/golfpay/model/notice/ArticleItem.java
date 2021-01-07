
package com.eye3.golfpay.model.notice;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


@SuppressWarnings("unused")
public class ArticleItem implements Serializable {

    @SerializedName("board_id")
    public  int board_id;
    @SerializedName("article_id")
    public int article_id;
    @SerializedName("title")
    public String title;
    @SerializedName("content")
    public String content;
    @SerializedName("filename_new")
    public String filename_new;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("file_url")
    public String file_url;
    @SerializedName("read_yn")
    public String read_yn;
}
