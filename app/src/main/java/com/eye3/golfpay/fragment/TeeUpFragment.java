package com.eye3.golfpay.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.R;
import com.eye3.golfpay.activity.MainActivity;
import com.eye3.golfpay.adapter.TeeUpAdapter;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.model.guest.ReserveGuestList;
import com.eye3.golfpay.model.teeup.TeeUpTime;
import com.eye3.golfpay.model.teeup.TodayReserveList;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;
import com.eye3.golfpay.service.CartLocationService;
import com.eye3.golfpay.util.Util;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeeUpFragment extends BaseFragment {

    private RecyclerView rvTeam;
    private TeeUpAdapter teeUpAdapter;
    private Timer todayReserveTimer;
    private TextView caddieNameTextView;
    public TeeUpFragment() {
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
        View v = inflater.inflate(R.layout.fr_teeup_menu, container, false);

        rvTeam = v.findViewById(R.id.rv_team);
        caddieNameTextView = v.findViewById(R.id.tv_caddie);
        return v;
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

    private void setLogout() {

        todayReserveTimer.cancel();
        Objects.requireNonNull(getActivity()).stopService((new Intent(getActivity(), CartLocationService.class)));
        getActivity().finish();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

                    Global.teeUpTime = response;
                    Global.caddieName = response.getCaddyInfo().getName();
                    caddieNameTextView.setText(Global.caddieName + " 캐디");

                    //*****************
                    LinearLayoutManager mManager = new LinearLayoutManager(mContext);
                    rvTeam.setHasFixedSize(true);
                    rvTeam.setLayoutManager(mManager);

                    teeUpAdapter = new TeeUpAdapter(mContext, new TeeUpAdapter.IOnClickAdapter() {
                        @Override
                        public void onAdapterItemClicked(Integer position) {
                            int reserveId = response.getTodayReserveList().get(position).getId();
                            DataInterface.getInstance(Global.HOST_ADDRESS_AWS).setPlayStatus(mContext, reserveId, "게임중", new DataInterface.ResponseCallback<ResponseData<Object>>() {
                                @Override
                                public void onSuccess(ResponseData<Object> response) {

                                    TodayReserveList item = teeUpAdapter.getItem(position);
                                    Global.selectedTeeUpIndex = position;
                                    Global.selectedReservation = item;
                                    Global.reserveId = String.valueOf(teeUpAdapter.getItem(position).getId());
                                    getReserveGuestList(Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getId());
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
                    teeUpAdapter.setData(Global.teeUpTime.getTodayReserveList());
                    rvTeam.setAdapter(teeUpAdapter);
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

    public void getReserveGuestList(int reserveId) {
        showProgress("게스트의 정보를 받아오는 중입니다....");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getReserveGuestList(reserveId, new DataInterface.ResponseCallback<ReserveGuestList>() {
            @Override
            public void onSuccess(ReserveGuestList response) {

                if (response.getRetMsg().equals("성공")) {
                    Global.guestList = response.getList();
                    //   GoNativeScreen(new CaddieFragment(), null);
                    mParentActivity.getDrawer().closeDrawer(GravityCompat.END, false);
                    hideProgress();

                    ((MainActivity)mParentActivity).navigationView.setVisibility(View.GONE);
                    ((MainActivity)mParentActivity).updateUI();
                    GoNativeScreen(new CaddieFragment(), null);
                    GoNavigationDrawer(new ViewMenuFragment(), null);
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
