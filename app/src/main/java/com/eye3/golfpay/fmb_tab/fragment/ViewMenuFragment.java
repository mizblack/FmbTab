package com.eye3.golfpay.fmb_tab.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.activity.MainActivity;
import com.eye3.golfpay.fmb_tab.adapter.TeeUpViewPagerAdapter;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.dialog.LogoutDialog;
import com.eye3.golfpay.fmb_tab.model.guest.ReserveGuestList;
import com.eye3.golfpay.fmb_tab.model.teeup.TeeUpTime;
import com.eye3.golfpay.fmb_tab.model.teeup.TodayReserveList;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.net.ResponseData;
import com.eye3.golfpay.fmb_tab.service.CartLocationService;
import com.eye3.golfpay.fmb_tab.util.SettingsCustomDialog;
import com.eye3.golfpay.fmb_tab.util.Util;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewMenuFragment extends BaseFragment {
    private ViewPager teeUpViewPager;
    private TeeUpViewPagerAdapter teeUpAdapter;
    private TextView caddieNameTextView;
    private TextView groupNameTextView;
    private TextView reservationPersonNameTextView;
    private TextView roundingTeeUpTimeTextView;
    private TextView inOutTextView00;
    private TextView inOutTextView01;
    private TextView tvCartNo;
    private TextView controlTxtView;
    private ImageButton btnLeftMove, btnRightMove;
    private View selectTobDivider, selectBottomDivider, roundingLinearLayout;
    private DrawerLayout drawer_layout;
    private SettingsCustomDialog settingsCustomDialog;
    private TextView gpsTextView00;
    private TextView gpsTextView01;
    private View courseDivider;

    private TextView scoreBoardTextView00;
    private TextView scoreBoardTextView01;
    private View scoreBoardDivider;

    private TextView nearestLongestTextView00;
    private TextView nearestLongestTextView01;
    private View nearestLongestDivider;

    private TextView rankingTextView00;
    private TextView rankingTextView01;
    private View rankingDivider;

    private TextView caddieTextView00;
    private TextView caddieTextView01;
    private View caddieDivider;

    private TextView orderTextView00;
    private TextView orderTextView01;
    private View orderDivider;

    private TextView paymentTextView00;
    private TextView paymentTextView01;
    private View paymentDivider;

    private TextView topdressingTextView00;
    private TextView topdressingTextView01;
    private View topdressingDivider;

    private TextView galleryTextView00;
    private TextView galleryTextView01;
    private View galleryDivider;
    private LinearLayout gpsLinear, scoreBoardLinear, nearstLongestLinear, rankingNormalLinear, caddieLinear,
            orderLinear, paymentLinear, settingLinear, scoreLinear, controlLinear, closeLinear, caddieCancelLinearLayout;


    private RelativeLayout view_background;

    Timer todayReserveTimer;
    TextView mTvRoundStartfinish;
    TextView btnClose;


    public ViewMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTodayReservesForCaddy(getActivity(), Global.CaddyNo);
        startTimer();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_view_menu, container, false);
        teeUpViewPager = v.findViewById(R.id.teeUpViewPager);
        gpsLinear = v.findViewById(R.id.gpsLinearLayout);
        scoreBoardLinear = v.findViewById(R.id.scoreBoardLinearLayout);
        nearstLongestLinear = v.findViewById(R.id.nearestLongestLinearLayout);
        rankingNormalLinear = v.findViewById(R.id.rankingNormalLinearLayout);
        caddieLinear = v.findViewById(R.id.caddieLinearLayout);
        orderLinear = v.findViewById(R.id.orderLinearLayout);
        paymentLinear = v.findViewById(R.id.paymentLinearLayout);
        settingLinear = v.findViewById(R.id.settingsLinearLayout);
        scoreLinear = v.findViewById(R.id.scoreLinearLayout);
        controlLinear = v.findViewById(R.id.controlLinearLayout);
        closeLinear = v.findViewById(R.id.closeLinearLayoutViewMenu);
        caddieNameTextView = v.findViewById(R.id.caddieNameTextView);
        caddieCancelLinearLayout = v.findViewById(R.id.caddieCancelLinearLayout);

        view_background = v.findViewById(R.id.view_background);


        btnLeftMove = v.findViewById(R.id.btn_move_left);
        btnRightMove = v.findViewById(R.id.btn_move_right);
        btnClose = v.findViewById(R.id.btn_close);
        init(v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @SuppressLint("CutPasteId")
    private void init(View v) {
        gpsTextView00 = Objects.requireNonNull(v.findViewById(R.id.gpsTextView00));
        gpsTextView01 = v.findViewById(R.id.gpsTextView01);
        courseDivider = v.findViewById(R.id.courseDivider);

        scoreBoardTextView00 = v.findViewById(R.id.scoreBoardTextView00);
        scoreBoardTextView01 = v.findViewById(R.id.scoreBoardTextView01);
        scoreBoardDivider = v.findViewById(R.id.scoreBoardDivider);

        nearestLongestTextView00 = v.findViewById(R.id.nearestLongestTextView00);
        nearestLongestTextView01 = v.findViewById(R.id.nearestLongestTextView01);
        nearestLongestDivider = v.findViewById(R.id.nearestLongestDivider);

        rankingTextView00 = v.findViewById(R.id.rankingTextView00);
        rankingTextView01 = v.findViewById(R.id.rankingTextView01);
        rankingDivider = v.findViewById(R.id.rankingDivider);

        caddieTextView00 = v.findViewById(R.id.caddieTextView00);
        caddieTextView01 = v.findViewById(R.id.caddieTextView01);
        caddieDivider = v.findViewById(R.id.caddieDivider);

        orderTextView00 = v.findViewById(R.id.orderTextView00);
        orderTextView01 = v.findViewById(R.id.orderTextView01);
        orderDivider = v.findViewById(R.id.orderDivider);

        paymentTextView00 = v.findViewById(R.id.paymentTextView00);
        paymentTextView01 = v.findViewById(R.id.paymentTextView01);
        paymentDivider = v.findViewById(R.id.paymentDivider);

        topdressingTextView00 = v.findViewById(R.id.topdressingTextView00);
        topdressingTextView01 = v.findViewById(R.id.topdressingTextView01);
        topdressingDivider = v.findViewById(R.id.topdressingDivider);

        galleryTextView00 = v.findViewById(R.id.galleryTextView00);
        galleryTextView01 = v.findViewById(R.id.galleryTextView01);
        galleryDivider = v.findViewById(R.id.galleryDivider);

        drawer_layout = (mParentActivity).findViewById(R.id.drawer_layout);
        selectTobDivider = Objects.requireNonNull(v.findViewById(R.id.selectTobDivider));
        selectBottomDivider = v.findViewById(R.id.selectBottomDivider);

        tvCartNo = v.findViewById(R.id.cart_no);

        mTvRoundStartfinish = v.findViewById(R.id.first_round_start);
        mTvRoundStartfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //api 적용시킬것
                ((TextView) v).setText("전반종료");
            }
        });
        TextView showListTextView = v.findViewById(R.id.showListTextView);
        showListTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTobDivider.setVisibility(View.VISIBLE);
                selectBottomDivider.setVisibility(View.VISIBLE);
                //teeUpRecyclerView.setVisibility(View.VISIBLE);
                roundingLinearLayout.setVisibility(View.GONE);

                disableMenu();
                setDisableColor();
            }
        });
        groupNameTextView = v.findViewById(R.id.groupNameTextView);
        reservationPersonNameTextView = v.findViewById(R.id.reservationPersonNameTextView);
        roundingTeeUpTimeTextView = v.findViewById(R.id.teeUpTimeTextView);
        inOutTextView00 = v.findViewById(R.id.inOutTextView00);
        inOutTextView01 = v.findViewById(R.id.inOutTextView01);
        roundingLinearLayout = v.findViewById(R.id.roundingLinearLayout);
        controlTxtView = v.findViewById(R.id.controlTextViewViewMenu);

        selectBottomDivider.setVisibility(View.VISIBLE);
        //teeUpRecyclerView.setVisibility(View.VISIBLE);
        roundingLinearLayout.setVisibility(View.GONE);
