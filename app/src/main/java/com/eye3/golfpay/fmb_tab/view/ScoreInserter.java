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
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.AppDef;

import java.util.ArrayList;
import java.util.List;
//새로 구현할것
public class ScoreInserter extends RelativeLayout {
    public static final int NUM_STROKE_SELLECTED_PREVIOUS_KEY = 1 + 2 << 24;
    public static final int NUM_PAR_SELLECTED_PREVIOUS_KEY = 1 + 3 << 24;
    public static final int NUM_PUTT_SELLECTED_PREVIOUS_KEY = 1 + 4 << 24;

    AppDef.ScoreType mScoreType;
    public ArrayList mParScoreIntegerArrayList;
    public TextView[] mParScoreTextViewArr;
    public TextView mSelectedParInserterTv;
    public TextView mPreVParInserterTv;
    public int mSelectedParScoreTvIdx = -1000;

    public ArrayList mStrokesScoreIntegerArrayList;
    public TextView[] mStrokeScoreTextViewArr;
    public TextView mSelectedStrokeInserterTv;
    public TextView mPrevStrokeInserterTv;
    public int mSelectedStrokeScoreTvIdx = -1000;

    public ArrayList mPuttIntegerArrayList;
    public TextView[] mPuttScoreTextViewArr;
    public TextView mSelectedPuttInserterTv;
    public TextView mPrevPuttInserterTv;
    public int mSelectedPuttScoreTvIdx = -1000;

    ArrayList mNearestIntegerArrayList;
    ArrayList mLongestIntegerArrayList;
    LinearLayout mLinearScoreInserterContainer;
    Context mContext;

