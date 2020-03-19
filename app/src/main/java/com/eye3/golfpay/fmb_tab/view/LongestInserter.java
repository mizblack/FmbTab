package com.eye3.golfpay.fmb_tab.view;


import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.view.ContextThemeWrapper;

import com.eye3.golfpay.fmb_tab.R;

import java.util.List;

public class LongestInserter extends Inserter {

    public LongestInserter(Context context) {
        super(context);
        init();
    }

    @Override
    public void createIntegerArrayList() {
        mIntInserterList =  incrementsLoop(100, 500, 10);
    }

    private void init(){
        createIntegerArrayList();
        createInserter();
    }

    public void createInserter(){
       super.createInserter();

    }

}
