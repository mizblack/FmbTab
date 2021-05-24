
package com.eye3.golfpay.model.teeup;

import com.eye3.golfpay.model.order.PlayStatus;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class TodayReserveList implements Serializable {

    @Expose
    @SerializedName("group")
    private String group;
    @SerializedName("group_id")
    private String groupId;
    @SerializedName("guest_data")
    private ArrayList<GuestDatum> guestData;
    @Expose
    private String guestName;
    @SerializedName("guest_type")
    private Object guestType;
    @Expose
    private int id;
    @SerializedName("reserve_no")
    private String reserveNo;
    @SerializedName("team_no")
    private int teamNo;
    @Expose
    private String teeoff;
    @SerializedName("play_status")
    private String playStatus;
    @SerializedName("status")
    private String status;
    @SerializedName("inout_course")
    private String inout_course;

    public int getTeamNo() {
        return teamNo;
    }

    public String getInoutCourse() {
        return inout_course;
    }

    public String getGroup() {
        return group;
    }

    public String getGroupId() {
        return groupId;
    }

    public ArrayList<GuestDatum> getGuestData() {
        return guestData;
    }

    public String getGuestName() {
        return guestName;
    }

    public Object getGuestType() {
        return guestType;
    }

    public int getId() {
        return id;
    }

    public String getReserveNo() {
        return reserveNo;
    }

    public String getStatus() {
        return status;
    }

    public String getTeeoff() {
        return teeoff;
    }

    public String getPlayStatus() {
        return playStatus;
    }
}
