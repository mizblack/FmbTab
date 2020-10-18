package com.eye3.golfpay.fmb_tab.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.exifinterface.media.ExifInterface;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.AppDef;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.common.UIThread;
import com.eye3.golfpay.fmb_tab.dialog.PopupDialog;
import com.eye3.golfpay.fmb_tab.dialog.RestaurantsPopupDialog;
import com.eye3.golfpay.fmb_tab.fragment.CaddieMainFragment;
import com.eye3.golfpay.fmb_tab.fragment.ControlFragment;
import com.eye3.golfpay.fmb_tab.fragment.CourseFragment;
import com.eye3.golfpay.fmb_tab.fragment.LoginFragment;
import com.eye3.golfpay.fmb_tab.fragment.ScoreFragment;
import com.eye3.golfpay.fmb_tab.model.chat.ChatData;
import com.eye3.golfpay.fmb_tab.model.chat.LaravelModel;
import com.eye3.golfpay.fmb_tab.model.gps.GpsInfo;
import com.eye3.golfpay.fmb_tab.model.guest.CaddieInfo;
import com.eye3.golfpay.fmb_tab.model.guest.ClubInfo;
import com.eye3.golfpay.fmb_tab.model.guest.GuestInfo;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.net.ResponseData;
import com.eye3.golfpay.fmb_tab.service.CartLocationService;
import com.eye3.golfpay.fmb_tab.util.Util;
import com.eye3.golfpay.fmb_tab.view.CaddieViewBasicGuestItem;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import net.mrbin99.laravelechoandroid.Echo;
import net.mrbin99.laravelechoandroid.EchoCallback;
import net.mrbin99.laravelechoandroid.EchoOptions;
import net.mrbin99.laravelechoandroid.channel.SocketIOPrivateChannel;

import java.io.IOException;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    NavigationView navigationView;
    public DrawerLayout drawer_layout;
    TextView gpsTxtView, scoreTxtView, controlTxtView, groupNameTextView;
    TextView reservationPersonNameTextView, roundingTeeUpTimeTextView, inOutTextView00, inOutTextView01;
    LocationManager mLocationManager;
    ImageView markView, cancelView;
    LinearLayout ll_login;
    Echo echo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        CaddieInfo ca = new CaddieInfo();

        ca.setCaddie_id("1");
        ca.setTeamMemo("team memo");
        ca.getTeamPhotos().add("team_photo1");
        ca.getTeamPhotos().add("team_photo2");
        ca.getTeamPhotos().add("team_photo3");

        GuestInfo gi = new GuestInfo();
        gi.setReserveGuestId("guest id");
        gi.setCarNo("car number");
        gi.setGuestMemo("guest memo");
        gi.setHp("phone number");

        ClubInfo ci = new ClubInfo();
        ci.cover.add("2");
        ci.iron.add("0");
        ci.iron.add("1");
        ci.iron.add("2");
        ci.putter.add("2");
        ci.putter_cover.add("2");
        ci.utility.add("2");
        ci.wedge.add("Pw");
        ci.wedge.add("Sw");
        ci.wedge.add("PSw");
        ci.wood_cover.add("2");
        ci.wood.add("7");
        ci.wood.add("8");
        gi.setClubInfo(ci);

        ca.getGuestInfo().add(gi);
//        ca.getGuestInfo().add(gi);
//        ca.getGuestInfo().add(gi);
//        ca.getGuestInfo().add(gi);

        Gson gson = new Gson();
        String json = gson.toJson(ca);
        Log.d("json", json);

        systemUIHide();
        setContentView(R.layout.activity_main);
        init();
        hideMainBottomBar();
        requestPermission();
        startLocationService();
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);

        setLaravel();

        //sendGpsInfo("138", 37.2113911, 127.5687663, 6722);
    }

    private void requestPermission() {
        //permission 추가할경우 new String[]에  android.Manifest.permission.XXX 추가
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CAMERA},
                    0);
        }

    }

    private void startLocationService() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(getApplicationContext(), CartLocationService.class));
        } else {
            startService(new Intent(getApplicationContext(), CartLocationService.class));
        }
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

        gpsTxtView = findViewById(R.id.main_bottom_bar).findViewById(R.id.gpsTextView);
        gpsTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseFragment courseFragment = new CourseFragment();
                GoNativeScreen(courseFragment, null);
                Global.courseFragment = courseFragment;
                closeDrawerLayout();
            }
        });

        scoreTxtView = findViewById(R.id.main_bottom_bar).findViewById(R.id.scoreTextView);
        scoreTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new ScoreFragment(), null);
                closeDrawerLayout();
            }
        });

        controlTxtView = findViewById(R.id.main_bottom_bar).findViewById(R.id.controlTextView);
        controlTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new ControlFragment(), null);
                closeDrawerLayout();
            }
        });

        markView = findViewById(R.id.main_bottom_bar).findViewById(R.id.mark);
        markView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawerLayout();
            }
        });

        groupNameTextView = findViewById(R.id.groupNameTextView);
        reservationPersonNameTextView = findViewById(R.id.reservationPersonNameTextView);
        roundingTeeUpTimeTextView = findViewById(R.id.teeUpTimeTextView);
        inOutTextView00 = findViewById(R.id.inOutTextView00);
        inOutTextView01 = findViewById(R.id.inOutTextView01);

