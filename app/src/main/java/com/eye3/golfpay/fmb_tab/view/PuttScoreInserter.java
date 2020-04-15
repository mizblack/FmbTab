package com.eye3.golfpay.fmb_tab.view;

import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;
//여기 구현할것
public class PuttScoreInserter extends Inserter {
    public ArrayList mPuttIntegerArrayList;
    public TextView[] mPuttScoreTextViewArr;
    public TextView mSelectedPuttInserterTv;
    public TextView mPrevPuttInserterTv;
    public int mSelectedPuttScoreTvIdx = -1000;
    public PuttScoreInserter(Context context) {
        super(context);
        init(context);

    }
    @Override
    public void createIntegerArrayList() {

    }

    @Override
    public void init(Context context) {
        super.init(context);

    }
}
