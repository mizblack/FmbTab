package com.eye3.golfpay.fmb_tab.view;

import android.content.Context;
import android.util.AttributeSet;

import com.eye3.golfpay.fmb_tab.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WedgeInserter extends Inserter {
    List<String> mWedgeList;
    List<Object> mWedgeClubSeries ;

    public WedgeInserter(Context context) {
        super(context);
    }

    public WedgeInserter(Context context, List<String> wedgeList) {
        super(context);
        this.mWedgeList = wedgeList;
    }

    public WedgeInserter(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public void setmWedgeClubSeries(){
        mWedgeClubSeries = new ArrayList<>() ;
        mWedgeClubSeries.add("Pw");
        mWedgeClubSeries.add("Sw");
        mWedgeClubSeries.add("PSw");
        mWedgeClubSeries.add("52w");
        mWedgeClubSeries.add("53w");
        mWedgeClubSeries.add("54w");
        mWedgeClubSeries.add("55w");
        mWedgeClubSeries.add("56w");
        mWedgeClubSeries.add("57w");
        mWedgeClubSeries.add("58w");
        mWedgeClubSeries.add("60w");

    }

    @Override
    public void createIntegerArrayList() {
         mIntInserterList = mWedgeClubSeries;
    }

    @Override
    public void init(Context context) {
        super.init(context);
        setmWedgeClubSeries();
        createIntegerArrayList();
        createInserterForMultiChoice(getResources().getDimensionPixelSize(R.dimen.club_inserter_width), getResources().getDimensionPixelSize(R.dimen.club_inserter_height));

    }
}
