package com.eye3.golfpay.fmb_tab.model.guest;

import com.google.gson.annotations.SerializedName;

import java.io.File;

public class GuestInfo {

    @SerializedName("guest_name")
    private String guestName = "";
    @SerializedName("reserve_guest_id")
    private String reserveGuestId = "";
    @SerializedName("car_no")
    private String carNo = "";
    @SerializedName("hp")
    private String hp = "";
    @SerializedName("guest_memo")
    private String guestMemo = "";
    @SerializedName("sign_image")
    private File signImage = null;
    @SerializedName("sign_image_end")
    private File signImage_end = null;
    @SerializedName("club_image")
    private File clubImage = null;
    @SerializedName("club_image_end")
    private File clubImage_final = null;
    @SerializedName("club_info")
    private ReqClubInfo clubInfo = null;

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

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

    public File getSignImage_end() {
        return signImage_end;
    }

    public void setSignImage_end(File signImage_end) {
        this.signImage_end = signImage_end;
    }

    public File getClubImage_final() {
        return clubImage_final;
    }

    public void setClubImage_final(File clubImage_final) {
        this.clubImage_final = clubImage_final;
    }

    public ClubInfo getClubInfo() {
        ClubInfo ci = new ClubInfo();

        return ci;
    }

    public void setClubInfo(ClubInfo ci) {

        clubInfo = new ReqClubInfo();
        for (String wood : ci.wood) {
            clubInfo.wood += (wood + ",");
        }

        for (String utility : ci.utility) {
            clubInfo.utility += (utility + ",");
        }

        for (String iron : ci.iron) {
            clubInfo.iron += (iron + ",");
        }

        for (String putter : ci.putter) {
            clubInfo.putter += (putter + ",");
        }

        for (String wedge : ci.wedge) {
            clubInfo.wedge += (wedge + ",");
        }

        for (String wood_cover : ci.wood_cover) {
            clubInfo.wood_cover += (wood_cover + ",");
        }

        for (String putter_cover : ci.putter_cover) {
            clubInfo.putter_cover += (putter_cover + ",");
        }

        for (String cover : ci.cover) {
            clubInfo.cover += (cover + ",");
        }
    }
}
