package com.eye3.golfpay.model.guest;

import com.eye3.golfpay.model.caddyNote.CaddyNoteInfo;
import com.eye3.golfpay.model.field.Club;

import java.util.ArrayList;

public class ClubInfo {

    public ArrayList<CaddyNoteInfo.ClubInfo> wood = new ArrayList<>();
    public ArrayList<CaddyNoteInfo.ClubInfo> utility = new ArrayList<>();
    public ArrayList<CaddyNoteInfo.ClubInfo> iron = new ArrayList<>();
    public ArrayList<CaddyNoteInfo.ClubInfo> putter = new ArrayList<>();
    public ArrayList<CaddyNoteInfo.ClubInfo> wedge = new ArrayList<>();

    public String phoneNumber = "";
    public String carNumber = "";
    public String memo = "";

    public ArrayList<CaddyNoteInfo.ClubInfo> getWood() {
        return wood;
    }

    public void setWood(ArrayList<CaddyNoteInfo.ClubInfo> wood) {
        this.wood = wood;
    }

    public ArrayList<CaddyNoteInfo.ClubInfo> getUtility() {
        return utility;
    }

    public void setUtility(ArrayList<CaddyNoteInfo.ClubInfo> utility) {
        this.utility = utility;
    }

    public ArrayList<CaddyNoteInfo.ClubInfo> getIron() {
        return iron;
    }

    public void setIron(ArrayList<CaddyNoteInfo.ClubInfo> iron) {
        this.iron = iron;
    }

    public ArrayList<CaddyNoteInfo.ClubInfo> getPutter() {
        return putter;
    }

    public void setPutter(ArrayList<CaddyNoteInfo.ClubInfo> putter) {
        this.putter = putter;
    }

    public ArrayList<CaddyNoteInfo.ClubInfo> getWedge() {
        return wedge;
    }

    public void setWedge(ArrayList<CaddyNoteInfo.ClubInfo> wedge) {
        this.wedge = wedge;
    }

    public int totalCount() {
        int totalCount = 0;
        totalCount += wood.size();
        totalCount += utility.size();
        totalCount += iron.size();
        totalCount += wedge.size();
        //totalCount += getCoverCount(putter);
        //totalCount += getCoverCount(cover);

        return totalCount;
    }

    public int getCoverCount(ArrayList<String> arr) {

        for (String s: arr) {
            String a = s.substring(0, 1);
            return Integer.parseInt(a);
        }

        return 0;
    }

    public int getPutterCount() {
        return putter.size();
    }

    public int coverCount() {
//        int coverCount = 0;
//        coverCount += getCoverCount(cover);
//
//        return coverCount;

        return 0;
    }
}
