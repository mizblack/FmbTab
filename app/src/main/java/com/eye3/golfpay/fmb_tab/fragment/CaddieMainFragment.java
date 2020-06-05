package com.eye3.golfpay.fmb_tab.fragment;

import android.animation.StateListAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.guest.Guest;
import com.eye3.golfpay.fmb_tab.view.CaddieViewBasicGuestItem;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CaddieMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CaddieMainFragment extends Fragment {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fr_caddie_main, container, false);
        createGuestBasicView(view.findViewById(R.id.view_main));

        return view;
    }

    public void slidingDown() {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    private void createGuestBasicView(LinearLayout container) {

        mGuestViewContainerLinearLayout = container;
        guestList = Global.guestList;
        for (int i = 0; guestList.size() > i; i++) {
            container.addView(createGuestItemView(guestList.get(i)));
        }
    }

    private View createGuestItemView(Guest guest) {
        CaddieViewBasicGuestItem guestItemView = new CaddieViewBasicGuestItem(getActivity(), guest, guestList.size());

        return guestItemView;
    }
}
