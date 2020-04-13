package com.eye3.golfpay.fmb_tab.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.view.ContextThemeWrapper;

import com.eye3.golfpay.fmb_tab.R;

import java.util.ArrayList;
import java.util.List;

public abstract class Inserter extends RelativeLayout {
    Context mContext;
    LinearLayout mLinearScoreInserterContainer;
    TextView mSelectedChildView ;
    TextView mPrevChildView ;
    List<Integer> mIntInserterList;
    HorizontalScrollView mhorizontalScrollView;

    public Inserter(Context context) {
        super(context);
        mContext = context;
        init(context);
    }

    public Inserter(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context);
    }


    public void init(Context context) {
        mSelectedChildView = new TextView(context);
        createIntegerArrayList();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.inserter_layout, this, false);
        mhorizontalScrollView = v.findViewById(R.id.linear_insert_sv);
        mhorizontalScrollView.setOnScrollChangeListener(new OnScrollChangeListener() {
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
        mLinearScoreInserterContainer = v.findViewById(R.id.linear_score_insert_container);
        addView(v);
    }

    ArrayList<Integer> incrementsLoop(int a, int b, int increments) {
        //  singleton.log("incrementsLoop(" + a + ", " + b + ", " + increments + ")");
        ArrayList<Integer> integerArrayList = new ArrayList<>();

        for (int i = a; i <= b; ) {
            integerArrayList.add(i);
            i = i + increments;
        }
        return integerArrayList;
    }

    public abstract void createIntegerArrayList();

    public void setTextBoldSelected(String value) {

        for (int i = 0; mLinearScoreInserterContainer.getChildCount() > i; i++) {
            View v = mLinearScoreInserterContainer.getChildAt(i);

            v.getBackground().setColorFilter(Color.parseColor("#00AEC9"), PorterDuff.Mode.SRC);
        }
    }

    public void setSelectedChildViewText(String value) {
            mSelectedChildView.setText(value);
//        for (int i = 0; mLinearScoreInserterContainer.getChildCount() > i; i++) {
//            View v = mLinearScoreInserterContainer.getChildAt(i);
//            if (((TextView) v.getTag()).getText().toString().trim().equals(value)) {
//
//                v.getBackground().setColorFilter(Color.parseColor("#00AEC9"), PorterDuff.Mode.SRC);
//                return;
//            }
//        }
    }

    protected void createInserter() {
        for (int i = 0; mIntInserterList.size() > i; i++) {
            TextView tvInt = new TextView(new ContextThemeWrapper(mContext, R.style.InserterTextView), null, 0);

            LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(130, 150);
            tvInt.setLayoutParams(lllp);
            tvInt.setGravity(Gravity.CENTER);
            tvInt.setTag(mIntInserterList.get(i));
            tvInt.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mSelectedChildView = (TextView)v;

                    return false;
                }
            });
            tvInt.setText(String.valueOf(mIntInserterList.get(i)));
            mLinearScoreInserterContainer.addView(tvInt);
        }
    }

    protected void createInserter(int width , int height) {
        for (int i = 0; mIntInserterList.size() > i; i++) {
            TextView tvInt = new TextView(new ContextThemeWrapper(mContext, R.style.InserterTextView), null, 0);

            LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(width, height);
            tvInt.setLayoutParams(lllp);
            tvInt.setGravity(Gravity.CENTER);
            tvInt.setTag(mIntInserterList.get(i));
            tvInt.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mSelectedChildView = (TextView)v;

                    return false;
                }
            });
            tvInt.setText(String.valueOf(mIntInserterList.get(i)));
            mLinearScoreInserterContainer.addView(tvInt);
        }
    }

    public TextView getmSelectedChildView() {
          return mSelectedChildView;
    }
    public void setmSelectedChildView(TextView view){
        mSelectedChildView = view;
    }

}
