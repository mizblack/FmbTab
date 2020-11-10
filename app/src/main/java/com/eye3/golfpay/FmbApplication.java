package com.eye3.golfpay;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class FmbApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();
        Fresco.initialize(getApplicationContext());

        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder().name("appdb.realm").build();
        Realm.setDefaultConfiguration(config);
    }
}