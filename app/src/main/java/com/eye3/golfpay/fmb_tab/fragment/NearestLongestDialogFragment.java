package com.eye3.golfpay.fmb_tab.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.adapter.NearestLongestAdapter;
import com.eye3.golfpay.fmb_tab.common.AppDef;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.common.UIThread;
import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.eye3.golfpay.fmb_tab.model.field.Hole;
import com.eye3.golfpay.fmb_tab.model.teeup.GuestDatum;
import com.eye3.golfpay.fmb_tab.model.teeup.Player;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.net.ResponseData;
import com.eye3.golfpay.fmb_tab.view.LongestInserter;
import com.eye3.golfpay.fmb_tab.view.NearestInserter;

import java.util.ArrayList;

public class NearestLongestDialogFragment extends DialogFragment {

    protected ProgressDialog pd; // 프로그레스바 선언

    protected String TAG = getClass().getSimpleName();
    private ArrayList<GuestDatum> guestArrayList = Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getGuestData();
    private LinearLayout guestItemLinearLayout;
    private View cancelLinearLayout;
    private TextView mTabLongest, mTabNearest;

    private String mNearestOrLongest = AppDef.NEAREST;
    private  Course mCourse;
    private Hole mHole;

    private RecyclerView recyclerView;
    LinearLayoutManager learingLayout;
    NearestLongestAdapter adapter;

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

                if (mNearestOrLongest.equals(AppDef.NEAREST)) {
                    mTabNearest.setTextAppearance(R.style.GlobalTextView_32SP_gray_NotoSans_Medium);
                }
                mNearestOrLongest = AppDef.LONGEST;
                ((TextView) v).setTextAppearance(R.style.GlobalTextView_32SP_ebonyBlack_NotoSans_Medium);
                changeScreenTo(AppDef.LONGEST);

            }
        });
        mTabNearest = view.findViewById(R.id.nearestTextView);
        mTabNearest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNearestOrLongest.equals(AppDef.LONGEST)) {
                    mTabLongest.setTextAppearance(R.style.GlobalTextView_32SP_gray_NotoSans_Medium);
                }
                mNearestOrLongest = AppDef.NEAREST;
                ((TextView) v).setTextAppearance(R.style.GlobalTextView_32SP_ebonyBlack_NotoSans_Medium);
                changeScreenTo(AppDef.NEAREST);
            }
        });
        ( (TextView)view.findViewById(R.id.long_near_hole_no)).setText("Hole "+ mHole.hole_no);
        ( (TextView)view.findViewById(R.id.long_near_par)).setText("Par" + mHole.par);
        ( (TextView)view.findViewById(R.id.long_near_course)).setText("Course(" + mCourse.courseName + ")");

        return view;
    }

    public void setmNearestOrLongest(String NearestLongest) {
        mNearestOrLongest = NearestLongest;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void makeNearest() {
        guestItemLinearLayout.removeAllViewsInLayout();
        ( (TextView)getView().findViewById(R.id.measurement_unit)).setText("Centimeters");
        if (guestArrayList.size() != 0) {
            for (int i = 0; i < guestArrayList.size(); i++) {

                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.item_nearest_longest_input, null, false);

                LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 160);
                v.setLayoutParams(lllp);

                final LinearLayout selectorLinearLayout = v.findViewById(R.id.selectorLinearLayout);
                recyclerView = selectorLinearLayout.findViewById(R.id.reyclerview);
                learingLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(learingLayout);

                final TextView tvMeters = v.findViewById(R.id.metersTextView);
                TextView nameTextView = v.findViewById(R.id.nameTextView);

                adapter = new NearestLongestAdapter(getContext(), new NearestLongestAdapter.IOnClickAdapter() {
                    @Override
                    public void onAdapterItemClicked(final Integer id) {
                        tvMeters.setText(adapter.getItem(id) + "CM");
                    }
                });

                recyclerView.setAdapter(adapter);
                nameTextView.setText(guestArrayList.get(i).getGuestName());
                guestItemLinearLayout.addView(v);
            }
        }
    }

    private void makeLongest() {
        guestItemLinearLayout.removeAllViewsInLayout();
        ( (TextView)getView().findViewById(R.id.measurement_unit)).setText("Meters");
        if (guestArrayList.size() != 0) {
            for (int i = 0; i < guestArrayList.size(); i++) {

                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.item_nearest_longest_input, null, false);

                LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 160);
                v.setLayoutParams(lllp);

                final LinearLayout selectorLinearLayout = v.findViewById(R.id.selectorLinearLayout);
                recyclerView = selectorLinearLayout.findViewById(R.id.reyclerview);
                learingLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(learingLayout);

                final TextView tvMeters = v.findViewById(R.id.metersTextView);
                TextView nameTextView = v.findViewById(R.id.nameTextView);

                adapter = new NearestLongestAdapter(getContext(), new NearestLongestAdapter.IOnClickAdapter() {
                    @Override
                    public void onAdapterItemClicked(final Integer id) {
                        tvMeters.setText(adapter.getItem(id) + "M");
                    }
                });

                recyclerView.setAdapter(adapter);
                nameTextView.setText(guestArrayList.get(i).getGuestName());
                guestItemLinearLayout.addView(v);
            }
        }
    }


    private void changeScreenTo(String nearestOrLongest) {
        if (nearestOrLongest.equals(AppDef.NEAREST))

            makeNearest();
        else
            makeLongest();
    }

    public void setHole(Hole hole){
        this.mHole = hole;
    }
    public void setCourse(Course course){
        this.mCourse = course;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(mNearestOrLongest.equals(AppDef.NEAREST)){
            mTabNearest.performClick();
            ( (TextView)getView().findViewById(R.id.measurement_unit)).setText("Centimeters");
        } else {
            mTabLongest.performClick();
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
