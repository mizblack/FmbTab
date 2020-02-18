
package com.eye3.golfpay.fmb_tab.model.guest;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class Guest implements Serializable {

    @SerializedName("arr_club_image")
    private ArrayList<ArrClubImage> arrClubImage;
    @Expose
    private String carNumber;
    @SerializedName("cart_bill")
    private String cartBill;
    @Expose
    private String checkin;
    @SerializedName("club_image")
    private String clubImage;
    @SerializedName("club_url")
    private String clubUrl;
    @Expose
    private String finish;
    @SerializedName("finish_price")
    private String finishPrice;
    @Expose
    private String greenfee;
    @Expose
    private String guestName;
    @SerializedName("guest_type")
    private Object guestType;
    @SerializedName("hole_add")
    private String holeAdd;
    @Expose
    private String id;
    @SerializedName("member_type")
    private Object memberType;
    @Expose
    private String memo;
    @SerializedName("paid_amount")
    private String paidAmount;
    @SerializedName("pay_location")
    private Object payLocation;
    @Expose
    private String phoneNumber;
    @SerializedName("proshop_bill")
    private String proshopBill;
    @SerializedName("rent_bill")
    private String rentBill;
    @SerializedName("restaurant_bill")
    private String restaurantBill;
    @SerializedName("room_bill")
    private String roomBill;
    @SerializedName("shade_bill")
    private String shadeBill;
    @SerializedName("sign_image")
    private String signImage;
    @SerializedName("sign_url")
    private Uri signUrl;
    @SerializedName("team_memo")
    private String teamMemo;
    @SerializedName("total_price")
    private String totalPrice;

    public ArrayList<ArrClubImage> getArrClubImage() {
        return arrClubImage;
    }

    public void setArrClubImage(ArrayList<ArrClubImage> arrClubImage) {
        this.arrClubImage = arrClubImage;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCartBill() {
        return cartBill;
    }

    public void setCartBill(String cartBill) {
        this.cartBill = cartBill;
    }

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getClubImage() {
        return clubImage;
    }

    public void setClubImage(String clubImage) {
        this.clubImage = clubImage;
    }

    public String getClubUrl() {
        return clubUrl;
    }

    public void setClubUrl(String clubUrl) {
        this.clubUrl = clubUrl;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getFinishPrice() {
        return finishPrice;
    }

    public void setFinishPrice(String finishPrice) {
        this.finishPrice = finishPrice;
    }

    public String getGreenfee() {
        return greenfee;
    }

    public void setGreenfee(String greenfee) {
        this.greenfee = greenfee;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public Object getGuestType() {
        return guestType;
    }

    public void setGuestType(Object guestType) {
        this.guestType = guestType;
    }

    public String getHoleAdd() {
        return holeAdd;
    }

    public void setHoleAdd(String holeAdd) {
        this.holeAdd = holeAdd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getMemberType() {
        return memberType;
    }

    public void setMemberType(Object memberType) {
        this.memberType = memberType;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Object getPayLocation() {
        return payLocation;
    }

    public void setPayLocation(Object payLocation) {
        this.payLocation = payLocation;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProshopBill() {
        return proshopBill;
    }

    public void setProshopBill(String proshopBill) {
        this.proshopBill = proshopBill;
    }

    public String getRentBill() {
        return rentBill;
    }

    public void setRentBill(String rentBill) {
        this.rentBill = rentBill;
    }

    public String getRestaurantBill() {
        return restaurantBill;
    }

    public void setRestaurantBill(String restaurantBill) {
        this.restaurantBill = restaurantBill;
    }

    public String getRoomBill() {
        return roomBill;
    }

    public void setRoomBill(String roomBill) {
        this.roomBill = roomBill;
    }

    public String getShadeBill() {
        return shadeBill;
    }

    public void setShadeBill(String shadeBill) {
        this.shadeBill = shadeBill;
    }

    public String getSignImage() {
        return signImage;
    }

    public void setSignImage(String signImage) {
        this.signImage = signImage;
    }

    public Uri getSignUrl() {
        return signUrl;
    }

    public void setSignUrl(Uri signUrl) {
        this.signUrl = signUrl;
    }

    public String getTeamMemo() {
        return teamMemo;
    }

    public void setTeamMemo(String teamMemo) {
        this.teamMemo = teamMemo;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

}
