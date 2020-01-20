package com.eye3.golfpay.fmb_tab.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.activity.BaseActivity;
import com.eye3.golfpay.fmb_tab.common.UIThread;
import com.eye3.golfpay.fmb_tab.databinding.ActivityMainBinding;
import com.eye3.golfpay.fmb_tab.fragment.CaddieFragment;
import com.eye3.golfpay.fmb_tab.fragment.ControlFragment;
import com.eye3.golfpay.fmb_tab.fragment.CourseFragment;
import com.eye3.golfpay.fmb_tab.fragment.EditorFragment;
import com.eye3.golfpay.fmb_tab.fragment.MainWorkFragment;
import com.eye3.golfpay.fmb_tab.fragment.NearestLongestFragment;
import com.eye3.golfpay.fmb_tab.fragment.NoticeFragment;
import com.eye3.golfpay.fmb_tab.fragment.OrderFragment;
import com.eye3.golfpay.fmb_tab.fragment.QRScanFragment;
import com.eye3.golfpay.fmb_tab.fragment.ScoreFragment;
import com.eye3.golfpay.fmb_tab.fragment.ScoreInputFragment;
import com.eye3.golfpay.fmb_tab.fragment.SettingsFragment;
import com.eye3.golfpay.fmb_tab.fragment.ShadePaymentFragment;
import com.eye3.golfpay.fmb_tab.service.CartLocationService;
import com.eye3.golfpay.fmb_tab.util.FmbCustomDialog;
import com.google.android.material.navigation.NavigationView;

import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


public class MainActivity extends BaseActivity<ActivityMainBinding> implements NavigationView.OnNavigationItemSelectedListener {

    private FmbCustomDialog wmmsDlg;
    final int CAMERA_REQUEST_CODE = 1;
    NavigationView navigationView;
    DrawerLayout drawer_layout;
    FmbCustomDialog commonDialog;
    TextView gpsTxtView , scoreTxtView, controlTxtView;
    ImageView markView, cancelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        init();
        startLocationService();

        //홈타이틀 이벤트
//        ((Button) findViewById(R.id.btnDrawerOpen)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EventForTitleView(v);
//            }
//        });

//        ((ImageButton) findViewById(R.id.btnTitleHome)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EventForTitleView(v);
//            }
//        });

        //GoHomeScreen();
//        GoNativeScreenAdd(new ScoreFragment(), null);
//        GoNativeScreenAdd(new QRScanFragment(), null);
//        GoNativeScreenAdd(new SettingsFragment(), null);
//        GoNativeScreenAdd(new CourseFragment(), null);
//        GoNativeScreenAdd(new ScoreInputFragment(), null);
//        GoNativeScreenAdd(new NearestLongestFragment(), null);
//        GoNativeScreenAdd(new OrderFragment(), null);
//        GoNativeScreenAdd(new ShadePaymentFragment(), null);
//        GoNativeScreenAdd(new ControlFragment(), null);
//        GoNativeScreenAdd(new CaddieFragment(), null);
//        GoNativeScreenAdd(new EditorFragment(), null);
        GoNativeScreenAdd(new NoticeFragment(), null);

    }

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


    private void init() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        //      String name = mRealm.where(UserInfo.class).findFirst().getuserName();
        // ((TextView) navigationView.getHeaderView(0).findViewById(R.id.user_name)).setText(Global.userInfo.userName);
//        ((Button) navigationView.getHeaderView(0).findViewById(R.id.btn_logout)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showLogoutDialog(MainActivity.this, "정말로 로그아웃 하시겠습니까?");
//            }
//        });
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);

        cancelView=  findViewById(R.id.content_login).findViewById(R .id.cancelIcon);
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
            }
        });

        scoreTxtView = findViewById(R.id.main_bottom_bar).findViewById(R.id.scoreTextView);
        scoreTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new ScoreFragment(), null);
            }
        });

        controlTxtView = findViewById(R.id.main_bottom_bar).findViewById(R.id.controlTextView);
        controlTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new ControlFragment(), null);
            }
        });

        markView = findViewById(R.id.main_bottom_bar).findViewById(R.id.mark);
        markView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.openDrawer(GravityCompat.END);
            }
        });
//**************************************************************************************************************
       // 메뉴뷰 이벤트처리
        findViewById(R.id.startTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDrawerViewToMenuView();
            }
        });

      //  getBind().contentLogin.menuViewInclude.


    }

    protected void changeDrawerViewToMenuView(){
        findViewById(R.id.content_login).findViewById(R.id.login_view_include).setVisibility(View.INVISIBLE);
        findViewById(R.id.content_login).findViewById(R.id.menu_view_include).setVisibility(View.VISIBLE);

    }

    private void showLogoutDialog(final Context context, String msg) {
        commonDialog = new FmbCustomDialog(context, null, msg, "확인", "취소", new View.OnClickListener() {
            @Override
            public void onClick(View view) {   //확인버튼 이벤트
//                mRealm.executeTransaction(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//                        RealmResults<UserInfo> result = realm.where(UserInfo.class).equalTo("userId", Global.userInfo.userId).findAll();
//                        result.deleteAllFromRealm();
//                    }
//                });
                Toast.makeText(MainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }   //취소버튼 이벤트
        }, new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                commonDialog.dismiss();
            }
        });

        try {
            if (commonDialog != null)
                commonDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        if (id == R.id.takeover_test) {
            // Handle the camera action
            //  GoNativeScreenAdd(new FrDeviceSearch(), null);
        } else if (id == R.id.nav_device_search) {
            //           GoNativeScreenAdd(new FrDeviceSearch(), null);
//            Intent intent = new Intent(this, MarkerMapActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
        } else if (id == R.id.failover_inspection) {
            ;
        } else if (id == R.id.manual) {

        }

        drawer_layout.closeDrawer(GravityCompat.END);
        return true;
    }

    /**
     * .
     * 메뉴 열기
     */
    public void OpenMenuMap() {
        if (mNativeFragment != null) {
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
    protected boolean HideMenuMap() {
        if (mNativeFragment != null) {
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
        //여기 수정할것
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }
}
