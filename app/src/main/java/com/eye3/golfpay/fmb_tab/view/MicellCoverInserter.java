package com.eye3.golfpay.fmb_tab.view;

import android.content.Context;
import android.util.AttributeSet;

import com.eye3.golfpay.fmb_tab.R;

import java.util.List;

public class MicellCoverInserter extends Inserter {
    List<String> mMicellCoverList;

    public MicellCoverInserter(Context context) {
        super(context);
    }

    public MicellCoverInserter(Context context, List<String> woodList) {
        super(context);
        this.mMicellCoverList = woodList;
    }

    public MicellCoverInserter(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    @Override
    public void createIntegerArrayList() {
        mIntInserterList =  incrementsLoop(0, 5, 1);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        createIntegerArrayList();
        createInserterForMultiChoice(getResources().getDimensionPixelSize(R.dimen.club_inserter_width), getResources().getDimensionPixelSize(R.dimen.club_inserter_height));

    }
}
