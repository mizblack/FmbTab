package com.eye3.golfpay.fmb_tab.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.view.ContextThemeWrapper;

import com.eye3.golfpay.fmb_tab.R;

import java.util.List;

public class NearestInserter extends Inserter {
  //  List<Integer> mNearestInserterList;

    public NearestInserter(Context context) {
        super(context);
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
        mIntInserterList = incrementsLoop(10, 2000, 100);
    }


}


