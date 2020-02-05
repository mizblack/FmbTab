package com.eye3.golfpay.fmb_tab.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.activity.MainActivity;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.eye3.golfpay.fmb_tab.model.teeup.Player;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.net.ResponseData;
import com.eye3.golfpay.fmb_tab.view.TabCourseLinear;
import java.util.ArrayList;


public class ScoreFragment extends BaseFragment {

    protected String TAG = getClass().getSimpleName();
    static final int NUM_OF_HOLES = 9;
    static int NUM_OF_COURSE;
    private View tabBar;
    private LinearLayout courseLinearLayout;
    private LinearLayout pinkNearestOrLinearLayout;
    private View rightLinearLayout;
    private TextView rightButtonTextView;

    ArrayList<Player> mPlayerList = new ArrayList<>();
    ArrayList<Course> mCourseList = new ArrayList<>();
    //코스탭바
    TextView[] CourseTabBar;
    //코스스코어보드]
    TabCourseLinear[] mTabCourseArr;
    FrameLayout mTabHolder;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getReserveScore();

    }

    private void createCourseTab(ArrayList<Player> mPlayerList, ArrayList<Course> mCourseList) {
        if (NUM_OF_COURSE <= 0) {
            Toast.makeText(getActivity(), "진행할 코스가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; NUM_OF_COURSE > i; i++) {
            mTabCourseArr[i] = new TabCourseLinear(getActivity(), mPlayerList, mCourseList.get(i));
            mTabCourseArr[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mTabHolder.addView(mTabCourseArr[i]);
        }
    }

    private ArrayList<Course> getCourse(ArrayList<Player> playerList) {
        //첫번째 플레이어 코스가 전체코스임.
        return playerList.get(0).playingCourse;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_score, container, false);
        mTabHolder = v.findViewById(R.id.tabHolder);
        tabBar = v.findViewById(R.id.tab_bar);
        courseLinearLayout = v.findViewById(R.id.courseLinearLayout);
        ((MainActivity) mParentActivity).showMainBottomBar();

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

    //최상위 코스 선택바
    private void createTabBar(final TextView[] tvCourseBarArr, ArrayList<Course> mCourseList) {
        for (int i = 0; mCourseList.size() > i; i++) {
            final int idx = i;
            tvCourseBarArr[i] = new TextView(getActivity());
            tvCourseBarArr[i].setLayoutParams(new ViewGroup.LayoutParams(150, ViewGroup.LayoutParams.MATCH_PARENT));
            tvCourseBarArr[i].setText(mCourseList.get(i).courseName);
            //   tvCourseBarArr[i].setTextColor(0xff000000);
            tvCourseBarArr[i].setTextAppearance(R.style.MainTabTitleTextView);
            tvCourseBarArr[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectCourse(idx);
                }
            });
            courseLinearLayout.addView(tvCourseBarArr[i]);

        }
    }

    //코스바 선택시 보여주는 함수
    private void selectCourse(int selected) {
        for (int i = 0; CourseTabBar.length > i; i++) {
            CourseTabBar[i].setTextColor(Color.GRAY);
            mTabCourseArr[i].setVisibility(View.INVISIBLE);
        }
        CourseTabBar[selected].setTextColor(Color.BLACK);
        mTabCourseArr[selected].setVisibility(View.VISIBLE);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        courseLinearLayout = tabBar.findViewById(R.id.courseLinearLayout);
        pinkNearestOrLinearLayout = tabBar.findViewById(R.id.pinkNearestOrLinearLayout);
        rightButtonTextView = tabBar.findViewById(R.id.rightButton);
        rightLinearLayout = tabBar.findViewById(R.id.rightLinearLayout);
        courseLinearLayout.setVisibility(View.VISIBLE);
        pinkNearestOrLinearLayout.setVisibility(View.VISIBLE);
        rightButtonTextView.setText("Ranking");

        rightLinearLayoutOnClick();
    }


    private void getReserveScore() {
        showProgress("스코어 정보를 가져오는 중입니다.");
        DataInterface.getInstance().getReserveScore(getActivity(), Global.reserveId, new DataInterface.ResponseCallback<ResponseData<Player>>() {
            @Override
            public void onSuccess(ResponseData<Player> response) {
                hideProgress();
                if (response.getResultCode().equals("ok")) {
                    mPlayerList = (ArrayList<Player>) response.getList();
                    mCourseList = getCourse(mPlayerList);
                    Global.playerList = mPlayerList;
                    NUM_OF_COURSE = response.getList().get(0).playingCourse.size(); //코스수를 지정한다. courseNum을 요청할것
                    CourseTabBar = new TextView[NUM_OF_COURSE];
                    createTabBar(CourseTabBar, mCourseList);
                    mTabCourseArr = new TabCourseLinear[NUM_OF_COURSE];
                    createCourseTab(mPlayerList, mCourseList);
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


