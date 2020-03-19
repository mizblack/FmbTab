package com.eye3.golfpay.fmb_tab.view;


import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.view.ContextThemeWrapper;

import com.eye3.golfpay.fmb_tab.R;

import java.util.List;

public class LongestInserter extends Inserter {
     List<Integer> mLongestInserterList;

    public LongestInserter(Context context) {
        super(context);
    }

    public LongestInserter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public void createIntegerArrayList() {
        mLongestInserterList =  incrementsLoop(100, 500, 10);
    }

    private void init(){
        createIntegerArrayList();
        createLongestInserter();
    }

    public void createLongestInserter(){
        for (int i = 0; mLongestInserterList.size() > i; i++) {
           TextView tvLongeset = new TextView(mContext);
            TextView tv = new TextView(new ContextThemeWrapper(mContext, R.style.ScoreInserterTextView), null, 0);
            final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
            final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width, height);
            tv.setLayoutParams(param);
            tvLongeset.setTag(mLongestInserterList.get(i));
            tvLongeset.setText(String.valueOf(mLongestInserterList.get(i)));
            mLinearScoreInserterContainer.addView(tvLongeset);
        }
    }

}
