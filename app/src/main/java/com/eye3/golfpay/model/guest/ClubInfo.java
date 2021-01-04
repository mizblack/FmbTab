package com.eye3.golfpay.model.guest;

import java.util.ArrayList;

public class ClubInfo {

    public ArrayList<String> wood = new ArrayList<>();
    public ArrayList<String> utility = new ArrayList<>();
    public ArrayList<String> iron = new ArrayList<>();
    public ArrayList<String> putter = new ArrayList<>();
    public ArrayList<String> wedge = new ArrayList<>();
    public ArrayList<String> wood_cover = new ArrayList<>();
    public ArrayList<String> putter_cover = new ArrayList<>();
    public ArrayList<String> cover = new ArrayList<>();

    public ArrayList<String> getWood() {
        return wood;
    }

    public void setWood(ArrayList<String> wood) {
        this.wood = wood;
    }

    public ArrayList<String> getUtility() {
        return utility;
    }

    public void setUtility(ArrayList<String> utility) {
        this.utility = utility;
    }

    public ArrayList<String> getIron() {
        return iron;
    }

    public void setIron(ArrayList<String> iron) {
        this.iron = iron;
    }

    public ArrayList<String> getWedge() {
        return wedge;
    }

    public void setWedge(ArrayList<String> wedge) {
        this.wedge = wedge;
    }

    public ArrayList<String> getPutter() {
        return putter;
    }

    public void setPutter(ArrayList<String> putter) {
        this.putter = putter;
    }

    public ArrayList<String> getWood_cover() {
        return wood_cover;
    }

    public void setWood_cover(ArrayList<String> wood_cover) {
        this.wood_cover = wood_cover;
    }

    public ArrayList<String> getPutter_cover() {
        return putter_cover;
    }

    public void setPutter_cover(ArrayList<String> putter_cover) {
        this.putter_cover = putter_cover;
    }

    public ArrayList<String> getCover() {
        return cover;
    }

    public void setCover(ArrayList<String> cover) {
        this.cover = cover;
    }

    public int totalCount() {
        int totalCount = 0;
        totalCount += wood.size();
        totalCount += utility.size();
        totalCount += iron.size();
        totalCount += wedge.size();
        totalCount += getCoverCount(putter);
        totalCount += getCoverCount(wood_cover);
        totalCount += getCoverCount(putter_cover);
        totalCount += getCoverCount(cover);

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
        return getCoverCount(putter);
    }

    public int coverCount() {
        int coverCount = 0;
        coverCount += getCoverCount(wood_cover);
        coverCount += getCoverCount(putter_cover);
        coverCount += getCoverCount(cover);

        return coverCount;
    }
}
