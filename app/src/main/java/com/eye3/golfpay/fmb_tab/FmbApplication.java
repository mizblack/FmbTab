package com.eye3.golfpay.fmb_tab;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class FmbApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();
        Fresco.initialize(getApplicationContext());
    }
}