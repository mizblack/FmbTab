
package com.eye3.golfpay.model.teeup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


@SuppressWarnings("unused")
public class GuestDatum implements Serializable {


    @SerializedName("guest_id")
    @Expose
    private String guest_id ;
    private String id = guest_id;

    @SerializedName("guestName")
    @Expose
    private String guestName;

    @SerializedName("cart_bill")
    private Long cartBill;
    @Expose
    private String checkin;
    @Expose
    private String finish;
    @SerializedName("finish_price")
    private Long finishPrice;
    @Expose
    private Long greenfee;

    @SerializedName("guest_type")
    private String guestType;
    @SerializedName("hole_add")
    private Long holeAdd;

    @SerializedName("paid_amount")
    private Long paidAmount;
    @SerializedName("pay_location")
    private Object payLocation;
    @SerializedName("proshop_bill")
    private Long proshopBill;
    @SerializedName("rent_bill")
    private Long rentBill;
    @SerializedName("restaurant_bill")
    private Long restaurantBill;
    @SerializedName("room_bill")
    private Long roomBill;
    @SerializedName("shade_bill")
    private Long shadeBill;
    @SerializedName("total_price")
    private Long totalPrice;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("carNumber")
    private String carNumber;

    private float nearest = -1;
    private float longest = -1;
    private int longestRank = 6;
    private int nearestRank = 6;

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

    public void setLongestRank(int rank) {
        this.longestRank = rank;
    }

    public int getNearestRank() {
        return nearestRank;
    }

    public void setNearestRank(int nearestRank) {
        this.nearestRank = nearestRank;
    }

    public Long getCartBill() {
        return cartBill;
    }

    public void setCartBill(Long cartBill) {
        this.cartBill = cartBill;
    }

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public Long getFinishPrice() {
        return finishPrice;
    }

    public void setFinishPrice(Long finishPrice) {
        this.finishPrice = finishPrice;
    }

    public Long getGreenfee() {
        return greenfee;
    }

    public void setGreenfee(Long greenfee) {
        this.greenfee = greenfee;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestType() {
        return guestType;
    }

    public void setGuestType(String guestType) {
        this.guestType = guestType;
    }

    public Long getHoleAdd() {
        return holeAdd;
    }

    public void setHoleAdd(Long holeAdd) {
        this.holeAdd = holeAdd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Long paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Object getPayLocation() {
        return payLocation;
    }

    public void setPayLocation(Object payLocation) {
        this.payLocation = payLocation;
    }

    public Long getProshopBill() {
        return proshopBill;
    }

    public void setProshopBill(Long proshopBill) {
        this.proshopBill = proshopBill;
    }

    public Long getRentBill() {
        return rentBill;
    }

    public void setRentBill(Long rentBill) {
        this.rentBill = rentBill;
    }

    public Long getRestaurantBill() {
        return restaurantBill;
    }

    public void setRestaurantBill(Long restaurantBill) {
        this.restaurantBill = restaurantBill;
    }

    public Long getRoomBill() {
        return roomBill;
    }

    public void setRoomBill(Long roomBill) {
        this.roomBill = roomBill;
    }

    public Long getShadeBill() {
        return shadeBill;
    }

    public void setShadeBill(Long shadeBill) {
        this.shadeBill = shadeBill;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }
}
