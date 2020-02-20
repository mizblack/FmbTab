package com.eye3.golfpay.fmb_tab.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.AppDef;

import java.util.ArrayList;

public class ScoreInserter extends HorizontalScrollView {
    AppDef.ScoreType mScoreType;
    public ArrayList mParScoreIntegerArrayList;
    public TextView[] mParScoreTextViewArr;
    public TextView mSelectedParInserterTv;
    public int mSelectedParScoreTvIdx = -1000;

   public  ArrayList mStrokesScoreIntegerArrayList;
    public TextView[] mStrokeScoreTextViewArr;
    public TextView mSelectedStrokeInserterTv;
    public int mSelectedStrokeScoreTvIdx =  -1000;

   public  ArrayList mPuttIntegerArrayList;
    public TextView[] mPuttScoreTextViewArr;
    public TextView mSelectedPuttInserterTv;
    public int mSelectedPuttScoreTvIdx = -1000;

    ArrayList mNearestIntegerArrayList;
    ArrayList mLongestIntegerArrayList;
    LinearLayout mLinearScoreInserterContainer;
    Context mContext;

    public ScoreInserter(Context context) {
        super(context);
        init(context);

    }

    public ScoreInserter(Context context, String type) {
        super(context);
        init(context, type);
    }

    public ScoreInserter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Context context) {
        this.mContext = context;
        createIntegerArrayList();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.score_inserter_layout, this, false);
        mLinearScoreInserterContainer =  v.findViewById(R.id.linear_score_insert_container);
         //createScoreInserters();
        addView(v);
    }

    public void init(Context context, String type) {
        this.mContext = context;
        this.mSelectedParInserterTv = new TextView(mContext);
        this.mSelectedStrokeInserterTv = new TextView(mContext);
        this.mSelectedPuttInserterTv = new TextView(mContext);

        createIntegerArrayList();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.score_inserter_layout, this, false);
        mLinearScoreInserterContainer = v.findViewById(R.id.linear_score_insert_container);
        createScoreInserter(type);
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

    void createIntegerArrayList() {
        mParScoreIntegerArrayList = incrementsLoop(2 * (-1), 10, 1);
        mStrokesScoreIntegerArrayList = incrementsLoop(1, 10, 1);
        mPuttIntegerArrayList = incrementsLoop(0, 10, 1);
        mNearestIntegerArrayList = incrementsLoop(0, 20, 1);
        mLongestIntegerArrayList = incrementsLoop(100, 300, 10);
    }



    public void setAllBackGroundColor(TextView[] tvArr, int color) {
        for (int i = 0; tvArr.length > i; i++) {
            tvArr[i].setBackgroundColor(color);
        }
    }

    public void setAllBackGroundResource(TextView[] tvArr, int id) {
        for (int i = 0; tvArr.length > i; i++) {
            tvArr[i].setBackgroundResource(R.drawable.score_inserter_bg);
        }
    }

    public static void setAllBackGroundResourceBack(TextView[] tvArr, int id) {
        for (int i = 0; tvArr.length > i; i++) {
            tvArr[i].getBackground().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.DST_IN) ;
            tvArr[i].setBackgroundResource(R.drawable.score_inserter_bg);
        }
    }

    public void setBackGroundColorSelected(TextView[] tv, int idx) {
        tv[idx].setBackgroundResource(R.drawable.score_inserter_bg);
        tv[idx].getBackground().setColorFilter(Color.parseColor("#00AEC9"), PorterDuff.Mode.DST_IN);

    }

    public void setBackGroundColorSelected(TextView[] tv, String score, int idx) {
         if(idx <= -1000)
             return;
        tv[idx].setBackgroundResource(R.drawable.score_inserter_bg);
        tv[idx].getBackground().setColorFilter(Color.parseColor("#00AEC9"), PorterDuff.Mode.DST_IN);
    }

    public static TextView findScoreInserterTextViewbyScore(TextView[] tvArr, String score) {
        for (TextView aTv : tvArr) {
            if (aTv.getText().toString().trim().equals(score))
                return aTv;
        }
        return null;

    }


