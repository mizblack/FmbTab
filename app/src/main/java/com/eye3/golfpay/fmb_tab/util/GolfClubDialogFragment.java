package com.eye3.golfpay.fmb_tab.util;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.field.Club;
import com.eye3.golfpay.fmb_tab.model.guest.Guest;
import com.eye3.golfpay.fmb_tab.view.IronInserter;
import com.eye3.golfpay.fmb_tab.view.MicellCoverInserter;
import com.eye3.golfpay.fmb_tab.view.PutterCoverInserter;
import com.eye3.golfpay.fmb_tab.view.PutterInserter;
import com.eye3.golfpay.fmb_tab.view.UtilityInserter;
import com.eye3.golfpay.fmb_tab.view.WedgeInserter;
import com.eye3.golfpay.fmb_tab.view.WoodCoverInserter;
import com.eye3.golfpay.fmb_tab.view.WoodInserter;

import java.util.List;

public class GolfClubDialogFragment extends DialogFragment {
   //최초 선택된 내장객
    Guest mGuest;
    LinearLayout mGuestLinear;
    //현재 선택된 게스트이름 뷰
    TextView mSelectedChildView;
    //이전 선택된 게스트이름 뷰
    TextView mPrevSelectChildView;
    WoodInserter mWoodInserter;
    TextView mTvWoodCount;
    UtilityInserter mUtilityInserter;
    TextView mTvUtilityCount;
    IronInserter mIronInserter;
    TextView mTvIronCount;
    WedgeInserter mWedgeInserter;
    TextView mTvWedgeCount;
    PutterInserter mPutterInserter;
    TextView mTvPutterCount;
    PutterCoverInserter mPutterCoverInserter;
    TextView mTvPutterCoverCount;
    WoodCoverInserter mWoodCoverInserter;
    TextView mTvWoodCoverCount;
    MicellCoverInserter mMicellCoverInserter;
    TextView mTvMicellCoverCount;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent);
    }

    public void setGuest(Guest guest){
        mGuest = guest;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_golf_club_list, container, false);
        mGuestLinear = view.findViewById(R.id.guest_container_linear);

        mWoodInserter =  view.findViewById(R.id.wood_inserter);
        mTvWoodCount = view.findViewById(R.id.tv_wood_qty);

        mUtilityInserter  =  view.findViewById(R.id.utility_inserter);
        mTvUtilityCount = view.findViewById(R.id.tv_utility_qty);

        mIronInserter   =  view.findViewById(R.id.iron_inserter);
        mTvIronCount = view.findViewById(R.id.tv_iron_qty);

        mWedgeInserter  =  view.findViewById(R.id.wedge_inserter);
        mTvWedgeCount = view.findViewById(R.id.tv_wedge_qty);

        mPutterInserter  =  view.findViewById(R.id.putter_inserter);
        mTvPutterCount = view.findViewById(R.id.tv_putter_qty);

        mPutterCoverInserter   =  view.findViewById(R.id.putter_cover_inserter);
        mTvPutterCoverCount = view.findViewById(R.id.tv_putter_cover_qty);

        mWoodCoverInserter  =  view.findViewById(R.id.wood_cover_inserter);
        mTvWoodCoverCount =  view.findViewById(R.id.tv_wood_cover_qty);

        mMicellCoverInserter  =  view.findViewById(R.id.micell_cover_inserter);
        mTvMicellCoverCount =  view.findViewById(R.id.tv_micell_cover_qty);

        createGuestList(mGuestLinear);
        view.findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

    private void createGuestList(LinearLayout container) {

        for (int i = 0; Global.guestList.size() > i; i++) {
            final int idx = i;
            TextView tv = new TextView(new ContextThemeWrapper(getActivity(), R.style.ShadeGuestNameTextView), null, 0);
            final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
            final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width, height);
            tv.setLayoutParams(param);
            tv.setTag(Global.guestList.get(idx));
            tv.setText(Global.guestList.get(idx).getGuestName());

            tv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mPrevSelectChildView != null) {
                        initTextColor(mSelectedChildView);
                    }
                    mSelectedChildView = (TextView) v;
                    // 같은 버튼 세번 눌럿을시 문제 발생오류 해결
                    if (mSelectedChildView == mPrevSelectChildView && mSelectedChildView.getCurrentTextColor() == 0) {
                        initTextColor(mSelectedChildView);
                    } else if (mSelectedChildView == mPrevSelectChildView && mPrevSelectChildView.getCurrentTextColor() == -6710887) {
                        setTextColorSelected(mSelectedChildView);
                    } else {
                        setTextColorSelected(mSelectedChildView);
                    }

                    if(mSelectedChildView != mPrevSelectChildView)
                        initTextColor(mPrevSelectChildView);
                    mPrevSelectChildView = mSelectedChildView;
                    Guest a_guest = (Guest)mSelectedChildView.getTag();
                    initGolfClubList(a_guest.clubList);
                    return false;
                }
            });
            container.addView(tv);
        }
    }

    private void initGolfClubList(List<Club> clubList){
        //*************
//      for(int i = 0;  a_GuestClubs.woodList.size() > i ; i++){
//         String a_wood = a_GuestClubs.woodList.get(i);
//          mWoodInserter
//      }
    }

    public void initTextColor(TextView tv) {
        if (tv != null)
        tv.setTextColor(getResources().getColor(R.color.gray, null)); //-6710887
    }

    public void setTextColorSelected(TextView tv) {
        if (tv != null)
            tv.setTextColor(Color.parseColor("#000000"));
    }
}
