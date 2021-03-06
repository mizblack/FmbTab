package com.eye3.golfpay.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.eye3.golfpay.dialog.LogoutDialog;
import com.eye3.golfpay.model.guest.Guest;
import com.eye3.golfpay.model.guest.GuestDriverYesNo;
import com.eye3.golfpay.model.guest.ReserveGuestList;
import com.eye3.golfpay.model.teeup.TeeUpTime;
import com.eye3.golfpay.model.teeup.TodayReserveList;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;
import com.eye3.golfpay.service.CartLocationService;
import com.eye3.golfpay.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeeUpFragment extends BaseFragment {

    private RecyclerView rvTeam;
    private TeeUpAdapter teeUpAdapter = null;
    private Timer todayReserveTimer;
    private TextView caddieNameTextView;
    public TeeUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTodayReservesForCaddy(Global.CaddyNo);
        //startTimer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_teeup_menu, container, false);

        rvTeam = v.findViewById(R.id.rv_team);
        caddieNameTextView = v.findViewById(R.id.tv_caddie);
        v.findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        v.findViewById(R.id.btn_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTodayReservesForCaddy(Global.CaddyNo);
            }
        });
        return v;
    }

    private void logout() {
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
                requestLogout();
            }
        });
    }

//    private void startTimer() {
//
//        todayReserveTimer = new Timer();
//        todayReserveTimer.schedule(new TimerTask() {
//
//            @Override
//            public void run() {
//                getTodayReservesForCaddy(getActivity(), Global.CaddyNo);
//                //    update();
//            }
//
//        }, 10 * 1000, 1000 * 5);
//    }

