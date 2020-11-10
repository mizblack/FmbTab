
package com.eye3.golfpay.model.teeup;

import java.io.Serializable;

import io.realm.RealmObject;

@SuppressWarnings("unused")
public class GuestScoreDB extends RealmObject implements Serializable {

    private String guest_id ;
    private float nearest = -1;
    private float longest = -1;
    private int longestRank = 6;
    private int nearestRank = 6;

    public String getGuest_id() {
        return guest_id;
    }

    public void setGuest_id(String guest_id) {
        this.guest_id = guest_id;
    }

    public float getNearest() {
        return nearest;
    }

    public void setNearest(float nearest) {
        this.nearest = nearest;
    }

    public float getLongest() {
        return longest;
    }

    public void setLongest(float longest) {
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
