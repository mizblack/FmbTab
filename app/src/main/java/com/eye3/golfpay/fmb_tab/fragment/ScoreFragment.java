package com.eye3.golfpay.fmb_tab.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.activity.MainActivity;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.eye3.golfpay.fmb_tab.model.field.Hole;
import com.eye3.golfpay.fmb_tab.model.teeup.GuestDatum;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.util.FmbCustomDialog;
import com.eye3.golfpay.fmb_tab.util.ScoreDialog;

import java.util.ArrayList;
import java.util.Objects;


public class ScoreFragment extends BaseFragment {


    protected String TAG = getClass().getSimpleName();
    static final int NUM_OF_HOLES = 9;
    ArrayList<String[]> scores = new ArrayList<>();
    ScoreAdapter mScoreAdapter;
    LinearLayoutManager mManager;
    RecyclerView recycleScore;

    private View tabBar;
    private View courseLinearLayout;
    private View pinkNearestOrLinearLayout;
    private TextView course01TextView;
    private TextView course02TextView;
    private TextView course03TextView;
    private View course01Tab;
    private View course02Tab;
    private View course03Tab;
    private View rightLinearLayout;
    private TextView rightButtonTextView;
    private ArrayList<Course> courseList = new ArrayList<>();
    LinearLayout[] ScoreTab = new LinearLayout[4];
    ArrayList<GuestDatum> playerList = new ArrayList<>();
    ScoreDialog sDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
        }
        playerList = Global.teeUpTime.getTodayReserveList().get(0).getGuestData();
        getCourseInfo();
        Course first_course = new Course();
        Hole[] first_course_hole = new Hole[9];
        for (int i = 0; first_course_hole.length > i; i++) {
            Hole a_hole = new Hole(String.valueOf(i + 1), "4", "300");
            first_course.arrHole[i] = a_hole;

        }


        first_course.courseName = "코스1";
        first_course.id = "1";


        String[] points = new String[11];
        String[] points1 = new String[11];
        String[] points2 = new String[11];
        String[] points3 = new String[11];
        String[] points4 = new String[11];
        points[0] = "3rd";
        points[1] = "홍길동";
        points[2] = "4";
        points[3] = "4";
        points[4] = "4";
        points[5] = "5";
        points[6] = "3";
        points[7] = "4";
        points[8] = "4";
        points[9] = "3";
        points[10] = "4";
        points1[0] = "2nd";
        points1[1] = "김영광";
        points1[2] = "4";
        points1[3] = "4";
        points1[4] = "3";
        points1[5] = "5";
        points1[6] = "3";
        points1[7] = "4";
        points1[8] = "3";
        points1[9] = "4";
        points1[10] = "4";
        points2[0] = "1nd";
        points2[1] = "김치욱";
        points2[2] = "4";
        points2[3] = "4";
        points2[4] = "3";
        points2[5] = "5";
        points2[6] = "4";
        points2[7] = "4";
        points2[8] = "3";
        points2[9] = "4";
        points2[10] = "4";
//        points3[0] = "5th";
//        points3[1] = "한상예";
//        points3[2] = "4";
//        points3[3] = "4";
//        points3[4] = "4";
//        points3[5] = "5";
//        points3[6] = "4";
//        points3[7] = "4";
//        points3[8] = "4";
//        points3[9] = "4";
//        points3[10] = "4";
//        points4[0] = "3nd";
//        points4[1] = "노근수";
//        points4[2] = "4";
//        points4[3] = "4";
//        points4[4] = "4";
//        points4[5] = "5";
//        points4[6] = "4";
//        points4[7] = "4";
//        points4[8] = "4";
//        points4[9] = "4";
//        points4[10] = "4";
        scores.add(points);
        scores.add(points1);
        scores.add(points2);
