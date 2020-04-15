package com.eye3.golfpay.fmb_tab.view;

import android.content.Context;
import android.util.AttributeSet;

import com.eye3.golfpay.fmb_tab.R;

import java.util.List;

public class PutterInserter extends Inserter {
    List<String> mPutterList;

    public PutterInserter(Context context) {
        super(context);
    }

    public PutterInserter(Context context, List<String> PutterList) {
        super(context);
        this.mPutterList = PutterList;
    }

    public PutterInserter(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    @Override
    public void createIntegerArrayList() {
        mIntInserterList =  incrementsLoop(0, 2, 1);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        createIntegerArrayList();
        createInserter(getResources().getDimensionPixelSize(R.dimen.club_inserter_width), getResources().getDimensionPixelSize(R.dimen.club_inserter_height));

    }
    void setmPutterList( List<String> putterList){
        this.mPutterList = putterList;
    }
}
