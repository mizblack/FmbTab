package com.eye3.golfpay.model.score;

import com.eye3.golfpay.model.field.Course;
import com.eye3.golfpay.model.field.NearLong;
import com.eye3.golfpay.model.teeup.Player;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class NearLongScoreBoard implements Serializable {

   @SerializedName("ret_code")
   @Expose
   private String  resultCode = "";

   @SerializedName("ret_msg")
   @Expose
   private String resultMessage = "";

   @SerializedName("guest_gametype_ranking")
   @Expose
   public  ArrayList<NearLong> guest_gametype_ranking = new ArrayList<>();
}
