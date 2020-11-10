package com.eye3.golfpay.model.score;

import com.eye3.golfpay.model.field.Course;
import com.eye3.golfpay.model.teeup.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class ScoreBoard implements Serializable {
   public  ArrayList<Course> courseInfoList = new ArrayList<>();
   public  ArrayList<Player> playerList =  new ArrayList<>();

  // int a =   board.playerList.get(0).courseScores.get(0).scoreSet[0].hit;

}
