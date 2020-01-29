package com.eye3.golfpay.fmb_tab.model.score;

import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.eye3.golfpay.fmb_tab.model.field.Hole;

import java.io.Serializable;

public class HoleScoreSet extends Course implements Serializable {

    public Score[] scoreSet = new Score[9];
}
