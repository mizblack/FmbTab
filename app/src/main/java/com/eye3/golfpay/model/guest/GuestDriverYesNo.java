
package com.eye3.golfpay.model.guest;

import com.eye3.golfpay.model.field.Club;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("unused")
public class GuestDriverYesNo implements Serializable {
    //새로 추가됨
    public String guestId = "";
    public boolean driver = false;

    public GuestDriverYesNo(String id, boolean driver){
        this.guestId = id;  this.driver = driver;
    }
}
