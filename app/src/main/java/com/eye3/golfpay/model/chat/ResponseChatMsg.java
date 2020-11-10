
package com.eye3.golfpay.model.chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class ResponseChatMsg implements Serializable {

    @SerializedName("cc_id")
    @Expose
    private int cc_id;
    @SerializedName("from")
    @Expose
    private String from;

    @SerializedName("sender")
    @Expose
    private String sender;

    @SerializedName("sender_type")
    @Expose
    private String sender_type;

    @SerializedName("from_name")
    @Expose
    private String from_name;

    @SerializedName("sender_name")
    @Expose
    private String sender_name;

    @SerializedName("to_group")
    @Expose
    private String to_group;

    @SerializedName("to_group_name")
    @Expose
    private String to_group_name;

    @SerializedName("to")
    @Expose
    private String to;

    @SerializedName("to_name")
    @Expose
    private String to_name;

    @SerializedName("receiver")
    @Expose
    private String receiver;
    @SerializedName("receiver_name")
    @Expose
    private String receiver_name;

    @SerializedName("receiver_type")
    @Expose
    private String receiver_type;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("display_time")
    @Expose
    private String display_time;
    @SerializedName("timestamp")
    @Expose
    private long timestamp;

    public int getCc_id() {
        return cc_id;
    }

    public void setCc_id(int cc_id) {
        this.cc_id = cc_id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender_type() {
        return sender_type;
    }

    public void setSender_type(String sender_type) {
        this.sender_type = sender_type;
    }

    public String getFrom_name() {
        return from_name;
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getTo_group() {
        return to_group;
    }

    public void setTo_group(String to_group) {
        this.to_group = to_group;
    }

    public String getTo_group_name() {
        return to_group_name;
    }

    public void setTo_group_name(String to_group_name) {
        this.to_group_name = to_group_name;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo_name() {
        return to_name;
    }

    public void setTo_name(String to_name) {
        this.to_name = to_name;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getReceiver_type() {
        return receiver_type;
    }

    public void setReceiver_type(String receiver_type) {
        this.receiver_type = receiver_type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDisplay_time() {
        return display_time;
    }

    public void setDisplay_time(String display_time) {
        this.display_time = display_time;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
