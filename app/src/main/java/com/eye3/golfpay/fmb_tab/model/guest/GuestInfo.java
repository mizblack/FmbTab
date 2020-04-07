package com.eye3.golfpay.fmb_tab.model.guest;

import com.google.gson.annotations.SerializedName;

import java.io.File;

public class GuestInfo {

    @SerializedName("reserve_guest_id")
    private String reserveGuestId = "";
    @SerializedName("car_no")
    private String carNo = "";
    @SerializedName("hp")
    private String hp = "";
    @SerializedName("guest_memo")
    private String guestMemo = "";
    @SerializedName("team_memo")
    private String teamMemo = "";
    @SerializedName("sign_image")
    private File signImage = null;
    @SerializedName("club_image")
    private File clubImage = null;

    public void setReserveGuestId(String reserveGuestId) {
        this.reserveGuestId = reserveGuestId;
    }

    public String getReserveGuestId() {
        return reserveGuestId;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getHp() {
        return hp;
    }

    public void setGuestMemo(String guestMemo) {
        this.guestMemo = guestMemo;
    }

    public String getGuestMemo() {
        return guestMemo;
    }

    public void setTeamMemo(String teamMemo) {
        this.teamMemo = teamMemo;
    }

    public String getTeamMemo() {
        return teamMemo;
    }

    public void setSignImage(File signImage) {
        this.signImage = signImage;
    }

    public File getSignImage() {
        return signImage;
    }

    public void setClubImage(File clubImage) {
        this.clubImage = clubImage;
    }

    public File getClubImage() {
        return clubImage;
    }
}
