package com.eye3.golfpay.fmb_tab.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class Inserter extends RelativeLayout {


    public static final int KEY_MULTI_CHOICE_SELECTED = 3 + 2 << 24;
    Context mContext;
    LinearLayout mLinearScoreInserterContainer;
    TextView mSelectedChildView;
    TextView mPrevSelectChildView;
    List<Object> mIntInserterList;
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

    ArrayList<Object> incrementsLoop(int a, int b, int increments) {
        //  singleton.log("incrementsLoop(" + a + ", " + b + ", " + increments + ")");
        ArrayList<Object> integerArrayList = new ArrayList<>();

        for (int i = a; i <= b; ) {
            integerArrayList.add(i);
            i = i + increments;
        }
        return integerArrayList;
    }

    protected void makeChildViewSelected(int value){
        setBackGroundSelected ((TextView)mLinearScoreInserterContainer.getChildAt(value));
        setTextColorSelected((TextView)mLinearScoreInserterContainer.getChildAt(value));
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
    }

    protected void createInserterForSingleChoice() {
        for (int i = 0; mIntInserterList.size() > i; i++) {
            TextView tvInt = new TextView(new ContextThemeWrapper(mContext, R.style.InserterTextView), null, 0);

            LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(130, 150);
            tvInt.setLayoutParams(lllp);
            tvInt.setGravity(Gravity.CENTER);
            tvInt.setTag(mIntInserterList.get(i));
            tvInt.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mSelectedChildView = (TextView) v;
                    return false;
                }
            });
            tvInt.setText(String.valueOf(mIntInserterList.get(i)));
            mLinearScoreInserterContainer.addView(tvInt);
        }
    }

    public void initTextViewBackground(TextView tv) {
        tv.getBackground().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.DST_IN);
        tv.setBackgroundResource(R.drawable.score_inserter_bg);
    }

    public void setBackGroundSelected(TextView tv) {
        tv.setBackgroundResource(R.drawable.score_inserter_bg);
        //     tv.setTextColor(Color.WHITE);
        tv.getBackground().setColorFilter(Color.parseColor("#00ABC5"), PorterDuff.Mode.SRC);

    }

    public void initTextColor(TextView tv) {
        if (tv != null)   //gray
            tv.setTextColor(Color.parseColor("#999999"));
    }

    public void setTextColorSelected(TextView tv) {
        if (tv != null)
            tv.setTextColor(Color.WHITE); //-1
    }


    protected void createInserterForSingleChoice(int width, int height) {

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
                        initTextViewBackground(mPrevSelectChildView);
                    }
                    mSelectedChildView = (TextView) v;
                    // 같은 버튼 세번 눌럿을시 문제 발생오류 해결
                    if (mSelectedChildView == mPrevSelectChildView && mSelectedChildView.getCurrentTextColor() == -1) {
                        initTextViewBackground(mSelectedChildView);
                        initTextColor(mSelectedChildView);
                    } else if (mSelectedChildView == mPrevSelectChildView && mPrevSelectChildView.getCurrentTextColor() == -6710887) {
                        setBackGroundSelected(mSelectedChildView);
                        setTextColorSelected(mSelectedChildView);
                    } else {
                        setBackGroundSelected(mSelectedChildView);
                        setTextColorSelected(mSelectedChildView);
                    }

                    if(mSelectedChildView != mPrevSelectChildView)
                        initTextColor(mPrevSelectChildView);
                    mPrevSelectChildView = mSelectedChildView;
                    return false;
                }
            });
            tvInt.setText(String.valueOf(mIntInserterList.get(i)));
            mLinearScoreInserterContainer.addView(tvInt);
        }
    }

    protected void createInserterForMultiChoice(int width, int height) {

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

                    mSelectedChildView = (TextView) v;
                    if (mSelectedChildView.getCurrentTextColor() == -1) {
                        initTextViewBackground(mSelectedChildView);
                        initTextColor(mSelectedChildView);
                        mSelectedChildView.setTag(KEY_MULTI_CHOICE_SELECTED, false);
                    } else if ( mSelectedChildView.getCurrentTextColor() == -6710887) {
                        setBackGroundSelected(mSelectedChildView);
                        setTextColorSelected(mSelectedChildView);
                        mSelectedChildView.setTag(KEY_MULTI_CHOICE_SELECTED, true);
                    }

                    return false;
                }
            });
            tvInt.setText(String.valueOf(mIntInserterList.get(i)));
            mLinearScoreInserterContainer.addView(tvInt);
        }

    }

    public int countSelectedChildView(){
        int count = 0;

        //현재 선택된 복수의 tv숫자를 리턴한다
        return count;
    }

    public static int getBackgroundColor(TextView textView) {
        Drawable drawable = textView.getBackground();
        if (drawable instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) drawable;
            if (Build.VERSION.SDK_INT >= 11) {
                return colorDrawable.getColor();
            }
            try {
                Field field = colorDrawable.getClass().getDeclaredField("mState");
                field.setAccessible(true);
                Object object = field.get(colorDrawable);
                field = object.getClass().getDeclaredField("mUseColor");
                field.setAccessible(true);
                return field.getInt(object);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }


    public TextView getmSelectedChildView() {
        return mSelectedChildView;
    }

    public void setmSelectedChildView(TextView view) {
        mSelectedChildView = view;
    }

}
