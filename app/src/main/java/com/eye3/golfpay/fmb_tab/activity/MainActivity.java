package com.eye3.golfpay.fmb_tab.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.common.UIThread;
import com.eye3.golfpay.fmb_tab.fragment.ControlFragment;
import com.eye3.golfpay.fmb_tab.fragment.CourseFragment;
import com.eye3.golfpay.fmb_tab.fragment.QRScanFragment;
import com.eye3.golfpay.fmb_tab.fragment.ScoreFragment;
import com.eye3.golfpay.fmb_tab.fragment.ViewMenuFragment;
import com.eye3.golfpay.fmb_tab.model.login.Login;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.service.CartLocationService;
import com.eye3.golfpay.fmb_tab.util.Security;
import com.eye3.golfpay.fmb_tab.util.SettingsCustomDialog;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    final int CAMERA_REQUEST_CODE = 1;
    NavigationView navigationView;
    DrawerLayout drawer_layout;
    SettingsCustomDialog settingsCustomDialog;
    TextView gpsTxtView, scoreTxtView, controlTxtView, startTextView, nameEditText, phoneNumberEditText, groupNameTextView, reservationPersonNameTextView, roundingTeeUpTimeTextView, inOutTextView00, inOutTextView01;
    ImageView markView, cancelView;
    LinearLayout ll_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        systemUIHide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        init();
        startLocationService();


    }

    @SuppressLint("ObsoleteSdkInt")
    private void startLocationService() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(getApplicationContext(), CartLocationService.class));
            } else {
                startService(new Intent(getApplicationContext(), CartLocationService.class));
            }
        }
    }


    private void login(final Context context, String id, String pwd) {
        showProgress("로그인 중입니다....");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).login(MainActivity.this, id, pwd, new DataInterface.ResponseCallback<Login>() {
            @Override
            public void onSuccess(Login response) {
                hideProgress();
                systemUIHide();

                if (response.getRetCode().equals("ok")) {
                    Global.CaddyNo = String.valueOf(response.getCaddyNo());
                    changeDrawerViewToMenuView();
                }

            }

            @Override
            public void onError(Login response) {
                hideProgress();
                systemUIHide();
            }

            @Override
            public void onFailure(Throwable t) {
                hideProgress();
                systemUIHide();
            }
        });
    }

    @SuppressLint("CutPasteId")
    private void init() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);

        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer_layout.openDrawer(GravityCompat.END);

        startTextView = findViewById(R.id.startTextView);
        nameEditText = findViewById(R.id.nameEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        ll_login = findViewById(R.id.login_view_include);
        startTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cancelView = findViewById(R.id.cancelIcon);
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.closeDrawer(GravityCompat.END);

            }
        });
        gpsTxtView = findViewById(R.id.main_bottom_bar).findViewById(R.id.gpsTextView);
        gpsTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new CourseFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        scoreTxtView = findViewById(R.id.main_bottom_bar).findViewById(R.id.scoreTextView);
        scoreTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new ScoreFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        controlTxtView = findViewById(R.id.main_bottom_bar).findViewById(R.id.controlTextView);
        controlTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new ControlFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        markView = findViewById(R.id.main_bottom_bar).findViewById(R.id.mark);
        markView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.openDrawer(GravityCompat.END);
            }
        });

        // 메뉴뷰 이벤트처리
        findViewById(R.id.startTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    login(getApplicationContext(), nameEditText.getText().toString(), Security.encrypt(phoneNumberEditText.getText().toString()));
                } catch (NoSuchPaddingException
                        | NoSuchAlgorithmException
                        | InvalidAlgorithmParameterException
                        | InvalidKeyException
                        | BadPaddingException
                        | IllegalBlockSizeException e) {
                    e.printStackTrace();
                }

                //changeDrawerViewToMenuView();
                //  drawer_layout.closeDrawer(GravityCompat.END);

                closeKeyboard(findViewById(R.id.nameEditText));
                closeKeyboard(findViewById(R.id.phoneNumberEditText));

            }
        });

        findViewById(R.id.startQRLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new QRScanFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        groupNameTextView = findViewById(R.id.groupNameTextView);
        reservationPersonNameTextView = findViewById(R.id.reservationPersonNameTextView);
        roundingTeeUpTimeTextView = findViewById(R.id.teeUpTimeTextView);
        inOutTextView00 = findViewById(R.id.inOutTextView00);
        inOutTextView01 = findViewById(R.id.inOutTextView01);

        findViewById(R.id.login_view_include).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard(findViewById(R.id.nameEditText));
                closeKeyboard(findViewById(R.id.phoneNumberEditText));
            }
        });

        findViewById(R.id.content_main_inc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard(findViewById(R.id.nameEditText));
                closeKeyboard(findViewById(R.id.phoneNumberEditText));
            }
        });

    }


    //**************************************************
    public void changeDrawerViewToMenuView() {

        ll_login.setVisibility(View.GONE);
        GoNavigationDrawer(new ViewMenuFragment(), null);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.takeover_test) {
//            // Handle the camera action
//            //  GoNativeScreenAdd(new FrDeviceSearch(), null);
//        } else if (id == R.id.nav_device_search) {
//            //           GoNativeScreenAdd(new FrDeviceSearch(), null);
////            Intent intent = new Intent(this, MarkerMapActivity.class);
////            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////            startActivity(intent);
//        } else if (id == R.id.failover_inspection) {
//            ;
//        } else if (id == R.id.manual) {
//
//        }

        drawer_layout.closeDrawer(GravityCompat.END);
        return true;
    }

    /**
     * .
     * 메뉴 열기
     */
    public void OpenMenuMap() {
        if (mBaseFragment != null) {
            UIThread.executeInUIThread(new Runnable() {
                @Override
                public void run() {
                    drawer_layout.openDrawer(GravityCompat.END);
                }
            });
        } else {
            //   ShowToast("메뉴 구성 중입니다. 잠시만 기다려 주세요.");
            return;
            //requestMenuList();
        }
    }

    /**
     * 메뉴 닫기
     *
     * @return true: 닫기 성공, false: 실패 (or 무시)
     */
    public boolean HideMenuMap() {
        if (mBaseFragment != null) {
            UIThread.executeInUIThread(new Runnable() {
                @Override
                public void run() {
                    drawer_layout.closeDrawer(GravityCompat.START);
                }
            });
            return true;
        }
        return false;
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
        systemUIHide();
    }

}
