package com.eye3.golfpay.model.caddyNote;

import com.eye3.golfpay.model.guest.ClubInfo;
import com.eye3.golfpay.model.guest.ReqClubInfo;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.ArrayList;
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
    private ArrayList<ClubInfo> wood = null;
    @SerializedName("utility")
    private ArrayList<ClubInfo> utility = null;
    @SerializedName("iron")
    private ArrayList<ClubInfo> iron = null;
    @SerializedName("putter")
    private ArrayList<ClubInfo> putter = null;
    @SerializedName("wedge")
    private ArrayList<ClubInfo> wedge = null;

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

    public ArrayList<ClubInfo> getWood() {
        return wood;
    }

    public void setWood(ArrayList<ClubInfo> wood) {
        this.wood = wood;
    }

    public ArrayList<ClubInfo> getUtility() {
        return utility;
    }

    public void setUtility(ArrayList<ClubInfo> utility) {
        this.utility = utility;
    }

    public ArrayList<ClubInfo> getIron() {
        return iron;
    }

    public void setIron(ArrayList<ClubInfo> iron) {
        this.iron = iron;
    }

    public ArrayList<ClubInfo> getPutter() {
        return putter;
    }

    public void setPutter(ArrayList<ClubInfo> putter) {
        this.putter = putter;
    }

    public ArrayList<ClubInfo> getWedge() {
        return wedge;
    }

    public void setWedge(ArrayList<ClubInfo> wedge) {
        this.wedge = wedge;
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

    public static class ClubInfo {
        public String club;
        public boolean cover;
    }
}
