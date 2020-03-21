package com.eye3.golfpay.fmb_tab.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.common.UIThread;
import com.eye3.golfpay.fmb_tab.model.teeup.GuestDatum;
import com.eye3.golfpay.fmb_tab.model.teeup.Player;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.net.ResponseData;
import com.eye3.golfpay.fmb_tab.view.NearestLongestInputItem;

import java.util.ArrayList;

public class NearestLongestDialogFragment extends DialogFragment {
    protected ProgressDialog pd; // 프로그레스바 선언

    protected String TAG = getClass().getSimpleName();
    private ArrayList<GuestDatum> guestArrayList = Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getGuestData();
    private LinearLayout guestItemLinearLayout;
    private View cancelLinearLayout;
    private ArrayList<NearestLongestInputItem> nearestLongestInputItemArrayList = new ArrayList<>();
    private TextView mTabLongest, mTabNearest;
    private String mNearestOrLongest = "롱기스트";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_nearest_longest, container, false);
        guestItemLinearLayout = view.findViewById(R.id.guestItemLinearLayout);
        cancelLinearLayout = view.findViewById(R.id.cancelLinearLayout);
        mTabLongest = view.findViewById(R.id.longestTextView);
        mTabLongest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mNearestOrLongest.equals("니어스트")) {
                    mTabNearest.setTextAppearance(R.style.MainTabTitleTextViewNormal);
                }
                mNearestOrLongest = "롱기스트";
                ((TextView) v).setTextAppearance(R.style.MainTabTitleTextViewBold);


            }
        });
        mTabNearest = view.findViewById(R.id.nearestTextView);
        mTabNearest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNearestOrLongest.equals("롱기스트")) {
                    mTabLongest.setTextAppearance(R.style.MainTabTitleTextViewNormal);
                }
                mNearestOrLongest ="니어스트";
                ((TextView)v).setTextAppearance(R.style.MainTabTitleTextViewBold);

            }
        });
        return view;
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (guestArrayList.size() != 0) {
            for (int i = 0; i < guestArrayList.size(); i++) {
                final NearestLongestInputItem nearestLongestInputItem = new NearestLongestInputItem(getContext());
                nearestLongestInputItemArrayList.add(nearestLongestInputItem);
                guestItemLinearLayout.addView(nearestLongestInputItem);
                final View selectorLinearLayout = nearestLongestInputItem.findViewById(R.id.selectorLinearLayout);
                TextView nameTextView = nearestLongestInputItem.findViewById(R.id.nameTextView);
                nameTextView.setText(guestArrayList.get(i).getGuestName());

                selectorLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for (int i = 0; i < nearestLongestInputItemArrayList.size(); i++) {
                            View selectorLinearLayoutTemp = nearestLongestInputItemArrayList.get(i).findViewById(R.id.selectorLinearLayout);
                            selectorLinearLayoutTemp.setBackgroundColor(0xffffffff);
                            View modifyDividerTemp = nearestLongestInputItemArrayList.get(i).findViewById(R.id.modifyDivider);
                            modifyDividerTemp.setVisibility(View.GONE);
                            TextView modifyTextViewTemp = nearestLongestInputItemArrayList.get(i).findViewById(R.id.modifyTextView);
                            modifyTextViewTemp.setText("수정");
                            modifyTextViewTemp.setTextColor(0xffcccccc);
                        }

                        selectorLinearLayout.setBackgroundResource(R.drawable.shape_black_edge);
                        View modifyDivider = nearestLongestInputItem.findViewById(R.id.modifyDivider);
                        modifyDivider.setVisibility(View.VISIBLE);
                        TextView modifyTextView = nearestLongestInputItem.findViewById(R.id.modifyTextView);
                        modifyTextView.setText("입력");
                        modifyTextView.setTextColor(0xff000000);

                    }
                });
            }
        }

        cancelLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    private void getLongest() {
        showProgress("Longest정보를 가져오는 중입니다.");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getReserveScore(getActivity(), Global.reserveId, "null", new DataInterface.ResponseCallback<ResponseData<Player>>() {
            @Override
            public void onSuccess(ResponseData<Player> response) {
//                hideProgress();
//                if (response.getResultCode().equals("ok")) {
//                    mPlayerList = (ArrayList<Player>) response.getList();
//                    mCourseList = getCourse(mPlayerList);
//                    NUM_OF_COURSE = response.getList().get(0).playingCourse.size(); //코스수를 지정한다. courseNum 을 요청할것
//                    CourseTabBar = new TextView[NUM_OF_COURSE];
//
//                    createTabBar(CourseTabBar, mCourseList);
//                    initScoreBoard();

//                } else if (response.getResultCode().equals("fail")) {
//                    Toast.makeText(getActivity(), response.getResultMessage(), Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onError(ResponseData<Player> response) {
                hideProgress();
                response.getError();
            }

            @Override
            public void onFailure(Throwable t) {
                hideProgress();
            }
        });

    }

    protected void showProgress(final String msg) {
        UIThread.executeInUIThread(new Runnable() {
            @Override
            public void run() {
                if (pd == null) {
                    // 객체를 1회만 생성한다.
                    pd = new ProgressDialog(getActivity()); // 생성한다.
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
