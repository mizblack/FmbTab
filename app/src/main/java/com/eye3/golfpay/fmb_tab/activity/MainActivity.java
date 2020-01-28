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
import android.widget.TextView;
import android.widget.Toast;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.common.UIThread;
import com.eye3.golfpay.fmb_tab.fragment.CaddieFragment;
import com.eye3.golfpay.fmb_tab.fragment.ControlFragment;
import com.eye3.golfpay.fmb_tab.fragment.CourseFragment;
import com.eye3.golfpay.fmb_tab.fragment.NoticeFragment;
import com.eye3.golfpay.fmb_tab.fragment.OrderFragment;
import com.eye3.golfpay.fmb_tab.fragment.QRScanFragment;
import com.eye3.golfpay.fmb_tab.fragment.ScoreFragment;
import com.eye3.golfpay.fmb_tab.fragment.ShadePaymentFragment;
import com.eye3.golfpay.fmb_tab.model.login.Login;
import com.eye3.golfpay.fmb_tab.model.score.ScoreBoard;
import com.eye3.golfpay.fmb_tab.model.teeup.TeeUpTime;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.service.CartLocationService;
import com.eye3.golfpay.fmb_tab.util.FmbCustomDialog;
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
    FmbCustomDialog fmbDialog;
    SettingsCustomDialog settingsCustomDialog;
    TextView gpsTxtView, scoreTxtView, controlTxtView, startTextView, nameEditText, phoneNumberEditText, caddieNameTextView;
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
        //      GoNativeScreen(new ScoreFragment(), null);
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
        //      GoNativeScreen(new NoticeFragment(), null);

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

    private void getTodayReservesForCaddy(final Context context, String caddy_id) {
        showProgress("티업시간을 받아오는 중입니다....");
        DataInterface.getInstance().getTodayReservesForCaddy(MainActivity.this, caddy_id, new DataInterface.ResponseCallback<TeeUpTime>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(TeeUpTime response) {
                hideProgress();

                if (response.getRetCode().equals("ok")) {
                    Toast.makeText(context, "안녕하세요 " + response.getCaddyInfo().getName() + "님!\n티업시간을 선택해주세요.", Toast.LENGTH_LONG).show();
                    caddieNameTextView = findViewById(R.id.menu_view_include).findViewById(R.id.caddieNameTextView);
                    caddieNameTextView.setText(response.getCaddyInfo().getName() + " 캐디");
                    Global.teeUpTime = response;
                }
            }

            @Override
            public void onError(TeeUpTime response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void login(final Context context, String id, String pwd) {
        showProgress("로그인 중입니다....");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).login(MainActivity.this, id, pwd, new DataInterface.ResponseCallback<Login>() {
            @Override
            public void onSuccess(Login response) {
                hideProgress();

                if (response.getRetCode().equals("ok")) {
                    getTodayReservesForCaddy(context, "" + response.getCaddyNo());
                }

            }

            @Override
            public void onError(Login response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @SuppressLint("CutPasteId")
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
        drawer_layout.openDrawer(GravityCompat.END);

        startTextView = findViewById(R.id.loginNmenu).findViewById(R.id.startTextView);
        nameEditText = findViewById(R.id.loginNmenu).findViewById(R.id.nameEditText);
        phoneNumberEditText = findViewById(R.id.loginNmenu).findViewById(R.id.phoneNumberEditText);
        startTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        cancelView = findViewById(R.id.loginNmenu).findViewById(R.id.cancelIcon);
        cancelView = findViewById(R.id.loginNmenu).findViewById(R.id.cancelIcon);
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
//**************************************************************************************************************
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

                changeDrawerViewToMenuView();
                //  drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        findViewById(R.id.startQRLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new QRScanFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        //******************************************************************************************************

        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fmbDialog = new FmbCustomDialog(MainActivity.this, "Logout", "로그아웃 하시겠습니까?", "아니오", "네", leftListener, rightListener, true);
                fmbDialog.show();
            }
        });


        // 캐디수첩
        findViewById(R.id.caddieLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new CaddieFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        // 그늘집 주문하기
        findViewById(R.id.orderLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new OrderFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        // 공지사항
        findViewById(R.id.noticeLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new NoticeFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        findViewById(R.id.paymentLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.gpsLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new CourseFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        // 설정
        findViewById(R.id.settingsLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsCustomDialog = new SettingsCustomDialog(MainActivity.this);
                settingsCustomDialog.show();
            }
        });

        findViewById(R.id.scoreBoardLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new ScoreFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        findViewById(R.id.controlLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new ControlFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        findViewById(R.id.closeLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.closeDrawer(GravityCompat.END);

            }
        });


        //   getBind().contentLogin.loginViewInclude.start

        //     getBind().contentLogin.menuViewInclude.


    }

    private View.OnClickListener leftListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            fmbDialog.dismiss();
        }
    };

    private View.OnClickListener rightListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if(drawer != null)
                  drawer.closeDrawer(GravityCompat.END);
            fmbDialog.dismiss();

            //   setLogout();
        }
    };

    private void setLogout() {
        finish();
    }

    public void changeDrawerViewToMenuView() {
        findViewById(R.id.loginNmenu).findViewById(R.id.login_view_include).setVisibility(View.INVISIBLE);
        findViewById(R.id.loginNmenu).findViewById(R.id.menu_view_include).setVisibility(View.VISIBLE);
    }

//    private void showLogoutDialog(final Context context, String msg) {
//        fmbDialog = new FmbCustomDialog(context, null, msg, "확인", "취소", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {   //확인버튼 이벤트
//
//                Toast.makeText(MainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
//                finish();
//            }   //취소버튼 이벤트
//        }, new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                fmbDialog.dismiss();
//            }
//        });
//
//        try {
//            if (fmbDialog != null)
//                fmbDialog.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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
        //여기 수정할것
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

        ScoreBoard board = new ScoreBoard();

    }

    private void getCourseInfo(){

    }

}