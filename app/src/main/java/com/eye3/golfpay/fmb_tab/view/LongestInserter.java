package com.eye3.golfpay.fmb_tab.view;


import android.content.Context;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.inter.BoldScroll;

public class LongestInserter extends Inserter implements BoldScroll {

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
        setBoldScroll(mhorizontalScrollView);
    }

    public void createInserter(){
       super.createInserter();

    }

    @Override
    public void setBoldScroll(HorizontalScrollView hrScrollView) {
        hrScrollView.setOnScrollChangeListener(new OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int[] tvlocation = new int[2];
                int[] scrollViewlocation = new int[2];

                ((HorizontalScrollView) v).getLocationOnScreen(scrollViewlocation);
                mSelectedChildView.getLocationOnScreen(tvlocation);
                if (tvlocation[0] < scrollViewlocation[0]+ 175 + 60 && tvlocation[0] > scrollViewlocation[0]+ 175 -60) {
                    ((TextView) mSelectedChildView).setTextAppearance(R.style.InserterTextViewBold);
                }else{
                    ((TextView) mSelectedChildView).setTextAppearance(R.style.InserterTextView);
                }
            }
        });
    }
}
