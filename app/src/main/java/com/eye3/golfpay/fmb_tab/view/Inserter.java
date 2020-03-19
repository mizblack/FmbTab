package com.eye3.golfpay.fmb_tab.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.eye3.golfpay.fmb_tab.R;

import java.util.ArrayList;

public abstract class Inserter extends RelativeLayout {
    Context mContext;
    LinearLayout mLinearScoreInserterContainer;

    public Inserter(Context context) {
        super(context);
    }

    public Inserter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public void init(Context context) {
        this.mContext = context;
        createIntegerArrayList();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.score_inserter_layout, this, false);
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

    public  abstract void  createIntegerArrayList();

}
