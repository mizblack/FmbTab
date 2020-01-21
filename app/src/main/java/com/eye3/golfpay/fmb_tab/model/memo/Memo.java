
package com.eye3.golfpay.fmb_tab.model.memo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Memo implements Serializable {

    @SerializedName("cc_id")
    private int ccId;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("group_id")
    private Object groupId;
    @Expose
    private Object header;
    @Expose
    private int id;
    @SerializedName("member_id")
    private Object memberId;
    @Expose
    private String message;
    @Expose
    private Object mode;
    @SerializedName("notice_end_time")
    private Object noticeEndTime;
    @SerializedName("notice_yn")
    private String noticeYn;
    @SerializedName("part_id")
    private Object partId;
    @SerializedName("part_team_id")
    private Object partTeamId;
    @SerializedName("que_yn")
    private String queYn;
    @Expose
    private Object receiver;
    @SerializedName("receiver_id")
    private Object receiverId;
    @SerializedName("reg_datetime")
    private String regDatetime;
    @SerializedName("reserve_guest_id")
    private Object reserveGuestId;
    @SerializedName("reserve_id")
    private int reserveId;
    @Expose
    private Sender sender;
    @SerializedName("sender_id")
    private int senderId;
    @Expose
    private String type;
    @SerializedName("updated_at")
    private Object updatedAt;
    @Expose
    private Object url;
    @SerializedName("view_yn")
    private String viewYn;

    public int getCcId() {
        return ccId;
    }

    public void setCcId(int ccId) {
        this.ccId = ccId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Object getGroupId() {
        return groupId;
    }

    public void setGroupId(Object groupId) {
        this.groupId = groupId;
    }

    public Object getHeader() {
        return header;
    }

    public void setHeader(Object header) {
        this.header = header;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getMemberId() {
        return memberId;
    }

    public void setMemberId(Object memberId) {
        this.memberId = memberId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getMode() {
        return mode;
    }

    public void setMode(Object mode) {
        this.mode = mode;
    }

    public Object getNoticeEndTime() {
        return noticeEndTime;
    }

    public void setNoticeEndTime(Object noticeEndTime) {
        this.noticeEndTime = noticeEndTime;
    }

    public String getNoticeYn() {
        return noticeYn;
    }

    public void setNoticeYn(String noticeYn) {
        this.noticeYn = noticeYn;
    }

    public Object getPartId() {
        return partId;
    }

    public void setPartId(Object partId) {
        this.partId = partId;
    }

    public Object getPartTeamId() {
        return partTeamId;
    }

    public void setPartTeamId(Object partTeamId) {
        this.partTeamId = partTeamId;
    }

    public String getQueYn() {
        return queYn;
    }

    public void setQueYn(String queYn) {
        this.queYn = queYn;
    }

    public Object getReceiver() {
        return receiver;
    }

    public void setReceiver(Object receiver) {
        this.receiver = receiver;
    }

    public Object getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Object receiverId) {
        this.receiverId = receiverId;
    }

    public String getRegDatetime() {
        return regDatetime;
    }

    public void setRegDatetime(String regDatetime) {
        this.regDatetime = regDatetime;
    }

    public Object getReserveGuestId() {
        return reserveGuestId;
    }

    public void setReserveGuestId(Object reserveGuestId) {
        this.reserveGuestId = reserveGuestId;
    }

    public int getReserveId() {
        return reserveId;
    }

    public void setReserveId(int reserveId) {
        this.reserveId = reserveId;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getUrl() {
        return url;
    }

    public void setUrl(Object url) {
        this.url = url;
    }

    public String getViewYn() {
        return viewYn;
    }

    public void setViewYn(String viewYn) {
        this.viewYn = viewYn;
    }

}