//        scores.add(points3);
//        scores.add(points4);
        TextView[] tvHoleInfo1 = new TextView[9];
        TextView[] tvHoleInfo2 = new TextView[9];
        TextView[] tvHoleInfo3 = new TextView[9];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_score, container, false);
        recycleScore = v.findViewById(R.id.scoreRecylerView);
        recycleScore.setHasFixedSize(true);
        mManager = new LinearLayoutManager(getActivity());
        recycleScore.setLayoutManager(mManager);
        mScoreAdapter = new ScoreAdapter(getActivity(), scores);
        recycleScore.setAdapter(mScoreAdapter);
        mScoreAdapter.notifyDataSetChanged();
        setCourseTab(v);
        ((MainActivity) mParentActivity).showMainBottomBar();
        return v;
    }

    private void setCourseTab(View v) {
        for (int i = 0; ScoreTab.length > i; i++) {
            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole1)).setText("1");
            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole1par)).setText("4");
            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole1Meters)).setText("342");

            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole2)).setText("2");
            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole2par)).setText("3");
            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole2Meters)).setText("342");

            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole3)).setText("3");
            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole3par)).setText("4");
            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole3Meters)).setText("242");

            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole4)).setText("4");
            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole4par)).setText("4");
            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole4Meters)).setText("372");

            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole5)).setText("5");
            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole5par)).setText("3");
            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole5Meters)).setText("545");

            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole6)).setText("6");
            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole6par)).setText("4");
            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole6Meters)).setText("545");

            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole7)).setText("7");
            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole7par)).setText("4");
            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole7Meters)).setText("456");

            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole8)).setText("8");
            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole8par)).setText("4");
            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole8Meters)).setText("445");

            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole9)).setText("9");
            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole9par)).setText("4");
            ((TextView) v.findViewById(R.id.course01Tab).findViewById(R.id.scoreColumn).findViewById(R.id.hole9Meters)).setText("477");

        }
    }

    private void tabTitleOnClick(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == course01TextView) {
                    course01TextView.setTextColor(0xff000000);
                    course02TextView.setTextColor(0xffcccccc);
                    course03TextView.setTextColor(0xffcccccc);

                    course01Tab.setVisibility(View.VISIBLE);
                    course02Tab.setVisibility(View.GONE);
                    course03Tab.setVisibility(View.GONE);
                } else if (view == course02TextView) {
                    course01TextView.setTextColor(0xffcccccc);
                    course02TextView.setTextColor(0xff000000);
                    course03TextView.setTextColor(0xffcccccc);

                    course01Tab.setVisibility(View.GONE);
                    course02Tab.setVisibility(View.VISIBLE);
                    course03Tab.setVisibility(View.GONE);
                } else if (view == course03TextView) {
                    course01TextView.setTextColor(0xffcccccc);
                    course02TextView.setTextColor(0xffcccccc);
                    course03TextView.setTextColor(0xff000000);

                    course01Tab.setVisibility(View.GONE);
                    course02Tab.setVisibility(View.GONE);
                    course03Tab.setVisibility(View.VISIBLE);
                }
            }
        });
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
        course01Tab = getView().findViewById(R.id.course01Tab);
        course02Tab = getView().findViewById(R.id.course02Tab);
        course03Tab = getView().findViewById(R.id.course03Tab);
        rightButtonTextView = tabBar.findViewById(R.id.rightButton);
        rightLinearLayout = tabBar.findViewById(R.id.rightLinearLayout);

        courseLinearLayout.setVisibility(View.VISIBLE);
        pinkNearestOrLinearLayout.setVisibility(View.VISIBLE);
        course01TextView.setTextColor(0xff000000);
        course02TextView.setTextColor(0xffcccccc);
        course03TextView.setTextColor(0xffcccccc);
        rightButtonTextView.setText("Ranking");

        tabTitleOnClick(course01TextView);
        tabTitleOnClick(course02TextView);
        tabTitleOnClick(course03TextView);

        rightLinearLayoutOnClick();

        //   mBtnTaokeoverTest = (Button)getView().findViewById(R.id.btnTakeoverTest);
        //  mBtnTaokeoverTest.setOnClickListener();