//        caddieCancelLinearLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                fmbDialog = new FmbCustomDialog(getActivity(), "Logout", "로그아웃 하시겠습니까?", "아니오", "네", leftListener, rightListener, true);
//                fmbDialog.show();
//                return false;
//            }
//        });

        controlTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new ControlFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

//        ImageView markView = getActivity().findViewById(R.id.main_bottom_bar).findViewById(R.id.mark);
//        markView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawer_layout.openDrawer(GravityCompat.END);
//            }
//        });

        v.findViewById(R.id.btn_logout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                LogoutDialog dlg = new LogoutDialog(getContext(), R.style.DialogTheme);
                WindowManager.LayoutParams wmlp = dlg.getWindow().getAttributes();
                wmlp.gravity = Gravity.CENTER;

                // Set alertDialog "not focusable" so nav bar still hiding:
                dlg.getWindow().
                        setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

                dlg.getWindow().getDecorView().setSystemUiVisibility(Util.DlgUIFalg);
                dlg.show();

                dlg.setListener(new LogoutDialog.IListenerLogout() {
                    @Override
                    public void onLogout() {
                        if (drawer_layout != null)
                            drawer_layout.closeDrawer(GravityCompat.END);
                        setLogout();
                    }
                });

                return false;
            }
        });

        // 캐디수첩
        v.findViewById(R.id.caddieLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new CaddieFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        // 그늘집 주문하기
        v.findViewById(R.id.orderLinearLayout).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GoNativeScreen(new OrderFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        // 공지사항
        v.findViewById(R.id.noticeLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new NoticeFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        // 배토관리
        v.findViewById(R.id.topdressingLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new TopDressingFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        v.findViewById(R.id.paymentLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        v.findViewById(R.id.gpsLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseFragment courseFragment = new CourseFragment();
                GoNativeScreen(courseFragment, null);
                Global.courseFragment = courseFragment;
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        // 설정
        v.findViewById(R.id.settingsLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsCustomDialog = new SettingsCustomDialog(getActivity());
                settingsCustomDialog.show();
            }
        });

        v.findViewById(R.id.scoreLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new ScoreFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        v.findViewById(R.id.scoreBoardLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new ScoreFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        v.findViewById(R.id.nearestLongestLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new ScoreFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
                NearestLongestDialogFragment nearestLongestDialogFragment = new NearestLongestDialogFragment();
                showDialogFragment(nearestLongestDialogFragment);
            }
        });

        v.findViewById(R.id.rankingNormalLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  GoNativeScreen(new ScoreFragment(), null);
                GoNativeScreen(new RankingFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        v.findViewById(R.id.controlLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new ControlFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        v.findViewById(R.id.galleryLinear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new GalleryFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);

            }
        });

        v.findViewById(R.id.closeLinearLayoutViewMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //메인 메뉴만 있을 경우 메뉴가 사라지는 버그가 있음
                if (mParentActivity.getPreviousBaseFragment() != null)
                    drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        showListTextView = v.findViewById(R.id.showListTextView);
        showListTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //목록보기
                view_background.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.ebonyBlack));
                v.findViewById(R.id.header).setVisibility(View.INVISIBLE);
                v.findViewById(R.id.header_list).setVisibility(View.VISIBLE);
                roundingLinearLayout.setVisibility(View.GONE);

                selectTobDivider.setVisibility(View.VISIBLE);
                selectBottomDivider.setVisibility(View.VISIBLE);
                teeUpViewPager.setVisibility(View.VISIBLE);
                teeUpViewPager.setCurrentItem(teeUpViewPager.getCurrentItem());

                //read only
                teeUpAdapter.setListMode(true);

                btnLeftMove.setVisibility(View.VISIBLE);
                btnRightMove.setVisibility(View.VISIBLE);

                btnRightMove.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shape_irisblue_circle));
                btnLeftMove.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shape_irisblue_circle));

                if (teeUpAdapter.getCount() == 1) {
                    btnLeftMove.setVisibility(View.GONE);
                    btnRightMove.setVisibility(View.GONE);
                } else if (teeUpViewPager.getCurrentItem() == 0) {
                    btnLeftMove.setVisibility(View.GONE);
                } else if (teeUpViewPager.getCurrentItem()+1 == teeUpAdapter.getCount()) {
                    btnRightMove.setVisibility(View.GONE);
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTobDivider.setVisibility(View.GONE);
                selectBottomDivider.setVisibility(View.GONE);
                teeUpViewPager.setVisibility(View.GONE);
                roundingLinearLayout.setVisibility(View.VISIBLE);
                btnLeftMove.setVisibility(View.GONE);
                btnRightMove.setVisibility(View.GONE);
                view_background.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.irisBlue));
                v.findViewById(R.id.header).setVisibility(View.VISIBLE);
                v.findViewById(R.id.header_list).setVisibility(View.INVISIBLE);

                //read only 해제
                teeUpAdapter.setListMode(false);

                btnRightMove.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shape_black_circle));
                btnLeftMove.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shape_black_circle));
            }
        });

        disableMenu();
        setDisableColor();
    }

    private void showDialogFragment(DialogFragment dialogFragment) {
        FragmentManager supportFragmentManager = ((MainActivity) (getContext())).getSupportFragmentManager();
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        dialogFragment.show(transaction, TAG);
        assert dialogFragment.getFragmentManager() != null;
        dialogFragment.getFragmentManager().executePendingTransactions();
    }

    private void setLogout() {

        todayReserveTimer.cancel();
        Objects.requireNonNull(getActivity()).stopService((new Intent(getActivity(), CartLocationService.class)));
        getActivity().finish();
    }

    private void getTodayReservesForCaddy(final Context context, String caddy_id) {
        //   showProgress("티업시간을 받아오는 중입니다....");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getTodayReservesForCaddy(caddy_id, new DataInterface.ResponseCallback<TeeUpTime>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(TeeUpTime response) {
                hideProgress();
                systemUIHide();

                if (response == null)
                    return;

                if (response.getRetCode() != null && response.getRetCode().equals("ok")) {

                    //todayReserveTimer.cancel();
                    tvCartNo.setText(response.getCaddyInfo().getCart_no() + "번 카트");
                    caddieNameTextView.setText(response.getCaddyInfo().getName() + " 캐디");
                    Global.teeUpTime = response;
                    //*****************
                    teeUpAdapter = new TeeUpViewPagerAdapter(getContext(), Global.teeUpTime.getTodayReserveList(), new TeeUpViewPagerAdapter.OnAdapterClickListener() {
                        @Override
                        public void onClicked(int position) {

                            int reserveId = getReserveId(response);
                            DataInterface.getInstance(Global.HOST_ADDRESS_AWS).setPlayStatus(context, reserveId, "게임중", new DataInterface.ResponseCallback<ResponseData<Object>>() {
                                @Override
                                public void onSuccess(ResponseData<Object> response) {
                                    selectTobDivider.setVisibility(View.GONE);
                                    selectBottomDivider.setVisibility(View.GONE);
                                    teeUpViewPager.setVisibility(View.GONE);
                                    roundingLinearLayout.setVisibility(View.VISIBLE);
                                    btnLeftMove.setVisibility(View.GONE);
                                    btnRightMove.setVisibility(View.GONE);

                                    TodayReserveList item = teeUpAdapter.getItem(position);
                                    Global.selectedTeeUpIndex = position;
                                    Global.selectedReservation = item;
                                    Global.reserveId = String.valueOf(teeUpAdapter.getItem(position).getId());
                                    groupNameTextView.setText(item.getGroup());
                                    reservationPersonNameTextView.setText(item.getGuestName());
                                    roundingTeeUpTimeTextView.setText(Util.timeMapper(item.getTeeoff()));
                                    setInOutTextView(item.getInoutCourse());
                                    enableMenu();
                                }

                                @Override
                                public void onError(ResponseData<Object> response) {

                                }

                                @Override
                                public void onFailure(Throwable t) {

                                }
                            });
                        }
                    });

                    teeUpViewPager.setAdapter(teeUpAdapter);
                    teeUpViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            btnLeftMove.setVisibility(View.VISIBLE);
                            btnRightMove.setVisibility(View.VISIBLE);

                            if (teeUpAdapter.getCount() == 1) {
                                btnLeftMove.setVisibility(View.GONE);
                                btnRightMove.setVisibility(View.GONE);
                            } else if (position == 0) {
                                btnLeftMove.setVisibility(View.GONE);
                            } else if (position+1 ==  teeUpAdapter.getCount()) {
                                btnRightMove.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onPageSelected(int position) {


                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });

                    btnLeftMove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            teeUpViewPager.setCurrentItem(teeUpViewPager.getCurrentItem()-1);
                        }
                    });
                    btnRightMove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            teeUpViewPager.setCurrentItem(teeUpViewPager.getCurrentItem()+1);
                        }
                    });
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

    private int getReserveId(TeeUpTime response) {
        return response.getTodayReserveList().get(teeUpViewPager.getCurrentItem()).getId();
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

    String inOutMapper(String course) {
        return course + "코스";
    }

    private void startTimer() {

        todayReserveTimer = new Timer();
        todayReserveTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                getTodayReservesForCaddy(getActivity(), Global.CaddyNo);
                //    update();
            }

        }, 10 * 1000, 1000 * 5);
    }


    private void setEnableColor() {

        int color = ContextCompat.getColor(getContext(), R.color.FMB_Color_FF7E8181);

        gpsTextView00.setTextColor(color);
        gpsTextView01.setTextColor(color);
        courseDivider.setBackgroundColor(color);

        scoreBoardTextView00.setTextColor(color);
        scoreBoardTextView01.setTextColor(color);
        scoreBoardDivider.setBackgroundColor(color);

        nearestLongestTextView00.setTextColor(color);
        nearestLongestTextView01.setTextColor(color);
        nearestLongestDivider.setBackgroundColor(color);

        rankingTextView00.setTextColor(color);
        rankingTextView01.setTextColor(color);
        rankingDivider.setBackgroundColor(color);

        caddieTextView00.setTextColor(color);
        caddieTextView01.setTextColor(color);
        caddieDivider.setBackgroundColor(color);

        orderTextView00.setTextColor(color);
        orderTextView01.setTextColor(color);
        orderDivider.setBackgroundColor(color);

        paymentTextView00.setTextColor(color);
        paymentTextView01.setTextColor(color);
        paymentDivider.setBackgroundColor(color);

        topdressingTextView00.setTextColor(color);
        topdressingTextView01.setTextColor(color);
        topdressingDivider.setBackgroundColor(color);

        galleryTextView00.setTextColor(color);
        galleryTextView01.setTextColor(color);
        galleryDivider.setBackgroundColor(color);
    }

    private void enableMenu() {

        getReserveGuestList(Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getId());
        gpsLinear.setEnabled(true);
        scoreBoardLinear.setEnabled(true);
        nearstLongestLinear.setEnabled(true);
        rankingNormalLinear.setEnabled(true);
        caddieLinear.setEnabled(true);
        orderLinear.setEnabled(true);
        paymentLinear.setEnabled(true);
        settingLinear.setEnabled(true);
        scoreLinear.setEnabled(true);
        controlLinear.setEnabled(true);
        closeLinear.setEnabled(true);

        setEnableColor();
    }

    private void setDisableColor() {

        int color = ContextCompat.getColor(getContext(), R.color.FMB_Color_88999999);

        gpsTextView00.setTextColor(color);
        gpsTextView01.setTextColor(color);
        courseDivider.setBackgroundColor(color);

        scoreBoardTextView00.setTextColor(color);
        scoreBoardTextView01.setTextColor(color);
        scoreBoardDivider.setBackgroundColor(color);

        nearestLongestTextView00.setTextColor(color);
        nearestLongestTextView01.setTextColor(color);
        nearestLongestDivider.setBackgroundColor(color);

        rankingTextView00.setTextColor(color);
        rankingTextView01.setTextColor(color);
        rankingDivider.setBackgroundColor(color);

        caddieTextView00.setTextColor(color);
        caddieTextView01.setTextColor(color);
        caddieDivider.setBackgroundColor(color);

        orderTextView00.setTextColor(color);
        orderTextView01.setTextColor(color);
        orderDivider.setBackgroundColor(color);

        paymentTextView00.setTextColor(color);
        paymentTextView01.setTextColor(color);
        paymentDivider.setBackgroundColor(color);

        topdressingTextView00.setTextColor(color);
        topdressingTextView01.setTextColor(color);
        topdressingDivider.setBackgroundColor(color);

        galleryTextView00.setTextColor(color);
        galleryTextView01.setTextColor(color);
        galleryDivider.setBackgroundColor(color);
    }

    private void disableMenu() {
        if ((mParentActivity).getDrawerLayout() != null) {
            (mParentActivity).getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        }

        gpsLinear.setEnabled(false);
        scoreBoardLinear.setEnabled(false);
        nearstLongestLinear.setEnabled(false);
        rankingNormalLinear.setEnabled(false);
        caddieLinear.setEnabled(false);
        orderLinear.setEnabled(false);
        paymentLinear.setEnabled(false);

        settingLinear.setEnabled(false);
        scoreLinear.setEnabled(false);
        controlLinear.setEnabled(false);
        closeLinear.setEnabled(false);

        setDisableColor();
    }

    public void getReserveGuestList(int reserveId) {
        showProgress("게스트의 정보를 받아오는 중입니다....");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getReserveGuestList(reserveId, new DataInterface.ResponseCallback<ReserveGuestList>() {
            @Override
            public void onSuccess(ReserveGuestList response) {

                if (response.getRetMsg().equals("성공")) {
                    Global.guestList = response.getList();
                    //   GoNativeScreen(new CaddieFragment(), null);
                    // drawer_layout.closeDrawer(GravityCompat.END);
                    hideProgress();
                }
            }

            @Override
            public void onError(ReserveGuestList response) {
                Toast.makeText(getContext(), "정보를 받아오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                hideProgress();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                hideProgress();
            }
        });
    }
}
