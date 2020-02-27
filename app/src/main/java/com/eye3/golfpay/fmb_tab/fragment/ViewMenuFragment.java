package com.eye3.golfpay.fmb_tab.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.guest.Guest;
import com.eye3.golfpay.fmb_tab.model.guest.ReserveGuestList;
import com.eye3.golfpay.fmb_tab.model.teeup.TeeUpTime;
import com.eye3.golfpay.fmb_tab.model.teeup.TodayReserveList;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.service.CartLocationService;
import com.eye3.golfpay.fmb_tab.util.FmbCustomDialog;
import com.eye3.golfpay.fmb_tab.util.SettingsCustomDialog;

import java.util.ArrayList;
import java.util.Objects;

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


    public ViewMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTodayReservesForCaddy(getActivity(), Global.CaddyNo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fr_view_menu, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @SuppressLint("CutPasteId")
    private void init() {
        gpsTextView00 = Objects.requireNonNull(getView()).findViewById(R.id.gpsTextView00);
        gpsTextView01 = getView().findViewById(R.id.gpsTextView01);
        courseDivider = getView().findViewById(R.id.courseDivider);

        scoreBoardTextView00 = getView().findViewById(R.id.scoreBoardTextView00);
        scoreBoardTextView01 = getView().findViewById(R.id.scoreBoardTextView01);
        scoreBoardDivider = getView().findViewById(R.id.scoreBoardDivider);

        nearestLongestTextView00 = getView().findViewById(R.id.nearestLongestTextView00);
        nearestLongestTextView01 = getView().findViewById(R.id.nearestLongestTextView01);
        nearestLongestDivider = getView().findViewById(R.id.nearestLongestDivider);

        rankingTextView00 = getView().findViewById(R.id.rankingTextView00);
        rankingTextView01 = getView().findViewById(R.id.rankingTextView01);
        rankingDivider = getView().findViewById(R.id.rankingDivider);

        caddieTextView00 = getView().findViewById(R.id.caddieTextView00);
        caddieTextView01 = getView().findViewById(R.id.caddieTextView01);
        caddieDivider = getView().findViewById(R.id.caddieDivider);

        orderTextView00 = getView().findViewById(R.id.orderTextView00);
        orderTextView01 = getView().findViewById(R.id.orderTextView01);
        orderDivider = getView().findViewById(R.id.orderDivider);

        paymentTextView00 = getView().findViewById(R.id.paymentTextView00);
        paymentTextView01 = getView().findViewById(R.id.paymentTextView01);
        paymentDivider = getView().findViewById(R.id.paymentDivider);

        topdressingTextView00 = getView().findViewById(R.id.topdressingTextView00);
        topdressingTextView01 = getView().findViewById(R.id.topdressingTextView01);
        topdressingDivider = getView().findViewById(R.id.topdressingDivider);

        galleryTextView00 = getView().findViewById(R.id.galleryTextView00);
        galleryTextView01 = getView().findViewById(R.id.galleryTextView01);
        galleryDivider = getView().findViewById(R.id.galleryDivider);

        drawer_layout = (mParentActivity).findViewById(R.id.drawer_layout);
        selectTobDivider = Objects.requireNonNull(getView()).findViewById(R.id.selectTobDivider);
        selectBottomDivider = getView().findViewById(R.id.selectBottomDivider);

        tvCartNo = getView().findViewById(R.id.cart_no);
        teeUpRecyclerView = getView().findViewById(R.id.teeUpRecyclerView);
        TextView showListTextView = getView().findViewById(R.id.showListTextView);
        showListTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTobDivider.setVisibility(View.VISIBLE);
                selectBottomDivider.setVisibility(View.VISIBLE);
                teeUpRecyclerView.setVisibility(View.VISIBLE);
                roundingLinearLayout.setVisibility(View.GONE);

                disableMenu();
                setDisableColor();
            }
        });
        groupNameTextView = getView().findViewById(R.id.groupNameTextView);
        reservationPersonNameTextView = getView().findViewById(R.id.reservationPersonNameTextView);
        roundingTeeUpTimeTextView = getView().findViewById(R.id.teeUpTimeTextView);
        inOutTextView00 = getView().findViewById(R.id.inOutTextView00);
        inOutTextView01 = getView().findViewById(R.id.inOutTextView01);
        roundingLinearLayout = getView().findViewById(R.id.roundingLinearLayout);
        TextView controlTxtView = Objects.requireNonNull(getActivity()).findViewById(R.id.main_bottom_bar).findViewById(R.id.controlTextView);

        selectBottomDivider.setVisibility(View.VISIBLE);
        teeUpRecyclerView.setVisibility(View.VISIBLE);
        roundingLinearLayout.setVisibility(View.GONE);


        controlTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new ControlFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        ImageView markView = getActivity().findViewById(R.id.main_bottom_bar).findViewById(R.id.mark);
        markView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.openDrawer(GravityCompat.END);
            }
        });

        getView().findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fmbDialog = new FmbCustomDialog(getActivity(), "Logout", "로그아웃 하시겠습니까?", "아니오", "네", leftListener, rightListener, true);
                fmbDialog.show();
            }
        });


        // 캐디수첩
        getView().findViewById(R.id.caddieLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReserveGuestList(Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getId());
            }
        });

        // 그늘집 주문하기
        getView().findViewById(R.id.orderLinearLayout).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GoNativeScreen(new OrderFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        // 공지사항
        getView().findViewById(R.id.noticeLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new NoticeFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        getView().findViewById(R.id.paymentLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        getView().findViewById(R.id.gpsLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseFragment courseFragment = new CourseFragment();
                GoNativeScreen(courseFragment, null);
                Global.courseFragment = courseFragment;
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        // 설정
        getView().findViewById(R.id.settingsLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsCustomDialog = new SettingsCustomDialog(getActivity());
                settingsCustomDialog.show();
            }
        });

        getView().findViewById(R.id.scoreLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new ScoreFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        getView().findViewById(R.id.scoreBoardLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new ScoreFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        getView().findViewById(R.id.nearestLongestLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new NearestLongestFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        getView().findViewById(R.id.rankingNormalLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  GoNativeScreen(new ScoreFragment(), null);
                GoNativeScreen(new RankingFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        getView().findViewById(R.id.controlLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new ControlFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        getView().findViewById(R.id.closeLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.closeDrawer(GravityCompat.END);

            }
        });

        showListTextView = getView().findViewById(R.id.showListTextView);
        showListTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        showProgress("티업시간을 받아오는 중입니다....");
        DataInterface.getInstance(Global.HOST_ADDRESS_DEV).getTodayReservesForCaddy(caddy_id, new DataInterface.ResponseCallback<TeeUpTime>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(TeeUpTime response) {
                hideProgress();
                systemUIHide();

                if (response.getRetCode().equals("ok")) {
                    Toast.makeText(context, "안녕하세요 " + response.getCaddyInfo().getName() + "님!\n티업시간을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    tvCartNo.setText(response.getCaddyInfo().getCart_no() + "번 카트");
                    caddieNameTextView = Objects.requireNonNull(getView()).findViewById(R.id.caddieNameTextView);
                    caddieNameTextView.setText(response.getCaddyInfo().getName() + " 캐디");
                    Global.teeUpTime = response;

                    teeUpAdapter = new TeeUpAdapter(Global.teeUpTime.getTodayReserveList());
                    teeUpRecyclerView = getView().findViewById(R.id.teeUpRecyclerView);
                    teeUpRecyclerView.setHasFixedSize(true);
                    LinearLayoutManager manager = new LinearLayoutManager(getActivity());
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
        if (mParentActivity.getDrawer() != null) {
            mParentActivity.getDrawer().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

        Objects.requireNonNull(getView()).findViewById(R.id.gpsLinearLayout).setEnabled(true);
        getView().findViewById(R.id.scoreBoardLinearLayout).setEnabled(true);
        getView().findViewById(R.id.nearestLongestLinearLayout).setEnabled(true);
        getView().findViewById(R.id.rankingNormalLinearLayout).setEnabled(true);
        getView().findViewById(R.id.caddieLinearLayout).setEnabled(true);
        getView().findViewById(R.id.orderLinearLayout).setEnabled(true);
        getView().findViewById(R.id.paymentLinearLayout).setEnabled(true);

        getView().findViewById(R.id.settingsLinearLayout).setEnabled(true);
        getView().findViewById(R.id.scoreLinearLayout).setEnabled(true);
        getView().findViewById(R.id.controlLinearLayout).setEnabled(true);
        getView().findViewById(R.id.closeLinearLayout).setEnabled(true);

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

        Objects.requireNonNull(getView()).findViewById(R.id.gpsLinearLayout).setEnabled(false);
        getView().findViewById(R.id.scoreBoardLinearLayout).setEnabled(false);
        getView().findViewById(R.id.nearestLongestLinearLayout).setEnabled(false);
        getView().findViewById(R.id.rankingNormalLinearLayout).setEnabled(false);
        getView().findViewById(R.id.caddieLinearLayout).setEnabled(false);
        getView().findViewById(R.id.orderLinearLayout).setEnabled(false);
        getView().findViewById(R.id.paymentLinearLayout).setEnabled(false);

        getView().findViewById(R.id.settingsLinearLayout).setEnabled(false);
        getView().findViewById(R.id.scoreLinearLayout).setEnabled(false);
        getView().findViewById(R.id.controlLinearLayout).setEnabled(false);
        getView().findViewById(R.id.closeLinearLayout).setEnabled(false);

        setDisableColor();
    }

    private class TeeUpAdapter extends RecyclerView.Adapter<TeeUpAdapter.TeeUpTimeItemViewHolder> {

        ArrayList<TodayReserveList> todayReserveList;
        TextView teeUpTimeTextView, reservationGuestNameTextView;

        TeeUpAdapter(ArrayList<TodayReserveList> todayReserveList) {
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
        public TeeUpAdapter.TeeUpTimeItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tee_up, viewGroup, false);
            return new TeeUpAdapter.TeeUpTimeItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TeeUpAdapter.TeeUpTimeItemViewHolder scoreItemViewHolder, int position) {
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

    private void getReserveGuestList(int reserveId) {
        showProgress("플레이어의 정보를 받아오는 중입니다....");
        DataInterface.getInstance(Global.HOST_ADDRESS_DEV).getReserveGuestList(reserveId, new DataInterface.ResponseCallback<ReserveGuestList>() {
            @Override
            public void onSuccess(ReserveGuestList response) {
                if (response.getRetMsg().equals("성공")) {
                    Global.guestArrayList = response.getList();
                    GoNativeScreen(new CaddieFragment(), null);
                    drawer_layout.closeDrawer(GravityCompat.END);
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
