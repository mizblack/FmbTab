package com.eye3.golfpay.fmb_tab.view;

import android.content.Context;
import android.util.AttributeSet;

import com.eye3.golfpay.fmb_tab.R;

import java.util.List;

public class WoodCoverInserter extends Inserter {
    List<String> mWoodCoverList;

    public WoodCoverInserter(Context context) {
        super(context);
    }

    public WoodCoverInserter(Context context, List<String> woodList) {
        super(context);
        this.mWoodCoverList = woodList;
    }

    public WoodCoverInserter(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    @Override
    public void createIntegerArrayList() {
        mIntInserterList =  incrementsLoop(0, 7, 1);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        createIntegerArrayList();
        createInserterForMultiChoice(getResources().getDimensionPixelSize(R.dimen.club_inserter_width), getResources().getDimensionPixelSize(R.dimen.club_inserter_height));

    }
}
