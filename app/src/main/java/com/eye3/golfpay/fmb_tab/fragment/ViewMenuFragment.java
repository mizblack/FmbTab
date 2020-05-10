package com.eye3.golfpay.fmb_tab.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.guest.ReserveGuestList;
import com.eye3.golfpay.fmb_tab.model.teeup.TeeUpTime;
import com.eye3.golfpay.fmb_tab.model.teeup.TodayReserveList;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.service.CartLocationService;
import com.eye3.golfpay.fmb_tab.util.FmbCustomDialog;
import com.eye3.golfpay.fmb_tab.util.SettingsCustomDialog;
import com.eye3.golfpay.fmb_tab.view.VisitorsGuestItem;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewMenuFragment extends BaseFragment {
    private RecyclerView teeUpRecyclerView;
    private TeeUpAdapter teeUpAdapter;
    private TextView caddieNameTextView;
    private TextView groupNameTextView;
    private TextView reservationPersonNameTextView;
    private TextView roundingTeeUpTimeTextView;
    private TextView inOutTextView00;
    private TextView inOutTextView01;
    private TextView tvCartNo;
    private TextView controlTxtView;
    private View selectTobDivider, selectBottomDivider, roundingLinearLayout;
    private DrawerLayout drawer_layout;
    private FmbCustomDialog fmbDialog;
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
            orderLinear, paymentLinear, settingLinear, scoreLinear, controlLinear, closeLinear , caddieCancelLinearLayout;

    Timer timer;

    TextView mTvRoundStartfinish;

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
        teeUpRecyclerView = v.findViewById(R.id.teeUpRecyclerView);
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
        teeUpRecyclerView = v.findViewById(R.id.teeUpRecyclerView);
        mTvRoundStartfinish = v.findViewById(R.id.first_round_start);
        mTvRoundStartfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView) v).setText("전반종료");
            }
        });
        TextView showListTextView = v.findViewById(R.id.showListTextView);
        showListTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTobDivider.setVisibility(View.VISIBLE);
                selectBottomDivider.setVisibility(View.VISIBLE);
                teeUpRecyclerView.setVisibility(View.VISIBLE);
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
        teeUpRecyclerView.setVisibility(View.VISIBLE);
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
                fmbDialog = new FmbCustomDialog(getActivity(), "Logout", "로그아웃 하시겠습니까?", "아니오", "네", leftListener, rightListener, true);
                fmbDialog.show();

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
                GoNativeScreen(new OrderFragment2(), null);
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
                Toast.makeText(mContext, "개발중입니다. ", Toast.LENGTH_SHORT).show();
