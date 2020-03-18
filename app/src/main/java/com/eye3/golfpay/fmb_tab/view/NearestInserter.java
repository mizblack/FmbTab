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
    List<Integer> mNearestInserterList;
    public NearestInserter(Context context) {
        super(context);
    }

    public NearestInserter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        createIntegerArrayList();
        createNearestInserter();
    }

    @Override
    public void createIntegerArrayList() {
        //단위 cm  10cm~ 20M
        mNearestInserterList =  incrementsLoop(10, 2000, 100);
    }

    public void createNearestInserter(){
        for (int i = 0; mNearestInserterList.size() > i; i++) {
            TextView tvLongeset = new TextView(mContext);
            TextView tv = new TextView(new ContextThemeWrapper(mContext, R.style.ScoreInserterTextView), null, 0);
            final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
            final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width, height);
            tv.setLayoutParams(param);
            tvLongeset.setTag(mNearestInserterList.get(i));
            tvLongeset.setText(String.valueOf(mNearestInserterList.get(i)));
            mLinearScoreInserterContainer.addView(tvLongeset);
        }
    }


}
