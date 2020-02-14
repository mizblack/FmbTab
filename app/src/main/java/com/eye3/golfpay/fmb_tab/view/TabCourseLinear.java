package com.eye3.golfpay.fmb_tab.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.AppDef;
import com.eye3.golfpay.fmb_tab.listener.ScoreInputFinishListener;
import com.eye3.golfpay.fmb_tab.model.field.Hole;
import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.eye3.golfpay.fmb_tab.model.teeup.Player;
import com.eye3.golfpay.fmb_tab.util.ScoreDialog;
import com.eye3.golfpay.fmb_tab.util.Util;

import java.util.ArrayList;

public class TabCourseLinear extends LinearLayout {
    static final int NUM_OF_HOLES = 9;
    //최상단 홀정보를 보여주기위한 홀더
    LinearLayout mHolderLinear;
    //홀정보레이아웃 어레이
    HoleInfoLinear[] holeInfoLinear = new HoleInfoLinear[10];
    RecyclerView mScoreRecylerView;
    ScoreAdapter mScoreAdapter;
    Context mContext;
    ScoreDialog sDialog;
    //각홀의 레이아웃 아이디
    int mHoleScoreLayoutIdx;
    //탭아이디 (중요)
    int mTabIdx ;
    ArrayList<Player> mPlayerList = new ArrayList<Player>();

    public TabCourseLinear(Context context) {
        super(context);

    }

    /*
     * idx : tab의 순서idx (순서대로 tabcourselinear 뿌려주면됨)
     */
    public TabCourseLinear(Context context, ArrayList<Player> playerList, int tabIdx) {
        super(context);
        init(context, playerList, tabIdx);

    }

    public TabCourseLinear(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Context context, ArrayList<Player> playerList, int tabIdx) {

        mContext = context;
        mTabIdx = tabIdx;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.tab_course, this, false);
        mHolderLinear = v.findViewById(R.id.scoreColumn).findViewById(R.id.holderInfoLinear);

        createHoleInfoLinear(context, playerList.get(0).playingCourse.get(tabIdx));

