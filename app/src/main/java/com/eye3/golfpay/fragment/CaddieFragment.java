package com.eye3.golfpay.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.eye3.golfpay.R;
import com.eye3.golfpay.activity.MainActivity;
import com.eye3.golfpay.common.AppDef;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.listener.ITakePhotoListener;
import com.eye3.golfpay.listener.OnEditorFinishListener;
import com.eye3.golfpay.model.guest.Guest;
import com.eye3.golfpay.model.guest.ReserveGuestList;
import com.eye3.golfpay.model.photo.PhotoResponse;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.util.EditorDialogFragment;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CaddieFragment extends BaseFragment {

    protected String TAG = getClass().getSimpleName();
    View v;
    private final TextView[] tvGuestNames = new TextView[5];

    private FragmentManager fragmentManager;
    private CaddieMainFragment caddieMainFragment;
    private FragmentTransaction transaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getReserveGuestList(Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getId());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fr_caddie, container, false);

        tvGuestNames[0] = v.findViewById(R.id.tv_guest_name1);
        tvGuestNames[1] = v.findViewById(R.id.tv_guest_name2);
        tvGuestNames[2] = v.findViewById(R.id.tv_guest_name3);
        tvGuestNames[3] = v.findViewById(R.id.tv_guest_name4);
        tvGuestNames[4] = v.findViewById(R.id.tv_guest_name5);

        mParentActivity.showMainBottomBar();
        fragmentManager = getChildFragmentManager();
        caddieMainFragment = new CaddieMainFragment();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_fragment, caddieMainFragment).commitAllowingStateLoss();

        int i = 0;
        for (Guest guest : Global.guestList) {
            tvGuestNames[i].setVisibility(View.VISIBLE);
            tvGuestNames[i].setText(guest.getGuestName());
            if (guest.getBagName() != null && !guest.getBagName().isEmpty()) {
                tvGuestNames[i].append("(" + guest.getBagName() + ")");
            }
            i++;
        }

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void getReserveGuestList(int reserveId) {
        showProgress("게스트의 정보를 받아오는 중입니다....");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getReserveGuestList(reserveId, new DataInterface.ResponseCallback<ReserveGuestList>() {
            @Override
            public void onSuccess(ReserveGuestList response) {
                if (response.getRetMsg().equals("성공")) {
                    Global.guestList = response.getList();

                    if (Global.guestOrdering != null) {
                        for (int i = 0; i < Global.guestOrdering.size(); i++) {
                            String flag = Global.guestOrdering.get(i);
                            for (int j = i; j < Global.guestList.size(); j++) {
                                if (flag.equals(Global.guestList.get(j).getId())) {
                                    if (i == j) {
                                        break;
                                    }

                                    Collections.swap(Global.guestList, i, j);
                                }
                            }
                        }
                    }

                    hideProgress();
                    update();
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

    public void update() {
        int i = 0;
        for (Guest guest : Global.guestList) {
            tvGuestNames[i].setVisibility(View.VISIBLE);
            tvGuestNames[i].setText(guest.getGuestName());
            if (guest.getBagName() != null && !guest.getBagName().isEmpty()) {
                tvGuestNames[i].append("(" + guest.getBagName() + ")");
            }
            i++;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