//                GoNativeScreen(new ScoreFragment(), null);
//                NearestLongestDialogFragment nearestLongestDialogFragment = new NearestLongestDialogFragment();
//                assert getFragmentManager() != null;
//                nearestLongestDialogFragment.show(getFragmentManager(), TAG);
//                drawer_layout.closeDrawer(GravityCompat.END);
//                systemUIHide();
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
                drawer_layout.closeDrawer(GravityCompat.END);

            }
        });

        showListTextView = v.findViewById(R.id.showListTextView);
        showListTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTobDivider.setVisibility(View.VISIBLE);
                selectBottomDivider.setVisibility(View.VISIBLE);
                teeUpRecyclerView.setVisibility(View.VISIBLE);
                roundingLinearLayout.setVisibility(View.GONE);

                disableMenu();
                setDisableColor();
            }
        });

        disableMenu();
        setDisableColor();

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
            if (drawer_layout != null)
                drawer_layout.closeDrawer(GravityCompat.END);
            fmbDialog.dismiss();
            setLogout();
        }
    };

    private void setLogout() {

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

                if (response.getRetCode() != null && response.getRetCode().equals("ok")) {
                    tvCartNo.setText(response.getCaddyInfo().getCart_no() + "번 카트");
                    caddieNameTextView.setText(response.getCaddyInfo().getName() + " 캐디");
                    Global.teeUpTime = response;
                    teeUpAdapter = new TeeUpAdapter(Global.teeUpTime.getTodayReserveList());
                    teeUpRecyclerView.setHasFixedSize(true);
                    LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                    teeUpRecyclerView.setLayoutManager(manager);
                    teeUpRecyclerView.setAdapter(teeUpAdapter);
                    teeUpAdapter.notifyDataSetChanged();
                    if(mContext != null)
                        Toast.makeText(getActivity(), "안녕하세요 " + response.getCaddyInfo().getName() + "님! \n티업시간을 선택해주세요.", Toast.LENGTH_SHORT).show();


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

    private void startTimer() {

        timer = new Timer();

        timer.schedule(new TimerTask() {

            @Override

            public void run() {

                getTodayReservesForCaddy(getActivity(), Global.CaddyNo);


                //    update();

            }

        }, 10 * 1000, 1000 * 5);

    }


    private void setEnableColor() {
        gpsTextView00.setTextColor(0xff7e8181);
        gpsTextView01.setTextColor(0xff7e8181);
        courseDivider.setBackgroundColor(0xff7e8181);

        scoreBoardTextView00.setTextColor(0xff7e8181);
        scoreBoardTextView01.setTextColor(0xff7e8181);
        scoreBoardDivider.setBackgroundColor(0xff7e8181);

        nearestLongestTextView00.setTextColor(0xff7e8181);
        nearestLongestTextView01.setTextColor(0xff7e8181);
        nearestLongestDivider.setBackgroundColor(0xff7e8181);

        rankingTextView00.setTextColor(0xff7e8181);
        rankingTextView01.setTextColor(0xff7e8181);
        rankingDivider.setBackgroundColor(0xff7e8181);

        caddieTextView00.setTextColor(0xff7e8181);
        caddieTextView01.setTextColor(0xff7e8181);
        caddieDivider.setBackgroundColor(0xff7e8181);

        orderTextView00.setTextColor(0xff7e8181);
        orderTextView01.setTextColor(0xff7e8181);
        orderDivider.setBackgroundColor(0xff7e8181);

        paymentTextView00.setTextColor(0xff7e8181);
        paymentTextView01.setTextColor(0xff7e8181);
        paymentDivider.setBackgroundColor(0xff7e8181);

        topdressingTextView00.setTextColor(0xff7e8181);
        topdressingTextView01.setTextColor(0xff7e8181);
        topdressingDivider.setBackgroundColor(0xff7e8181);

        galleryTextView00.setTextColor(0xff7e8181);
        galleryTextView01.setTextColor(0xff7e8181);
        galleryDivider.setBackgroundColor(0xff7e8181);
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
        gpsTextView00.setTextColor(0x88999999);
        gpsTextView01.setTextColor(0x88999999);
        courseDivider.setBackgroundColor(0x88999999);

        scoreBoardTextView00.setTextColor(0x88999999);
        scoreBoardTextView01.setTextColor(0x88999999);
        scoreBoardDivider.setBackgroundColor(0x88999999);

        nearestLongestTextView00.setTextColor(0x88999999);
        nearestLongestTextView01.setTextColor(0x88999999);
        nearestLongestDivider.setBackgroundColor(0x88999999);

        rankingTextView00.setTextColor(0x88999999);
        rankingTextView01.setTextColor(0x88999999);
        rankingDivider.setBackgroundColor(0x88999999);

        caddieTextView00.setTextColor(0x88999999);
        caddieTextView01.setTextColor(0x88999999);
        caddieDivider.setBackgroundColor(0x88999999);

        orderTextView00.setTextColor(0x88999999);
        orderTextView01.setTextColor(0x88999999);
        orderDivider.setBackgroundColor(0x88999999);

        paymentTextView00.setTextColor(0x88999999);
        paymentTextView01.setTextColor(0x88999999);
        paymentDivider.setBackgroundColor(0x88999999);

        topdressingTextView00.setTextColor(0x88999999);
        topdressingTextView01.setTextColor(0x88999999);
        topdressingDivider.setBackgroundColor(0x88999999);

        galleryTextView00.setTextColor(0x88999999);
        galleryTextView01.setTextColor(0x88999999);
        galleryDivider.setBackgroundColor(0x88999999);
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

    private class TeeUpAdapter extends RecyclerView.Adapter<TeeUpAdapter.TeeUpTimeItemViewHolder> {

        View mMenuView, divider;

        ArrayList<TodayReserveList> todayReserveList;
        TextView teeUpTimeTextView, reservationGuestNameTextView, startTextView;
        LinearLayout visitorsGuestItemLinearLayout;

        TeeUpAdapter(ArrayList<TodayReserveList> todayReserveList) {
            this.todayReserveList = todayReserveList;
        }

        class TeeUpTimeItemViewHolder extends RecyclerView.ViewHolder {

            TeeUpTimeItemViewHolder(View v) {
                super(v);

                teeUpTimeTextView = v.findViewById(R.id.teeUpTimeTextView);
                reservationGuestNameTextView = v.findViewById(R.id.reservationPersonNameTextView);
                visitorsGuestItemLinearLayout = v.findViewById(R.id.visitorsGuestItemLinearLayout);
                divider = v.findViewById(R.id.divider);
                startTextView = v.findViewById(R.id.startTextView);
                //    mCurrentItemView = itemView;
                startTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timer.cancel();
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
        public TeeUpAdapter.TeeUpTimeItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            mMenuView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tee_up, viewGroup, false);
            return new TeeUpAdapter.TeeUpTimeItemViewHolder(mMenuView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull TeeUpAdapter.TeeUpTimeItemViewHolder scoreItemViewHolder, int position) {
       //     if (position == 0) {
                mMenuView.setBackgroundResource(R.drawable.shape_black_edge);
//                divider.setVisibility(View.VISIBLE);
                startTextView.setText("시작");
                startTextView.setTextColor(0xff00abc5);
      //      }
            teeUpTimeTextView.setText(todayReserveList.get(position).getInoutCourse() + " " + timeMapper(todayReserveList.get(position).getTeeoff()));
            reservationGuestNameTextView.setText(todayReserveList.get(position).getGuestName());
            for (int i = 0; i < todayReserveList.get(position).getGuestData().size(); i++) {
                VisitorsGuestItem visitorsGuestItem = new VisitorsGuestItem(mContext);
                TextView memberNameTextView = visitorsGuestItem.findViewById(R.id.memberNameTextView);
                ImageView ivCheckin = visitorsGuestItem.findViewById(R.id.iv_checkin);
                memberNameTextView.setText(todayReserveList.get(position).getGuestData().get(i).getGuestName());
                if ("N".equals(todayReserveList.get(position).getGuestData().get(i).getCheckin())) {
                    //내장객이 입장을 안했을때
                    //  ivCheckin.setImageAlpha(50);
                    ivCheckin.setVisibility(View.INVISIBLE);
                    startTextView.setEnabled(false);
                }
                visitorsGuestItemLinearLayout.addView(visitorsGuestItem);

            }

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
//            timeString = time.split(":")[0] + ":" + time.split(":")[1] + amOrPm;
            timeString = time.split(":")[0] + ":" + time.split(":")[1];
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
