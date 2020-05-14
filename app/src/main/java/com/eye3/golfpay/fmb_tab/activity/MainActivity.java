package com.eye3.golfpay.fmb_tab.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.AppDef;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.common.UIThread;
import com.eye3.golfpay.fmb_tab.fragment.CaddieFragment;
import com.eye3.golfpay.fmb_tab.fragment.ControlFragment;
import com.eye3.golfpay.fmb_tab.fragment.CourseFragment;
import com.eye3.golfpay.fmb_tab.fragment.LoginFragment;
import com.eye3.golfpay.fmb_tab.fragment.ScoreFragment;
import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.net.ResponseData;
import com.eye3.golfpay.fmb_tab.service.CartLocationService;
import com.eye3.golfpay.fmb_tab.view.CaddieViewGuestItem;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    NavigationView navigationView;
    public DrawerLayout drawer_layout;
    TextView gpsTxtView, scoreTxtView, controlTxtView, groupNameTextView;
    TextView reservationPersonNameTextView, roundingTeeUpTimeTextView, inOutTextView00, inOutTextView01;

    ImageView markView, cancelView;
    LinearLayout ll_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        systemUIHide();
        setContentView(R.layout.activity_main);
        getAllCourseInfo(MainActivity.this);
        init();
        hideMainBottomBar();
        requestPermission();
        startLocationService();
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
    }

    private void requestPermission(){
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


    private void getAllCourseInfo(Context context) {
        showProgress("코스 정보를 가져오는 중입니다.");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getCourseInfo(context, "1", new DataInterface.ResponseCallback<ResponseData<Course>>() {

            @Override
            public void onSuccess(ResponseData<Course> response) {
                hideProgress();
                if (response.getResultCode().equals("ok")) {
                    Global.courseInfoList = response.getList();


                } else if (response.getResultCode().equals("fail")) {
                    // Toast.makeText(getAct, response.getResultMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(ResponseData<Course> response) {
                hideProgress();
            }

            @Override
            public void onFailure(Throwable t) {
                hideProgress();
            }
        });
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

            CaddieViewGuestItem guestItem = (CaddieViewGuestItem) CaddieFragment.mGuestViewContainerLinearLayout.getChildAt(traversalByGuestId(AppDef.guestid));

            setImagewithUri(guestItem.mClubImageView, AppDef.imageFilePath);
            //  guestItem.mClubImageView.setImageBitmap(clubImageBitmap);
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

}
