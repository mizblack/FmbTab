package com.eye3.golfpay.fmb_tab.service;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.location.GnssStatus;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.util.Logger;
import com.eye3.golfpay.fmb_tab.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;


public class CartLocationService extends Service {
    private static final String TAG = "CartLocationService";
    private LocationManager mLocationManager = null;
    private static int LOCATION_INTERVAL;
    private static final float LOCATION_DISTANCE = 0.0f;
    private static final int NOTIFICATION_ID = 1;

    private WindowManager.LayoutParams params;
    private WindowManager mManager;

    private static final int ALWAYS_ON_TOP_SERVICE_ID = 1;

    private Timer timer;
    private long allocationIdx = -1;

    private int satelliteCount = 0;
    private float[] cn0DbHz;

    private Handler mHanlder ;


    public void setHandler(Handler handler){
        this.mHanlder = handler;
    }

    private Notification createNotification(Context context, RemoteViews remoteViews) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, Util.getNotiChId(this, Global.NotiAlarmChannelID.CHANNEL_LOC))
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(getString(R.string.txt_noti_cont));


        if (remoteViews != null) notificationBuilder.setCustomContentView(remoteViews);

        return notificationBuilder.build();
    }

    //   @SuppressLint("MissingPermission")
    //   @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        LOCATION_INTERVAL = 3 * 1000;

        initializeLocationManager();

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_custom);
        remoteViews.setTextViewText(R.id.serviceNotiTitle, getString(R.string.app_name));
        remoteViews.setTextViewText(R.id.serviceNotiMessage, getString(R.string.txt_noti_cont));
        startForeground(NOTIFICATION_ID, createNotification(this, null));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
            mLocationManager.registerGnssStatusCallback(new GnssStatus.Callback() {
                @Override
                public void onStarted() {
                    super.onStarted();
                }

                @Override
                public void onStopped() {
                    super.onStopped();
                }

                @Override
                public void onFirstFix(int ttffMillis) {
                    super.onFirstFix(ttffMillis);
                }

                @Override
                public void onSatelliteStatusChanged(GnssStatus status) {
                    super.onSatelliteStatusChanged(status);
                    try {
                        satelliteCount = status.getSatelliteCount();

                        cn0DbHz = new float[satelliteCount];

                        for (int i = 0; i < satelliteCount; i++) {
                            cn0DbHz[i] = status.getCn0DbHz(i);
                        }

                    } catch (Exception e) {
//                        e.printStackTrace();
                        satelliteCount = 0;
                        cn0DbHz = new float[]{0};
                    }
                }
            });

        } else {
            mLocationManager.addGpsStatusListener(new GpsStatus.Listener() {
                @Override
                public void onGpsStatusChanged(int event) {
                    switch (event) {
                        case GpsStatus.GPS_EVENT_STARTED:
//                            Log.i(TAG, "onGpsStatusChanged(): GPS started");
                            break;
                        case GpsStatus.GPS_EVENT_FIRST_FIX:
//                            Log.i(TAG, "onGpsStatusChanged(): time to first fix in ms = " + gpsStatus.getTimeToFirstFix());
                            break;
                        case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                            try {
                                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    Activity#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for Activity#requestPermissions for more details.
                                    return;
                                }
                                GpsStatus status = mLocationManager.getGpsStatus(null);
                                Iterable sats = status.getSatellites();
                                Iterator satI = sats.iterator();
                                int count = 0;
                                ArrayList<Float> tmpCn0DbHz = new ArrayList<>();

                                while (satI.hasNext()) {
                                    GpsSatellite gpssatellite = (GpsSatellite) satI.next();
                                    if (gpssatellite.usedInFix()) {
                                        tmpCn0DbHz.add(gpssatellite.getSnr());
                                        count++;
                                    }
                                }

                                if(tmpCn0DbHz.size() > 0) {
                                    cn0DbHz = new float[tmpCn0DbHz.size()];
                                    for(int i=0; i<cn0DbHz.length; i++) {
                                        cn0DbHz[i] = tmpCn0DbHz.get(i);
                                    }
                                } else {
                                    cn0DbHz = new float[]{0};
                                }

                                satelliteCount = count;

                            } catch (Exception e) {
                                e.printStackTrace();
                                satelliteCount = 0;
                                cn0DbHz = new float[]{0};
                            }
                            break;

                        case GpsStatus.GPS_EVENT_STOPPED:
//                            Log.i(TAG, "onGpsStatusChanged(): GPS stopped");
                            break;
                    }
                }
            });
        }

        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, (LOCATION_INTERVAL <= 0) ? 3000 : LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[0]);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, (LOCATION_INTERVAL <= 0) ? 3000 : LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[1]);

        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }


    }





    private String showLogOfLocationInfo(Location location) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onLocationChanged: ").append(location.toString());
        stringBuilder.append("\n각도 : ").append(location.getBearing());
        stringBuilder.append("\n위성개수 : ").append(satelliteCount);
        stringBuilder.append("\n신호세기 : ").append(Arrays.toString(cn0DbHz));
        stringBuilder.append("\n속도 : ").append(location.getSpeed());
        stringBuilder.append("\n정확도 : ").append(location.getAccuracy());
        stringBuilder.append("\n제공프로바이더 : ").append(location.getProvider());

        return stringBuilder.toString();
    }

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);

            mLastLocation.getBearing();
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + showLogOfLocationInfo(location));

            mLastLocation.set(location);
       //     Global.CurrentLocation = mLastLocation;


        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }


    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listener, ignore", ex);
                }
            }
        }

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager - LOCATION_INTERVAL: " + LOCATION_INTERVAL + " LOCATION_DISTANCE: " + LOCATION_DISTANCE);
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }




}
