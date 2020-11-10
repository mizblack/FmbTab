package com.eye3.golfpay.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.eye3.golfpay.R;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.common.UIThread;
import com.eye3.golfpay.dialog.ClubInfoDialog;
import com.eye3.golfpay.listener.IClubInfoListener;
import com.eye3.golfpay.model.guest.CaddieInfo;
import com.eye3.golfpay.model.guest.Guest;
import com.eye3.golfpay.model.guest.GuestInfo;
import com.eye3.golfpay.util.Util;
import com.eye3.golfpay.view.CaddieViewBasicGuestItem;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CaddieMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CaddieMainFragment extends Fragment implements IClubInfoListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "CaddieMainFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Guest> guestList = Global.guestList;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private ImageView ivGrab;
    private CaddieInfo caddieInfo;
    protected static ProgressDialog pd; // 프로그레스바 선언
    public static LinearLayout mGuestViewContainerLinearLayout;

    public CaddieMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CaddieMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CaddieMainFragment newInstance(String param1, String param2) {
        CaddieMainFragment fragment = new CaddieMainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        caddieInfo = new CaddieInfo();

        for (Guest guest: guestList) {
            GuestInfo gi = new GuestInfo();
            gi.setReserveGuestId(guest.getId());
            gi.setCarNo(guest.getCarNumber());
            gi.setGuestMemo(guest.getMemo());
            gi.setHp(guest.getPhoneNumber());
            caddieInfo.getGuestInfo().add(gi);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fr_caddie_main, container, false);
        createGuestBasicView(view.findViewById(R.id.view_main));

        view.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReserveGuestInfo();
            }
        });

        return view;
    }

    public void slidingDown() {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    private void createGuestBasicView(LinearLayout container) {

        mGuestViewContainerLinearLayout = container;
        guestList = Global.guestList;
        for (int i = 0; caddieInfo.getGuestInfo().size() > i; i++) {
            container.addView(createGuestItemView(caddieInfo.getGuestInfo().get(i)));
        }
    }

    private View createGuestItemView(GuestInfo guest) {
        CaddieViewBasicGuestItem guestItemView = new CaddieViewBasicGuestItem(getActivity(), guest, caddieInfo.getGuestInfo().size(), this);

        return guestItemView;
    }

    @Override
    public void onShowClubInfoDlg(String id) {

        ClubInfoDialog dlg = new ClubInfoDialog(getContext(), caddieInfo, findGuestId(id));
        WindowManager.LayoutParams wmlp = dlg.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        dlg.getWindow().getDecorView().setSystemUiVisibility(Util.DlgUIFalg);
        dlg.show();

        dlg.setiListenerDialog(new ClubInfoDialog.IListenerDialog() {
            @Override
            public void onSave() {
                for (int i = 0; i < mGuestViewContainerLinearLayout.getChildCount(); i++) {
                    ((CaddieViewBasicGuestItem)mGuestViewContainerLinearLayout.getChildAt(i)).drawClubInfo(caddieInfo.getGuestInfo().get(i).getClubInfo());
                }
            }
        });
    }

    private int findGuestId(String id) {

        int i = 0;
        for (Guest guest: guestList) {
            if (guest.getId().equals(id)) {
                return i;
            }

            i++;
        }

        return 0;
    }

    private void setReserveGuestInfo() {
//        showProgress("데이터를 전송중입니다." );
//        RequestBody reserveGuestId = null, carNo = null, hp = null, guestMemo = null, teamMemo = null, requestFile = null, requestFile2 = null;
//
//        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).setReserveGuestInfo(reserveGuestId, carNo, hp, guestMemo, teamMemo, signImage, clubImage, new DataInterface.ResponseCallback<GuestInfoResponse>() {
//            @Override
//            public void onSuccess(GuestInfoResponse response) {
//                if ("ok".equals(response.getRetCode())) {
//                    if (Global.guestList.size() - 1 > sendCountNum) {
//                        sendCountNum++;
//                    } else if (sendCountNum == Global.guestList.size() - 1) {
//                        Toast.makeText(getContext(), "전송이 완료되었습니다.", Toast.LENGTH_SHORT).show();
//                        hideProgress();
//                    }
//                }
//
//            }
//
//            @Override
//            public void onError(GuestInfoResponse response) {
//                hideProgress();
//                Toast.makeText(getContext(), response.getRetMsg(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                hideProgress();
//            }
//        });
    }

    protected void showProgress(final String msg) {
        UIThread.executeInUIThread(new Runnable() {
            @Override
            public void run() {
                if (pd == null) {
                    // 객체를 1회만 생성한다.
                    pd = new ProgressDialog(getContext()); // 생성한다.
                    pd.setCancelable(true);
                    // 백키로 닫는 기능을 제거한다.
                }
                pd.setMessage(msg);
                // 원하는 메시지를 세팅한다.
                pd.show();
                // 화면에 띠워라
            }
        });

    }


    // 프로그레스 다이얼로그 숨기기
    protected void hideProgress() {
        UIThread.executeInUIThread(new Runnable() {
            @Override
            public void run() {
                if (pd != null && pd.isShowing()) {
                    // 닫는다 : 객체가 존재하고, 보일때만
                    pd.dismiss();
                }
            }
        });

    }
}
