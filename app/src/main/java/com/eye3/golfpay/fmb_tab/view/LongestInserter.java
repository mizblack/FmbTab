package com.eye3.golfpay.fmb_tab.view;


import android.content.Context;

public class LongestInserter extends Inserter {

    public LongestInserter(Context context) {
        super(context);
        init();
    }

    @Override
    public void createIntegerArrayList() {
        mIntInserterList =  incrementsLoop(100, 500, 10);
    }

    private void init(){
        createIntegerArrayList();
        createInserter();
    }

    public void createInserter(){
       super.createInserter();

    }

}