        mScoreRecylerView = v.findViewById(R.id.scoreRecylerView);
        mPlayerList = playerList;
        initRecyclerView(playerList, tabIdx );
        addView(v);

    }

    private void initRecyclerView(ArrayList<Player> playerList , int tabIdx) {
        LinearLayoutManager mManager;

        mScoreRecylerView.setHasFixedSize(true);
        mManager = new LinearLayoutManager(mContext);
        mScoreRecylerView.setLayoutManager(mManager);
        mScoreAdapter = new ScoreAdapter(mContext, playerList, playerList.get(0).playingCourse.get(tabIdx) );
        mScoreRecylerView.setAdapter(mScoreAdapter);
        mScoreAdapter.notifyDataSetChanged();
    }

    /*
     *  최상단 홀정보 보여주는 뷰생성
     *
     */
    private void createHoleInfoLinear(Context context, Course course) {
        Hole[] holes = course.holes;

        int totalPar = 0;
        int totalMeter = 0;
        for (int k = 0; holes.length > k; k++) {
            holeInfoLinear[k] = new HoleInfoLinear(context, holes[k]);
            holeInfoLinear[k].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mHolderLinear.addView(holeInfoLinear[k]);
            totalPar = totalPar + Integer.valueOf(holes[k].par);
            totalMeter = totalMeter + Integer.valueOf(holes[k].hole_total_size);
        }
        //홀인포 전체를 나타내는 마지막 셀정보 입력
        Hole totalHole = new Hole();
        totalHole.par = String.valueOf(totalPar);
        totalHole.hole_total_size = String.valueOf(totalMeter);
        holeInfoLinear[holeInfoLinear.length - 1] = new HoleInfoLinear(context, totalHole);
        TextView tvCourseName = holeInfoLinear[holeInfoLinear.length - 1].findViewById(R.id.course_name);
        TextView tvCoursId = holeInfoLinear[holeInfoLinear.length - 1].findViewById(R.id.hole_id);
        tvCoursId.setVisibility(View.GONE);
        tvCourseName.setVisibility(View.VISIBLE);

        tvCourseName.setText(course.courseName);
        mHolderLinear.addView(holeInfoLinear[holeInfoLinear.length - 1]);

    }


    private class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreItemViewHolder> {
        ArrayList<Player> playerList;
        Course mCurrentCourse;

        public ScoreAdapter(Context context, ArrayList<Player> playerList, Course mCourse) {
            this.playerList = playerList;
            mCurrentCourse = mCourse;
        }


        public class ScoreItemViewHolder extends RecyclerView.ViewHolder {
            protected TextView tvRank, tvName;
            protected LinearLayout[] holeScoreLayout = new LinearLayout[9];
            protected LinearLayout[] courseTotal = new LinearLayout[2]; //동적생성전 임시처리
            protected TextView wholeTotal;
            protected LinearLayout ll_score_row;

            public ScoreItemViewHolder(View view) {
                super(view);
                ll_score_row = view.findViewById(R.id.ll_score_row);
                tvRank = view.findViewById(R.id.rank);
                tvName = view.findViewById(R.id.name);

                holeScoreLayout[0] = view.findViewById(R.id.hole1_ll);
                holeScoreLayout[1] = view.findViewById(R.id.hole2_ll);
                holeScoreLayout[2] = view.findViewById(R.id.hole3_ll);
                holeScoreLayout[3] = view.findViewById(R.id.hole4_ll);
                holeScoreLayout[4] = view.findViewById(R.id.hole5_ll);
                holeScoreLayout[5] = view.findViewById(R.id.hole6_ll);
                holeScoreLayout[6] = view.findViewById(R.id.hole7_ll);
                holeScoreLayout[7] = view.findViewById(R.id.hole8_ll);
                holeScoreLayout[8] = view.findViewById(R.id.hole9_ll);
                courseTotal[0] = view.findViewById(R.id.ll_course0_total);


                wholeTotal = view.findViewById(R.id.whole_totalColumn);

                for (int i = 0; NUM_OF_HOLES > i; i++) {
                    holeScoreLayout[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            switch (v.getId()) {
                                case R.id.hole1_ll:
                                    mHoleScoreLayoutIdx = 0 ;
                                    notifyDataSetChanged();
                                    break;
                                case R.id.hole2_ll:
                                    mHoleScoreLayoutIdx =   1 ;
                                    notifyDataSetChanged();
                                    break;
                                case R.id.hole3_ll:
                                    mHoleScoreLayoutIdx =    2;;
                                    notifyDataSetChanged();
                                    break;
                                case R.id.hole4_ll:
                                    mHoleScoreLayoutIdx = 3 ;
                                    notifyDataSetChanged();
                                    break;
                                case R.id.hole5_ll:
                                    mHoleScoreLayoutIdx = 4 ;
                                    notifyDataSetChanged();
                                    break;
                                case R.id.hole6_ll:
                                    mHoleScoreLayoutIdx = 5 ;
                                    notifyDataSetChanged();
                                    break;
                                case R.id.hole7_ll:
                                    mHoleScoreLayoutIdx = 6;
                                    notifyDataSetChanged();
                                    break;
                                case R.id.hole8_ll:
                                    mHoleScoreLayoutIdx = 7 ;
                                    notifyDataSetChanged();
                                    break;
                                case R.id.hole9_ll:
                                    mHoleScoreLayoutIdx = 8;
                                    notifyDataSetChanged();
                                    break;
                                default:

                            }
                            sDialog = new ScoreDialog(mContext, "타이틀", "", "취소", "확인", null, null, mPlayerList, mCurrentCourse, mTabIdx, mHoleScoreLayoutIdx);
                            sDialog.setOnScoreInputFinishListener(listener);
                            sDialog.show();

                        }
                    });
                }


            }

            private View.OnClickListener leftListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sDialog.dismiss();
                    //  sDialog.
                }
            };

            private View.OnClickListener rightListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //
                }
            };

            ScoreInputFinishListener listener = new ScoreInputFinishListener() {
                @Override
                public void OnScoreInputFinished(ArrayList<Player> playerList) {
                    mPlayerList = playerList;
                    notifyDataSetChanged();
                }
            };
        }


        // RecyclerView에 새로운 데이터를 보여주기 위해 필요한 ViewHolder를 생성해야 할 때 호출됩니다.
        @Override
        public ScoreAdapter.ScoreItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.score_row, viewGroup, false);
            ScoreAdapter.ScoreItemViewHolder viewHolder = new ScoreAdapter.ScoreItemViewHolder(view);
            return viewHolder;
        }

        /*
         * holeScoreLayout[] :홀스코어뷰
         * course.holes[].playedScore: 홀별스코어 점수데이터
         */
        @Override
        public void onBindViewHolder(@NonNull ScoreAdapter.ScoreItemViewHolder scoreItemViewHolder, int i) {
            final int pos = i;                            //course tab index
            Course course = mPlayerList.get(i).playingCourse.get(mTabIdx);
            if (i % 2 == 0) {
                scoreItemViewHolder.ll_score_row.setBackgroundColor(Color.parseColor("#EBEFF1"));

            } else {
                scoreItemViewHolder.ll_score_row.setBackgroundColor(Color.parseColor("#F5F7F8"));

            }
            if (playerList.get(i).Ranking.equals("1")) {
                scoreItemViewHolder.tvRank.setTextColor(Color.CYAN);
                scoreItemViewHolder.tvName.setTextColor(Color.CYAN);
            }
            scoreItemViewHolder.tvRank.setText(playerList.get(i).Ranking);
            scoreItemViewHolder.tvName.setText(playerList.get(i).name);

            for (int j = 0; scoreItemViewHolder.holeScoreLayout.length > j; j++) {
                if (j == mHoleScoreLayoutIdx) {
                    scoreItemViewHolder.holeScoreLayout[j].setBackgroundColor(Color.GRAY);
                    holeInfoLinear[j].setBackgroundColor(Color.BLUE);
                } else {
                    scoreItemViewHolder.holeScoreLayout[j].setBackgroundColor(Color.WHITE);
                    holeInfoLinear[j].setBackgroundColor(Color.WHITE);
                }
            }


            ((TextView) scoreItemViewHolder.holeScoreLayout[0].findViewById(R.id.hole1_hit)).setText(AppDef.Par_Tar(course.holes[0].playedScore, AppDef.isTar));
            ((TextView) scoreItemViewHolder.holeScoreLayout[0].findViewById(R.id.hole1_putt)).setText(course.holes[0].playedScore.putting);

            ((TextView) scoreItemViewHolder.holeScoreLayout[1].findViewById(R.id.hole2_hit)).setText(AppDef.Par_Tar(course.holes[1].playedScore, AppDef.isTar));
            ((TextView) scoreItemViewHolder.holeScoreLayout[1].findViewById(R.id.hole2_putt)).setText(course.holes[1].playedScore.putting);

            ((TextView) scoreItemViewHolder.holeScoreLayout[2].findViewById(R.id.hole3_hit)).setText(AppDef.Par_Tar(course.holes[2].playedScore, AppDef.isTar));
            ((TextView) scoreItemViewHolder.holeScoreLayout[2].findViewById(R.id.hole3_putt)).setText(course.holes[2].playedScore.putting);

            ((TextView) scoreItemViewHolder.holeScoreLayout[3].findViewById(R.id.hole4_hit)).setText(AppDef.Par_Tar(course.holes[3].playedScore, AppDef.isTar));
            ((TextView) scoreItemViewHolder.holeScoreLayout[3].findViewById(R.id.hole4_putt)).setText(course.holes[3].playedScore.putting);

            ((TextView) scoreItemViewHolder.holeScoreLayout[4].findViewById(R.id.hole5_hit)).setText(AppDef.Par_Tar(course.holes[4].playedScore, AppDef.isTar));
            ((TextView) scoreItemViewHolder.holeScoreLayout[4].findViewById(R.id.hole5_putt)).setText(course.holes[4].playedScore.putting);

            ((TextView) scoreItemViewHolder.holeScoreLayout[5].findViewById(R.id.hole6_hit)).setText(AppDef.Par_Tar(course.holes[5].playedScore, AppDef.isTar));
            ((TextView) scoreItemViewHolder.holeScoreLayout[5].findViewById(R.id.hole6_putt)).setText(course.holes[5].playedScore.putting);

            ((TextView) scoreItemViewHolder.holeScoreLayout[6].findViewById(R.id.hole7_hit)).setText(AppDef.Par_Tar(course.holes[6].playedScore, AppDef.isTar));
            ((TextView) scoreItemViewHolder.holeScoreLayout[6].findViewById(R.id.hole7_putt)).setText(course.holes[6].playedScore.putting);

            ((TextView) scoreItemViewHolder.holeScoreLayout[7].findViewById(R.id.hole8_hit)).setText(AppDef.Par_Tar(course.holes[7].playedScore, AppDef.isTar));
            ((TextView) scoreItemViewHolder.holeScoreLayout[7].findViewById(R.id.hole8_putt)).setText(course.holes[7].playedScore.putting);

            ((TextView) scoreItemViewHolder.holeScoreLayout[8].findViewById(R.id.hole9_hit)).setText(AppDef.Par_Tar(course.holes[8].playedScore, AppDef.isTar));
            ((TextView) scoreItemViewHolder.holeScoreLayout[8].findViewById(R.id.hole9_putt)).setText(course.holes[8].playedScore.putting);

            //코스토탈
            ((TextView) scoreItemViewHolder.courseTotal[0].findViewById(R.id.ll_course0_total).findViewById(R.id.course0_total_tar)).setText(Par_Tar_Total(course, AppDef.isTar));
            ((TextView) scoreItemViewHolder.courseTotal[0].findViewById(R.id.ll_course0_total).findViewById(R.id.course0_toatal_putt)).setText(Putt_Total(course));

//            ((TextView) scoreItemViewHolder.courseTotal[1].findViewById(R.id.ll_course1_total).findViewById(R.id.course1_total_tar)).setText(Par_Tar_Total(course, AppDef.isTar));
//            ((TextView) scoreItemViewHolder.courseTotal[1].findViewById(R.id.ll_course1_total).findViewById(R.id.course1_total_putt)).setText(Putt_Total(course));
            //동적생성시 3개이상도 할수있게할것

            //전체토탈

        }

        @Override
        public int getItemCount() {
            return playerList.size();
        }
    }


    private String Par_Tar_Total(Course course, boolean istar) {
        int total_score = 0;
        if (istar) {
            for (int i = 0; course.holes.length > i; i++) {
                if (Util.isIntegerNumber(course.holes[i].playedScore.tar))
                    total_score = total_score + Integer.valueOf(course.holes[i].playedScore.tar);
            }
        } else {
            for (int i = 0; course.holes.length > i; i++) {
                if (Util.isIntegerNumber(course.holes[i].playedScore.par))
                    total_score = total_score + Integer.valueOf(course.holes[i].playedScore.par);
            }
        }
        //다시 스트링으로 변환해 리턴
        return String.valueOf(total_score);
    }

    private String Putt_Total(Course course) {
        int total_score = 0;

        for (int i = 0; course.holes.length > i; i++) {
            if (Util.isIntegerNumber(course.holes[i].playedScore.par))
                total_score = total_score + Integer.valueOf(course.holes[i].playedScore.putting);
        }

        //다시 스트링으로 변환해 리턴
        return String.valueOf(total_score);
    }



    public int getTotalDistance(Hole[] holes) {
        int totalDistance = 0;

        for (int i = 0; holes.length > i; i++) {
            if (holes[i].hole_total_size == null) {
                return -1;
            } else {
                if (Util.isNumber(holes[i].hole_total_size))
                    totalDistance = totalDistance + Integer.valueOf(holes[i].hole_total_size);
            }
        }
        return totalDistance;
    }


}
