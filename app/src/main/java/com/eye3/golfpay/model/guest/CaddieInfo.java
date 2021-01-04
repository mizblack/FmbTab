package com.eye3.golfpay.model.guest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.ArrayList;

public class CaddieInfo {

    @SerializedName("caddie_id")
    @Expose
    private String caddie_id = "";

    @SerializedName("reserve_id")
    @Expose
    private String reserveId = "";

    @SerializedName("guest_info")
    @Expose
    private ArrayList<GuestInfo> guestInfo = new ArrayList<>();

    @SerializedName("team_memo")
    @Expose
    private String teamMemo;

    @SerializedName("team_photos")
    @Expose
    private ArrayList<String> teamPhotos = new ArrayList<>();


    public String getCaddie_id() {
        return caddie_id;
    }

    public void setCaddie_id(String caddie_id) {
        this.caddie_id = caddie_id;
    }

    public ArrayList<GuestInfo> getGuestInfo() {
        return guestInfo;
    }

    public void setGuestInfo(ArrayList<GuestInfo> guestInfo) {
        this.guestInfo = guestInfo;
    }

    public String getTeamMemo() {
        return teamMemo;
    }

    public void setTeamMemo(String teamMemo) {
        this.teamMemo = teamMemo;
    }

    public ArrayList<String> getTeamPhotos() {
        return teamPhotos;
    }

    public void setTeamPhotos(ArrayList<String> teamPhotos) {
        this.teamPhotos = teamPhotos;
    }
}
