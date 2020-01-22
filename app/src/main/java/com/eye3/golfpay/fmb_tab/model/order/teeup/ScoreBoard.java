package com.eye3.golfpay.fmb_tab.model.order.teeup;

import com.eye3.golfpay.fmb_tab.model.field.Course;

import java.io.Serializable;
import java.util.ArrayList;

public class ScoreBoard implements Serializable {
   public  ArrayList<Course> courseInfoList = new ArrayList<>();
   public  ArrayList<Player> playerList =  new ArrayList<>();
   //   player.playedCourseList.get(0).holeList.get(0).hit;
}
