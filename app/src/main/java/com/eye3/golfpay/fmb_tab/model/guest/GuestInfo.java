package com.eye3.golfpay.fmb_tab.model.guest;

import com.google.gson.annotations.SerializedName;

public class GuestInfo {

    @SerializedName("reserve_guest_id")
    private String reserveGuestId;
    @SerializedName("car_no")
    private String carNo;
    @SerializedName("hp")
    private String hp;
    @SerializedName("guest_memo")
    private String guestMemo;
    @SerializedName("team_memo")
    private String teamMemo;
    @SerializedName("sign_image")
    private String signImage;
    @SerializedName("club_image")
    private String clubImage;

    public GuestInfo(String reserveGuestId, String carNo, String hp, String guestMemo, String teamMemo, String signImage, String clubImage) {
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

    public void setSignImage(String signImage) {
        this.signImage = signImage;
    }

    public void setClubImage(String clubImage) {
        this.clubImage = clubImage;
    }
}
