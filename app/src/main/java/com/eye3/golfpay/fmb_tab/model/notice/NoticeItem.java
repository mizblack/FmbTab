
package com.eye3.golfpay.fmb_tab.model.notice;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


@SuppressWarnings("unused")
public class NoticeItem implements Serializable {

    @SerializedName("admins_id")
    public  int mAdminsId;
    @SerializedName("cc_id")
    public int mCcId;
    @SerializedName("contents")
    public String mContents;
    @SerializedName("created_at")

    public String mCreatedAt;
    @SerializedName("id")
    public int mId;
    @SerializedName("title")
    public String mTitle;
    @SerializedName("updated_at")
    public String mUpdatedAt;
    @SerializedName("use_yn")
    public String mUseYn = "n";


    public int getAdminsId() {
        return mAdminsId;
    }

    public void setAdminsId(int adminsId) {
        mAdminsId = adminsId;
    }

    public int getCcId() {
        return mCcId;
    }

    public void setCcId(int ccId) {
        mCcId = ccId;
    }

    public String getContents() {
        return mContents;
    }

    public void setContents(String contents) {
        mContents = contents;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public Object getUseYn() {
        return mUseYn;
    }

    public void setUseYn(String useYn) {
        mUseYn = useYn;
    }

}
