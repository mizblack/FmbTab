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
import com.eye3.golfpay.fmb_tab.model.score.Score;
import com.eye3.golfpay.fmb_tab.model.teeup.Player;
import com.eye3.golfpay.fmb_tab.util.ScoreDialog;
import com.eye3.golfpay.fmb_tab.util.Util;

import java.util.ArrayList;

public class TabCourseLinear extends LinearLayout {
    static final int NUM_OF_HOLES = 9;

   // LinearLayout mScoreColumnLinear, mHolderLinear;
  //  HoleInfoLinear mHoleInfoLinear
    //최상단 홀정보를 보여주기위한 홀더
    LinearLayout mHolderLinear;
    RecyclerView mScoreRecylerView;
    ScoreAdapter mScoreAdapter;
    Context mContext;
    ScoreDialog sDialog;
    //클릭한 스코어 배경 바꿀때 필요한 idx 해당홀인덱스(hole_id -1)
    int mHoleScoreLayoutIdx;

    ArrayList<Player> mPlayerList = new ArrayList<Player>();
    //course_id -1과 동일함
    int mCourseId ;

    public TabCourseLinear(Context context) {
        super(context);

    }

    public TabCourseLinear(Context context, ArrayList<Player> playerList, int courseId) {
        super(context);
        init(context, playerList,  courseId);

    }

