package com.eye3.golfpay.model.guest;

import com.eye3.golfpay.model.caddyNote.CaddyNoteInfo;
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
    private ReqClubInfo reqClubInfo = null;
    public ClubInfo clubInfo = new ClubInfo();

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

    public ReqClubInfo getReqClubInfo() {
        return reqClubInfo;
    }

    public void setClubInfo(ClubInfo ci) {

        clubInfo = ci;
        reqClubInfo = new ReqClubInfo();
        for (CaddyNoteInfo.ClubInfo wood : ci.wood) {
            reqClubInfo.wood += (wood.club + ",");
            if (wood.cover)
                reqClubInfo.wood_cover += (wood.club + ",");
        }

        for (CaddyNoteInfo.ClubInfo utility : ci.utility) {
            reqClubInfo.utility += (utility.club  + ",");
            if (utility.cover)
                reqClubInfo.utility_cover += (utility.club + ",");
        }

        for (CaddyNoteInfo.ClubInfo iron : ci.iron) {
            reqClubInfo.iron += (iron.club  + ",");
            if (iron.cover)
                reqClubInfo.iron_cover += (iron.club + ",");
        }

        for (CaddyNoteInfo.ClubInfo putter : ci.putter) {
            reqClubInfo.putter += (putter.club  + ",");
            if (putter.cover)
                reqClubInfo.putter_cover += (putter.club + ",");
        }

        for (CaddyNoteInfo.ClubInfo wedge : ci.wedge) {
            reqClubInfo.wedge += (wedge.club  + ",");
            if (wedge.cover)
                reqClubInfo.wedge_cover += (wedge.club + ",");
        }
    }

    public ClubInfo getClubInfo() {
        return clubInfo;
    }
}
