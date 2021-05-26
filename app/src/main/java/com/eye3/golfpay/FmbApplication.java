package com.eye3.golfpay;

import androidx.multidex.MultiDexApplication;
import com.facebook.drawee.backends.pipeline.Fresco;


public class FmbApplication extends MultiDexApplication {

    private static FmbApplication instance;
    public static FmbApplication getInstance() {
        return instance;
    }

    public FmbApplication() {
        instance = this;
    }

    @Override public void onCreate() {
        super.onCreate();
        Fresco.initialize(getApplicationContext());
    }
}