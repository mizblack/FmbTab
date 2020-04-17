package com.eye3.golfpay.fmb_tab.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.AppDef;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.listener.ScoreInputFinishListener;
import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.eye3.golfpay.fmb_tab.model.teeup.Player;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.net.ResponseData;
import com.eye3.golfpay.fmb_tab.view.TabCourseLinear;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScoreFragment extends BaseFragment {

    protected String TAG = getClass().getSimpleName();
    static final int NUM_OF_HOLES = 9;
    static int NUM_OF_COURSE;
    private View tabBar, nearest, longest;
    private LinearLayout courseLinearLayout;
    private LinearLayout pinkNearestOrLinearLayout;
    private View rightLinearLayout;
    private TextView rightButtonTextView;
    List<Player> mPlayerList;
    List<Course> mCourseList;
    //코스탭바
    TextView[] CourseTabBar;
    //코스스코어보드
    TabCourseLinear mScoreBoard;
    int mTabIdx = 0;

    private static ArrayList<String> firstGuestIdList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getReserveScore();

    }

    //스코어 보드를 생성한다.
    private void createScoreTab(List<Player> mPlayerList, int mTabIdx) {
        showProgress("스코어생성 작업중");

        if (NUM_OF_COURSE <= 0) {
            Toast.makeText(getActivity(), "진행할 코스가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
                                                                             //mPlayerList.get(i)가 아님
        mScoreBoard.init(Objects.requireNonNull(getActivity()), mPlayerList, Global.courseInfoList.get(mTabIdx), mTabIdx);
        mScoreBoard.setOnScoreInputFinishListener(new ScoreInputFinishListener() {
            @Override
            public void OnScoreInputFinished(List<Player> playerList) {
                refreshScore();
            }
        });

        hideProgress();

    }

    private void initScoreBoard() {
        selectCourse(mPlayerList, 0);
    }

    private List<Course> getCtypedCourseForPlayerList(List<Player> playerList) {
        //첫번째 플레이어 코스가 전체코스임.
        //예약에서 inout 을 확인하고 코스순서를 다시 재정렬함
        if(playerList.size() == 0){
            return null;
        }
        if (AppDef.CType.OUT.equals(Global.selectedReservation.getInoutCourse())) {
            if (!AppDef.CType.OUT.equals(playerList.get(0).playingCourse.get(0).ctype)) {
                swapList(playerList.get(0).playingCourse.get(0), playerList.get(0).playingCourse.get(1), playerList.get(0).playingCourse);
            }
        } else if (AppDef.CType.IN.equals(Global.selectedReservation.getInoutCourse())) {
            if (!AppDef.CType.IN.equals(playerList.get(0).playingCourse.get(0).ctype)) {
                swapList(playerList.get(0).playingCourse.get(0), playerList.get(0).playingCourse.get(1), playerList.get(0).playingCourse);
            }
        }
        return playerList.get(0).playingCourse;

    }

    private List<Course> getCtypedCourseforCourseList(List<Course> courseList) {

        //예약에서 inout 을 확인하고 코스순서를 다시 재정렬함
        if (AppDef.CType.OUT.equals(Global.selectedReservation.getInoutCourse())) {
            if (!AppDef.CType.OUT.equals(courseList.get(0).ctype)) {
                swapList(courseList.get(0), courseList.get(1), courseList);
            }
        } else if (AppDef.CType.IN.equals(Global.selectedReservation.getInoutCourse())) {
            if (!AppDef.CType.IN.equals(courseList.get(0).ctype)) {
                swapList(courseList.get(0), courseList.get(1), courseList);
            }
        }
        return courseList;

    }

    /*
     * 자유 컨트리클럽을 위한 inout 코스 변경
     */
    private void swapList(Course first, Course second, List<Course> courses) {

        courses.add(0, second);
        courses.remove(2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_score, container, false);
        mScoreBoard = v.findViewById(R.id.scoreBoard);
        tabBar = v.findViewById(R.id.tab_bar);
        nearest = tabBar.findViewById(R.id.nearest);
        longest = tabBar.findViewById(R.id.longest);
        nearest.setVisibility(View.GONE);
        longest.setVisibility(View.GONE);
        courseLinearLayout = v.findViewById(R.id.courseLinearLayout);
        mParentActivity.showMainBottomBar();
        return v;
    }

    private void rightLinearLayoutOnClick() {
        rightLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle  bundle = new Bundle();
                bundle.putString("ani_direction", "up");
                GoNativeScreen(new RankingFragment(), bundle);
            }
        });
    }

    //최상위 코스 선택바
    private void createTabBar(final TextView[] tvCourseBarArr, List<Course> mCourseList) {
        for (int i = 0; mCourseList.size() > i; i++) {
            final int idx = i;
            tvCourseBarArr[idx] = new TextView(new ContextThemeWrapper(getActivity(), R.style.MainTabTitleTextViewPadding), null, 0);
            tvCourseBarArr[idx].setText(mCourseList.get(i).courseName);
            tvCourseBarArr[idx].setTag(mCourseList.get(idx));
            tvCourseBarArr[idx].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTabIdx = idx;
                    Global.CurrentCourse = (Course) tvCourseBarArr[idx].getTag();

                    selectCourse(mPlayerList, idx);
                }
            });
            courseLinearLayout.addView(tvCourseBarArr[i]);
        }
    }

    //코스바 선택시 스코어보드 생성 함수
    private void selectCourse(List<Player> playerList, int selectedTabIdx) {
        CourseTabBar[selectedTabIdx].setTextColor(Color.BLACK);
        for (int i = 0; CourseTabBar.length > i; i++) {
            if (i != selectedTabIdx)
                CourseTabBar[i].setTextColor(Color.GRAY);
        }
        TabCourseLinear.setmHoleScoreLayoutIdx(-1);
        createScoreTab(playerList, selectedTabIdx);

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
    //순위에따른 위치고정 함수
    @Deprecated
    private void reArrangeOrder(List<Player> playerList) {
        for (int i = 0; firstGuestIdList.size() > i; i++) {
            for (int j = 0; playerList.size() > j; j++) {

                if (firstGuestIdList.get(i).equals(playerList.get(j).guest_id)) {
                    playerList.set(i, playerList.get(j));
                }
            }
        }
    }
    //순위에따른 위치고정 함수의 초기값 설정함수
    @Deprecated
    private void getFirstGuestIdList(List<Player> playerList) {
        for (int i = 0; playerList.size() > i; i++)
            firstGuestIdList.add(playerList.get(i).guest_id);
    }

    private void getReserveScore() {
        showProgress("스코어 정보를 가져오는 중입니다.");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getReserveScore(getActivity(), Global.reserveId, "null", new DataInterface.ResponseCallback<ResponseData<Player>>() {
            @Override
            public void onSuccess(ResponseData<Player> response) {
                hideProgress();
                if (response.getResultCode().equals("ok")) {
                    mPlayerList = response.getList();
               //     mCourseList = getCtypedCourseForPlayerList(mPlayerList);
                     mCourseList = getCtypedCourseForPlayerList(mPlayerList);
                     Global.courseInfoList =  getCtypedCourseforCourseList(Global.courseInfoList);

                    Global.CurrentCourse = mCourseList.get(0);
                    Global.CurrentHole = Global.CurrentCourse.holes.get(0);
                    NUM_OF_COURSE = mCourseList.size();
                    CourseTabBar = new TextView[NUM_OF_COURSE];

                    createTabBar(CourseTabBar,   Global.courseInfoList);
                    initScoreBoard();

                } else if (response.getResultCode().equals("fail")) {
                    Toast.makeText(getActivity(), response.getResultMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(ResponseData<Player> response) {
                hideProgress();
                response.getError();
            }

            @Override
            public void onFailure(Throwable t) {
                getReserveScore();
                hideProgress();
            }
        });

    }

    private void refreshScore() {
        showProgress("스코어 정보를 갱신 중입니다.");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getReserveScore(getActivity(), Global.reserveId, "null", new DataInterface.ResponseCallback<ResponseData<Player>>() {
            @Override
            public void onSuccess(ResponseData<Player> response) {
                hideProgress();
                if (response.getResultCode().equals("ok")) {
                    mPlayerList = response.getList();
                    createScoreTab(mPlayerList, mTabIdx);

                } else if (response.getResultCode().equals("fail")) {
                    Toast.makeText(getActivity(), response.getResultMessage(), Toast.LENGTH_SHORT).show();
                }
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

}
