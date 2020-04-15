package com.eye3.golfpay.fmb_tab.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
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
    TextView mSelectedChildView;
    TextView mPrevSelectChildView;
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
//            tvInt.setOnTouchListener(new OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if(mPrevSelectChildView != null){
//                        initTextViewBack(mPrevSelectChildView);
//                    }
//                    mSelectedChildView = (TextView)v;
//                    mPrevSelectChildView =  mSelectedChildView;
//                    setBackGroundColorSelected(mSelectedChildView);
//                    return false;
//                }
//            });
            tvInt.setText(String.valueOf(mIntInserterList.get(i)));
            mLinearScoreInserterContainer.addView(tvInt);
        }
    }

    public void initTextViewBack(TextView tv) {
        tv.getBackground().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.DST_IN);
        tv.setBackgroundResource(R.drawable.score_inserter_bg);
        // tv.getBackground().setColorFilter(Color.parseColor("#00AEC9"), PorterDuff.Mode.SRC);
        tv.setTextColor(Color.GRAY);
        //  tv.getBackground().setColorFilter(Colo, PorterDuff.Mode.SRC);
    }

    public void setBackGroundColorSelected(TextView tv) {
        tv.setBackgroundResource(R.drawable.score_inserter_bg);
        tv.setTextColor(Color.WHITE);
        tv.getBackground().setColorFilter(Color.parseColor("#00ABC5"), PorterDuff.Mode.SRC);

    }

    protected void createInserter(int width, int height) {
        for (int i = 0; mIntInserterList.size() > i; i++) {
            TextView tvInt = new TextView(new ContextThemeWrapper(mContext, R.style.InserterTextView), null, 0);
            tvInt.setBackgroundResource(R.drawable.score_inserter_bg);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tvInt.setTextAppearance(R.style.ScoreInserterTextView);
            }
            LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(width, height);
            tvInt.setLayoutParams(lllp);
            tvInt.setGravity(Gravity.CENTER);
            tvInt.setTag(mIntInserterList.get(i));
            tvInt.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mPrevSelectChildView != null) {
                        initTextViewBack(mPrevSelectChildView);
                    }
                    mSelectedChildView = (TextView) v;
                    // 같은 버튼 세번 눌럿을시 문제 발생
                    if (mPrevSelectChildView == mSelectedChildView)
                        initTextViewBack(mPrevSelectChildView);
                      else
                          setBackGroundColorSelected(mSelectedChildView);
                    mPrevSelectChildView = mSelectedChildView;

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

    public void setmSelectedChildView(TextView view) {
        mSelectedChildView = view;
    }

}