    public TabCourseLinear(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Context context, ArrayList<Player> playerList,  int mCourseId) {

     //   mTabIdx = tabIdx;
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.tab_course, this, false);
        mHolderLinear = v.findViewById(R.id.scoreColumn).findViewById(R.id.holderInfoLinear);
        createHoleInfoLinear(context, playerList.get(0).playingCourse.get(mCourseId-1));
        mScoreRecylerView = v.findViewById(R.id.scoreRecylerView);
        mPlayerList = playerList;
        this.mCourseId = mCourseId;
        initRecyclerView(playerList);
        addView(v);

    }

    private void initRecyclerView(ArrayList<Player> playerList) {
        LinearLayoutManager mManager;

        mScoreRecylerView.setHasFixedSize(true);
        mManager = new LinearLayoutManager(mContext);
        mScoreRecylerView.setLayoutManager(mManager);
        mScoreAdapter = new ScoreAdapter(mContext, playerList,playerList.get(0).playingCourse.get(mCourseId -1),mCourseId);
        mScoreRecylerView.setAdapter(mScoreAdapter);
        mScoreAdapter.notifyDataSetChanged();
    }

    private void createHoleInfoLinear(Context context, Course course) {
        Hole[] holes = course.holes;

        int totalPar = 0;
        int totalMeter = 0;
        for (int k = 0; holes.length > k; k++) {
            HoleInfoLinear a_holeInfoLinear = new HoleInfoLinear(context, holes[k]);
            a_holeInfoLinear.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mHolderLinear.addView(a_holeInfoLinear);
            totalPar = totalPar + Integer.valueOf(holes[k].par);
            totalMeter = totalMeter + Integer.valueOf(holes[k].hole_total_size);
        }
        Hole totalHole = new Hole();
        totalHole.par = String.valueOf(totalPar);
        totalHole.hole_total_size = String.valueOf(totalMeter);
        HoleInfoLinear a_holeInfoTotalLinear = new HoleInfoLinear(context, totalHole);
        TextView tvCourseName = a_holeInfoTotalLinear.findViewById(R.id.course_name);
        TextView tvCoursId = a_holeInfoTotalLinear.findViewById(R.id.hole_id);
        tvCoursId.setVisibility(View.GONE);
        tvCourseName.setVisibility(View.VISIBLE);

        tvCourseName.setText(course.courseName);
        mHolderLinear.addView(a_holeInfoTotalLinear);

    }


    private class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreItemViewHolder> {
        ArrayList<Player> playerList ;
        Course mCurrentCourse;
        int   mCourseId ;
        public ScoreAdapter(Context context, ArrayList<Player> playerList, Course mCourse , int mCourseId) {
            this.playerList = playerList;
            this.mCourseId = mCourseId;
            mCurrentCourse = mCourse;
        }


        public class ScoreItemViewHolder extends RecyclerView.ViewHolder {
            protected TextView tvRank, tvName;
            protected LinearLayout[] holeScoreLayout = new LinearLayout[9];
            protected LinearLayout[] courseTotal = new LinearLayout[2]; //동적생성전 임시처리
            protected TextView wholeTotal;

            public ScoreItemViewHolder(View view) {
                super(view);
                tvRank = view.findViewById(R.id.rank);
                tvName = view.findViewById(R.id.name);

                holeScoreLayout[0] = view.findViewById(R.id.hole1LinearLayout);
                holeScoreLayout[1] = view.findViewById(R.id.hole2LinearLayout);
                holeScoreLayout[2] = view.findViewById(R.id.hole3LinearLayout);
                holeScoreLayout[3] = view.findViewById(R.id.hole4LinearLayout);
                holeScoreLayout[4] = view.findViewById(R.id.hole5LinearLayout);
                holeScoreLayout[5] = view.findViewById(R.id.hole6LinearLayout);
                holeScoreLayout[6] = view.findViewById(R.id.hole7LinearLayout);
                holeScoreLayout[7] = view.findViewById(R.id.hole8LinearLayout);
                holeScoreLayout[8] = view.findViewById(R.id.hole9LinearLayout);
                courseTotal[0] = view.findViewById(R.id.ll_course0_total);


                wholeTotal = view.findViewById(R.id.whole_totalColumn);

                for ( int i = 0; NUM_OF_HOLES > i; i++) {
                    holeScoreLayout[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String title = "";
                            Hole mSelectedHole = null;
                            switch (v.getId()) {
                                case R.id.hole1LinearLayout:
                                    title = "Par" + "/" + "Course/Hole1";
                                    mHoleScoreLayoutIdx = 0;
                                    notifyDataSetChanged();
                                    break;
                                case R.id.hole2LinearLayout:
                                    title = "Par" + "/" + "Course/Hole2";
                                    mHoleScoreLayoutIdx = 1;
                                    notifyDataSetChanged();
                                    break;
                                case R.id.hole3LinearLayout:
                                    title = "Par" + "/" + "Course/Hole3";
                                    mHoleScoreLayoutIdx = 2;
                                    notifyDataSetChanged();
                                    break;
                                case R.id.hole4LinearLayout:
                                    title = "Par" + "/" + "Course/Hole4";
                                    mHoleScoreLayoutIdx = 3;
                                    notifyDataSetChanged();
                                    break;
                                case R.id.hole5LinearLayout:
                                    title = "Par" + "/" + "Course/Hole5";
                                    mHoleScoreLayoutIdx = 4;
                                    notifyDataSetChanged();
                                    break;
                                case R.id.hole6LinearLayout:
                                    title = "Par" + "/" + "Course/Hole6";
                                    mHoleScoreLayoutIdx = 5;
                                    notifyDataSetChanged();
                                    break;
                                case R.id.hole7LinearLayout:
                                    title = "Par" + "/" + "Course/Hole7";
                                    mHoleScoreLayoutIdx = 6;
                                    notifyDataSetChanged();
                                    break;
                                case R.id.hole8LinearLayout:
                                    title = "Par" + "/" + "Course/Hole8";
                                    mHoleScoreLayoutIdx = 7;
                                    notifyDataSetChanged();
                                    break;
                                case R.id.hole9LinearLayout:
                                    title = "Par" + "/" + "Course/Hole9";
                                    mHoleScoreLayoutIdx = 8;
                                    notifyDataSetChanged();
                                    break;
                                default:

                            }
                            sDialog = new ScoreDialog(mContext, "타이틀", "", "취소", "확인", null, null, mPlayerList, mCurrentCourse, mHoleScoreLayoutIdx);
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


        @Override
        public void onBindViewHolder(@NonNull ScoreAdapter.ScoreItemViewHolder scoreItemViewHolder, int i) {
            final int pos = i;                            //course tab index
            Course course = mPlayerList.get(i).playingCourse.get(mCourseId-1);


            scoreItemViewHolder.tvRank.setText("lst");
            scoreItemViewHolder.tvName.setText(playerList.get(i).name);

            for (int j = 0; scoreItemViewHolder.holeScoreLayout.length > j; j++) {
                if (j == mHoleScoreLayoutIdx)
                    scoreItemViewHolder.holeScoreLayout[j].setBackgroundColor(Color.GRAY);
                else
                    scoreItemViewHolder.holeScoreLayout[j].setBackgroundColor(Color.WHITE);
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


//    public int convertTarIntoPar(int tar, int parNumForHole) {
//        return tar - parNumForHole;
//    }
//
//    public int convertTarIntoParForString(int tar, int parNumForHole) {
//        return tar - parNumForHole;
//    }
//
//    public int convertParIntoTar(int par, int parNumForHole) {
//        return par + parNumForHole;
//    }

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
