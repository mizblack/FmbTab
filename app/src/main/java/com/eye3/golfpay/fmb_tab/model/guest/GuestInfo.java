package com.eye3.golfpay.fmb_tab.model.guest;

import com.google.gson.annotations.SerializedName;

import java.io.File;

public class GuestInfo {

    @SerializedName("reserve_guest_id")
    public  String reserveGuestId;
    @SerializedName("car_no")
    public  String carNo;
    @SerializedName("hp")
    public String hp;
    @SerializedName("guest_memo")
    public String guestMemo;
    @SerializedName("team_memo")
    public String teamMemo;
    @SerializedName("sign_image")
    public File signImage;
    @SerializedName("club_image")
    public File clubImage;

    public GuestInfo(String reserveGuestId, String carNo, String hp, String guestMemo, String teamMemo, File signImage, File clubImage) {
        this.reserveGuestId = reserveGuestId;
        this.carNo = carNo;
        this.hp = hp;
        this.guestMemo = guestMemo;
        this.teamMemo = teamMemo;
        this.signImage = signImage;
        this.clubImage = clubImage;
    }

    public void setReserveGuestId(String reserveGuestId) {
        this.reserveGuestId = reserveGuestId;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public void setGuestMemo(String guestMemo) {
        this.guestMemo = guestMemo;
    }

    public void setTeamMemo(String teamMemo) {
        this.teamMemo = teamMemo;
    }

    public void setSignImage(File signImage) {
        this.signImage = signImage;
    }

    public void setClubImage(File clubImage) {
        this.clubImage = clubImage;
    }
}