//        getView().findViewById(R.id.btnTakeoverTest).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                GoNativeScreenAdd(new FrDeviceSearch(), null);
////                Intent intent = new Intent(getActivity(), DeviceSearchActivity.class);
////                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                startActivity(intent);
//            }
//        });
//        recycleScore.setHasFixedSize(true);
//        mManager = new LinearLayoutManager(getActivity());
//        recycleScore.setLayoutManager(mManager);
//        //     mScoreAdapter = new ScoreAdapter(getActivity(), mTestItemList, mTitle.getText().toString().trim(),selectedDevice ,mManager);
//        mScoreAdapter = new ScoreAdapter(getActivity(), scores);
//        recycleScore.setAdapter(mScoreAdapter);
//        mScoreAdapter.notifyDataSetChanged();
    }

    private class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreItemViewHolder> {
        ArrayList<String[]> scoresList;
        protected LinearLayout ll_item;
        FmbCustomDialog fmbDialog;

        public ScoreAdapter(Context context, ArrayList<String[]> scoresList) {
            this.scoresList = scoresList;
        }

        public class ScoreItemViewHolder extends RecyclerView.ViewHolder {
            protected TextView[] tvPar = new TextView[9];
            protected TextView tvRank, tvName;

            public ScoreItemViewHolder(View view) {
                super(view);
                tvRank = view.findViewById(R.id.rank);
                tvName = view.findViewById(R.id.name);

                tvPar[0] = view.findViewById(R.id.hole1);
                tvPar[1] = view.findViewById(R.id.hole2);
                tvPar[2] = view.findViewById(R.id.hole3);
                tvPar[3] = view.findViewById(R.id.hole4);
                tvPar[4] = view.findViewById(R.id.hole5);
                tvPar[5] = view.findViewById(R.id.hole6);
                tvPar[6] = view.findViewById(R.id.hole7);
                tvPar[7] = view.findViewById(R.id.hole8);
                tvPar[8] = view.findViewById(R.id.hole9);
                for (int i = 0; NUM_OF_HOLES > i; i++) {
                    tvPar[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String title = "";
                            switch (v.getId()) {
                                case R.id.hole1:
                                    title = "Par" + "/" + "Course/Hole1" ;
                                case R.id.hole2:
                                    title = "Par" + "/" + "Course/Hole2" ;
                                case R.id.hole3:
                                    title = "Par" + "/" + "Course/Hole3" ;
                                case R.id.hole4:
                                    title = "Par" + "/" + "Course/Hole4" ;
                                case R.id.hole5:
                                    title = "Par" + "/" + "Course/Hole5" ;
                                case R.id.hole6:
                                    title = "Par" + "/" + "Course/Hole6" ;
                                case R.id.hole7:
                                    title = "Par" + "/" + "Course/Hole7" ;
                                case R.id.hole8:
                                    title = "Par" + "/" + "Course/Hole8" ;
                                case R.id.hole9:
                                    title = "Par" + "/" + "Course/Hole9" ;

                            }
                            sDialog = new ScoreDialog(getActivity(), title, "", "취소", "확인", leftListener, rightListener, true);
                            sDialog.show();

                        }
                    });
                }


            }

            private View.OnClickListener leftListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sDialog.dismiss();
                }
            };

            private View.OnClickListener rightListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //
                }
            };
        }

        // RecyclerView에 새로운 데이터를 보여주기 위해 필요한 ViewHolder를 생성해야 할 때 호출됩니다.
        @Override
        public ScoreItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            //  View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.score_row, viewGroup, false);
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.score_row, viewGroup, false);
            ScoreItemViewHolder viewHolder = new ScoreItemViewHolder(view);
            //  ll_item = view.findViewById(R.id.ll_schedule_item);

            return viewHolder;
        }


        @Override
        public void onBindViewHolder(@NonNull ScoreItemViewHolder scoreItemViewHolder, int i) {
            final int pos = i;
            for (int k = 0; scoresList.get(i).length > k; k++) {
                String[] list = scoresList.get(i);
                scoreItemViewHolder.tvRank.setText(list[0]);
                scoreItemViewHolder.tvName.setText(playerList.get(i).getGuestName());

                scoreItemViewHolder.tvPar[0].setText(list[2]);
                scoreItemViewHolder.tvPar[1].setText(list[3]);
                scoreItemViewHolder.tvPar[2].setText(list[4]);
                scoreItemViewHolder.tvPar[3].setText(list[5]);
                scoreItemViewHolder.tvPar[4].setText(list[6]);
                scoreItemViewHolder.tvPar[5].setText(list[7]);
                scoreItemViewHolder.tvPar[6].setText(list[8]);
                scoreItemViewHolder.tvPar[7].setText(list[9]);
                scoreItemViewHolder.tvPar[8].setText(list[10]);
            }
        }

        @Override
        public int getItemCount() {
            return scores.size();
        }


    }

    private void getCourseInfo() {
        String cc_id = "1";
        DataInterface.getInstance().getCourseInfo(getActivity(), cc_id, new DataInterface.ResponseCallback<Object>() {
            @Override
            public void onSuccess(Object response) {
                courseList = (ArrayList<Course>) response;

            }

            @Override
            public void onError(Object response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


}


