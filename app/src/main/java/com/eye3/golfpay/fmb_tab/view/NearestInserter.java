package com.eye3.golfpay.fmb_tab.view;

import android.content.Context;
import android.util.AttributeSet;

public class NearestInserter extends Inserter {
  //  List<Integer> mNearestInserterList;

    public NearestInserter(Context context) {
        super(context);
        init();
    }

    public NearestInserter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        createIntegerArrayList();
        createInserter();
    }

    @Override
    public void createIntegerArrayList() {
        //단위 cm  10cm~ 20M
        mIntInserterList = incrementsLoop(10, 500, 10);
    }


}


