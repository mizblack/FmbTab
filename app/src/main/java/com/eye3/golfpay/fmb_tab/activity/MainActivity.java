package com.eye3.golfpay.fmb_tab.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.eye3.golfpay.fmb_tab.fragment.NearestLongestFragment;
import com.eye3.golfpay.fmb_tab.fragment.NoticeFragment;
import com.eye3.golfpay.fmb_tab.fragment.OrderFragment;
import com.eye3.golfpay.fmb_tab.fragment.QRScanFragment;
import com.eye3.golfpay.fmb_tab.fragment.RankingFragment;
import com.eye3.golfpay.fmb_tab.fragment.ScoreFragment;
import com.eye3.golfpay.fmb_tab.model.login.Login;
import com.eye3.golfpay.fmb_tab.model.teeup.TeeUpTime;
import com.eye3.golfpay.fmb_tab.model.teeup.TodayReserveList;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.service.CartLocationService;
import com.eye3.golfpay.fmb_tab.util.FmbCustomDialog;
import com.eye3.golfpay.fmb_tab.util.Security;
import com.eye3.golfpay.fmb_tab.util.SettingsCustomDialog;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    final int CAMERA_REQUEST_CODE = 1;
    NavigationView navigationView;
    DrawerLayout drawer_layout;
    FmbCustomDialog fmbDialog;
    SettingsCustomDialog settingsCustomDialog;
    TextView gpsTxtView, scoreTxtView, controlTxtView, startTextView, nameEditText, phoneNumberEditText, caddieNameTextView, groupNameTextView, reservationPersonNameTextView, roundingTeeUpTimeTextView, inOutTextView00, inOutTextView01, showListTextView;
    ImageView markView, cancelView;
    RecyclerView teeUpRecyclerView;
    TeeUpAdapter teeUpAdapter;
    View selectTobDivider, selectBottomDivider, roundingLinearLayout;
    Bundle caddieFragmentBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        systemUIHide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        init();
        startLocationService();


    }

    //   @SuppressLint("ObsoleteSdkInt")
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
                systemUIHide();

                if (response.getRetCode().equals("ok")) {
                    //    GoNativeScreen(new ScoreFragment(), null);
                    Toast.makeText(context, "안녕하세요 " + response.getCaddyInfo().getName() + "님!\n티업시간을 선택해주세요.", Toast.LENGTH_LONG).show();
                    caddieNameTextView = findViewById(R.id.menu_view_include).findViewById(R.id.caddieNameTextView);
                    caddieNameTextView.setText(response.getCaddyInfo().getName() + " 캐디");
                    Global.teeUpTime = response;

                    teeUpAdapter = new TeeUpAdapter(MainActivity.this, Global.teeUpTime.getTodayReserveList());
                    teeUpRecyclerView = findViewById(R.id.teeUpRecyclerView);
                    teeUpRecyclerView.setHasFixedSize(true);
                    LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
                    teeUpRecyclerView.setLayoutManager(manager);
                    teeUpRecyclerView.setAdapter(teeUpAdapter);
                    teeUpAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(TeeUpTime response) {
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

    private void login(final Context context, String id, String pwd) {
        showProgress("로그인 중입니다....");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).login(MainActivity.this, id, pwd, new DataInterface.ResponseCallback<Login>() {
            @Override
            public void onSuccess(Login response) {
                hideProgress();
                systemUIHide();

                if (response.getRetCode().equals("ok")) {
                    getTodayReservesForCaddy(context, "" + response.getCaddyNo());
                    Global.CaddyNo = String.valueOf(response.getCaddyNo());
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
                caddieFragmentBundle = new Bundle();
                caddieFragmentBundle.putInt("selectedTeeUpIndex", Global.selectedTeeUpIndex);
                GoNativeScreen(new CaddieFragment(), caddieFragmentBundle);
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

        findViewById(R.id.scoreLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new ScoreFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        findViewById(R.id.scoreBoardLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new ScoreFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        findViewById(R.id.nearestLongestLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new NearestLongestFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        findViewById(R.id.rankingNormalLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  GoNativeScreen(new ScoreFragment(), null);
                GoNativeScreen(new RankingFragment(), null);
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

        selectTobDivider = findViewById(R.id.selectTobDivider);
        selectBottomDivider = findViewById(R.id.selectBottomDivider);
        roundingLinearLayout = findViewById(R.id.roundingLinearLayout);

        groupNameTextView = findViewById(R.id.groupNameTextView);
        reservationPersonNameTextView = findViewById(R.id.reservationPersonNameTextView);
        roundingTeeUpTimeTextView = findViewById(R.id.teeUpTimeTextView);
        inOutTextView00 = findViewById(R.id.inOutTextView00);
        inOutTextView01 = findViewById(R.id.inOutTextView01);

        showListTextView = findViewById(R.id.showListTextView);
        showListTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTobDivider.setVisibility(View.VISIBLE);
                selectBottomDivider.setVisibility(View.VISIBLE);
                teeUpRecyclerView.setVisibility(View.VISIBLE);
                roundingLinearLayout.setVisibility(View.GONE);

                disableMenu();
            }
        });

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

        disableMenu();
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
            if (drawer != null)
                drawer.closeDrawer(GravityCompat.END);
            fmbDialog.dismiss();
            setLogout();
        }
    };

    private void setLogout() {

        stopService((new Intent(getApplicationContext(), CartLocationService.class)));
        finish();
    }

    public void changeDrawerViewToMenuView() {
        findViewById(R.id.loginNmenu).findViewById(R.id.login_view_include).setVisibility(View.INVISIBLE);
        findViewById(R.id.loginNmenu).findViewById(R.id.menu_view_include).setVisibility(View.VISIBLE);
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

    void enableMenu() {
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        findViewById(R.id.gpsLinearLayout).setEnabled(true);
        findViewById(R.id.scoreBoardLinearLayout).setEnabled(true);
        findViewById(R.id.nearestLongestLinearLayout).setEnabled(true);
        findViewById(R.id.rankingNormalLinearLayout).setEnabled(true);
        findViewById(R.id.caddieLinearLayout).setEnabled(true);
        findViewById(R.id.orderLinearLayout).setEnabled(true);
        findViewById(R.id.paymentLinearLayout).setEnabled(true);

        findViewById(R.id.settingsLinearLayout).setEnabled(true);
        findViewById(R.id.scoreLinearLayout).setEnabled(true);
        findViewById(R.id.controlLinearLayout).setEnabled(true);
        findViewById(R.id.closeLinearLayout).setEnabled(true);
    }

    void disableMenu() {
//        gpsLinearLayout
//        scoreBoardLinearLayout
//        nearestLongestLinearLayout
//        rankingLinearLayout
//        caddieLinearLayout
//        orderLinearLayout
//        paymentLinearLayout

//        settingsLinearLayout
//        scoreLinearLayout
//        controlLinearLayout
//        closeLinearLayout
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);

        findViewById(R.id.gpsLinearLayout).setEnabled(false);
        findViewById(R.id.scoreBoardLinearLayout).setEnabled(false);
        findViewById(R.id.nearestLongestLinearLayout).setEnabled(false);
        findViewById(R.id.rankingNormalLinearLayout).setEnabled(false);
        findViewById(R.id.caddieLinearLayout).setEnabled(false);
        findViewById(R.id.orderLinearLayout).setEnabled(false);
        findViewById(R.id.paymentLinearLayout).setEnabled(false);

        findViewById(R.id.settingsLinearLayout).setEnabled(false);
        findViewById(R.id.scoreLinearLayout).setEnabled(false);
        findViewById(R.id.controlLinearLayout).setEnabled(false);
        findViewById(R.id.closeLinearLayout).setEnabled(false);
    }

    private class TeeUpAdapter extends RecyclerView.Adapter<TeeUpAdapter.TeeUpTimeItemViewHolder> {

        ArrayList<TodayReserveList> todayReserveList;
        TextView teeUpTimeTextView, reservationGuestNameTextView;

        TeeUpAdapter(Context context, ArrayList<TodayReserveList> todayReserveList) {
            this.todayReserveList = todayReserveList;
        }

        class TeeUpTimeItemViewHolder extends RecyclerView.ViewHolder {

            TeeUpTimeItemViewHolder(View view) {
                super(view);

                teeUpTimeTextView = view.findViewById(R.id.teeUpTimeTextView);
                reservationGuestNameTextView = view.findViewById(R.id.reservationPersonNameTextView);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        selectTobDivider.setVisibility(View.GONE);
                        selectBottomDivider.setVisibility(View.GONE);
                        teeUpRecyclerView.setVisibility(View.GONE);
                        roundingLinearLayout.setVisibility(View.VISIBLE);

                        int position = getAdapterPosition();
                        Global.selectedTeeUpIndex = position;
                        Global.selectedReservation = todayReserveList.get(position);
                        Global.reserveId = String.valueOf(todayReserveList.get(position).getId());
                        groupNameTextView.setText(todayReserveList.get(position).getGroup());
                        reservationPersonNameTextView.setText(todayReserveList.get(position).getGuestName());
                        roundingTeeUpTimeTextView.setText(timeMapper(todayReserveList.get(position).getTeeoff()));
                        setInOutTextView(todayReserveList.get(position).getInoutCourse());

                        enableMenu();
                    }
                });

            }

        }

        @NonNull
        @Override
        public TeeUpTimeItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tee_up, viewGroup, false);
            return new TeeUpTimeItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TeeUpTimeItemViewHolder scoreItemViewHolder, int position) {
            teeUpTimeTextView.setText(timeMapper(todayReserveList.get(position).getTeeoff()));
            reservationGuestNameTextView.setText(todayReserveList.get(position).getGuestName());
        }

        @Override
        public int getItemCount() {
            return todayReserveList.size();
        }

        String timeMapper(String time) {
            String timeString;
            String amOrPm;
            if (Integer.parseInt(time.split(":")[0]) >= 12) {
                amOrPm = " PM";
            } else {
                amOrPm = " AM";
            }
            timeString = time.split(":")[0] + ":" + time.split(":")[1] + amOrPm;
            return timeString;
        }

        String inOutMapper(String course) {
            return course + "코스";
        }

        void setInOutTextView(String course) {
            String courseTemp;
            if (course.equals("IN")) {
                courseTemp = "OUT";
            } else {
                courseTemp = "IN";
            }
            inOutTextView00.setText(inOutMapper(course));
            inOutTextView01.setText(inOutMapper(courseTemp));
        }

    }

}

