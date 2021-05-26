package com.eye3.golfpay.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
import androidx.fragment.app.Fragment;

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
import com.eye3.golfpay.fragment.CourseFragment;
import com.eye3.golfpay.fragment.LoginFragment;
import com.eye3.golfpay.listener.ITakePhotoListener;
import com.eye3.golfpay.model.chat.ChatData;
import com.eye3.golfpay.model.chat.LaravelModel;
import com.eye3.golfpay.model.field.Course;
import com.eye3.golfpay.model.gps.GpsInfo;
import com.eye3.golfpay.model.gps.ResponseCartInfo;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;
import com.eye3.golfpay.service.CartLocationService;
import com.eye3.golfpay.service.GpsTracker;
import com.eye3.golfpay.util.BitmapUtils;
import com.eye3.golfpay.util.DateUtils;
import com.eye3.golfpay.util.Util;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import net.mrbin99.laravelechoandroid.Echo;
import net.mrbin99.laravelechoandroid.EchoCallback;
import net.mrbin99.laravelechoandroid.EchoOptions;
import net.mrbin99.laravelechoandroid.channel.SocketIOPrivateChannel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        startTimerTask();
    }

    private void requestPermission() {
        //permission 추가할경우 new String[]에  android.Manifest.permission.XXX 추가
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CAMERA},
                    0);
        }
    }

    public void startLocationService() {

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


    private final int LOCATION_REFRESH_DISTANCE = 1; // 30 meters. The Minimum Distance to be changed to get location update
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    private final int interval = 3;

    public void startListeningUserLocation() {

        // check for permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, interval, LOCATION_REFRESH_DISTANCE, locationListener);

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

    public void startListeningUserLocation2() {
        gpsTracker = new GpsTracker(MainActivity.this);
        startGPSTimer();
    }

    TimerTask gpsTimerTask;
    Timer gpsTimer = new Timer();
    public void startGPSTimer() {
        gpsTimerTask = new TimerTask() {

            @Override
            public void run() {
                gpsTracker.getLocation();
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();

//                UIThread.executeInUIThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(MainActivity.this, "현재위치 \n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_LONG).show();
//                    }
//                });

                sendGpsInfo(Global.CaddyNo, latitude, longitude, Global.reserveId);
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

    private void stopListeningUserLocation() {
        mLocationManager.removeUpdates(locationListener);
    }

    //define the listener
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            sendGpsInfo(Global.CaddyNo, location.getLatitude(), location.getLongitude(), Global.reserveId);
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
                                            getCourseFragment().updateCourse(response.getData());
                                        }
                                    });
                                }
                            }
                        } catch(NullPointerException e) {
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
                        showMessagePopup(chatData.message, chatData.sender_name, getTime);
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

            ImageView logo = findViewById(R.id.iv_logo);
            Glide.with(this)
                    .load(Global.HOST_BASE_ADDRESS_AWS + Global.tabletLogo)
                    .into(logo);

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

                double lat = latlng[start][0];
                double lng = latlng[start][1];
                sendGpsInfo(Global.CaddyNo, lat, lng, Global.reserveId);
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

    double[][] latlng = {
//            {37.079644296696,	127.572275330223},
//            {37.079579011896,	127.572314541376},
//            {37.079497331103,	127.572372431790},
//            {37.079392158309,	127.572423104189},
//            {37.079274529237,	127.572448080689},
//            {37.079163445571,	127.572399290735},
//            {37.079141617254,	127.572439778138},
//            {37.079167483122,	127.572397160379},
//            {37.079110809174,	127.572401882745},
//            {37.078993457689,	127.572441037133},
//            {37.078865034330,	127.572497160164},
//            {37.078771156885,	127.572542651373},
//            {37.078739182854,	127.572569676578},
//            {37.078732259908,	127.572583632473},
//            {37.078718739027,	127.572605716807},
//            {37.078713938871,	127.572617826898},
//            {37.078700248896,	127.572620859572},
//            {37.078635790592,	127.572624720186},
//            {37.078606298624,	127.572623526274},
//            {37.078474660601,	127.572617284907},
//            {37.078332070464,	127.572603745473},
//            {37.078207145230,	127.572592243408},
//            {37.078100703038,	127.572596575211},
//            {37.078021302230,	127.572601022082},
//            {37.077939540399,	127.572600158736},
//            {37.077892324455,	127.572591930565},
//            {37.077851740185,	127.572597121822},
//            {37.077784723539,	127.572610260249},
//            {37.077696837478,	127.572626964528},
//            {37.077594714492,	127.572647502864},
//            {37.077541625430,	127.572654636357},
//            {37.077472909866,	127.572681980745},
//            {37.077388069492,	127.572718847227},
//            {37.077279646683,	127.572792321251},
//            {37.077179843315,	127.572872096190},
//            {37.077096650906,	127.573010508316},
//            {37.077057175252,	127.573139259428},
//            {37.077013921926,	127.573284943914},
//            {37.076959987685,	127.573420282986},
//            {37.076877002382,	127.573524624521},
//            {37.076781294369,	127.573589360081},
//            {37.076711170695,	127.573620344913},
//            {37.076710153056,	127.573608927650},
//            {37.076709517899,	127.573590472359},
//            {37.076719799238,	127.573581294857},
//            {37.076731016592,	127.573569886743},
//            {37.076719019202,	127.573576884106},
//            {37.076648586251,	127.573607994731},
//            {37.076572289820,	127.573651199442},
//            {37.076470002530,	127.573722322232},
//            {37.076346719038,	127.573834749832},
//            {37.076273362898,	127.573965394101},
//            {37.076185303278,	127.574061545806},
//            {37.076063624798,	127.574085715096},
//            {37.075925061212,	127.574107540068},
//            {37.075765349828,	127.574145816110},
//            {37.075616637151,	127.574170325034},
//            {37.075486634607,	127.574165889750},
//            {37.075435752058,	127.574136716459},
//            {37.075436300179,	127.574122186403},
//            {37.075436264553,	127.574110522503},
//            {37.075447970074,	127.574127579414},
//            {37.075457958690,	127.574137281528},
//            {37.075469992422,	127.574141898554},
//            {37.075373286154,	127.574100697801},
//            {37.075269564305,	127.574016630204},
//            {37.075173368575,	127.573909732561},
//            {37.075128695455,	127.573798584572},
//            {37.075156962848,	127.573693675396},
//            {37.075230127525,	127.573637044586},
//            {37.075309392361,	127.573611215007},
//            {37.075435009555,	127.573609662225},
//            {37.075452849230,	127.573595005966},
//            {37.075389962624,	127.573621807653},
//            {37.075429583032,	127.573613394846},
//            {37.075534039406,	127.573559429897},
//            {37.075654382148,	127.573469434720},
//            {37.075760331461,	127.573352338028},
//            {37.075810830024,	127.573205122785},
//            {37.075778936928,	127.573053983986},
//            {37.075673704218,	127.573017051680},
            {37.080972470000,	127.572123750000},
            {37.080972470000,   127.572123750000},
            {37.080987240000,   127.572135190000},
            {37.080998760000,   127.572133540000},
            {37.081013450000,   127.572129010000},
            {37.081027540000,   127.572121670000},
            {37.081037530000,   127.572111810000},
            {37.075527208064,	127.573051012289},
            {37.075569192744,	127.572970469523},
            {37.075532666202,	127.573071268292},
            {37.075404784307,	127.573149177816},
            {37.075277503126,	127.573184766652},
            {37.075177601638,	127.573226005649},
            {37.075076935939,	127.573208984330},
            {37.075042805634,	127.573219833232},
            {37.075030724964,	127.573214427659},
            {37.075016673641,	127.573211851775},
            {37.075021956006,	127.573194206082},
            {37.074997379388,	127.573200434001},
            {37.074911321314,	127.573250778632},
            {37.074803817040,	127.573308679328},
            {37.074681148886,	127.573361699776},
            {37.074594319230,	127.573459418705},
            {37.074559862987,	127.573636984621},
            {37.074525417702,	127.573806261880},
            {37.074442411924,	127.573937156351},
            {37.074325623083,	127.574008930431},
            {37.074182843164,	127.574038260611},
            {37.074045894289,	127.574063880093},
            {37.073903332516,	127.574088875469},
            {37.073786243321,	127.574131579497},
            {37.073683912849,	127.574194421195},
            {37.073591355477,	127.574274085477},
            {37.073512241200,	127.574352855609},
            {37.073450651556,	127.574443029962},
            {37.073412833110,	127.574505227589},
            {37.073385961503,	127.574518066283},
            {37.073376529848,	127.574538617776},
            {37.073336086020,	127.574604491876},
            {37.073241352718,	127.574706229289},
            {37.073114420236,	127.574788336831},
            {37.073000384405,	127.574891161882},
            {37.072911875942,	127.574983842184},
            {37.072819797254,	127.575087661611},
            {37.072728524427,	127.575173910985},
            {37.072656927643,	127.575183803652},
            {37.072642433585,	127.575159082958},
            {37.072627973892,	127.575154936610},
            {37.072572304235,	127.575124337028},
            {37.072499586145,	127.575062661541},
            {37.072436758608,	127.574987364207},
            {37.072380598805,	127.574885206299},
            {37.072341047321,	127.574784151978},
            {37.072314922730,	127.574650146676},
            {37.072340086834,	127.574511739052},
            {37.072359249561,	127.574476451650},
            {37.072367555018,	127.574480590947},
            {37.072369175145,	127.574466761149},
            {37.072391350987,	127.574435474746},
            {37.072460770986,	127.574336464751},
            {37.072538066416,	127.574219948581},
            {37.072644485812,	127.574188253113},
            {37.072775420580,	127.574146299753},
            {37.072878867902,	127.574004212139},
            {37.072949079200,	127.573883499886},
            {37.072983418163,	127.573712750345},
            {37.073012670697,	127.573554436957},
            {37.073082002121,	127.573400290731},
            {37.073145500508,	127.573254041500},
            {37.073211580225,	127.573101983390},
            {37.073323075596,	127.572989420427},
            {37.073446652674,	127.572924939000},
            {37.073592868187,	127.572827923417},
            {37.073694973391,	127.572669030448},
            {37.073754387055,	127.572506736385},
            {37.073765390916,	127.572331824410},
            {37.073740155006,	127.572164247219},
            {37.073713739861,	127.571982533942},
            {37.073677046322,	127.571804266969},
            {37.073665632150,	127.571647353440},
            {37.073708889935,	127.571485655163},
            {37.073780059429,	127.571324971927},
            {37.073870098029,	127.571207372694},
            {37.073980967046,	127.571095205072},
            {37.074097493439,	127.570977651244},
            {37.074185696261,	127.570850035162},
            {37.074270959513,	127.570730617244},
            {37.074369551293,	127.570616889746},
            {37.074494976021,	127.570577438939},
            {37.074623013177,	127.570631458317},
            {37.074684935586,	127.570764206230},
            {37.074693659826,	127.570850836164},
            {37.074697853956,	127.570886749684},
            {37.074696873241,	127.570919494503},
            {37.074690787011,	127.571049677149},
            {37.074656928985,	127.571189536699},
            {37.074604180917,	127.571334220976},
            {37.074549066620,	127.571452667911},
            {37.074496958589,	127.571584315010},
            {37.074471885092,	127.571701776816},
            {37.074458267009,	127.571834030065},
            {37.074456638467,	127.571928914018},
            {37.074540869259,	127.571997201084},
            {37.074735761161,	127.571926662431},
            {37.074727779977,	127.571938676021},
            {37.074687259801,	127.572135847817},
            {37.074838961796,	127.572176382603},
            {37.074949101016,	127.572133833610},
            {37.075097026052,	127.572132445989},
            {37.075240572695,	127.572091090630},
            {37.075284202840,	127.572066822489},
            {37.075419131489,	127.571980005027},
            {37.075541844530,	127.571891496473},
            {37.075626944057,	127.571730706127},
            {37.075715514403,	127.571630458900},
            {37.075828306365,	127.571549855333},
            {37.075941666229,	127.571463756245},
            {37.076052426056,	127.571350342232},
            {37.076133769637,	127.571221958736},
            {37.076244599440,	127.571116024760},
            {37.076381252059,	127.571030464479},
            {37.076509948719,	127.570960719626},
            {37.076647253314,	127.570910925366},
            {37.076784078174,	127.570886400626},
            {37.076932913273,	127.570857541849},
            {37.077082838582,	127.570810837412},
            {37.077216838309,	127.570755218316},
            {37.077347964626,	127.570686628363},
            {37.077491009894,	127.570641541849},
            {37.077646715808,	127.570629080280},
            {37.077771302332,	127.570607020963},
            {37.077897934693,	127.570585152613},
            {37.078050745233,	127.570555999268},
            {37.078195612229,	127.570539117462},
            {37.078315748692,	127.570566492066},
            {37.078447728048,	127.570613712530},
            {37.078603207880,	127.570658517713},
            {37.078758103536,	127.570678997636},
            {37.078898142825,	127.570700650440},
            {37.079033606251,	127.570699901763},
            {37.079177975330,	127.570677960503},
            {37.079316114841,	127.570671437273},
            {37.079465073416,	127.570713154991},
            {37.079640276158,	127.570776146843},
            {37.079683227145,	127.570784913763},
            {37.079692218450,	127.570783915233},
            {37.079704743922,	127.570800123035},
            {37.079793809434,	127.570867280194},
            {37.079926163654,	127.570913606507},
            {37.080023134253,	127.570852373953},
            {37.080020173848,	127.570753330166},
            {37.080026076094,	127.570750259309},
            {37.080023536818,	127.570738656508},
            {37.080004274157,	127.570718549723},
            {37.079903745978,	127.570678416522},
            {37.079791300277,	127.570664838299},
            {37.079722575401,	127.570569283047},
            {37.079730873218,	127.570410776589},
            {37.079692275467,	127.570266264456},
            {37.079581604966,	127.570167686583},
            {37.079466216494,	127.570071919135},
            {37.079351349051,	127.570001273051},
            {37.079234131310,	127.570051723160},
            {37.079137294370,	127.570181803383},
            {37.079030555290,	127.570315084136},
            {37.078899624384,	127.570382312572},
            {37.078749448236,	127.570365791272},
            {37.078599288275,	127.570333894370},
            {37.078464294189,	127.570327366363},
            {37.078344388708,	127.570326285520},
            {37.078208784024,	127.570324869999},
            {37.078061548339,	127.570304431249},
            {37.077914001312,	127.570277727947},
            {37.077767452594,	127.570265750215},
            {37.077629846496,	127.570236381713},
            {37.077489040504,	127.570215738820},
            {37.077343395141,	127.570223495778},
            {37.077204759825,	127.570206658389},
            {37.077112689383,	127.570187765537},
            {37.077053911690,	127.570172947940},
            {37.077009919814,	127.570169623157},
            {37.076914501411,	127.570217965659},
            {37.076871271424,	127.570327305749},
            {37.076865260972,	127.570423655285},
            {37.076833967822,	127.570542978651},
            {37.076759174553,	127.570628862826},
            {37.076649652248,	127.570601952093},
            {37.076600134826,	127.570464237690},
            {37.076598371978,	127.570345709615},
            {37.076602297544,	127.570274207437},
            {37.076483433939,	127.570194737593},
            {37.076591175284,	127.570094625366},
            {37.076627655196,	127.569978622269},
            {37.076628498895,	127.569836690346},
            {37.076655748433,	127.569678346323},
            {37.076709420194,	127.569538803842},
            {37.076778059014,	127.569388046564},
            {37.076869984325,	127.569245332595},
            {37.076956836603,	127.569085785053},
            {37.076981488351,	127.568918197405},
            {37.076986582295,	127.568759109121},
            {37.076970394993,	127.568669332167},
            {37.076934156799,	127.568599956882},
            {37.076956492223,	127.568607565931},
            {37.076942817157,	127.568594748671},
            {37.076830807774,	127.568484711244},
            {37.076747080294,	127.568364180368},
            {37.076741317146,	127.568210741479},
            {37.076764992339,	127.568070981586},
            {37.076736573611,	127.567963232267},
            {37.076709815103,	127.567916977800},
            {37.076640246400,	127.567922642328},
            {37.076550278555,	127.567992483371},
            {37.076477128675,	127.568098238349},
            {37.076442213149,	127.568218874365},
            {37.076424616255,	127.568347197652},
            {37.076394602471,	127.568491222748},
            {37.076341579559,	127.568651521361},
            {37.076280331672,	127.568812712041},
            {37.076215245790,	127.568971602036},
            {37.076155733839,	127.569117091246},
            {37.076094228608,	127.569235075360},
            {37.076013476318,	127.569352871273},
            {37.075919765147,	127.569490847413},
            {37.075848579315,	127.569625887041},
            {37.075796735153,	127.569775138449},
            {37.075758227391,	127.569950806686},
            {37.075720440945,	127.570118416980},
            {37.075752025894,	127.570285629659},
            {37.075813147702,	127.570442116698},
            {37.075829069871,	127.570616734541},
            {37.075757374219,	127.570767746763},
            {37.075649134177,	127.570873350183},
            {37.075521422206,	127.570942661113},
            {37.075401803297,	127.571011465199},
            {37.075278255516,	127.571064506028},
            {37.075131980250,	127.571078361763},
            {37.074990779580,	127.571112303064},
            {37.074857959060,	127.571078315876},
            {37.074751533166,	127.571045892963},
            {37.074691506234,	127.571064339774},
            {37.074668764939,	127.571094353567},
            {37.074625822981,	127.571211198869},
            {37.074574379775,	127.571338884879},
            {37.074524471363,	127.571460711738},
            {37.074482996441,	127.571592481982},
            {37.074454133096,	127.571742117190},
            {37.074449014511,	127.571869858668},
            {37.074450310672,	127.571990590129},
            {37.074442627806,	127.572130790228},
            {37.074472552209,	127.572314237462},
            {37.074513772977,	127.572434078141},
            {37.074572229021,	127.572551096157},
            {37.074638572243,	127.572566145939},
            {37.074632289226,	127.572602496836},
            {37.074676291828,	127.572641628949},
            {37.074841394161,	127.572753492133},
            {37.074972110892,	127.572784612959},
            {37.075106984305,	127.572801802261},
            {37.075273154604,	127.572791178701},
            {37.075409138451,	127.572745734725},
            {37.075537052174,	127.572781688337},
            {37.075682400826,	127.572841303061},
            {37.075825184620,	127.572731438662},
            {37.075939514701,	127.572695842246},
            {37.076058903384,	127.572701588640},
            {37.076202544985,	127.572711354409},
            {37.076319566050,	127.572657399989},
            {37.076384131524,	127.572493181075},
            {37.076412081389,	127.572286186700},
            {37.076418287916,	127.572245514647},
            {37.076422288148,	127.572234297348},
            {37.076452092748,	127.572122747046},
            {37.076524148785,	127.572066668020},
            {37.076516165411,	127.572076532532},
            {37.076532353819,	127.572081306169},
            {37.076666240261,	127.572117279994},
            {37.076784640997,	127.572194935899},
            {37.076924784934,	127.572228477346},
            {37.077039303428,	127.572149863427},
            {37.077158681323,	127.572145057599},
            {37.077330462934,	127.572168980785},
            {37.077476731855,	127.572193205876},
            {37.077630788112,	127.572185769260},
            {37.077766045682,	127.572151674652},
            {37.077887870528,	127.572061246663},
            {37.077966084497,	127.571903093228},
            {37.078085987930,	127.571782391379},
            {37.078194142554,	127.571676883664},
            {37.078333007912,	127.571575308398},
            {37.078476333076,	127.571517807227},
            {37.078589345451,	127.571511650357},
            {37.078743496538,	127.571503034250},
            {37.078877092075,	127.571503512244},
            {37.079010123897,	127.571505049431},
            {37.079150741011,	127.571503193331},
            {37.079304092430,	127.571525161010},
            {37.079464778457,	127.571546985291},
            {37.079587367865,	127.571496777974},
            {37.079739234114,	127.571413314909},
            {37.079856296393,	127.571340546835},
            {37.079982630289,	127.571311247443},
            {37.080107748337,	127.571301371785},
            {37.080174324635,	127.571374968073},
            {37.080194083226,	127.571535369902},
            {37.080190018055,	127.571712576455},
            {37.080144773416,	127.571875201322},
            {37.080103850748,	127.571964473971},
            {37.080054372068,	127.572080502829},
            {37.080051087148,	127.572069159243},
            {37.080061165201,	127.572104620682},
            {37.080098739547,	127.572225668444},
            {37.080106265260,	127.572283878462},
            {37.080238329292,	127.572413575954},
            {37.080429269936,	127.572390510174},
            {37.080577583186,	127.572343988432},
            {37.080683192575,	127.572339265656},
            {37.080773689218,	127.572350706096},
            {37.080893368141,	127.572377087875},
            {37.081012137021,	127.572448229470},
            {37.081141424229,	127.572598468635},
            {37.081030811891,	127.573162950045},
            {37.080994032367,	127.573227354310},
            {37.080983843171,	127.573333963614},
            {37.081070490527,	127.573352576944},
            {37.081117164665,	127.573302062996},
            {37.081171881254,	127.573224641415},
            {37.081269154429,	127.573090299893},
            {37.081244426130,	127.573064926592},
            {37.081314466707,	127.573011233758},
            {37.081442506763,	127.572961616474},
            {37.081508780153,	127.572898717588},
            {37.081606113139,	127.572952073564},
            {37.081672059367,	127.573051408796},
            {37.081772711121,	127.573121496660},
            {37.081900242427,	127.573113811509},
            {37.082020705879,	127.573056074226},
            {37.082127485526,	127.573011809726},
            {37.082246467902,	127.572969712216},
            {37.082357786479,	127.572940791320},
            {37.082464767906,	127.572934148918},
            {37.082568012046,	127.572930770894},
            {37.082693892055,	127.572927646516},
            {37.082813470736,	127.572925255450},
            {37.082941094675,	127.572947096271},
            {37.083083919681,	127.572984406455},
            {37.083242635414,	127.573012130876},
            {37.083379906537,	127.572993172829},
            {37.083511689603,	127.572936075133},
            {37.083647778061,	127.572876347953},
            {37.083771610433,	127.572834489621},
            {37.083903246830,	127.572760966425},
            {37.083986147251,	127.572625251237},
            {37.084041879385,	127.572443667126},
            {37.084097777114,	127.572276000692},
            {37.084178358978,	127.572163038547},
            {37.084295235316,	127.572090128251},
            {37.084439486872,	127.572029561451},
            {37.084581954081,	127.571954352459},
            {37.084718481175,	127.571880585279},
            {37.084809665382,	127.571830429676},
            {37.084854239079,	127.571832861054},
            {37.084949842068,	127.571837922738},
            {37.085076758644,	127.571841402627},
            {37.085189614144,	127.571848679109},
            {37.085310168767,	127.571840834764},
            {37.085471963565,	127.571777441991},
            {37.085561356977,	127.571843364317},
            {37.085566566565,	127.571830425662},
            {37.085615788084,	127.571777881420},
            {37.085737607039,	127.571760272533},
            {37.085849657385,	127.571820392396},
            {37.085950642416,	127.571911517305},
            {37.086043280446,	127.571983840966},
            {37.086133710027,	127.572119864491},
            {37.086191261333,	127.572244060791},
            {37.086234426499,	127.572423486654},
            {37.086212507424,	127.572584580191},
            {37.086234495470,	127.572703161238},
            {37.086267964867,	127.572771902166},
            {37.086309507750,	127.572848983906},
            {37.086368526981,	127.572995590718},
            {37.086414535733,	127.573109518074},
            {37.086470291984,	127.573221263125},
            {37.086553403250,	127.573342693885},
            {37.086621345923,	127.573474300812},
            {37.086646830947,	127.573545201662},
            {37.086633049856,	127.573553482007},
            {37.086648682708,	127.573603592930},
            {37.086660200329,	127.573682065116},
            {37.086667132860,	127.573735810603},
            {37.086671161544,	127.573787006099},
            {37.086694367787,	127.573924767392},
            {37.086759524040,	127.574061871867},
            {37.086834441284,	127.574236455153},
            {37.086913161908,	127.574406436143},
            {37.087013243858,	127.574535478336},
            {37.087132795204,	127.574646650612},
            {37.087236162281,	127.574759817721},
            {37.087356912460,	127.574855968373},
            {37.087493535028,	127.574904966839},
            {37.087644440943,	127.574941107062},
            {37.087789873825,	127.574978553321},
            {37.087934339590,	127.574948591803},
            {37.088051920262,	127.574851472476},
            {37.088129977068,	127.574684038291},
            {37.088299681111,	127.574436076332},
            {37.088318865569,	127.574390972641},
            {37.088333878979,	127.574307710269},
            {37.088345164706,	127.574175763089},
            {37.088382952825,	127.574047094799},
            {37.088373725524,	127.574027781531},
            {37.088360810401,	127.573923755101},
            {37.088278017572,	127.573787877315},
            {37.088182447710,	127.573640024773},
            {37.088077566670,	127.573513599633},
            {37.087962134780,	127.573404816547},
            {37.087848849274,	127.573287598618},
            {37.087769890108,	127.573142209274},
            {37.087694327642,	127.573000333115},
            {37.087645390949,	127.572852358499},
            {37.087589637761,	127.572682913011},
            {37.087521166614,	127.572542180205},
            {37.087446727465,	127.572376401263},
            {37.087375509203,	127.572217783201},
            {37.087303031993,	127.572071614783},
            {37.087216410620,	127.571933753753},
            {37.087103346124,	127.571814846183},
            {37.086986850173,	127.571713025175},
            {37.086874988167,	127.571600408227},
            {37.086757853111,	127.571477872838},
            {37.086666010314,	127.571350805765},
            {37.086611629797,	127.571197553591},
            {37.086533860785,	127.571067945710},
            {37.086461180445,	127.570924925246},
            {37.086355145361,	127.570849133142},
            {37.086232010220,	127.570833419173},
            {37.086106013214,	127.570790920793},
            {37.085977294532,	127.570711896814},
            {37.085858865589,	127.570671298582},
            {37.085751660912,	127.570676208454},
            {37.085630440988,	127.570749279723},
            {37.085517488489,	127.570866607832},
            {37.085398965400,	127.570952994728},
            {37.085268248190,	127.570999109895},
            {37.085116810363,	127.571016787777},
            {37.084970701306,	127.571047029747},
            {37.084846206630,	127.571124382066},
            {37.084797240920,	127.571261653762},
            {37.084791168306,	127.571363564035},
            {37.084787902876,	127.571361559414},
            {37.084794776399,	127.571370054396},
            {37.084793578356,	127.571424721441},
            {37.084788818986,	127.571532839041},
            {37.084790203607,	127.571681099557},
            {37.084791094351,	127.571828338382},
            {37.084740740596,	127.571951801369},
            {37.084657848151,	127.572067517417},
            {37.084544100038,	127.572162173651},
            {37.084418332888,	127.572230259749},
            {37.084276946725,	127.572297732009},
            {37.084185970536,	127.572434682126},
            {37.084135192894,	127.572610253883},
            {37.084073933804,	127.572757956155},
            {37.083995937999,	127.572901735083},
            {37.083938509203,	127.573038352120},
            {37.083896012776,	127.573190369038},
            {37.083853052341,	127.573357554454},
            {37.083815673342,	127.573532743418},
            {37.083814244977,	127.573708804913},
            {37.083818330802,	127.573876197369},
            {37.083847613192,	127.574072620694},
            {37.083741264908,	127.574114943829},
            {37.083601058794,	127.574107186442},
            {37.083439764994,	127.574123079864},
            {37.083302798852,	127.574133743955},
            {37.083152547568,	127.574106550279},
            {37.083011383200,	127.574126861492},
            {37.082875801364,	127.574162669462},
            {37.082735630884,	127.574098122697},
            {37.082597385464,	127.574051957470},
            {37.082459246846,	127.574039089595},
            {37.082318441641,	127.574049475019},
            {37.082186340625,	127.574053747665},
            {37.082074730279,	127.573999926881},
            {37.082018444796,	127.573938159299},
            {37.081962358413,	127.573857126256},
            {37.081938188767,	127.573819899025},
            {37.081933282431,	127.573812070067},
            {37.081929296002,	127.573797288212},
            {37.081905927137,	127.573768759141},
            {37.081838336724,	127.573704448686},
            {37.081751109007,	127.573694996833},
            {37.081649984617,	127.573691414743},
            {37.081559378266,	127.573616047181},
            {37.081479209160,	127.573528947066},
            {37.081390967193,	127.573436120356},
            {37.081262668500,	127.573396125571},
            {37.081175975602,	127.573460037080},
            {37.081109183628,	127.573573313201},
            {37.081036810690,	127.573690179926},
            {37.080959131146,	127.573727290773},
            {37.080849467149,	127.573697463816},
            {37.080760039876,	127.573710818919},
            {37.080733164999,	127.573734715096},
            {37.080731912913,	127.573720306617},
            {37.080653507909,	127.573678219337},
            {37.080673708601,	127.573793227878},
            {37.080699628179,	127.574061407519},
            {37.080712843273,	127.574215968872},
            {37.080748199707,	127.574355290561},
            {37.080747873564,	127.574483936215},
            {37.080736250150,	127.574623581561},
            {37.080720469905,	127.574782615729},
            {37.080712620029,	127.574934695303},
            {37.080704495492,	127.575092814338},
            {37.080704153454,	127.575260855804},
            {37.080685161861,	127.575419532440},
            {37.080670799816,	127.575569995963},
            {37.080666866570,	127.575750174966},
            {37.080648244448,	127.575908399671},
            {37.080627927895,	127.576060638753},
            {37.080629841995,	127.576237878254},
            {37.080638059541,	127.576406753318},
            {37.080663290337,	127.576561033446},
            {37.080698602451,	127.576669396646},
            {37.080733465554,	127.576770802019},
            {37.080749888591,	127.576842804732},
            {37.080741218634,	127.576838789097},
            {37.080754345775,	127.576840704616},
            {37.080761962090,	127.576833497527},
            {37.080758737965,	127.576865700544},
            {37.080772364240,	127.576949612776},
            {37.080774197321,	127.577022393221},
            {37.080780578514,	127.577077812597},
            {37.080802936859,	127.577150123970},
            {37.080837462367,	127.577237135413},
            {37.080865823604,	127.577329573648},
            {37.080881345491,	127.577412959661},
            {37.080875342057,	127.577523995564},
            {37.080843987640,	127.577668246714},
            {37.080844931274,	127.577781524520},
            {37.080848581095,	127.577917023090},
            {37.080853292085,	127.578022742401},
            {37.080866758462,	127.578097686516},
            {37.080909146388,	127.578192658754},
            {37.080938165842,	127.578264682373},
            {37.080973744214,	127.578320731384},
            {37.080982635598,	127.578308792879},
            {37.080992523892,	127.578315340355},
            {37.080964654696,	127.578423990039},
            {37.080887613776,	127.578588891021},
            {37.080880008467,	127.578635972738},
            {37.080872430397,	127.578649306918},
            {37.080843089113,	127.578706839339},
            {37.080789982346,	127.578829931316},
            {37.080724946489,	127.578985484126},
            {37.080660667540,	127.579149076593},
            {37.080598727758,	127.579315401115},
            {37.080537189119,	127.579458680749},
            {37.080466324335,	127.579601726317},
            {37.080367394047,	127.579712302124},
            {37.080268997909,	127.579817700033},
            {37.080177302566,	127.579950456672},
            {37.080119452102,	127.580129746665},
            {37.080059722164,	127.580298829774},
            {37.079962593628,	127.580432509157},
            {37.079855846416,	127.580564790625},
            {37.079798888487,	127.580698260675},
            {37.079761237075,	127.580851775817},
            {37.079787606784,	127.580995000650},
            {37.079871110562,	127.581109233604},
            {37.080016899044,	127.581159210333},
            {37.080041571380,	127.581166787434},
            {37.080059020772,	127.581173283817},
            {37.080147678354,	127.581207171208},
            {37.080234529038,	127.581270881126},
            {37.080375761082,	127.581332164517},
            {37.080580277417,	127.581290776322},
            {37.080641458147,	127.581199807607},
            {37.080645308064,	127.581172550841},
            {37.080679831289,	127.581122373421},
            {37.080744099268,	127.581024147722},
            {37.080828430590,	127.580889825806},
            {37.080921424295,	127.580748423275},
            {37.081006535534,	127.580591146501},
            {37.081098522820,	127.580433149305},
            {37.081225107315,	127.580336505721},
            {37.081319575710,	127.580210091501},
            {37.081373832156,	127.580027915712},
            {37.081391382029,	127.579848241952},
            {37.081422099982,	127.579687269252},
            {37.081486378077,	127.579534748093},
            {37.081553851368,	127.579384452828},
            {37.081607046141,	127.579221302745},
            {37.081654384140,	127.579057994296},
            {37.081699335992,	127.578881874921},
            {37.081742244446,	127.578709676293},
            {37.081786751268,	127.578531757785},
            {37.081836355497,	127.578366862681},
            {37.081908561206,	127.578206141627},
            {37.081972602789,	127.578045100394},
            {37.081990262702,	127.577873026641},
            {37.082006978300,	127.577687125304},
            {37.082053833213,	127.577555347053},
            {37.082139584206,	127.577426621371},
            {37.082236107390,	127.577292420779},
            {37.082323524564,	127.577150637261},
            {37.082411955039,	127.576980978377},
            {37.082456934495,	127.576817210842},
            {37.082469300132,	127.576633761837},
            {37.082447059907,	127.576451306988},
            {37.082392049224,	127.576283768657},
            {37.082269505788,	127.576181949548},
            {37.082126365836,	127.576083732599},
            {37.081993300911,	127.575997122121},
            {37.081885443188,	127.575929504766},
            {37.081778925601,	127.575849241676},
            {37.081680501687,	127.575768583346},
            {37.081617756830,	127.575645086234},
            {37.081561438312,	127.575610636225},
            {37.081439044607,	127.575595070666},
            {37.081324831997,	127.575578876587},
            {37.081272911016,	127.575474332834},
            {37.081276448501,	127.575352545354},
            {37.081268172851,	127.575319795666},
            {37.081322820241,	127.575270418110},
            {37.081418795739,	127.575265713744},
            {37.081471603736,	127.575347664617},
            {37.081492766674,	127.575475536766},
            {37.081560858385,	127.575528118116},
            {37.081695508441,	127.575580651300},
            {37.081827107120,	127.575625818937},
            {37.081941971858,	127.575690392384},
            {37.082073436122,	127.575757832601},
            {37.082220319915,	127.575834492369},
            {37.082346324141,	127.575898476969},
            {37.082466847172,	127.575957798885},
            {37.082617525730,	127.575992754513},
            {37.082767026487,	127.576004786621},
            {37.082912577410,	127.575986108008},
            {37.083037539572,	127.575925917269},
            {37.083165603515,	127.575849419659},
            {37.083304571112,	127.575762527175},
            {37.083419145155,	127.575697721777},
            {37.083522895186,	127.575646220519},
            {37.083675292910,	127.575573055094},
            {37.083828964694,	127.575522840109},
            {37.083961609431,	127.575496012536},
            {37.084095491568,	127.575468602424},
            {37.084216650617,	127.575396215044},
            {37.084285272564,	127.575248613041},
            {37.084316056561,	127.575065732126},
            {37.084287283672,	127.574871320338},
            {37.084260673398,	127.574832243580},
            {37.084256408099,	127.574806690080},
            {37.084248410513,	127.574672747969},
            {37.084216445946,	127.574500740205},
            {37.084141247655,	127.574375019225},
            {37.084235003145,	127.574275085834},
            {37.084149734484,	127.574273522597},
            {37.084033347594,	127.574223599484},
            {37.083915353603,	127.574230317535},
            {37.083790837240,	127.574268602249},
            {37.083660413081,	127.574321219145},
            {37.083536547140,	127.574352910089},
            {37.083397353616,	127.574383623319},
            {37.083262816652,	127.574453491640},
            {37.083224662686,	127.574631068848},
            {37.083150826399,	127.574796431152},
            {37.083048744532,	127.574893235781},
            {37.082925070468,	127.574946698341},
            {37.082788448372,	127.574983478918},
            {37.082676966779,	127.575045814881},
            {37.082550450240,	127.575118905495},
            {37.082439018515,	127.575167178480},
            {37.082327250466,	127.575191286726},
            {37.082188988156,	127.575186404355},
            {37.082081128722,	127.575177494837},
            {37.081978044407,	127.575156633230},
            {37.081878146061,	127.575135127380},
            {37.081794406342,	127.575127632715},
            {37.081740713678,	127.575127828083},
            {37.081732386556,	127.575134638055},
            {37.081721779179,	127.575144795254},
            {37.081725435612,	127.575157078061},
            {37.081732119787,	127.575148656480},
            {37.081741701152,	127.575140704239},
            {37.081751502189,	127.575144262242},
            {37.081750051775,	127.575128641503},
            {37.081754530982,	127.575118682954},
            {37.081763639714,	127.575125303732},
            {37.081767626325,	127.575138479761},
            {37.081775515104,	127.575143918216},
            {37.081771961991,	127.575156189544},
            {37.081699806666,	127.575131609081},
            {37.081585282385,	127.575090850376},
            {37.081498483150,	127.575040082264},
            {37.081386113879,	127.574957689227},
            {37.081269515192,	127.574854990913},
            {37.081172431274,	127.574754092621},
            {37.081131380610,	127.574618790011},
            {37.081103861424,	127.574455847390},
            {37.081079697937,	127.574297833097},
            {37.081069275045,	127.574125110332},
            {37.081070708079,	127.573840097268},
            {37.081045831374,	127.573748083216},
            {37.080999622758,	127.573708593521},
            {37.080991996695,	127.573691349065},
            {37.080938714847,	127.573701028622},
            {37.080863312291,	127.573682046570},
            {37.080808006871,	127.573613350887},
            {37.080802766544,	127.573532433991},
            {37.080866434540,	127.573439692161},
            {37.080939543701,	127.573361334794},
            {37.081017418935,	127.573273009014},
            {37.081095781804,	127.573157801754},
            {37.081167081092,	127.573036002976},
            {37.081218188978,	127.572944121643},
            {37.081111346555,	127.572992117848},
            {37.081155425962,	127.572930596201},
            {37.081251429786,	127.572779430816},
            {37.081203799133,	127.572670060989},
            {37.081137259831,	127.572578797934},
            {37.081057708090,	127.572486533100},
            {37.081036464620,	127.572325567267},
            {37.081065929299,	127.572190709877},
            {37.081107336289,	127.572047809101},
            {37.081124610221,	127.572096193550},
            {37.081138584755,	127.572055115248},
            {37.081147616383,	127.571982889216},
            {37.081209395100,	127.571878753280},
            {37.081265356838,	127.571773043965},
            {37.081346091193,	127.571660185919},
            {37.081424620943,	127.571587666812},
            {37.081486850787,	127.571461326343},
            {37.081603555580,	127.571405999119},
            {37.081702280090,	127.571411066560},
            {37.081803503915,	127.571333969678},
            {37.081824475917,	127.571171023242},
            {37.081779224101,	127.570987361720},
            {37.081790683985,	127.570824674264},
            {37.081872407203,	127.570685003622},
            {37.081954795718,	127.570550353178},
            {37.082053444510,	127.570417159610},
            {37.082179974786,	127.570310738169},
            {37.082296846349,	127.570216022273},
            {37.082403920192,	127.570106279094},
            {37.082510134697,	127.569993367183},
            {37.082653196007,	127.569905942763},
            {37.082805282559,	127.569892336477},
            {37.082945571298,	127.569933285783},
            {37.083089909035,	127.569997022289},
            {37.083215004133,	127.570031094109},
            {37.083344218082,	127.570059767181},
            {37.083477102222,	127.570076692015},
            {37.083620291383,	127.570077667385},
            {37.083765926899,	127.570044344438},
            {37.083888075823,	127.569963369428},
            {37.084012708440,	127.569873446075},
            {37.084142569391,	127.569795125551},
            {37.084277119925,	127.569709036190},
            {37.084400598164,	127.569643525221},
            {37.084533330633,	127.569586466929},
            {37.084669358005,	127.569535162261},
            {37.084811456223,	127.569514415381},
            {37.084954158300,	127.569482106776},
            {37.085099888427,	127.569416962411},
            {37.085243322329,	127.569375830247},
            {37.085382396075,	127.569367531037},
            {37.085514917492,	127.569411045138},
            {37.085611516391,	127.569495534838},
            {37.085652781720,	127.569639622661},
            {37.085662034383,	127.569801417147},
            {37.085692994330,	127.569973034218},
            {37.085851770635,	127.570016173063},
            {37.085823041240,	127.569981454661},
            {37.085827208858,	127.570033371288},
            {37.085917181706,	127.570083955949},
            {37.086014734588,	127.570064926142},
            {37.086099436203,	127.570053579540},
            {37.086087909072,	127.570023066713},
            {37.086106642605,	127.570034904230},
            {37.086203570080,	127.570097812765},
            {37.086249409191,	127.570216126322},
            {37.086234362878,	127.570359992912},
            {37.086256315440,	127.570481007958},
            {37.086334960592,	127.570539771091},
            {37.086454813776,	127.570466899930},
            {37.086558118543,	127.570356527241},
            {37.086662497241,	127.570421159140},
            {37.086766106551,	127.570536231560},
            {37.086837857748,	127.570638713089},
            {37.086825859650,	127.570781163239},
            {37.086824201016,	127.570947426381},
            {37.086862934042,	127.571116079265},
            {37.086905275479,	127.571283668192},
            {37.086974235688,	127.571440589089},
            {37.087079639437,	127.571553750073},
            {37.087189279078,	127.571647247178},
            {37.087279066502,	127.571758959701},
            {37.087349095710,	127.571902208590},
            {37.087412979868,	127.572031989963},
            {37.087483925939,	127.572163930249},
            {37.087581158546,	127.572266986807},
            {37.087681383402,	127.572350038616},
            {37.087766906360,	127.572468109783},
            {37.087835871285,	127.572598115476},
            {37.087890388750,	127.572756793290},
            {37.087953966594,	127.572921160970},
            {37.088046806390,	127.573052341952},
            {37.088145155261,	127.573174909454},
            {37.088218071995,	127.573293187803},
            {37.088306727929,	127.573430192656},
            {37.088409235684,	127.573564036697},
            {37.088526089797,	127.573656966060},
            {37.088650779587,	127.573728031483},
            {37.088769004517,	127.573812144728},
            {37.088816932586,	127.573877423189},
            {37.088808728737,	127.573891607725},
            {37.088834280273,	127.573928860833},
            {37.088924084122,	127.573984540126},
            {37.089053587752,	127.574036554696},
            {37.089186920203,	127.574065857162},
            {37.089283494522,	127.574039471043},
            {37.089292080849,	127.574047578905},
            {37.089298016721,	127.574058072996},
            {37.089304179173,	127.574073246033},
            {37.089295215652,	127.574069967622},
            {37.089288032922,	127.574062962129},
            {37.089280859521,	127.574048585921},
            {37.089281282294,	127.574035996351},
            {37.089275191815,	127.574020112468},
            {37.089273553233,	127.574007151372},
            {37.089263347600,	127.574008199971},
            {37.089267233640,	127.574018661101},
            {37.089272261026,	127.574034339514},
            {37.089277547275,	127.574045309777},
            {37.089289201710,	127.574047090787},
            {37.089302460114,	127.574050208687},
            {37.089299740095,	127.574061409492},
            {37.089291045679,	127.574065924870},
            {37.089302367547,	127.574063661014},
            {37.089292987360,	127.574067954870},
            {37.089280135379,	127.574070137997},
            {37.089270963643,	127.574065540418},
            {37.089262763996,	127.574056534390},
            {37.089265021753,	127.574043592714},
            {37.089273261627,	127.574033680930},
            {37.089277332555,	127.574023083676},
            {37.089275330431,	127.574008807319},
            {37.089268526747,	127.574000261988},
            {37.089259555962,	127.573996835662},
            {37.089248478614,	127.573988870015},
            {37.089260546858,	127.573990704998},
            {37.089273416398,	127.574002128170},
            {37.089284743666,	127.574016335013},
            {37.089292705152,	127.574026819170},
            {37.089301250196,	127.574030906151},
            {37.089314988587,	127.574028668172},
            {37.089325184009,	127.574025988320},
            {37.089315765281,	127.574036544545},
            {37.089357423932,	127.574007717282},
            {37.090598325598,	127.572031233444},
            {37.090589475142,	127.571945085400},
            {37.090503231513,	127.571847160186},
            {37.090460699101,	127.571761296578},
            {37.090401019230,	127.571690018885},
            {37.090317010174,	127.571586549847},
            {37.090230491274,	127.571459462269},
            {37.090167522031,	127.571287045949},
            {37.090104530105,	127.571122348254},
            {37.090034636412,	127.570954974198},
            {37.089993769669,	127.570801875044},
            {37.089971097473,	127.570630749939},
            {37.089941096593,	127.570478782467},
            {37.089903426146,	127.570307194776},
            {37.089860968682,	127.570141595030},
            {37.089823433481,	127.569985526894},
            {37.089789612645,	127.569829179606},
            {37.089755085866,	127.569673986455},
            {37.089693567521,	127.569540589383},
            {37.089595367229,	127.569437439710},
            {37.089470371957,	127.569349010692},
            {37.089359634906,	127.569312574419},
            {37.089247989349,	127.569266230686},
            {37.089120236927,	127.569230156863},
            {37.088978234419,	127.569258445032},
            {37.088828802767,	127.569280470614},
            {37.088676654820,	127.569315828635},
            {37.088543498670,	127.569288871941},
            {37.088434946008,	127.569229160959},
            {37.088341852875,	127.569183326295},
            {37.088218540791,	127.569111522609},
            {37.088095153769,	127.569071741346},
            {37.088028529972,	127.569072967275},
            {37.088018648972,	127.569073984097},
            {37.088029657961,	127.569066217454},
            {37.088013227137,	127.569069650311},
            {37.087926404191,	127.569084333876},
            {37.087861045450,	127.569091861298},
            {37.087839500775,	127.569104697093},
            {37.087756016969,	127.569151865326},
            {37.087741786455,	127.569155768452},
            {37.087659756096,	127.569192531369},
            {37.087574000382,	127.569225604384},
            {37.087525359444,	127.569239160835},
            {37.087467461623,	127.569240709644},
            {37.087389328987,	127.569185962986},
            {37.087349996175,	127.569101567691},
            {37.087379532439,	127.569005603113},
            {37.087437685693,	127.568939850963},
            {37.087446448624,	127.568932855642},
            {37.087455224554,	127.568919392871},
            {37.087464492409,	127.568920936575},
            {37.087475895359,	127.568918736255},
            {37.087539632315,	127.568870516883},
            {37.087621288327,	127.568785220524},
            {37.087684607873,	127.568691122285},
            {37.087747914212,	127.568599739391},
            {37.087822206756,	127.568491426761},
            {37.087892070195,	127.568393136486},
            {37.087976587003,	127.568304319254},
            {37.088063862485,	127.568248509451},
            {37.088159497965,	127.568232433389},
            {37.088270861992,	127.568232119682},
            {37.088393039674,	127.568242936593},
            {37.088494993892,	127.568292571905},
            {37.088585036367,	127.568366786234},
            {37.088671289156,	127.568436779972},
            {37.088759911411,	127.568495722080},
            {37.088852988330,	127.568571003633},
            {37.088922711058,	127.568672280818},
            {37.088994091283,	127.568791364008},
            {37.089070296005,	127.568929184532},
            {37.089167139382,	127.569046734673},
            {37.089301848759,	127.569114015781},
            {37.089431897495,	127.569161219886},
            {37.089563156154,	127.569210984250},
            {37.089679290283,	127.569265225824},
            {37.089774894673,	127.569326597419},
            {37.089872204626,	127.569378712148},
            {37.089984456249,	127.569409390096},
            {37.090072732142,	127.569359955442},
            {37.090162142596,	127.569294232013},
            {37.090244943755,	127.569249135338},
            {37.090327943813,	127.569223164938},
            {37.090436631260,	127.569198790138},
            {37.090548890816,	127.569164227930},
            {37.090649239332,	127.569115956924},
            {37.090737184323,	127.569047645138},
            {37.090792162729,	127.568974444349},
            {37.090828493394,	127.568872413821},
            {37.090847670136,	127.568725578594},
            {37.090833761669,	127.568601435450},
            {37.090818144067,	127.568497021561},
            {37.090801658685,	127.568407138901},
            {37.090772405212,	127.568386908095},
            {37.090720553718,	127.568235831513},
            {37.090653914145,	127.568178950820},
            {37.090586290004,	127.568084690009},
            {37.090518721071,	127.568041016709},
            {37.090435752182,	127.568013036034},
            {37.090362474975,	127.567986663542},
            {37.090290464773,	127.567948041781},
            {37.090236257094,	127.567925956681},
            {37.090151066526,	127.567927597924},
            {37.090079768415,	127.567892492144},
            {37.090019362482,	127.567860088635},
            {37.089959908474,	127.567840772705},
            {37.089888055478,	127.567826731415},
            {37.089815201855,	127.567820050105},
            {37.089727635133,	127.567807185854},
            {37.089638210222,	127.567769796418},
            {37.089558104976,	127.567734124945},
            {37.089470338358,	127.567692227249},
            {37.089386460585,	127.567641950446},
            {37.089303392338,	127.567571462932},
            {37.089220725784,	127.567485551157},
            {37.089144699222,	127.567397790370},
            {37.089060557369,	127.567337977548},
            {37.088965347858,	127.567278412372},
            {37.088872658669,	127.567205982199},
            {37.088777352127,	127.567134565004},
            {37.088668106520,	127.567078841283},
            {37.088552973326,	127.567044186142},
            {37.088431321269,	127.567044500414},
            {37.088324276360,	127.567063514360},
            {37.088202549333,	127.567087789936},
            {37.088083984652,	127.567149495266},
            {37.087976324678,	127.567262725137},
            {37.087856304390,	127.567341395136},
            {37.087714555134,	127.567415801161},
            {37.087591408267,	127.567470616102},
            {37.087472252281,	127.567524115786},
            {37.087356210162,	127.567584084126},
            {37.087242209161,	127.567635524900},
            {37.087129779050,	127.567698110183},
            {37.087043751627,	127.567809503149},
            {37.086976007892,	127.567949545603},
            {37.086937779895,	127.568126252518},
            {37.086973053873,	127.568277268010},
            {37.086977681468,	127.568421413803},
            {37.086922873411,	127.568557263796},
            {37.086833098104,	127.568684162693},
            {37.086737356996,	127.568782673824},
            {37.086638383009,	127.568881923530},
            {37.086551269736,	127.569007332946},
            {37.086554634440,	127.569160484500},
            {37.086621360137,	127.569265845363},
            {37.086713719046,	127.569326230698},
            {37.086808013144,	127.569399018040},
            {37.086965857331,	127.569489881508},
            {37.087032173440,	127.569488130169},
            {37.087020551397,	127.569482040764},
            {37.087035817727,	127.569481012819},
            {37.087124316164,	127.569496286883},
            {37.087262841806,	127.569580384830},
            {37.087387903857,	127.569577252656},
            {37.087496047671,	127.569527032400},
            {37.087611623879,	127.569540819680},
            {37.087700881555,	127.569606906316},
            {37.087735433771,	127.569726823594},
            {37.087709908629,	127.569830690802},
            {37.087710842390,	127.569929287074},
            {37.087772972654,	127.570034516683},
            {37.087848139115,	127.570113485752},
            {37.087931096977,	127.570198677517},
            {37.088021808851,	127.570288224771},
            {37.088111069413,	127.570375453797},
            {37.088193520923,	127.570473403004},
            {37.088275597474,	127.570586100308},
            {37.088366046304,	127.570702706437},
            {37.088453128538,	127.570813438600},
            {37.088536356906,	127.570933435235},
            {37.088624447470,	127.571068522614},
            {37.088691412205,	127.571219849839},
            {37.088768118383,	127.571367260002},
            {37.088858921925,	127.571493906419},
            {37.088965963964,	127.571597307845},
            {37.089095606335,	127.571632496133},
            {37.089222169533,	127.571643205354},
            {37.089317053656,	127.571705318184},
            {37.089324635053,	127.571790264017},
            {37.089316886495,	127.571805841636},
            {37.089290176563,	127.571909011250},
            {37.089247827257,	127.572006344878},
            {37.089173803344,	127.572083023686},
            {37.089094082110,	127.572087741248},
            {37.089100856440,	127.572095090151},
            {37.089091018826,	127.572088305292},
            {37.088989341647,	127.572034281898},
            {37.088852897921,	127.571967042182},
            {37.088716615767,	127.571891428554},
            {37.088655493361,	127.571755876916},
            {37.088615699838,	127.571552914890},
            {37.088522085758,	127.571405343478},
            {37.088439864362,	127.571264981033},
            {37.088357072598,	127.571125800991},
            {37.088269016538,	127.570988421541},
            {37.088173303896,	127.570864398974},
            {37.088084104716,	127.570723603485},
            {37.088004055435,	127.570596080233},
            {37.087923499969,	127.570479103078},
            {37.087837649902,	127.570377272846},
            {37.087737019447,	127.570272130311},
            {37.087618640801,	127.570157372622},
            {37.087524312933,	127.570062182164},
            {37.087404194409,	127.569982717993},
            {37.087283316306,	127.569926352528},
            {37.086953966065,	127.569697021417},
            {37.086833086587,	127.569616684892},
            {37.086743069236,	127.569550968223},
            {37.086655185328,	127.569503018128},
            {37.086545368574,	127.569422201977},
            {37.086412211095,	127.569347099327},
            {37.086283650013,	127.569388833611},
            {37.086180842061,	127.569489498816},
            {37.086115924068,	127.569660928340},
            {37.085958399905,	127.569941299722},
            {37.085901270889,	127.570045800540},
            {37.085806364896,	127.570199181664},
            {37.085763021711,	127.570275099432},
            {37.085658602162,	127.570308718438},
            {37.085515033302,	127.570360981926},
            {37.085379162615,	127.570487185500},
            {37.085350749449,	127.570503670575},
            {37.085296104022,	127.570537546637},
            {37.085257847011,	127.570580680756},
            {37.085212401014,	127.570616531453},
            {37.085166785168,	127.570652124281},
            {37.085150288128,	127.570653801854},
            {37.085140015023,	127.570651249438},
            {37.085137810697,	127.570663134300},
            {37.085146736709,	127.570669683462},
            {37.085158944601,	127.570665771505},
            {37.085169598434,	127.570668544932},
            {37.085180012260,	127.570667453605},
            {37.085189118417,	127.570668916725},
            {37.085198352907,	127.570675681438},
            {37.085203757702,	127.570685342983},
            {37.085193583807,	127.570693095007},
            {37.085183431257,	127.570699087327},
            {37.085176711683,	127.570691455027},
            {37.085180787120,	127.570702209107},
            {37.085192361793,	127.570702101629},
            {37.085188744283,	127.570690902251},
            {37.085178529179,	127.570688059808},
            {37.085184467643,	127.570697999508},
            {37.085175622818,	127.570695395488},
            {37.085181031250,	127.570685361346},
            {37.085164586805,	127.570701539690},
            {37.085068409390,	127.570746174753},
            {37.084969304527,	127.570791278575},
            {37.084876250628,	127.570800985868},
            {37.084766168729,	127.570761829151},
            {37.084683038125,	127.570696176466},
            {37.084593568332,	127.570702195138},
            {37.084484716225,	127.570751645774},
            {37.084396746299,	127.570831937893},
            {37.084362344335,	127.570972056790},
            {37.084327899395,	127.571100502357},
            {37.084260881333,	127.571236102513},
            {37.084154385464,	127.571345952346},
            {37.084018364508,	127.571423420419},
            {37.083880036522,	127.571487705565},
            {37.083748797218,	127.571555547649},
            {37.083626084368,	127.571642184380},
            {37.083541207756,	127.571718495676},
            {37.083483064403,	127.571780513386},
            {37.083444780808,	127.571822801744},
            {37.083335785117,	127.571886423769},
            {37.083183891946,	127.571957740678},
            {37.083063534914,	127.572000233369},
            {37.082930725392,	127.572038265039},
            {37.082794206279,	127.572081052019},
            {37.082649657638,	127.572123603578},
            {37.082499952239,	127.572168733985},
            {37.082370929557,	127.572206336804},
            {37.082229591100,	127.572279761834},
            {37.082121181931,	127.572361915650},
            {37.081996466127,	127.572427494894},
            {37.081859875238,	127.572463359287},
            {37.081685354720,	127.572527589598},
            {37.081643242343,	127.572577758899},
            {37.081633032023,	127.572580186343},
            {37.081594993750,	127.572608657060},
            {37.081526692832,	127.572661105239},
            {37.081443105902,	127.572703315423},
            {37.081359781723,	127.572727069766},
            {37.081271671171,	127.572769818502},
            {37.081215957363,	127.572859476572},
            {37.081171220315,	127.572900796894},
            {37.081120114686,	127.572940003267},
            {37.081085382303,	127.572977613847}
    };
}