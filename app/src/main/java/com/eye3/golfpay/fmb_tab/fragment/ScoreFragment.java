package com.eye3.golfpay.fmb_tab.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.activity.BaseActivity;
import com.eye3.golfpay.fmb_tab.activity.MainActivity;
import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.eye3.golfpay.fmb_tab.model.field.Hole;

import java.util.ArrayList;
import java.util.Objects;


public class ScoreFragment extends BaseFragment {


    protected String TAG = getClass().getSimpleName();
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
    private ArrayList<Course> CourseList = new ArrayList<>();
    LinearLayout[] ScoreTab  = new LinearLayout[4];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
        }

        Course first_course = new Course();
        Hole[] first_course_hole = new Hole[9] ;
        for(int i=0; first_course_hole.length >  i ;i++){
            Hole a_hole = new Hole(String.valueOf(i+1), 4, 300);
            first_course.arrHole[i] =  a_hole;

        }

        first_course.CourseName = "코스1" ;
        first_course.CourseId = "1";


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
        points[6] = "4";
        points[7] = "4";
        points[8] = "4";
        points[9] = "4";
        points[10] = "4";
        points1[0] = "2nd";
        points1[1] = "김영광";
        points1[2] = "4";
        points1[3] = "4";
        points1[4] = "4";
        points1[5] = "5";
        points1[6] = "4";
        points1[7] = "4";
        points1[8] = "4";
        points1[9] = "4";
        points1[10] = "4";
        points2[0] = "1nd";
        points2[1] = "김치욱";
        points2[2] = "4";
        points2[3] = "4";
        points2[4] = "4";
        points2[5] = "5";
        points2[6] = "4";
        points2[7] = "4";
        points2[8] = "4";
        points2[9] = "4";
        points2[10] = "4";
        points3[0] = "5th";
        points3[1] = "한상예";
        points3[2] = "4";
        points3[3] = "4";
        points3[4] = "4";
        points3[5] = "5";
        points3[6] = "4";
        points3[7] = "4";
        points3[8] = "4";
        points3[9] = "4";
        points3[10] = "4";
        points4[0] = "3nd";
        points4[1] = "노근수";
        points4[2] = "4";
        points4[3] = "4";
        points4[4] = "4";
        points4[5] = "5";
        points4[6] = "4";
        points4[7] = "4";
        points4[8] = "4";
        points4[9] = "4";
        points4[10] = "4";
        scores.add(points);
        scores.add(points1);
        scores.add(points2);
        scores.add(points3);
        scores.add(points4);


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

        ScoreTab[0]  = v.findViewById(R.id.course01Tab);

        ((MainActivity) mParentActivity ).showMainBottomBar();
        return v;
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

            }
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
                scoreItemViewHolder.tvName.setText(list[1]);

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
}


