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

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.common.UIThread;
import com.eye3.golfpay.fmb_tab.fragment.ControlFragment;
import com.eye3.golfpay.fmb_tab.fragment.CourseFragment;
import com.eye3.golfpay.fmb_tab.fragment.QRScanFragment;
import com.eye3.golfpay.fmb_tab.fragment.ScoreFragment;
import com.eye3.golfpay.fmb_tab.fragment.ViewMenuFragment;
import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.eye3.golfpay.fmb_tab.model.login.Login;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.net.ResponseData;
import com.eye3.golfpay.fmb_tab.service.CartLocationService;
import com.eye3.golfpay.fmb_tab.util.Security;
import com.google.android.material.navigation.NavigationView;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    public DrawerLayout drawer_layout;
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
        hideMainBottomBar();
        startLocationService();
        if(getVisibleFragment() == null)
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
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

    private void login(String id, String pwd) {
        //   showProgress("로그인 중입니다....");

        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).login(id, pwd, new DataInterface.ResponseCallback<Login>() {
            @Override
            public void onSuccess(Login response) {
                hideProgress();
                systemUIHide();

                if (response.getRetCode() != null && response.getRetCode().equals("ok")) {
                    Global.CaddyNo = String.valueOf(response.getCaddyNo());
                  //  changeDrawerViewToMenuView();
                } else {
                    changeDrawerViewToMenuView();
              //      Toast.makeText(MainActivity.this, "ID와 패스워드를 확인해주세요", Toast.LENGTH_SHORT).show();
              //     nameEditText.setText("");
               //     phoneNumberEditText.setText("");
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
                closeDrawerLayout();

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

        // 메뉴뷰 이벤트처리
        findViewById(R.id.startTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    login(nameEditText.getText().toString(), Security.encrypt(phoneNumberEditText.getText().toString()));
                } catch (NoSuchPaddingException
                        | NoSuchAlgorithmException
                        | InvalidAlgorithmParameterException
                        | InvalidKeyException
                        | BadPaddingException
                        | IllegalBlockSizeException e) {
                    e.printStackTrace();
                }

                closeKeyboard(findViewById(R.id.nameEditText));
                closeKeyboard(findViewById(R.id.phoneNumberEditText));

            }
        });

        findViewById(R.id.startQRLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new QRScanFragment(), null);
                closeDrawerLayout();
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

    public void changeDrawerViewToMenuView() {

        ll_login.setVisibility(View.GONE);
        getAllCourseInfo(MainActivity.this);

        GoNavigationDrawer(new ViewMenuFragment(), null);

    }

    private void getAllCourseInfo(Context context) {
        showProgress("코스 정보를 가져오는 중입니다.");
        DataInterface.getInstance().getCourseInfo(context, "1", new DataInterface.ResponseCallback<ResponseData<Course>>() {

            @Override
            public void onSuccess(ResponseData<Course> response) {
                hideProgress();
                if (response.getResultCode().equals("ok")) {
                    Global.courseInfoList = (ArrayList<Course>) response.getList();

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
        systemUIHide();
    }


}
