package com.eye3.golfpay.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.eye3.golfpay.R;
import com.eye3.golfpay.activity.MainActivity;
import com.eye3.golfpay.common.AppDef;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.common.UIThread;
import com.eye3.golfpay.dialog.GameHoleDialog;
import com.eye3.golfpay.listener.ScoreInputFinishListener;
import com.eye3.golfpay.model.field.Course;
import com.eye3.golfpay.model.order.ReserveGameType;
import com.eye3.golfpay.model.teeup.Player;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;
import com.eye3.golfpay.view.TabCourseLinear;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScoreFragment extends BaseFragment {

    protected String TAG = getClass().getSimpleName();
    static final int NUM_OF_HOLES = 9;
    static int NUM_OF_COURSE;
    private View tabBar, nearest, longest;
    private LinearLayout courseLinearLayout;
    private View rightLinearLayout;
    List<Player> mPlayerList;
    List<Course> mCourseList;
    //코스탭바
    TextView[] CourseTabBar;
    //코스스코어보드
    TabCourseLinear mScoreBoard;
    int mTabIdx = 0;

    private int advertisingCount = 0;
    private boolean advertsingContinue = true;
    private ImageView ivAdvertising1, ivAdvertising2, ivAdvertising3;

    private static final ArrayList<String> firstGuestIdList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getReserveScore();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        advertsingContinue = false;
    }

    //스코어 보드를 생성한다.
    private void createScoreTab(List<Player> mPlayerList, int mTabIdx) {
        showProgress("스코어생성 작업중");
        getNearLongHole();
    }

    private void initScoreBoard() {
        selectCourse(mPlayerList, 0);
    }

    private List<Course> getCtypedCourseForPlayerList(List<Player> playerList) {
        //첫번째 플레이어 코스가 전체코스임.
        //예약에서 inout 을 확인하고 코스순서를 다시 재정렬함
//        if(playerList.size() == 0){
//            return null;
//        }
//        if (AppDef.CType.OUT.equals(Global.selectedReservation.getInoutCourse())) {
//            if (!AppDef.CType.OUT.equals(playerList.get(0).playingCourse.get(0).ctype)) {
//                swapList(playerList.get(0).playingCourse.get(0), playerList.get(0).playingCourse.get(1), playerList.get(0).playingCourse);
//            }
//        } else if (AppDef.CType.IN.equals(Global.selectedReservation.getInoutCourse())) {
//            if (!AppDef.CType.IN.equals(playerList.get(0).playingCourse.get(0).ctype)) {
//                swapList(playerList.get(0).playingCourse.get(0), playerList.get(0).playingCourse.get(1), playerList.get(0).playingCourse);
//            }
//        }

        return playerList.get(0).course;
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
        courseLinearLayout = v.findViewById(R.id.courseLinearLayout);
        mParentActivity.showMainBottomBar();
        ivAdvertising1 = v.findViewById(R.id.iv_ad1);
        ivAdvertising2 = v.findViewById(R.id.iv_ad2);
        ivAdvertising3 = v.findViewById(R.id.iv_ad3);
        startAdvertisingTimerThread();

        //Nearest 클릭릭
        tabBar.findViewById(R.id.pinkNearestOrLongest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //NearestLongestDialogFragment nearestLongestDialogFragment = new NearestLongestDialogFragment();
                //showDialogFragment(nearestLongestDialogFragment);

                Bundle  bundle = new Bundle();
                bundle.putString("ani_direction", "up");
                GoNativeScreen(new NearestLongestFragment(), null);
            }
        });

        tabBar.findViewById(R.id.btn_gps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CourseFragment courseFragment = new CourseFragment();
                GoNativeScreen(courseFragment, null);
                Global.courseFragment = courseFragment;
                mParentActivity.getViewMenuFragment().selectMenu(R.id.view_gps);
            }
        });
        return v;
    }

    private void showDialogFragment(DialogFragment dialogFragment) {
        FragmentManager supportFragmentManager = ((MainActivity) (getContext())).getSupportFragmentManager();
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        dialogFragment.show(transaction, TAG);
        assert dialogFragment.getFragmentManager() != null;
        dialogFragment.getFragmentManager().executePendingTransactions();
    }

    private void rightLinearLayoutOnClick() {
        rightLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle  bundle = new Bundle();
                bundle.putString("ani_direction", "up");
                GoNativeScreen(new RankingFragment(), bundle);
                mParentActivity.getViewMenuFragment().selectMenu(R.id.view_ranking);
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
        TabCourseLinear.setHoleScoreLayoutIdx(0);
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
        LinearLayout ll_lear_long_tab = tabBar.findViewById(R.id.ll_lear_long_tab);
        TextView rightButtonTextView = tabBar.findViewById(R.id.rightButton);
        rightLinearLayout = tabBar.findViewById(R.id.rightLinearLayout);
        courseLinearLayout.setVisibility(View.VISIBLE);
        ll_lear_long_tab.setVisibility(View.VISIBLE);
        rightButtonTextView.setText("순위");

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
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getReserveScore(getActivity(),
                Global.reserveId, "null", new DataInterface.ResponseCallback<ResponseData<Player>>() {
            @Override
            public void onSuccess(ResponseData<Player> response) {
                hideProgress();

                if (response.getResultCode().equals("ok")) {
                    mPlayerList = response.getList();
               //     mCourseList = getCtypedCourseForPlayerList(mPlayerList);
                     mCourseList = getCtypedCourseForPlayerList(mPlayerList);
                     Global.courseInfoList =  getCtypedCourseforCourseList(Global.courseInfoList);

                    Global.CurrentCourse = mCourseList.get(0);
                    if(Global.CurrentCourse.holes.size() > 0 ) {
                        Global.CurrentHole = Global.CurrentCourse.holes.get(0);

                        NUM_OF_COURSE = mCourseList.size();
                        CourseTabBar = new TextView[NUM_OF_COURSE];

                        createTabBar(CourseTabBar, Global.courseInfoList);
                        initScoreBoard();
                    }else{
                        Toast.makeText(getActivity(), "홀정보가 없습니다. 관리자에게 연락하십시요.", Toast.LENGTH_SHORT).show();
                    }

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

    private void getNearLongHole() {

        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getReserveGameType(getContext(), new DataInterface.ResponseCallback<ReserveGameType>() {
            @Override
            public void onSuccess(ReserveGameType response) {
                if (response.ret_code.equals("ok")) {
                    if (NUM_OF_COURSE <= 0) {
                        Toast.makeText(getActivity(), "진행할 코스가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //mPlayerList.get(i)가 아님

                    String course = Global.CurrentCourse.courseName;

                    int nearest = -1;
                    int longest = -1;
                    if (course.contains("IN")) {
                        if (response.course_near.equals("IN")) {
                            nearest = response.hole_no_near-1;
                        }
                        if (response.course_long.equals("IN")) {
                            longest = response.hole_no_long-1;
                        }
                    } else if (course.contains("OUT")) {
                        if (response.course_near.equals("OUT")) {
                            nearest = response.hole_no_near-1;
                        }
                        if (response.course_long.equals("OUT")) {
                            longest = response.hole_no_long-1;
                        }
                    }

                    mScoreBoard.init(Objects.requireNonNull(getActivity()), mPlayerList, mTabIdx, nearest, longest);
                    mScoreBoard.setOnScoreInputFinishListener(new ScoreInputFinishListener() {
                        @Override
                        public void OnScoreInputFinished(List<Player> playerList) {
                            refreshScore();
                        }
                    });

                    hideProgress();
                }
            }

            @Override
            public void onError(ReserveGameType response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    private void startAdvertisingTimerThread() {

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (!advertsingContinue)
                    return;

                switch(advertisingCount%3) {
                    case 0 :
                        changeAdvertising(ivAdvertising1, ivAdvertising2);
                        break;
                    case 1 :
                        changeAdvertising(ivAdvertising2, ivAdvertising3);
                        break;
                    case 2 :
                        changeAdvertising(ivAdvertising3, ivAdvertising1);
                        break;
                }

                advertisingCount++;

            }
        }.start();
    }

    private void changeAdvertising(ImageView ivOut, ImageView ivIn) {

        UIThread.executeInUIThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Animation slowly_appear, slowlyDisappear;
                    slowlyDisappear = AnimationUtils.loadAnimation(getContext(), R.anim.slowly_fadeout);
                    slowly_appear = AnimationUtils.loadAnimation(getContext(), R.anim.slowly_fadein);
                    ivOut.setAnimation(slowlyDisappear);
                    ivOut.setVisibility(View.GONE);
                    ivIn.setAnimation(slowly_appear);
                    ivIn.setVisibility(View.VISIBLE);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        startAdvertisingTimerThread();
    }
}
