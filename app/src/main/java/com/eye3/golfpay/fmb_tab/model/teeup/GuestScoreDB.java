
package com.eye3.golfpay.fmb_tab.model.teeup;

import java.io.Serializable;

import io.realm.RealmObject;

@SuppressWarnings("unused")
public class GuestScoreDB extends RealmObject implements Serializable {

    private String guest_id ;
    private int nearest = -1;
    private int longest = -1;
    private int longestRank = 6;
    private int nearestRank = 6;

    public String getGuest_id() {
        return guest_id;
    }

    public void setGuest_id(String guest_id) {
        this.guest_id = guest_id;
    }

    public int getNearest() {
        return nearest;
    }

    public void setNearest(int nearest) {
        this.nearest = nearest;
    }

    public int getLongest() {
        return longest;
    }

    public void setLongest(int longest) {
        this.longest = longest;
    }

    public int getLongestRank() {
        return longestRank;
    }

    public void setLongestRank(int longestRank) {
        this.longestRank = longestRank;
    }

    public int getNearestRank() {
        return nearestRank;
    }

    public void setNearestRank(int nearestRank) {
        this.nearestRank = nearestRank;
    }
}
