
package com.eye3.golfpay.model.guest;

import com.eye3.golfpay.model.field.Club;
import com.eye3.golfpay.model.gallery.GalleryPicture;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Guest implements Serializable {
    //새로 추가됨

    @Expose
    private String carNumber;
    @Expose
    private String checkin;
    @SerializedName("club_image")
    private String clubImage;
    @SerializedName("club_url")
    private String clubUrl;
    @Expose
    private String guestName;
    @SerializedName("bagName")
    @Expose
    private String bagName;
    @SerializedName("guest_type")
    private Object guestType;
    @Expose
    private String id;
    @Expose
    private String phoneNumber;

   //새로 추가됨
    @SerializedName("club_set")
    public List<Club> clubList;

    //UI에서 사용
    public boolean selected = false;

    public String getCarNumber() {
        return carNumber;
    }

    public String getCheckin() {
        return checkin;
    }

    public String getClubImage() {
        return clubImage;
    }

    public String getClubUrl() {
        return clubUrl;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getBagName() {
        return bagName;
    }

    public String getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<Club> getClubList() {
        return clubList;
    }
}
