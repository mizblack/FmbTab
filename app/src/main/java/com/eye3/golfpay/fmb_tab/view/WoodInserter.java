package com.eye3.golfpay.fmb_tab.view;

import android.content.Context;
import android.util.AttributeSet;

import com.eye3.golfpay.fmb_tab.R;

import java.util.List;

public class WoodInserter extends Inserter {
    List<String> mWoodList;

    public WoodInserter(Context context) {
        super(context);
    }

    public WoodInserter(Context context, List<String> woodList) {
        super(context);
        this.mWoodList = woodList;
    }

    public WoodInserter(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    @Override
    public void createIntegerArrayList() {
        mIntInserterList =  incrementsLoop(1, 9, 1);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        createIntegerArrayList();
        createInserter(getResources().getDimensionPixelSize(R.dimen.club_inserter_width), getResources().getDimensionPixelSize(R.dimen.club_inserter_height));

    }
}
