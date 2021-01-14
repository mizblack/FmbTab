package com.eye3.golfpay.model.caddyNote;

import com.eye3.golfpay.model.guest.ClubInfo;
import com.eye3.golfpay.model.guest.ReqClubInfo;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.List;

public class CaddyNoteInfo {

    @SerializedName("id")
    private String id = "";
    @SerializedName("cc_id")
    private String cc_id = "";
    @SerializedName("team_memo")
    private String team_memo = "";
    @SerializedName("bagName")
    private String bagName = "";
    @SerializedName("guest_memo")
    private String guest_memo = "";
    @SerializedName("wood")
    private String wood = null;
    @SerializedName("utility")
    private String utility = null;
    @SerializedName("iron")
    private String iron = null;
    @SerializedName("putter")
    private String putter = null;
    @SerializedName("wedge")
    private String wedge = null;

    @SerializedName("wood_cover")
    private String wood_cover = null;
    @SerializedName("putter_cover")
    private String putter_cover = null;
    @SerializedName("etc_cover")
    private String etc_cover = null;

    @SerializedName("sign_before")
    private List<CaddyImage> sign_before = null;
    @SerializedName("sign_after")
    private List<CaddyImage> sign_after = null;
    @SerializedName("club_before")
    private List<CaddyImage> club_before = null;
    @SerializedName("club_after")
    private List<CaddyImage> club_after = null;

    @SerializedName("carNumber")
    private final String carNumber = null;
    @SerializedName("phoneNumber")
    private final String phoneNumber = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCc_id() {
        return cc_id;
    }

    public void setCc_id(String cc_id) {
        this.cc_id = cc_id;
    }

    public String getTeam_memo() {
        return team_memo;
    }

    public void setTeam_memo(String team_memo) {
        this.team_memo = team_memo;
    }

    public String getBagName() {
        return bagName;
    }

    public void setBagName(String bagName) {
        this.bagName = bagName;
    }

    public String getGuest_memo() {
        return guest_memo;
    }

    public void setGuest_memo(String guest_memo) {
        this.guest_memo = guest_memo;
    }

    public String getWood() {
        return wood;
    }

    public void setWood(String wood) {
        this.wood = wood;
    }

    public String getUtility() {
        return utility;
    }

    public void setUtility(String utility) {
        this.utility = utility;
    }

    public String getIron() {
        return iron;
    }

    public void setIron(String iron) {
        this.iron = iron;
    }

    public String getPutter() {
        return putter;
    }

    public void setPutter(String putter) {
        this.putter = putter;
    }

    public String getWedge() {
        return wedge;
    }

    public void setWedge(String wedge) {
        this.wedge = wedge;
    }

    public String getWood_cover() {
        return wood_cover;
    }

    public void setWood_cover(String wood_cover) {
        this.wood_cover = wood_cover;
    }

    public String getPutter_cover() {
        return putter_cover;
    }

    public void setPutter_cover(String putter_cover) {
        this.putter_cover = putter_cover;
    }

    public String getEtc_cover() {
        return etc_cover;
    }

    public void setEtc_cover(String etc_cover) {
        this.etc_cover = etc_cover;
    }

    public List<CaddyImage> getSign_before() {
        return sign_before;
    }

    public void setSign_before(List<CaddyImage> sign_before) {
        this.sign_before = sign_before;
    }

    public List<CaddyImage> getSign_after() {
        return sign_after;
    }

    public void setSign_after(List<CaddyImage> sign_after) {
        this.sign_after = sign_after;
    }

    public List<CaddyImage> getClub_before() {
        return club_before;
    }

    public void setClub_before(List<CaddyImage> club_before) {
        this.club_before = club_before;
    }

    public List<CaddyImage> getClub_after() {
        return club_after;
    }

    public void setClub_after(List<CaddyImage> club_after) {
        this.club_after = club_after;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
