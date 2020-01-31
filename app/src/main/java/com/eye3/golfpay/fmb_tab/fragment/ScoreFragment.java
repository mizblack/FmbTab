package com.eye3.golfpay.fmb_tab.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.activity.MainActivity;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.eye3.golfpay.fmb_tab.model.teeup.GuestDatum;
import com.eye3.golfpay.fmb_tab.model.teeup.Player;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.net.ResponseData;
import com.eye3.golfpay.fmb_tab.util.FmbCustomDialog;
import com.eye3.golfpay.fmb_tab.util.ScoreDialog;
import com.eye3.golfpay.fmb_tab.view.TabCourseLinear;

import java.util.ArrayList;
import java.util.Objects;


public class ScoreFragment extends BaseFragment {

    protected String TAG = getClass().getSimpleName();
    static final int NUM_OF_HOLES = 9;
    static int NUM_OF_COURSE = 1;
    private View tabBar;
    private View courseLinearLayout;
    private View pinkNearestOrLinearLayout;
    private TextView course01TextView;
    private TextView course02TextView;
    private TextView course03TextView;
    private View rightLinearLayout;
    private TextView rightButtonTextView;

    ArrayList<Player> mPlayerList = new ArrayList<>();
    ArrayList<Course> mCourseList = new ArrayList<>();

    TabCourseLinear[] mTabCourseArr;
     FrameLayout mTabHolder;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getReserveScore();

    }

    private void createCourseTab() {
       if(NUM_OF_COURSE <= 0){
           Toast.makeText(getActivity(), "진행할 코스가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
           return;
       }

        for (int i = 0; NUM_OF_COURSE > i; i++) {
            int tab_idx = i;
            mTabCourseArr[i]  = new TabCourseLinear(getActivity(), mPlayerList, mCourseList.get(i),  tab_idx);
            mTabCourseArr[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mTabHolder.addView( mTabCourseArr[i]);
        }
    }

    private ArrayList<Course> getCourse(ArrayList<Player> playerList){
       //첫번째 플레이어 코스가 전체코스임.
       return playerList.get(0).playingCourse;

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_score, container, false);
//        mHolderLayout = v.findViewById(R.id.scoreColumn).findViewById(R.id.holderInfoLinear);
        mTabHolder = v.findViewById(R.id.tabHolder);
        ((MainActivity) mParentActivity).showMainBottomBar();
     //   createCourseTab();
        return v;
    }

    private void rightLinearLayoutOnClick() {
        rightLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoNativeScreen(new RankingFragment(), null);
            }
        });
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        SetTitle("KT WMMS");
//        SetDividerVisibility(false);
        //   setDrawerLayoutEnable(true);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tabBar = Objects.requireNonNull(getView()).findViewById(R.id.tab_bar);
        courseLinearLayout = tabBar.findViewById(R.id.courseLinearLayout);
        pinkNearestOrLinearLayout = tabBar.findViewById(R.id.pinkNearestOrLinearLayout);
        course01TextView = tabBar.findViewById(R.id.course01Text);
        course02TextView = tabBar.findViewById(R.id.course02Text);
        course03TextView = tabBar.findViewById(R.id.course03Text);

        rightButtonTextView = tabBar.findViewById(R.id.rightButton);
        rightLinearLayout = tabBar.findViewById(R.id.rightLinearLayout);

        courseLinearLayout.setVisibility(View.VISIBLE);
        pinkNearestOrLinearLayout.setVisibility(View.VISIBLE);
        course01TextView.setTextColor(0xff000000);
        course02TextView.setTextColor(0xffcccccc);
        course03TextView.setTextColor(0xffcccccc);
        rightButtonTextView.setText("Ranking");

        rightLinearLayoutOnClick();
    }



//    private void getCourseInfo() {
//        String cc_id = "1";
//
//        DataInterface.getInstance(Global.HOST_ADDRESS_DEV).getCourseInfo(getActivity(), cc_id, new DataInterface.ResponseCallback<ResponseData<Course>>() {
//            @Override
//            public void onSuccess(ResponseData<Course> response) {
//
//                if (response.getResultCode().equals("ok")) {
//                    mCourseList = (ArrayList<Course>) response.getList();
//                    Global.courseInfoList = (ArrayList<Course>) response.getList();
//                    NUM_OF_COURSE = response.getList().size(); //코스수를 지정한다
//
//                    getReserveScore();
//                }
//            }
//
//            @Override
//            public void onError(ResponseData<Course> response) {
//
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//            }
//        });
//
//
//    }

    private void getReserveScore() {
        showProgress("스코어 정보를 가져오는 중입니다.");
        DataInterface.getInstance(Global.HOST_ADDRESS_DEV).getReserveScore(getActivity(), Global.reserveId , new DataInterface.ResponseCallback<ResponseData<Player>>() {
            @Override
            public void onSuccess(ResponseData<Player> response) {
                hideProgress();
                if (response.getResultCode().equals("ok")) {
                    mPlayerList = (ArrayList<Player>) response.getList();
                    mCourseList = getCourse(mPlayerList);
                     Global.playerList = mPlayerList;
                    NUM_OF_COURSE = response.getList().get(0).playingCourse.size(); //코스수를 지정한다. courseNum을 요청할것
                    mTabCourseArr = new TabCourseLinear[NUM_OF_COURSE];
                    createCourseTab();
                }
            }

            @Override
            public void onError(ResponseData<Player> response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


    }

}