//    private void stopTimer() {
//        todayReserveTimer.cancel();
//    }

    private void setLogout() {

        todayReserveTimer.cancel();
        Objects.requireNonNull(getActivity()).stopService((new Intent(getActivity(), CartLocationService.class)));
        getActivity().finish();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void getTodayReservesForCaddy(String caddy_id) {
        showProgress("??????????????? ???????????? ????????????....");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getTodayReservesForCaddy(caddy_id, new DataInterface.ResponseCallback<TeeUpTime>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(TeeUpTime response) {
                hideProgress();
                systemUIHide();

                if (response == null)
                    return;

                if (response.getRetMsg().equals("token error")) {
                    ((MainActivity)mParentActivity).navigationView.setVisibility(View.VISIBLE);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("TokenError", true);
                    GoNavigationDrawer(new LoginFragment(), bundle);

                    mParentActivity.setPreviousBaseFragment(new LoginFragment());
                    mParentActivity.GoRootScreenAdd(null);
                    mParentActivity.hideMainBottomBar();
                    ((MainActivity)mParentActivity).stopLocationUpdates();
                    //((MainActivity)mParentActivity).stopGpsTimerTask();
                    ((MainActivity)mParentActivity).stopTimerTask();
                    //stopTimer();
                    return;
                }

                if (response.getRetCode() != null && response.getRetCode().equals("ok")) {

                    //todayReserveTimer.cancel();

                    Global.teeUpTime = response;
                    Global.caddieName = response.getCaddyInfo().getName();
                    caddieNameTextView.setText(Global.caddieName + " ??????");

                    if (teeUpAdapter == null) {
                        LinearLayoutManager mManager = new LinearLayoutManager(mContext);
                        rvTeam.setHasFixedSize(true);
                        rvTeam.setLayoutManager(mManager);
                        initTeeUpAdapter();
                        rvTeam.setAdapter(teeUpAdapter);
                    }

                    teeUpAdapter.setData(Global.teeUpTime.getTodayReserveList());
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

    private void initTeeUpAdapter() {
        teeUpAdapter = new TeeUpAdapter(mContext, new TeeUpAdapter.IOnClickAdapter() {
            @Override
            public void onAdapterItemClicked(Integer position) {
                int reserveId = Global.teeUpTime.getTodayReserveList().get(position).getId();
//                if (Global.teeUpTime.getTodayReserveList().get(position).getPlayStatus().equals("??????")) {
//                    Toast.makeText(mContext, "???????????? ????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                    
                DataInterface.getInstance(Global.HOST_ADDRESS_AWS).setPlayStatus(mContext, reserveId, "?????????", new DataInterface.ResponseCallback<ResponseData<Object>>() {
                    @Override
                    public void onSuccess(ResponseData<Object> response) {

                        //stopTimer();
                        TodayReserveList item = teeUpAdapter.getItem(position);
                        Global.selectedTeeUpIndex = position;
                        Global.selectedReservation = item;

                        if (Global.guestOrdering != null) {
                            for (int i = 0; i < Global.guestOrdering.size(); i++) {
                                String flag = Global.guestOrdering.get(i);
                                for (int j = i; j < Global.selectedReservation.getGuestData().size(); j++) {
                                    if (flag.equals(Global.selectedReservation.getGuestData().get(j).getId())) {
                                        if (i == j) {
                                            break;
                                        }

                                        Collections.swap(Global.selectedReservation.getGuestData(), i, j);
                                    }
                                }
                            }
                        }
                        Global.reserveId = String.valueOf(teeUpAdapter.getItem(position).getId());
                        getReserveGuestList(Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getId());

                        //////////////////////////////////////////////////////////////////
                        // Debug Mode
//                        ((MainActivity)mParentActivity).startTimerTask();
//                        ((MainActivity)mParentActivity).startListeningUserLocationDebug();
                        ////////////////////////////////////////////////////////////////

                        ////////////////////////////////////////////////////////////////
                        // Real~
                        ((MainActivity)mParentActivity).initLocation();
                        ((MainActivity)mParentActivity).setLaravel();
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
    }

    public void getReserveGuestList(int reserveId) {
        showProgress("???????????? ????????? ???????????? ????????????....");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getReserveGuestList(reserveId, new DataInterface.ResponseCallback<ReserveGuestList>() {
            @Override
            public void onSuccess(ReserveGuestList response) {

                if (response.getRetMsg().equals("??????")) {
                    Global.guestList = response.getList();

                    //???????????? ?????? ??????
                    Global.guestDriverYesNo = new ArrayList<>();
                    for (Guest guest : Global.guestList) {
                        GuestDriverYesNo gd = new GuestDriverYesNo(guest.getId(), false);
                        Global.guestDriverYesNo.add(gd);
                    }

                    //   GoNativeScreen(new CaddieFragment(), null);
                    mParentActivity.getDrawer().closeDrawer(GravityCompat.END, false);
                    hideProgress();

                    //????????? ????????? ?????? ?????????
                    Global.gameSec = 0;
                    Global.gameBeforeSec = 0;
                    Global.gameAfterSec = 0;
                    Global.gameTimeStatus = Global.GameStatus.eNone;

                    ((MainActivity)mParentActivity).navigationView.setVisibility(View.GONE);
                    ((MainActivity)mParentActivity).updateUI();
                    //????????????
                    GoNativeScreen(new CaddieFragment(), null);
                    //GoNativeScreen(new OrderFragment(), null);
                    GoNavigationDrawer(new ViewMenuFragment(), null);
                }
            }

            @Override
            public void onError(ReserveGuestList response) {
                Toast.makeText(getContext(), "????????? ??????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                hideProgress();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                hideProgress();
            }
        });
    }

    public void requestLogout() {
        showProgress("???????????? ????????????....");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).logout(getContext(), new DataInterface.ResponseCallback<ResponseData<Object>>() {
            @Override
            public void onSuccess(ResponseData<Object> response) {
                hideProgress();
                ((MainActivity)mParentActivity).logout();
            }

            @Override
            public void onError(ResponseData<Object> response) {
                hideProgress();
            }

            @Override
            public void onFailure(Throwable t) {
                hideProgress();
            }
        });
    }
}
