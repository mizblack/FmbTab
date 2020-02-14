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
import com.eye3.golfpay.fmb_tab.activity.MainActivity;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.teeup.TeeUpTime;
import com.eye3.golfpay.fmb_tab.model.teeup.TodayReserveList;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.service.CartLocationService;
import com.eye3.golfpay.fmb_tab.util.FmbCustomDialog;
import com.eye3.golfpay.fmb_tab.util.SettingsCustomDialog;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewMenuFragment extends BaseFragment {
    RecyclerView teeUpRecyclerView;
    TeeUpAdapter teeUpAdapter;
    TextView gpsTxtView, scoreTxtView, controlTxtView, caddieNameTextView, groupNameTextView, reservationPersonNameTextView, roundingTeeUpTimeTextView, inOutTextView00, inOutTextView01, showListTextView, tvCartNo;
    ImageView markView, cancelView;
    View selectTobDivider, selectBottomDivider, roundingLinearLayout;
    DrawerLayout drawer_layout;
    FmbCustomDialog fmbDialog;
    SettingsCustomDialog settingsCustomDialog;

    public ViewMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTodayReservesForCaddy(getActivity(), Global.CaddyNo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_view_menu, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();

    }

    private void init() {
        drawer_layout = (mParentActivity).findViewById(R.id.drawer_layout);
        selectTobDivider = getView().findViewById(R.id.selectTobDivider);
        selectBottomDivider = getView().findViewById(R.id.selectBottomDivider);

        tvCartNo = getView().findViewById(R.id.cart_no);
        teeUpRecyclerView = getView().findViewById(R.id.teeUpRecyclerView);
        showListTextView = getView().findViewById(R.id.showListTextView);
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
        groupNameTextView = getView().findViewById(R.id.groupNameTextView);
        reservationPersonNameTextView = getView().findViewById(R.id.reservationPersonNameTextView);
        roundingTeeUpTimeTextView = getView().findViewById(R.id.teeUpTimeTextView);
        inOutTextView00 = getView().findViewById(R.id.inOutTextView00);
        inOutTextView01 = getView().findViewById(R.id.inOutTextView01);
        roundingLinearLayout = getView().findViewById(R.id.roundingLinearLayout);
        controlTxtView = getActivity().findViewById(R.id.main_bottom_bar).findViewById(R.id.controlTextView);

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

        markView = getActivity().findViewById(R.id.main_bottom_bar).findViewById(R.id.mark);
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
                Bundle caddieFragmentBundle = new Bundle();
                caddieFragmentBundle.putInt("selectedTeeUpIndex", Global.selectedTeeUpIndex);
                GoNativeScreen(new CaddieFragment(), caddieFragmentBundle);
                drawer_layout.closeDrawer(GravityCompat.END);
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
                GoNativeScreen(new CourseFragment(), null);
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
            }
        });


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

        getActivity().stopService((new Intent(getActivity(), CartLocationService.class)));
        getActivity().finish();
    }


    private void getTodayReservesForCaddy(final Context context, String caddy_id) {
        showProgress("티업시간을 받아오는 중입니다....");
        DataInterface.getInstance(Global.HOST_ADDRESS_DEV).getTodayReservesForCaddy(getActivity(), caddy_id, new DataInterface.ResponseCallback<TeeUpTime>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(TeeUpTime response) {
                hideProgress();
                systemUIHide();

                if (response.getRetCode().equals("ok")) {
                    //    GoNativeScreen(new ScoreFragment(), null);
                    Toast.makeText(context, "안녕하세요 " + response.getCaddyInfo().getName() + "님!\n티업시간을 선택해주세요.", Toast.LENGTH_LONG).show();
                    tvCartNo.setText(response.getCaddyInfo().getCart_no() + "번 카트");
                    caddieNameTextView = getView().findViewById(R.id.caddieNameTextView);
                    caddieNameTextView.setText(response.getCaddyInfo().getName() + " 캐디");
                    Global.teeUpTime = response;

                    teeUpAdapter = new TeeUpAdapter(getActivity(), Global.teeUpTime.getTodayReserveList());
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

    public void disableMenu() {
        if ((mParentActivity).getDrawerLayout() != null)
            (mParentActivity).getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);

        getView().findViewById(R.id.gpsLinearLayout).setEnabled(false);
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

    void setDisableColor() {
        TextView gpsTextView00 = getView().findViewById(R.id.gpsTextView00);
        TextView gpsTextView01 = getView().findViewById(R.id.gpsTextView01);
        gpsTextView00.setTextColor(0x88999999);
        gpsTextView01.setTextColor(0x88999999);

        TextView scoreBoardTextView00 = getView().findViewById(R.id.scoreBoardTextView00);
        TextView scoreBoardTextView01 = getView().findViewById(R.id.scoreBoardTextView01);
        scoreBoardTextView00.setTextColor(0x88999999);
        scoreBoardTextView01.setTextColor(0x88999999);

        TextView nearestLongestTextView00 = getView().findViewById(R.id.nearestLongestTextView00);
        TextView nearestLongestTextView01 = getView().findViewById(R.id.nearestLongestTextView01);
        nearestLongestTextView00.setTextColor(0x88999999);
        nearestLongestTextView01.setTextColor(0x88999999);

        TextView rankingTextView00 = getView().findViewById(R.id.rankingTextView00);
        TextView rankingTextView01 = getView().findViewById(R.id.rankingTextView01);
        rankingTextView00.setTextColor(0x88999999);
        rankingTextView01.setTextColor(0x88999999);

        TextView caddieTextView00 = getView().findViewById(R.id.caddieTextView00);
        TextView caddieTextView01 = getView().findViewById(R.id.caddieTextView01);
        caddieTextView00.setTextColor(0x88999999);
        caddieTextView01.setTextColor(0x88999999);

        TextView orderTextView00 = getView().findViewById(R.id.orderTextView00);
        TextView orderTextView01 = getView().findViewById(R.id.orderTextView01);
        orderTextView00.setTextColor(0x88999999);
        orderTextView01.setTextColor(0x88999999);

        TextView paymentTextView00 = getView().findViewById(R.id.paymentTextView00);
        TextView paymentTextView01 = getView().findViewById(R.id.paymentTextView01);
        paymentTextView00.setTextColor(0x88999999);
        paymentTextView01.setTextColor(0x88999999);
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

        private void enableMenu() {
            if (((MainActivity) mParentActivity).getDrawer() != null)
                ((MainActivity) mParentActivity).getDrawer().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            getView().findViewById(R.id.gpsLinearLayout).setEnabled(true);
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


        void setEnableColor() {
            TextView gpsTextView00 = getView().findViewById(R.id.gpsTextView00);
            TextView gpsTextView01 = getView().findViewById(R.id.gpsTextView01);
            gpsTextView00.setTextColor(0xff7e8181);
            gpsTextView01.setTextColor(0xff7e8181);

            TextView scoreBoardTextView00 = getView().findViewById(R.id.scoreBoardTextView00);
            TextView scoreBoardTextView01 = getView().findViewById(R.id.scoreBoardTextView01);
            scoreBoardTextView00.setTextColor(0xff7e8181);
            scoreBoardTextView01.setTextColor(0xff7e8181);

            TextView nearestLongestTextView00 = getView().findViewById(R.id.nearestLongestTextView00);
            TextView nearestLongestTextView01 = getView().findViewById(R.id.nearestLongestTextView01);
            nearestLongestTextView00.setTextColor(0xff7e8181);
            nearestLongestTextView01.setTextColor(0xff7e8181);

            TextView rankingTextView00 = getView().findViewById(R.id.rankingTextView00);
            TextView rankingTextView01 = getView().findViewById(R.id.rankingTextView01);
            rankingTextView00.setTextColor(0xff7e8181);
            rankingTextView01.setTextColor(0xff7e8181);

            TextView caddieTextView00 = getView().findViewById(R.id.caddieTextView00);
            TextView caddieTextView01 = getView().findViewById(R.id.caddieTextView01);
            caddieTextView00.setTextColor(0xff7e8181);
            caddieTextView01.setTextColor(0xff7e8181);

            TextView orderTextView00 = getView().findViewById(R.id.orderTextView00);
            TextView orderTextView01 = getView().findViewById(R.id.orderTextView01);
            orderTextView00.setTextColor(0xff7e8181);
            orderTextView01.setTextColor(0xff7e8181);

            TextView paymentTextView00 = getView().findViewById(R.id.paymentTextView00);
            TextView paymentTextView01 = getView().findViewById(R.id.paymentTextView01);
            paymentTextView00.setTextColor(0xff7e8181);
            paymentTextView01.setTextColor(0xff7e8181);
        }

        void setDisableColor() {
            TextView gpsTextView00 = getView().findViewById(R.id.gpsTextView00);
            TextView gpsTextView01 = getView().findViewById(R.id.gpsTextView01);
            gpsTextView00.setTextColor(0x88999999);
            gpsTextView01.setTextColor(0x88999999);

            TextView scoreBoardTextView00 = getView().findViewById(R.id.scoreBoardTextView00);
            TextView scoreBoardTextView01 = getView().findViewById(R.id.scoreBoardTextView01);
            scoreBoardTextView00.setTextColor(0x88999999);
            scoreBoardTextView01.setTextColor(0x88999999);

            TextView nearestLongestTextView00 = getView().findViewById(R.id.nearestLongestTextView00);
            TextView nearestLongestTextView01 = getView().findViewById(R.id.nearestLongestTextView01);
            nearestLongestTextView00.setTextColor(0x88999999);
            nearestLongestTextView01.setTextColor(0x88999999);

            TextView rankingTextView00 = getView().findViewById(R.id.rankingTextView00);
            TextView rankingTextView01 = getView().findViewById(R.id.rankingTextView01);
            rankingTextView00.setTextColor(0x88999999);
            rankingTextView01.setTextColor(0x88999999);

            TextView caddieTextView00 = getView().findViewById(R.id.caddieTextView00);
            TextView caddieTextView01 = getView().findViewById(R.id.caddieTextView01);
            caddieTextView00.setTextColor(0x88999999);
            caddieTextView01.setTextColor(0x88999999);

            TextView orderTextView00 = getView().findViewById(R.id.orderTextView00);
            TextView orderTextView01 = getView().findViewById(R.id.orderTextView01);
            orderTextView00.setTextColor(0x88999999);
            orderTextView01.setTextColor(0x88999999);

            TextView paymentTextView00 = getView().findViewById(R.id.paymentTextView00);
            TextView paymentTextView01 = getView().findViewById(R.id.paymentTextView01);
            paymentTextView00.setTextColor(0x88999999);
            paymentTextView01.setTextColor(0x88999999);
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


}
