package com.eye3.golfpay.fmb_tab.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.AppDef;
import com.eye3.golfpay.fmb_tab.fragment.ControlFragment;

import java.util.ArrayList;

public class ScoreInserter extends RelativeLayout {

    AppDef.ScoreType mScoreType;
    public ArrayList mParScoreIntegerArrayList;
    public TextView[] mParScoreTextViewArr;
    public TextView mSelectedParInserterTv;
    public int mSelectedParScoreTvIdx = -1000;

    public ArrayList mStrokesScoreIntegerArrayList;
    public TextView[] mStrokeScoreTextViewArr;
    public TextView mSelectedStrokeInserterTv;
    public int mSelectedStrokeScoreTvIdx = -1000;

    public ArrayList mPuttIntegerArrayList;
    public TextView[] mPuttScoreTextViewArr;
    public TextView mSelectedPuttInserterTv;
    public int mSelectedPuttScoreTvIdx = -1000;

    ArrayList mNearestIntegerArrayList;
    ArrayList mLongestIntegerArrayList;
    LinearLayout mScoreInserterContainer;
    Context mContext;

    ArrayList<LinearLayout> scores = new ArrayList<>();

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

        init(context, "type");
    }

    public void init(Context context) {
        this.mContext = context;
        createIntegerArrayList();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.inserter_layout, this, false);
        mScoreInserterContainer = v.findViewById(R.id.score_insert_container);
        addView(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void init(Context context, String type) {
        this.mContext = context;
        this.mSelectedParInserterTv = new TextView(mContext);
        this.mSelectedStrokeInserterTv = new TextView(mContext);
        this.mSelectedPuttInserterTv = new TextView(mContext);


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.inserter_layout, this, false);
        mScoreInserterContainer = v.findViewById(R.id.score_insert_container);

        createIntegerArrayList();

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

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < 4; i++) {
            final int position = i;
            View childView = inflater.inflate(R.layout.inserter_item, null, false);
            LinearLayout linearLayout = childView.findViewById(R.id.view_item);

            for (int j = -2; j < 14; j++) {
                FrameLayout item = (FrameLayout) inflater.inflate(R.layout.item_score, null, false);

                final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
                final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                item.setLayoutParams(params);

                TextView tvItem = item.findViewById(R.id.tv_item);
                tvItem.setText(j + "");

                View view = item.findViewById(R.id.view_select);

                item.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        unSelectAllRow(position);
                        view.setVisibility(View.VISIBLE);
                    }
                });

                linearLayout.addView(item);
            }

            scores.add(linearLayout);

            View separator = inflater.inflate(R.layout.item_separator, null, false);
            final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, RelativeLayout.LayoutParams.MATCH_PARENT, getResources().getDisplayMetrics());
            final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            separator.setLayoutParams(params);
            mScoreInserterContainer.addView(separator);

            mScoreInserterContainer.addView(linearLayout);
        }
    }

    private void unSelectAllRow(final int position) {
        LinearLayout view = scores.get(position);
        for (int i = 0; i < view.getChildCount(); i++) {
            View childView = view.getChildAt(i);
            childView.findViewById(R.id.view_select).setVisibility(View.GONE);
        }
    }

}
