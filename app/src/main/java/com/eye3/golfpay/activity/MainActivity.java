package com.eye3.golfpay.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.exifinterface.media.ExifInterface;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eye3.golfpay.BuildConfig;
import com.eye3.golfpay.R;
import com.eye3.golfpay.camera.StoreUriAsTempFileAsyncTask;
import com.eye3.golfpay.common.AppDef;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.common.UIThread;
import com.eye3.golfpay.dialog.PopupDialog;
import com.eye3.golfpay.dialog.RestaurantsPopupDialog;
import com.eye3.golfpay.fragment.ControlFragment;
import com.eye3.golfpay.fragment.LoginFragment;
import com.eye3.golfpay.listener.ITakePhotoListener;
import com.eye3.golfpay.model.chat.ChatData;
import com.eye3.golfpay.model.chat.LaravelModel;
import com.eye3.golfpay.model.gps.GpsInfo;
import com.eye3.golfpay.model.gps.ResponseCartInfo;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;
import com.eye3.golfpay.service.GpsTracker;
import com.eye3.golfpay.util.DateUtils;
import com.eye3.golfpay.util.Util;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import net.mrbin99.laravelechoandroid.Echo;
import net.mrbin99.laravelechoandroid.EchoCallback;
import net.mrbin99.laravelechoandroid.EchoOptions;
import net.mrbin99.laravelechoandroid.channel.SocketIOPrivateChannel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    public NavigationView navigationView;
    public DrawerLayout drawer_layout;
    TextView gpsTxtView, scoreTxtView, controlTxtView, groupNameTextView;
    TextView reservationPersonNameTextView, roundingTeeUpTimeTextView, inOutTextView00, inOutTextView01;
    LocationManager mLocationManager;
    LinearLayout markView, cancelView;
    LinearLayout ll_login;
    TextView tv_version;
    Echo echo;
    TextView currentTime;
    private int photoType = -1;
    ITakePhotoListener iTakePhotoListener;

    private GpsTracker gpsTracker;
    private PopupDialog popupDialog;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};


    LocationRequest mLocationRequest;
    FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private static final int REQUEST_CHECK_SETTINGS = 33;

    //실제 기록할 gps위치
    double real_latitude = 0.0f;
    double real_longitude = 0.0f;
    int gpsFailCount = 0;

    double hole_lat = 0.0f;
    double hole_lon = 0.0f;

    public int hereToHole = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        systemUIHide();
        setContentView(R.layout.activity_main);
        init();
        hideMainBottomBar();
        requestPermission();
        //startListeningUserLocation();지
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        popupDialog = new PopupDialog(MainActivity.this, R.style.DialogTheme);
        //화면꺼짐 방
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void requestPermission() {
        //permission 추가할경우 new String[]에  android.Manifest.permission.XXX 추가
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CAMERA},
                    0);
        }
    }

    public void initLocation() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        startGPSTimer();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

            }
        });

        result.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

            }
        });

        result.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    //초기에는 무조건 값을 넣는다.
                    if (real_latitude == 0.0f && real_longitude == 0.0f) {
                        real_latitude = latitude;
                        real_longitude = longitude;
                    } else {
                        if (Global.gameTimeStatus != Global.GameStatus.eEnd && Global.gameTimeStatus != Global.GameStatus.eNone) {
                            double distance = getDistance(real_latitude, real_longitude, latitude, longitude, "meter");
                            if (distance < 10 || gpsFailCount > 3) {
                                real_latitude = latitude;
                                real_longitude = longitude;
                                gpsFailCount = 0;
                            } else {
                                gpsFailCount++;
                            }
                        } else {
                            gpsFailCount = 0;
                            real_latitude = latitude;
                            real_longitude = longitude;
                        }
                    }

                    Log.d("locationCallback", String.format("%f, %f", latitude, longitude));
                }
            }
        };

        builder.setAlwaysShow(true);
        startLocationUpdates();
    }

    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        int R = 6371; // km

        double x = (lon2 - lon1) * Math.cos((lat1 + lat2) / 2);
        double y = lat2 - lat1;
        double distance = Math.sqrt(x * x + y * y) * R;

        return distance * 10f;
    }

    private static double getDistance(double lat1, double lon1, double lat2, double lon2, String unit) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "kilometer") {
            dist = dist * 1.609344;
        } else if (unit == "meter") {
            dist = dist * 1609.344;
        }

        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    private void startLocationUpdates() {
        startTimerTask();
        startGPSTimer();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // permission is denined by user, you can show your alert dialog here to send user to App settings to enable permission
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CHECK_SETTINGS);
            }

            return;
        }

        fusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.getMainLooper());
    }

    public void stopLocationUpdates() {
        if (fusedLocationClient != null)
            fusedLocationClient.removeLocationUpdates(locationCallback);

        stopGpsTimerTask();
        stopTimerTask();
    }

    public void openDrawerLayout() {
        if (drawer_layout != null)
            drawer_layout.openDrawer(GravityCompat.END);
    }

    private void closeDrawerLayout() {
        drawer_layout.closeDrawer(GravityCompat.END);
    }

    @SuppressLint("CutPasteId")
    private void init() {
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);

        drawer_layout = findViewById(R.id.drawer_layout);
        openDrawerLayout();
        drawer_layout.setScrimColor(Color.TRANSPARENT);
        //최초 로그인fragment를 호출한다.
        GoNavigationDrawer(new LoginFragment(), null);

        findViewById(R.id.main_bottom_bar).findViewById(R.id.dlgtest1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupDialog dlg = new PopupDialog(MainActivity.this, R.style.DialogTheme);
                WindowManager.LayoutParams wmlp = dlg.getWindow().getAttributes();
                wmlp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                wmlp.x = 0;   //x position
                wmlp.y = 0;   //y position

                // Set alertDialog "not focusable" so nav bar still hiding:
                dlg.getWindow().
                        setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);


                dlg.getWindow().getDecorView().setSystemUiVisibility(Util.DlgUIFalg);

                dlg.show();
            }
        });

        findViewById(R.id.main_bottom_bar).findViewById(R.id.dlgtest2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RestaurantsPopupDialog dlg = new RestaurantsPopupDialog(MainActivity.this, android.R.style.Theme_Holo_Light);
                dlg.getWindow().getDecorView().setSystemUiVisibility(Util.DlgUIFalg);
                dlg.show();
            }
        });


        markView = findViewById(R.id.main_bottom_bar).findViewById(R.id.mark);
        markView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawerLayout();
            }
        });

        currentTime = findViewById(R.id.main_bottom_bar).findViewById(R.id.tv_teeup_time);
        groupNameTextView = findViewById(R.id.groupNameTextView);
        reservationPersonNameTextView = findViewById(R.id.reservationPersonNameTextView);
        roundingTeeUpTimeTextView = findViewById(R.id.teeUpTimeTextView);
        tv_version = findViewById(R.id.tv_version);
        tv_version.setText("version : " + BuildConfig.VERSION_NAME);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        closeDrawerLayout();
        return true;
    }

    // 메뉴 열기
    public void OpenMenuMap() {
        if (mBaseFragment != null) {
            UIThread.executeInUIThread(new Runnable() {
                @Override
                public void run() {
                    openDrawerLayout();
                }
            });
        }
    }

    // 메뉴 닫기
    public void HideMenuMap() {

        if (mBaseFragment != null) {
            UIThread.executeInUIThread(new Runnable() {
                @Override
                public void run() {
                    if (drawer_layout != null)
                        drawer_layout.closeDrawer(GravityCompat.END);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null)
            outState.putInt("selected", Global.selectedTeeUpIndex);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
//        if (savedInstanceState != null)
//            Global.saveIdx = savedInstanceState.getInt("selected");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bitmap bitmap = BitmapFactory.decodeFile(AppDef.imageFilePath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(AppDef.imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }
            Bitmap clubImageBitmap = rotate(bitmap, exifDegree);

            //CaddieViewBasicGuestItem guestItem = (CaddieViewBasicGuestItem) CaddieMainFragment.mGuestViewContainerLinearLayout.getChildAt(traversalByGuestId(AppDef.guestid));
            //setImagewithUri(guestItem.mClubImageView, AppDef.imageFilePath);
            //guestItem.mClubImageView.setImageBitmap(clubImageBitmap);
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_CANCELED) {
        } else if (requestCode == RC_TAKE_PICTURE) {
            if (resultCode == 0)
                return;

            new StoreUriAsTempFileAsyncTask(this, new StoreUriAsTempFileAsyncTask.ICompleteFileSace() {
                @Override
                public void onComplete(String path) {

                    File f2 = new File(path);
                    long size = f2.length();

                    if (iTakePhotoListener != null)
                        iTakePhotoListener.onTakePhoto(path);

                }
            }).execute(mCameraTempUri);
        } else if (requestCode == PopupActivity.Id && resultCode == 100) {
            GoNativeScreen(new ControlFragment(), null);
        }
    }

    void setImagewithUri(ImageView img, String uri) {
        if (img != null) {
            Glide.with(MainActivity.this)
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .placeholder(R.drawable.ic_noimage)
                    .into(img);
        }
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    TimerTask gpsTimerTask;
    Timer gpsTimer = new Timer();

    public void startGPSTimer() {
        gpsTimerTask = new TimerTask() {
            @Override
            public void run() {
                sendGpsInfo(Global.CaddyNo, real_latitude, real_longitude, Global.reserveId);
            }
        };

        int interval = 5000;
//        if (BuildConfig.DEBUG) {
//            interval = 50000;
//        }

        gpsTimer.schedule(gpsTimerTask, 0, interval);
    }

    public void stopGpsTimerTask() {
        if (gpsTimerTask != null) {
            gpsTimerTask.cancel();
            gpsTimerTask = null;
        }
    }

    public void sendGpsInfo(String caddy_num, double lat, double lng, String reserve_id) {

        DataInterface.getInstance().sendGpsInfo(this, caddy_num, lat, lng, reserve_id,
                new DataInterface.ResponseCallback<ResponseData<ResponseCartInfo>>() {

                    @Override
                    public void onSuccess(ResponseData<ResponseCartInfo> response) {

                        try {
                            //글로벌 현재 코스 아이디 저장하기..
                            //CourseFragment 가 아닌 다른 UI에서 사용하기 위함
                            if (response.getResultMessage().equals("token error")) {
                                navigationView.setVisibility(View.VISIBLE);
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("TokenError", true);
                                GoNavigationDrawer(new LoginFragment(), bundle);

                                setPreviousBaseFragment(new LoginFragment());
                                GoRootScreenAdd(null);
                                hideMainBottomBar();
                                stopGpsTimerTask();
                                stopTimerTask();
                                return;
                            }

                            Global.CurrentCourseId = getCurrentCourse(response.getData().nearby_hole_list);
                            if (response.getResultCode().equalsIgnoreCase("ok")) {
                                if (getCourseFragment() != null) {
                                    UIThread.executeInUIThread(new Runnable() {
                                        @Override

                                        public void run() {

                                            hole_lat = response.getData().hole_lat;
                                            hole_lon = response.getData().hole_lon;
                                            if (BuildConfig.DEBUG) {

                                                ResponseCartInfo cartInfo = response.getData();

                                                if (cartInfo.nearby_hole_list.size() == 0) {
                                                    GpsInfo gpsInfoMe = new GpsInfo("나야나나", "me", 40, "118");
                                                    cartInfo.nearby_hole_list.add(gpsInfoMe);
                                                } else
                                                    cartInfo.nearby_hole_list.get(0).setPercent(40);

                                                GpsInfo gpsInfo1 = new GpsInfo("버즈", "back", 10, "56");
                                                GpsInfo gpsInfo2 = new GpsInfo("10cm", "back", 20, "18");
                                                GpsInfo gpsInfo3 = new GpsInfo("소란", "back", 40, "83");
                                                GpsInfo gpsInfo4 = new GpsInfo("고영배", "front", 20, "33");
                                                GpsInfo gpsInfo5 = new GpsInfo("장범준", "front", 50, "20");
                                                GpsInfo gpsInfo6 = new GpsInfo("김태리", "front", 80, "22");
                                                GpsInfo gpsInfo7 = new GpsInfo("최지우", "front", 95, "33");
                                                GpsInfo gpsInfo8 = new GpsInfo("전지현", "same_hole", 10, "117");
                                                GpsInfo gpsInfo9 = new GpsInfo("정유미", "same_hole", 20, "45");
                                                GpsInfo gpsInfo10 = new GpsInfo("송가인", "same_hole", 60, "153");
                                                GpsInfo gpsInfo11 = new GpsInfo("안영미", "same_hole", 80, "9");

//                                                cartInfo.nearby_hole_list.add(gpsInfo1);
//                                                cartInfo.nearby_hole_list.add(gpsInfo2);
//                                                cartInfo.nearby_hole_list.add(gpsInfo3);
//                                                cartInfo.nearby_hole_list.add(gpsInfo4);
//                                                cartInfo.nearby_hole_list.add(gpsInfo5);
//                                                cartInfo.nearby_hole_list.add(gpsInfo6);
//                                                cartInfo.nearby_hole_list.add(gpsInfo7);
//                                                cartInfo.nearby_hole_list.add(gpsInfo8);
//                                                cartInfo.nearby_hole_list.add(gpsInfo9);
//                                                cartInfo.nearby_hole_list.add(gpsInfo10);
//                                                cartInfo.nearby_hole_list.add(gpsInfo11);

                                                try {
                                                    getCourseFragment().updateCourse(cartInfo);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                try {
                                                    getCourseFragment().updateCourse(response.getData());
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            //Toast.makeText(MainActivity.this, "gps 보내다 죽었는데?", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ResponseData<ResponseCartInfo> response) {
                        //Toast.makeText(getBaseContext(), "정보를 받아오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        //Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getCurrentCourse(List<GpsInfo> gpsInfoList) {

        if (Global.courseInfoList == null)
            return null;

        if (gpsInfoList.size() == 0)
            return null;

        for (GpsInfo gpsInfo : gpsInfoList) {
            if (gpsInfo.getGubun().equalsIgnoreCase("me")) {

                for (int i = 0; i < Global.courseInfoList.size(); i++) {
                    if (Global.courseInfoList.get(i).ctype.equalsIgnoreCase(gpsInfo.getCtype())) {
                        return Global.courseInfoList.get(i).id;
                    }
                }
            }
        }

        return null;
    }

    public void setLaravel() {
        EchoOptions options = new EchoOptions();

        // Setup host of your Laravel Echo Server
        options.host = Global.HOST_LARAVEL_ADDRESS;
        /*
         * Add headers for authorizing your users (private and presence channels).
         * This line can change matching how you have configured
         * your guards on your Laravel application
         */
        //options.headers.put("Authorization", "Bearer {token}");

        // Create the client
        echo = new Echo(options);
        echo.connect(new EchoCallback() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "Laravel connect");
            }
        }, new EchoCallback() {
            @Override
            public void call(Object... args) {

            }
        });

        SocketIOPrivateChannel privateChannel = echo.privateChannel("Monitorchat");
        privateChannel.listen("Monitorchat", new EchoCallback() {
            @Override
            public void call(Object... args) {
                // Event thrown.
            }
        });

        echo.channel("Monitorchat")
                .listen("Monitorchat", new EchoCallback() {
                    @Override
                    public void call(Object... args) {
                        Gson gson = new Gson();
                        String json = gson.toJson(args[1]);
                        LaravelModel laravelModel = (LaravelModel) gson.fromJson(json, LaravelModel.class);
                        receiveChatMessage(laravelModel);
                    }
                });
    }

    private void receiveChatMessage(LaravelModel laravelModel) {

        UIThread.executeInUIThread(new Runnable() {
            @Override
            public void run() {

                if (laravelModel.nameValuePairs == null)
                    return;

                try {
                    ChatData chatData = laravelModel.nameValuePairs;

                    if (mBaseFragment.TAG.equals("LoginFragment") || mBaseFragment.TAG.equals("TeeUpFragment") || mBaseFragment.TAG.equals("MainWorkFragment")) {
                        return;
                    }
                    if (mBaseFragment.TAG.equals("ControlFragment")) {
                        if (isShowChat(chatData)) {
                            ((ControlFragment) mBaseFragment).receiveMessage(chatData.sender_name, chatData.message, chatData.timestamp);
                        }
                        return;
                    }

                    if (isShowChat(chatData)) {
                        long now = System.currentTimeMillis();
                        Date mDate = new Date(now);
                        SimpleDateFormat simpleDate = new SimpleDateFormat("a hh:mm", Locale.US);
                        String getTime = simpleDate.format(mDate);
                        showMessagePopupActivity(chatData.message, chatData.sender_name, getTime);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean isShowChat(ChatData chatData) {

        //내가 보낸건 안받음
        if (chatData.sender_id.equals(Global.CaddyNo))
            return false;

        if (chatData.type.equals("all")) {
            return true;
        }

        if (chatData.type.equals("caddy") && chatData.receiver_id.equals(Global.CaddyNo)) {
            return true;
        }

        if (chatData.type.equals("group") && chatData.group_id.equals(Global.selectedReservation.getGroupId())) {
            return true;
        }

        if (chatData.type.equals("course") && chatData.course_id.equals(Global.CurrentCourseId)) {

            return true;
        }


        return false;
    }

    private void showMessagePopupActivity(String message, String sender, String time) {
        Intent intent = new Intent(this, PopupActivity.class);
        intent.putExtra("message", message);
        intent.putExtra("sender", sender);
        intent.putExtra("time", time);
        startActivityForResult(intent, PopupActivity.Id);
    }

    private void showMessagePopup(String message, String sender, String time) {

        if (popupDialog.isShowing()) {
            popupDialog.dismiss();
        }

        popupDialog.setListener(new PopupDialog.IListenerDialogTouch() {
            @Override
            public void onTouch() {
                GoNativeScreen(new ControlFragment(), null);
            }
        });

        WindowManager.LayoutParams wmlp = popupDialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        wmlp.x = 0;   //x position
        wmlp.y = 0;   //y position

//        dlg.getWindow().
//                setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        popupDialog.getWindow().getDecorView().setSystemUiVisibility(Util.DlgUIFalg);
        popupDialog.show();

        popupDialog.setData(message, sender, time);
    }

    public void updateUI() {

        try {
            currentTime.setText(DateUtils.removeSecondFromTimeString(Global.selectedReservation.getTeeoff()));

            TextView course = findViewById(R.id.main_bottom_bar).findViewById(R.id.tv_course);
            course.setText(" / " + Global.selectedReservation.getInoutCourse() + " 코스");

            TextView name = findViewById(R.id.main_bottom_bar).findViewById(R.id.tv_name);
            name.setText(Global.selectedReservation.getGuestName());

            TextView group = findViewById(R.id.main_bottom_bar).findViewById(R.id.tv_teeup_group);
            String groupName = Global.selectedReservation.getGroup();
            group.setText(groupName);
            if (groupName == null || groupName.isEmpty()) {
                findViewById(R.id.main_bottom_bar).findViewById(R.id.iv_ball).setVisibility(View.GONE);
            }

            //네트워크 사용량을 줄이기 위한 로고 로컬에서 사용하기
//            ImageView logo = findViewById(R.id.iv_logo);
//            Glide.with(this)
//                    .load(Global.HOST_BASE_ADDRESS_AWS + Global.tabletLogo)
//                    .into(logo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Uri mCameraTempUri = null;
    public static final int RC_TAKE_PICTURE = 3333;

    public void startCamera(int photoType, ITakePhotoListener listener) {
        iTakePhotoListener = listener;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {

            try {
                ContentValues values = new ContentValues(1);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
                mCameraTempUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraTempUri);
                this.photoType = photoType;
                startActivityForResult(intent, RC_TAKE_PICTURE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    int start = 0;

    public void startListeningUserLocationDebug() {

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                real_latitude = latlng[start][0];
                real_longitude = latlng[start][1];
                sendGpsInfo(Global.CaddyNo, real_latitude, real_longitude, Global.reserveId);
                start++;
                startListeningUserLocationDebug();
            }
        }.start();
    }


    TimerTask timerTask;
    Timer timer = new Timer();
    IGameTimerListener iGameTimerListener;

    public interface IGameTimerListener {
        void onGameTimer();
    }

    public void startTimerTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {

                if (Global.gameTimeStatus != Global.GameStatus.eEnd && Global.gameTimeStatus != Global.GameStatus.eNone)
                    Global.gameSec++;
                if (Global.gameTimeStatus == Global.GameStatus.eBefore)
                    Global.gameBeforeSec++;
                if (Global.gameTimeStatus == Global.GameStatus.eAfter)
                    Global.gameAfterSec++;

                if (iGameTimerListener != null)
                    iGameTimerListener.onGameTimer();

                //자정체크
                Calendar rightNow = Calendar.getInstance();
                int hour = rightNow.get(Calendar.HOUR);
                int min = rightNow.get(Calendar.MINUTE);
                if (hour == 0 && min == 0) {
                    logout();
                }

                hereToHole = (int) getDistance(hole_lat, hole_lon, real_latitude, real_longitude, "meter");
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    public void setGameTimerListener(IGameTimerListener listener) {
        iGameTimerListener = listener;
    }

    public void stopTimerTask() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    public void logout() {
        navigationView.setVisibility(View.VISIBLE);
        closeFragment();
        GoNavigationDrawer(new LoginFragment(), null);
        setPreviousBaseFragment(new LoginFragment());
        GoRootScreenAdd(null);
        hideMainBottomBar();
        stopLocationUpdates();
        stopTimerTask();
    }

    double[][] latlng = {
            {37.0812759, 127.5717819},
            {37.0812759, 127.5717819},
            {37.081276, 127.571782},
            {37.081276, 127.5717821},
            {37.081276, 127.5717821},
            {37.0812761, 127.5717821},
            {37.081276, 127.571782},
            {37.081276, 127.571782},
            {37.0813336, 127.5716926},
            {37.0813486, 127.5716714},
            {37.0813486, 127.5716714},
            {37.0813482, 127.571671},
            {37.0813544, 127.5716603},
            {37.0813544, 127.5716603},
            {37.0813576, 127.5716576},
            {37.0813677, 127.5716407},
            {37.0813677, 127.5716407},
            {37.0813839, 127.5716113},
            {37.0814147, 127.5715507},
            {37.0814147, 127.5715507},
            {37.0814413, 127.5715154},
            {37.081499, 127.5714509},
            {37.081499, 127.5714509},
            {37.0815417, 127.5714121},
            {37.0816637, 127.5713711},
            {37.0816637, 127.5713711},
            {37.081711, 127.5713689},
            {37.0817917, 127.5713226},
            {37.0817917, 127.5713226},
            {37.0818517, 127.5712322},
            {37.0818566, 127.5711299},
            {37.0818566, 127.5711299},
            {37.0818244, 127.5710375},
            {37.081795, 127.5709015},
            {37.081795, 127.5709015},
            {37.0818066, 127.5708187},
            {37.0818573, 127.5707427},
            {37.0818573, 127.5707427},
            {37.0818932, 127.5707104},
            {37.0819504, 127.5706433},
            {37.0819504, 127.5706433},
            {37.081991, 127.5705813},
            {37.0820564, 127.5704783},
            {37.0820564, 127.5704783},
            {37.0821288, 127.5703803},
            {37.0822414, 127.5702789},
            {37.0822414, 127.5702789},
            {37.0823218, 127.5702153},
            {37.082425, 127.5701181},
            {37.082425, 127.5701181},
            {37.0825768, 127.5699292},
            {37.0825768, 127.5699292},
            {37.0826563, 127.5698697},
            {37.0827545, 127.5698422},
            {37.0827545, 127.5698422},
            {37.0828364, 127.5698451},
            {37.0829679, 127.5698869},
            {37.0829679, 127.5698869},
            {37.0830521, 127.5699304},
            {37.083181, 127.5699891},
            {37.083181, 127.5699891},
            {37.0832723, 127.5699981},
            {37.0834115, 127.5700174},
            {37.0834115, 127.5700174},
            {37.0835099, 127.5700314},
            {37.0836509, 127.5700596},
            {37.0836509, 127.5700596},
            {37.0837429, 127.5700493},
            {37.0838741, 127.5699629},
            {37.0838741, 127.5699629},
            {37.0839551, 127.5698881},
            {37.084077, 127.5697843},
            {37.084077, 127.5697843},
            {37.0841656, 127.5697263},
            {37.084313, 127.5696311},
            {37.084313, 127.5696311},
            {37.0844117, 127.5695734},
            {37.0845503, 127.5695071},
            {37.0845503, 127.5695071},
            {37.084647, 127.5694678},
            {37.0847892, 127.5694269},
            {37.0847892, 127.5694269},
            {37.0848793, 127.569436},
            {37.0850289, 127.5694311},
            {37.0850289, 127.5694311},
            {37.0851241, 127.5693791},
            {37.085258, 127.5693079},
            {37.085258, 127.5693079},
            {37.0853478, 127.5692817},
            {37.0854889, 127.5693027},
            {37.0854889, 127.5693027},
            {37.0855703, 127.5693576},
            {37.0856533, 127.5695023},
            {37.0856533, 127.5695023},
            {37.0856744, 127.5696324},
            {37.0856573, 127.569825},
            {37.0856573, 127.569825},
            {37.0856673, 127.5699479},
            {37.0857461, 127.5700733},
            {37.0858079, 127.5701045},
            {37.0859625, 127.5700965},
            {37.0859625, 127.5700965},
            {37.0860241, 127.570078},
            {37.0861147, 127.5701113},
            {37.0861147, 127.5701113},
            {37.0862304, 127.5701491},
            {37.0862827, 127.5702009},
            {37.0862827, 127.5702009},
            {37.0862926, 127.5702634},
            {37.0862645, 127.5703655},
            {37.0862645, 127.5703655},
            {37.0862515, 127.5704187},
            {37.0862561, 127.5705021},
            {37.0862561, 127.5705021},
            {37.0862954, 127.5705481},
            {37.086388, 127.5705506},
            {37.086388, 127.5705506},
            {37.0864394, 127.5704976},
            {37.0864935, 127.5703933},
            {37.0864935, 127.5703933},
            {37.0865559, 127.5703573},
            {37.086642, 127.5703995},
            {37.086642, 127.5703995},
            {37.0866716, 127.5704333},
            {37.0868487, 127.5706192},
            {37.0868635, 127.5707089},
            {37.0868635, 127.5707089},
            {37.0868339, 127.5707929},
            {37.086814, 127.5709418},
            {37.0868376, 127.5710613},
            {37.0868812, 127.5712356},
            {37.0868812, 127.5712356},
            {37.08692, 127.5713537},
            {37.0870072, 127.5714921},
            {37.0870072, 127.5714921},
            {37.0870801, 127.5715674},
            {37.0872081, 127.5716647},
            {37.0872081, 127.5716647},
            {37.0872821, 127.5717532},
            {37.0873537, 127.5719066},
            {37.0873537, 127.5719066},
            {37.0874, 127.5720147},
            {37.0874867, 127.5721597},
            {37.0874867, 127.5721597},
            {37.0875602, 127.5722471},
            {37.0876929, 127.5723489},
            {37.0876929, 127.5723489},
            {37.0877755, 127.5724242},
            {37.0878668, 127.5725671},
            {37.0878668, 127.5725671},
            {37.087907, 127.572675},
            {37.0879421, 127.5728511},
            {37.0879421, 127.5728511},
            {37.0879902, 127.5729611},
            {37.0880972, 127.5730893},
            {37.0880972, 127.5730893},
            {37.0881729, 127.5731644},
            {37.0882844, 127.5732935},
            {37.0882844, 127.5732935},
            {37.0883437, 127.5733859},
            {37.0884385, 127.5735276},
            {37.0884385, 127.5735276},
            {37.0885037, 127.5736066},
            {37.0886241, 127.5736863},
            {37.0886241, 127.5736863},
            {37.0887114, 127.5737239},
            {37.0888479, 127.5737966},
            {37.0888479, 127.5737966},
            {37.0889314, 127.5738541},
            {37.0890592, 127.5739245},
            {37.0890592, 127.5739245},
            {37.0891469, 127.5739615},
            {37.0892871, 127.5739825},
            {37.0892871, 127.5739825},
            {37.0893275, 127.573983},
            {37.08948, 127.5737321},
            {37.0895234, 127.5735662},
            {37.0895234, 127.5735662},
            {37.0895491, 127.5734602},
            {37.0896066, 127.5733029},
            {37.0896066, 127.5733029},
            {37.0896588, 127.5732046},
            {37.0897438, 127.57306},
            {37.0897438, 127.57306},
            {37.0898013, 127.5729677},
            {37.0899187, 127.5728579},
            {37.0899187, 127.5728579},
            {37.0900069, 127.5728006},
            {37.0901217, 127.5727104},
            {37.0901217, 127.5727104},
            {37.0901871, 127.5726155},
            {37.0902784, 127.5724692},
            {37.0902784, 127.5724692},
            {37.0903591, 127.5723974},
            {37.0904804, 127.5722986},
            {37.0904804, 127.5722986},
            {37.0905416, 127.5722043},
            {37.0906198, 127.5720673},
            {37.0906198, 127.5720673},
            {37.0906339, 127.5719774},
            {37.090557, 127.5718521},
            {37.090557, 127.5718521},
            {37.0904809, 127.5717628},
            {37.0903624, 127.5716276},
            {37.0903036, 127.5715308},
            {37.0902118, 127.5713794},
            {37.0902118, 127.5713794},
            {37.090163, 127.5712682},
            {37.0901012, 127.5710968},
            {37.0901012, 127.5710968},
            {37.0900605, 127.5709878},
            {37.0900158, 127.5708171},
            {37.0900158, 127.5708171},
            {37.0899922, 127.5706991},
            {37.0899639, 127.5705186},
            {37.0899639, 127.5705186},
            {37.0899369, 127.5703992},
            {37.0899072, 127.5702235},
            {37.0899072, 127.5702235},
            {37.0898814, 127.5701133},
            {37.0898366, 127.5699408},
            {37.0898366, 127.5699408},
            {37.0898103, 127.5698166},
            {37.0897755, 127.5696449},
            {37.0897755, 127.5696449},
            {37.089745, 127.5695499},
            {37.0896597, 127.5694275},
            {37.0896597, 127.5694275},
            {37.089647, 127.5694012},
            {37.0896326, 127.5693762},
            {37.0896326, 127.5693762},
            {37.0896176, 127.5693658},
            {37.0895401, 127.5693444},
            {37.0895401, 127.5693444},
            {37.0894546, 127.5693217},
            {37.0893101, 127.5692644},
            {37.0893101, 127.5692644},
            {37.0892222, 127.5692101},
            {37.0890795, 127.5692106},
            {37.0890795, 127.5692106},
            {37.0889856, 127.5692398},
            {37.0888298, 127.5692634},
            {37.0888298, 127.5692634},
            {37.0887419, 127.5692811},
            {37.0885986, 127.5692664},
            {37.0885986, 127.5692664},
            {37.0885144, 127.5692009},
            {37.0883966, 127.5690948},
            {37.0883966, 127.5690948},
            {37.0883163, 127.5690326},
            {37.088175, 127.5689838},
            {37.088175, 127.5689838},
            {37.0880781, 127.5690102},
            {37.0879541, 127.5690517},
            {37.0879541, 127.5690517},
            {37.0878579, 127.569071},
            {37.0877272, 127.5691302},
            {37.0877272, 127.5691302},
            {37.0876362, 127.5691768},
            {37.0875465, 127.5692503},
            {37.0875465, 127.5692503},
            {37.0875293, 127.5692848},
            {37.0875227, 127.5693305},
            {37.0875227, 127.5693305},
            {37.0875227, 127.5693827},
            {37.0875122, 127.56945},
            {37.0875122, 127.56945},
            {37.0874807, 127.5695034},
            {37.0874158, 127.5695603},
            {37.0874158, 127.5695603},
            {37.0873645, 127.569583},
            {37.0872615, 127.5695357},
            {37.0872615, 127.5695357},
            {37.087198, 127.5694769},
            {37.0870726, 127.5694662},
            {37.0870726, 127.5694662},
            {37.0869772, 127.5694594},
            {37.0868396, 127.5693788},
            {37.0868396, 127.5693788},
            {37.0867615, 127.5693162},
            {37.0866819, 127.5692882},
            {37.0866819, 127.5692882},
            {37.086674, 127.5693365},
            {37.0867224, 127.5694222},
            {37.0867224, 127.5694222},
            {37.0867243, 127.5694742},
            {37.0866796, 127.5695162},
            {37.0866796, 127.5695162},
            {37.0866288, 127.5695006},
            {37.0865232, 127.5693899},
            {37.0865232, 127.5693899},
            {37.086426, 127.5693418},
            {37.0862887, 127.5693956},
            {37.0862887, 127.5693956},
            {37.0862165, 127.569484},
            {37.0861521, 127.569645},
            {37.0861521, 127.569645},
            {37.0861177, 127.5697546},
            {37.086073, 127.5699004},
            {37.086073, 127.5699004},
            {37.0860414, 127.5699694},
            {37.0859938, 127.5700488},
            {37.0859938, 127.5700488},
            {37.0859463, 127.5701094},
            {37.0858738, 127.5702384},
            {37.0858738, 127.5702384},
            {37.0858198, 127.5703165},
            {37.0857033, 127.5703826},
            {37.0857033, 127.5703826},
            {37.0856172, 127.5704172},
            {37.0854936, 127.5704767},
            {37.0854936, 127.5704767},
            {37.0854291, 127.5705264},
            {37.0853343, 127.5706085},
            {37.0853343, 127.5706085},
            {37.0852744, 127.5706535},
            {37.0851778, 127.5707194},
            {37.0851778, 127.5707194},
            {37.0851115, 127.5707504},
            {37.0849989, 127.5707932},
            {37.0849253, 127.5708096},
            {37.0848317, 127.5707842},
            {37.0848317, 127.5707842},
            {37.0847811, 127.5707035},
            {37.0847351, 127.5705998},
            {37.0847351, 127.5705998},
            {37.0846787, 127.57059},
            {37.0845685, 127.57063},
            {37.0845685, 127.57063},
            {37.0844956, 127.5706876},
            {37.0844313, 127.5708108},
            {37.0844313, 127.5708108},
            {37.0844096, 127.5709414},
            {37.08435, 127.5711377},
            {37.08435, 127.5711377},
            {37.0842762, 127.5712268},
            {37.0841389, 127.5713149},
            {37.0841389, 127.5713149},
            {37.0840497, 127.5713738},
            {37.0839181, 127.5714602},
            {37.0839181, 127.5714602},
            {37.083826, 127.5715041},
            {37.0836996, 127.5715901},
            {37.0836996, 127.5715901},
            {37.0836292, 127.5716682},
            {37.0835224, 127.5717817},
            {37.0835224, 127.5717817},
            {37.0834482, 127.5718627},
            {37.0833232, 127.5719571},
            {37.0833232, 127.5719571},
            {37.0832336, 127.5719902},
            {37.0830922, 127.5720235},
            {37.0830922, 127.5720235},
            {37.0830047, 127.5720432},
            {37.0828783, 127.5720788},
            {37.0828783, 127.5720788},
            {37.0827897, 127.5721167},
            {37.0826612, 127.5721737},
            {37.0826612, 127.5721737},
            {37.0826213, 127.5721827},
            {37.0823411, 127.5722741},
            {37.0822234, 127.5723733},
            {37.0822234, 127.5723733},
            {37.0821512, 127.5724467},
            {37.0820198, 127.5725297},
            {37.0820198, 127.5725297},
            {37.0819249, 127.5725458},
            {37.0817961, 127.5725593},
            {37.0817961, 127.5725593},
            {37.0817095, 127.5726081},
            {37.0815994, 127.5727186},
            {37.0815994, 127.5727186},
            {37.0815138, 127.5727774},
            {37.0814147, 127.5728021},
            {37.0813585, 127.5728078},
            {37.0812807, 127.5728946},
            {37.081231, 127.5729745},
            {37.0811619, 127.5730476},
            {37.0811619, 127.5730476},
            {37.0811102, 127.5730582},
            {37.081062, 127.5730049},
            {37.081062, 127.5730049},
            {37.0810546, 127.572983},
            {37.0810546, 127.572983},
            {37.081027, 127.5729054},
            {37.081027, 127.5729054},
            {37.0810056, 127.5728695},
            {37.0810056, 127.5728695},
            {37.0809945, 127.5728514},
            {37.0809945, 127.5728514},
            {37.0809945, 127.5728514}
    };
}