//        findViewById(R.id.content_main_inc).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                closeKeyboard(findViewById(R.id.nameEditText));
//                closeKeyboard(findViewById(R.id.phoneNumberEditText));
//            }
//        });
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
        // systemUIHide();
        //  drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
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

            CaddieViewBasicGuestItem guestItem = (CaddieViewBasicGuestItem) CaddieMainFragment.mGuestViewContainerLinearLayout.getChildAt(traversalByGuestId(AppDef.guestid));
            setImagewithUri(guestItem.mClubImageView, AppDef.imageFilePath);
            guestItem.mClubImageView.setImageBitmap(clubImageBitmap);
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_CANCELED) {
            ;
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

    private int traversalByGuestId(String guestId) {
        int index = 0;
        for (int i = 0; i < Global.guestList.size(); i++) {
            if (guestId.equals(Global.guestList.get(i).getId())) {
                index = i;
            }
        }
        return index;
    }


    private int LOCATION_REFRESH_DISTANCE = 3; // 30 meters. The Minimum Distance to be changed to get location update
    private int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    private int interval = 3;

    private void startListeningUserLocation() {

// check for permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, interval, LOCATION_REFRESH_DISTANCE, locationListener);

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // permission is denined by user, you can show your alert dialog here to send user to App settings to enable permission
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    private void stopListeningUserLocation() {
        mLocationManager.removeUpdates(locationListener);
    }

    //define the listener
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    public void sendGpsInfo(String caddy_num, double lat, double lng, int reserve_id) {
        showProgress("게스트의 정보를 받아오는 중입니다....");
        DataInterface.getInstance(Global.HOST_ADDRESS_DEV_AWS).sendGpsInfo(this, caddy_num, lat, lng, reserve_id,
                new DataInterface.ResponseCallback<ResponseData<GpsInfo>>() {

                    @Override
                    public void onSuccess(ResponseData<GpsInfo> response) {

                        if (response.getResultMessage().equals("성공")) {
                            GpsInfo gpsInfo = response.getData();
                            hideProgress();
                        }
                    }

                    @Override
                    public void onError(ResponseData<GpsInfo> response) {
                        Toast.makeText(getBaseContext(), "정보를 받아오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        hideProgress();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        hideProgress();
                    }
                });
    }

    private void setLaravel() {
        EchoOptions options = new EchoOptions();

        // Setup host of your Laravel Echo Server
        options.host = "http://deverp.golfpay.co.kr:6001";

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


        SocketIOPrivateChannel privateChannel = echo.privateChannel("public-event");
        privateChannel.listen("PublicEvent", new EchoCallback() {
            @Override
            public void call(Object... args) {
                // Event thrown.
            }
        });

        echo.channel("public-event")
                .listen("PublicEvent", new EchoCallback() {
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

                ChatData chatData = laravelModel.nameValuePairs.event.nameValuePairs;
                if (chatData.mode.equals("chat")) {

                    if (mBaseFragment.TAG.equals("ControlFragment") == true) {
                        ((ControlFragment) mBaseFragment).receiveMessage(chatData.member_id, chatData.message);
                        return;
                    }

                    showMessagePopup();
                }
            }
        });
    }

    private void showMessagePopup() {
        PopupDialog dlg = new PopupDialog(MainActivity.this, R.style.DialogTheme);
        dlg.setListener(new PopupDialog.IListenerDialogTouch() {
            @Override
            public void onTouch() {
                GoNativeScreen(new ControlFragment(), null);
            }
        });

        WindowManager.LayoutParams wmlp = dlg.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        wmlp.x = 0;   //x position
        wmlp.y = 0;   //y position

        dlg.getWindow().
                setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);


        dlg.getWindow().getDecorView().setSystemUiVisibility(Util.DlgUIFalg);
        dlg.show();
    }
}