    public ScoreInserter(Context context) {
        super(context);
        init(context);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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
        View v = inflater.inflate(R.layout.inserter_layout, this, false);
        mLinearScoreInserterContainer = v.findViewById(R.id.linear_score_insert_container);
        addView(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void init(Context context, String type) {
        this.mContext = context;
        this.mSelectedParInserterTv = new TextView(mContext);
        this.mSelectedStrokeInserterTv = new TextView(mContext);
        this.mSelectedPuttInserterTv = new TextView(mContext);

        createIntegerArrayList();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.inserter_layout, this, false);
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
        mParScoreIntegerArrayList = incrementsLoop(3 * (-1), 6, 1);
        mStrokesScoreIntegerArrayList = incrementsLoop(1, 9, 1);
        mPuttIntegerArrayList = incrementsLoop(0, 5, 1);
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

    //스코어 블럭 배경을 초기화 시킨다.
    public static void initAllBackGroundResources(TextView[] tvArr) {
        for (int i = 0; tvArr.length > i; i++) {
            tvArr[i].getBackground().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.DST_IN);
            tvArr[i].setBackgroundResource(R.drawable.score_inserter_bg);

        }
    }

    public void setBackGroundColorSelected(TextView[] tv, int idx) {
        tv[idx].setBackgroundResource(R.drawable.score_inserter_bg);
        tv[idx].getBackground().setColorFilter(Color.parseColor("#00AEC9"), PorterDuff.Mode.SRC);

    }

    public void setBackGroundColorSelected(TextView[] tv, String score, int idx) {
        if (idx <= -1000)
            return;
        tv[idx].setBackgroundResource(R.drawable.score_inserter_bg);
        tv[idx].getBackground().setColorFilter(Color.parseColor("#00AEC9"), PorterDuff.Mode.SRC);
    }

    public static TextView findScoreInserterTextViewbyScore(TextView[] tvArr, String score) {
        for (TextView aTv : tvArr) {
            if (aTv.getText().toString().trim().equals(score))
                return aTv;
        }
        return null;

    }

    public void initScoreBackgroundSelected(String score, boolean isTar) {
        if (score.equals("-"))
            return;
        TextView tv;
        if (isTar) {
            if (mStrokeScoreTextViewArr != null) {
                tv = findScoreInserterTextViewbyScore(mStrokeScoreTextViewArr, score);
                tv.setBackgroundResource(R.drawable.score_inserter_bg);
                tv.getBackground().setColorFilter(Color.parseColor("#00AEC9"), PorterDuff.Mode.SRC);
                tv.setTag(ScoreInserter.NUM_STROKE_SELLECTED_PREVIOUS_KEY, true);
            }
        } else {
            if (mParScoreTextViewArr != null) {
                tv = findScoreInserterTextViewbyScore(mParScoreTextViewArr, score);
                tv.setBackgroundResource(R.drawable.score_inserter_bg);
                tv.getBackground().setColorFilter(Color.parseColor("#00AEC9"), PorterDuff.Mode.SRC);
                tv.setTag(ScoreInserter.NUM_PAR_SELLECTED_PREVIOUS_KEY, true);
            }
        }


    }

    public void initPuttScoreBackgroundSelected(String puttScore) {
        if (puttScore.equals("-"))
            return;
        TextView tv;

        if (mPuttScoreTextViewArr != null) {
            tv = findScoreInserterTextViewbyScore(mPuttScoreTextViewArr, puttScore);
            tv.setBackgroundResource(R.drawable.score_inserter_bg);
            tv.getBackground().setColorFilter(Color.parseColor("#00AEC9"), PorterDuff.Mode.SRC);
            tv.setTag(ScoreInserter.NUM_PUTT_SELLECTED_PREVIOUS_KEY, true);
        }
    }

    private void createScoreInserters() {

        for (int i = 0; mParScoreIntegerArrayList.size() > i; i++) {
            mParScoreTextViewArr[i] = new TextView(mContext);

            mParScoreTextViewArr[i].setText(String.valueOf(mParScoreIntegerArrayList.get(i)));
            mLinearScoreInserterContainer.addView(mParScoreTextViewArr[i]);
        }

        for (int i = 0; mStrokesScoreIntegerArrayList.size() > i; i++) {
            mStrokeScoreTextViewArr[i] = new TextView(mContext);
            mStrokeScoreTextViewArr[i].setText(String.valueOf(mStrokesScoreIntegerArrayList.get(i)));
            mLinearScoreInserterContainer.addView(mStrokeScoreTextViewArr[i]);
        }

        for (int i = 0; mPuttIntegerArrayList.size() > i; i++) {
            mPuttScoreTextViewArr[i] = new TextView(mContext);
            mPuttScoreTextViewArr[i].setText(String.valueOf(mPuttIntegerArrayList.get(i)));
            mLinearScoreInserterContainer.addView(mPuttScoreTextViewArr[i]);
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createScoreInserter(String scoreType) {

        switch (scoreType) {
            case AppDef.ScoreType.Par:
                mParScoreTextViewArr = new TextView[mParScoreIntegerArrayList.size()];
                for (int i = 0; mParScoreIntegerArrayList.size() > i; i++) {
                    final int idx_par = i;
                    mParScoreTextViewArr[idx_par] = new TextView(mContext);
                    mParScoreTextViewArr[idx_par].setBackgroundResource(R.drawable.score_inserter_bg);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        mParScoreTextViewArr[idx_par].setTextAppearance(R.style.ScoreInserterTextView);
                    }
                    mParScoreTextViewArr[idx_par].setGravity(Gravity.CENTER);
                    mParScoreTextViewArr[idx_par].setLayoutParams(new ViewGroup.LayoutParams(getResources().getDimensionPixelSize(R.dimen.score_inserter_width),getResources().getDimensionPixelSize(R.dimen.score_inserter_height)));
                    mParScoreTextViewArr[idx_par].setText(String.valueOf(mParScoreIntegerArrayList.get(i)));
                    mParScoreTextViewArr[idx_par].setTag(NUM_PAR_SELLECTED_PREVIOUS_KEY, false);
                    mParScoreTextViewArr[idx_par].setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if(mPreVParInserterTv != null){
                                mPreVParInserterTv.getBackground().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.DST_IN);
                                mPreVParInserterTv.setBackgroundResource(R.drawable.score_inserter_bg);
                                mSelectedParInserterTv.setTextColor(getResources().getColor(R.color.gray, null));
                            }

                            mPreVParInserterTv = (TextView)v;
                       //     initAllBackGroundResources(mParScoreTextViewArr);
                            v.getBackground().setColorFilter(Color.parseColor("#00AEC9"), PorterDuff.Mode.SRC);
                            mSelectedParInserterTv = (TextView) v;
                            mSelectedParInserterTv.setTextColor(Color.WHITE);
                            v.setTag(NUM_PAR_SELLECTED_PREVIOUS_KEY, true);
                            mSelectedParScoreTvIdx = idx_par;

                            return true;
                        }
                    });
                    mLinearScoreInserterContainer.addView(mParScoreTextViewArr[idx_par]);

                }
                break;
            case AppDef.ScoreType.Tar:
                mStrokeScoreTextViewArr = new TextView[mStrokesScoreIntegerArrayList.size()];
                for (int j = 0; mStrokesScoreIntegerArrayList.size() > j; j++) {
                    final int idx_stroke = j;
                    mStrokeScoreTextViewArr[idx_stroke] = new TextView(mContext);
                    mStrokeScoreTextViewArr[idx_stroke].setBackgroundResource(R.drawable.score_inserter_bg);
                    mStrokeScoreTextViewArr[idx_stroke].setTextAppearance(R.style.ScoreInserterTextView);
                    mStrokeScoreTextViewArr[idx_stroke].setGravity(Gravity.CENTER);
                    mStrokeScoreTextViewArr[idx_stroke].setLayoutParams(new ViewGroup.LayoutParams(getResources().getDimensionPixelSize(R.dimen.score_inserter_width),getResources().getDimensionPixelSize(R.dimen.score_inserter_height)));
                    mStrokeScoreTextViewArr[idx_stroke].setText(String.valueOf(mStrokesScoreIntegerArrayList.get(j)));
                    mStrokeScoreTextViewArr[idx_stroke].setTag(NUM_STROKE_SELLECTED_PREVIOUS_KEY, false);

                    mStrokeScoreTextViewArr[idx_stroke].setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            initAllBackGroundResources(mStrokeScoreTextViewArr);
                            v.getBackground().setColorFilter(Color.parseColor("#00AEC9"), PorterDuff.Mode.SRC);
                            mSelectedStrokeInserterTv = (TextView) v;
                            mSelectedStrokeInserterTv.setTextColor(Color.WHITE);
                            v.setTag(NUM_STROKE_SELLECTED_PREVIOUS_KEY, true);
                            mSelectedStrokeScoreTvIdx = idx_stroke;

                            return true;
                        }
                    });

                    mLinearScoreInserterContainer.addView(mStrokeScoreTextViewArr[idx_stroke]);

                }
                break;
            case AppDef.ScoreType.Putt:
                mPuttScoreTextViewArr = new TextView[mPuttIntegerArrayList.size()];
                for (int k = 0; mPuttIntegerArrayList.size() > k; k++) {
                    final int idx_putt = k;
                    mPuttScoreTextViewArr[idx_putt] = new TextView(mContext);
                    mPuttScoreTextViewArr[idx_putt].setBackgroundResource(R.drawable.score_inserter_bg);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        mPuttScoreTextViewArr[idx_putt].setTextAppearance(R.style.ScoreInserterTextView);
                    }
                    mPuttScoreTextViewArr[idx_putt].setGravity(Gravity.CENTER);
                    mPuttScoreTextViewArr[idx_putt].setLayoutParams(new ViewGroup.LayoutParams(getResources().getDimensionPixelSize(R.dimen.score_inserter_width),getResources().getDimensionPixelSize(R.dimen.score_inserter_height)));
                    mPuttScoreTextViewArr[idx_putt].setText(String.valueOf(mPuttIntegerArrayList.get(k)));
                    mPuttScoreTextViewArr[idx_putt].setTag(NUM_PUTT_SELLECTED_PREVIOUS_KEY, false);

                    mPuttScoreTextViewArr[idx_putt].setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            initAllBackGroundResources(mPuttScoreTextViewArr);
                            v.getBackground().setColorFilter(Color.parseColor("#00AEC9"), PorterDuff.Mode.SRC);
                            mSelectedPuttInserterTv = (TextView) v;
                            mSelectedPuttInserterTv.setTextColor(Color.WHITE);
                            v.setTag(NUM_PUTT_SELLECTED_PREVIOUS_KEY, true);
                            mSelectedPuttScoreTvIdx = idx_putt;

                            return true;
                        }
                    });
                    mLinearScoreInserterContainer.addView(mPuttScoreTextViewArr[idx_putt]);

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