//    void createIntegerViewArr() {
//        if (mParScoreIntegerArrayList != null) {
//            mParScoreTextViewArr = new TextView[mParScoreIntegerArrayList.size()];
//
//            for (int i = 0; mParScoreTextViewArr.length > i; i++) {
//                final int idx = i;
//                mParScoreTextViewArr[idx] = new TextView(mContext);
//                mParScoreTextViewArr[idx].setText(String.valueOf(mParScoreIntegerArrayList.get(i)));
//                mParScoreTextViewArr[idx].setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        setAllBackGroundResourceBack(mParScoreTextViewArr, Color.WHITE);
//                        v.setBackgroundColor(Color.parseColor("#00AEC9"));
//                        mSelectedParInserterTv = (TextView) v;
//                        mSelectedParScoreTvIdx = idx;
//                    }
//                });
//            }
//        }
//        if (mStrokesScoreIntegerArrayList != null) {
//            mStrokeScoreTextViewArr = new TextView[mStrokesScoreIntegerArrayList.size()];
//
//            for (int i = 0; mStrokeScoreTextViewArr.length > i; i++) {
//                final int idx = i;
//                mStrokeScoreTextViewArr[idx] = new TextView(mContext);
//                mStrokeScoreTextViewArr[idx].setText(String.valueOf(mStrokesScoreIntegerArrayList.get(i)));
//                mStrokeScoreTextViewArr[idx].setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        setAllBackGroundColor(mStrokeScoreTextViewArr, Color.WHITE);
//                        v.setBackgroundColor(Color.parseColor("#00AEC9"));
//                        mSelectedParInserterTv = (TextView) v;
//                        mSelectedStrokeScoreTvIdx = idx;
//                    }
//                });
//            }
//
//        }
//
//        if (mPuttIntegerArrayList != null) {
//            mPuttScoreTextViewArr = new TextView[mPuttIntegerArrayList.size()];
//            for (int i = 0; mPuttScoreTextViewArr.length > i; i++) {
//                final int idx = i;
//                mPuttScoreTextViewArr[i] = new TextView(mContext);
//                mPuttScoreTextViewArr[i].setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        setAllBackGroundColor(mPuttScoreTextViewArr, Color.WHITE);
//                        v.setBackgroundColor(Color.CYAN);
//                        mSelectedPuttScoreTvIdx = idx;
//                    }
//                });
//            }
//        }
//    }

    private void createScoreInserters() {

        for (int i = 0; mParScoreIntegerArrayList.size() > i; i++) {
            mParScoreTextViewArr[i]  = new TextView(mContext);

            mParScoreTextViewArr[i].setText(String.valueOf(mParScoreIntegerArrayList.get(i)));
            mLinearScoreInserterContainer.addView(mParScoreTextViewArr[i]);
        }

        for (int i = 0; mStrokesScoreIntegerArrayList.size() > i; i++) {
              mStrokeScoreTextViewArr[i] =  new TextView(mContext);
            mStrokeScoreTextViewArr[i].setText(String.valueOf(mStrokesScoreIntegerArrayList.get(i)));
            mLinearScoreInserterContainer.addView(mStrokeScoreTextViewArr[i]);
        }

        for (int i = 0; mPuttIntegerArrayList.size() > i; i++) {
            mPuttScoreTextViewArr[i] = new TextView(mContext);
            mPuttScoreTextViewArr[i].setText(String.valueOf(mPuttIntegerArrayList.get(i)));
            mLinearScoreInserterContainer.addView( mPuttScoreTextViewArr[i]);
        }


    }


    private void createScoreInserter(String scoreType) {
   //     LinearLayout aContainer = new LinearLayout(mContext);
        switch (scoreType.toString()) {
            case AppDef.ScoreType.Par:
                mParScoreTextViewArr = new TextView[ mParScoreIntegerArrayList.size()];
                for (int i = 0; mParScoreIntegerArrayList.size() > i; i++) {
                    final int idx = i;
                    mParScoreTextViewArr[idx] = new TextView(mContext);
                    mParScoreTextViewArr[idx].setBackgroundResource(R.drawable.score_inserter_bg);
                    mParScoreTextViewArr[idx].setTextAppearance(R.style.ScoreInserterTextView);
                    mParScoreTextViewArr[idx].setGravity(Gravity.CENTER);
                    mParScoreTextViewArr[idx].setLayoutParams(new ViewGroup.LayoutParams(80, 100));
                    mParScoreTextViewArr[idx].setText(String.valueOf(mParScoreIntegerArrayList.get(i)));
                    mParScoreTextViewArr[idx].setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setAllBackGroundResourceBack(mParScoreTextViewArr, R.drawable.score_inserter_bg);
                            v.getBackground().setColorFilter(Color.parseColor("#00AEC9"), PorterDuff.Mode.SRC);
                            mSelectedParInserterTv = (TextView) v;
                            mSelectedParScoreTvIdx = idx;
                        }
                    });
                    mLinearScoreInserterContainer.addView( mParScoreTextViewArr[i]);
                }
                break;
            case AppDef.ScoreType.Tar:
                mStrokeScoreTextViewArr = new TextView[ mStrokesScoreIntegerArrayList.size()];
                for (int i = 0; mStrokesScoreIntegerArrayList.size() > i; i++) {
                    final int idx = i;
                    mStrokeScoreTextViewArr[i] = new  TextView(mContext);
                    mStrokeScoreTextViewArr[i].setBackgroundResource(R.drawable.score_inserter_bg);
                    mStrokeScoreTextViewArr[i].setTextAppearance(R.style.ScoreInserterTextView);
                    mStrokeScoreTextViewArr[i].setGravity(Gravity.CENTER);
                    mStrokeScoreTextViewArr[i].setLayoutParams(new ViewGroup.LayoutParams(80, 100));
                    mStrokeScoreTextViewArr[i].setText(String.valueOf(mStrokesScoreIntegerArrayList.get(i)));
                    mStrokeScoreTextViewArr[i].setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setAllBackGroundResourceBack(mStrokeScoreTextViewArr, R.drawable.score_inserter_bg);
                            v.getBackground().setColorFilter(Color.parseColor("#00AEC9"), PorterDuff.Mode.SRC);
                            mSelectedStrokeInserterTv = (TextView) v;
                            mSelectedStrokeScoreTvIdx = idx;
                        }
                    });
                    mLinearScoreInserterContainer.addView(   mStrokeScoreTextViewArr[i]);
                }
                break;
            case AppDef.ScoreType.Putt:
                mPuttScoreTextViewArr = new TextView[ mPuttIntegerArrayList.size()];
                for (int i = 0; mPuttIntegerArrayList.size() > i; i++) {
                    final int idx = i;
                    mPuttScoreTextViewArr[i] = new  TextView(mContext);
                    mPuttScoreTextViewArr[i].setBackgroundResource(R.drawable.score_inserter_bg);
                    mPuttScoreTextViewArr[i].setTextAppearance(R.style.ScoreInserterTextView);
                    mPuttScoreTextViewArr[i].setGravity(Gravity.CENTER);
                    mPuttScoreTextViewArr[i].setLayoutParams(new ViewGroup.LayoutParams(80, 100));
                    mPuttScoreTextViewArr[i].setText(String.valueOf(mPuttIntegerArrayList.get(i)));
                    mPuttScoreTextViewArr[i].setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setAllBackGroundResourceBack(mPuttScoreTextViewArr, R.drawable.score_inserter_bg);
                            v.getBackground().setColorFilter(Color.parseColor("#00AEC9"), PorterDuff.Mode.SRC);
                            mSelectedPuttInserterTv = (TextView) v;
                            mSelectedPuttScoreTvIdx = idx;
                        }
                    });
                    mLinearScoreInserterContainer.addView(mPuttScoreTextViewArr[i]);
                }
                break;
            default:

        }

    }

//    public LinearLayout getmParScoreInserter() {
//        return createScoreInserter(AppDef.ScoreType.Par);
//    }
//
//    public LinearLayout getmTarScoreInserter() {
//        return createScoreInserter(AppDef.ScoreType.Tar);
//    }
//
//    public LinearLayout getmPuttScoreInserter() {
//        return createScoreInserter(AppDef.ScoreType.Putt);
//    }


}
