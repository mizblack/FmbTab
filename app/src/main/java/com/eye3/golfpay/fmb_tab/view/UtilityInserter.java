package com.eye3.golfpay.fmb_tab.view;

import android.content.Context;
import android.util.AttributeSet;

import com.eye3.golfpay.fmb_tab.R;

import java.util.List;

public class UtilityInserter extends Inserter {
    List<String> mUtilityList;

    public UtilityInserter(Context context) {
        super(context);
    }

    public UtilityInserter(Context context, List<String> UtilityList) {
        super(context);
        this.mUtilityList = UtilityList;
    }

    public UtilityInserter(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    @Override
    public void createIntegerArrayList() {
        mIntInserterList =  incrementsLoop(2, 7, 1);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        createIntegerArrayList();
        createInserterForMultiChoice(getResources().getDimensionPixelSize(R.dimen.club_inserter_width), getResources().getDimensionPixelSize(R.dimen.club_inserter_height));

    }
}
