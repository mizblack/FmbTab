package com.eye3.golfpay.fmb_tab.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.AppDef;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.listener.ScoreInputFinishListener;
import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.eye3.golfpay.fmb_tab.model.field.Hole;
import com.eye3.golfpay.fmb_tab.model.score.Score;
import com.eye3.golfpay.fmb_tab.model.teeup.Player;
import com.eye3.golfpay.fmb_tab.util.ScoreDialog;
import com.eye3.golfpay.fmb_tab.util.Util;

import java.util.List;

public class TabCourseLinear extends LinearLayout {
    static final int NUM_OF_HOLES = 9;

    //최상단 홀정보를 보여주기위한 홀더
    LinearLayout mHolderLinear;
    //홀정보레이아웃 어레이
    HoleInfoLinear[] holeInfoLinear = new HoleInfoLinear[10];
    RecyclerView mScoreRecyclerView;
    ScoreAdapter mScoreAdapter;
    Context mContext;
    //각홀의 레이아웃 아이디
    static int mHoleScoreLayoutIdx;
    //탭아이디 (중요)
    int mTabIdx;
    List<Player> mPlayerList;
    Spinner mDistanceSpinner;
    ArrayAdapter arrayAdapter;
    public ScoreDialog sDialog;
    ScoreInputFinishListener inputFinishListener;
    //ctype 에따라 정렬된 코스
    Course mCtypeArrangedCourse ;

    public TabCourseLinear(Context context) {
        super(context);

    }

    public static int getmHoleScoreLayoutIdx() {
        return mHoleScoreLayoutIdx;
    }

    public static void setmHoleScoreLayoutIdx(int Idx) {
        mHoleScoreLayoutIdx = Idx;
    }

    private boolean isPreviousHoleScoreFilledUp(List<Player> playerList, int mTabIdx, int mHoleScoreLayoutIdx) {

        int previousHoleScoreLayoutIdx = mHoleScoreLayoutIdx - 1;
        if (playerList == null)
            return false;
        //최초 홀은 무조건 통과
        if (mTabIdx == 0 && mHoleScoreLayoutIdx == 0)
            return true;
        for (int i = 0; playerList.size() > i; i++) {

            if (mTabIdx == 0) {

                // the last(current) course
                if (playerList.get(i).playingCourse.get(mTabIdx).holes.get(previousHoleScoreLayoutIdx).playedScore.tar.equals("-"))
                    return false;
                if (playerList.get(i).playingCourse.get(mTabIdx).holes.get(previousHoleScoreLayoutIdx).playedScore.putting.equals("-"))
                    return false;

            } else {
                if (playerList.get(i).playingCourse.get(mTabIdx - 1).holes.get(8).playedScore.tar.equals("-"))
                    return false;
                if (playerList.get(i).playingCourse.get(mTabIdx - 1).holes.get(8).playedScore.putting.equals("-"))
                    return false;

                for (int k = 0; previousHoleScoreLayoutIdx > k; k++) {
                    if (playerList.get(i).playingCourse.get(mTabIdx).holes.get(previousHoleScoreLayoutIdx).playedScore.tar.equals("-"))
                        return false;
                    if (playerList.get(i).playingCourse.get(mTabIdx).holes.get(previousHoleScoreLayoutIdx).playedScore.putting.equals("-"))
                        return false;
                }
            }

        }
        return true;
    }
    /*
     * idx : tab 의 순서 idx (순서대로 tabCourseLinear 뿌려주면됨)
     */
//    public TabCourseLinear(Context context, ArrayList<Player> playerList, Course ctyped, int tabIdx) {
//        super(context);
//        init(context, playerList, ctyped, tabIdx);
//
//    }

    public TabCourseLinear(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(final Context context, final List<Player> playerList, Course ctyped,
                     int tabIdx) {

        this.mContext = context;
        this.mTabIdx = tabIdx;
        this.mPlayerList = playerList;
        this.mCtypeArrangedCourse = ctyped;
       //여기 수정할것*******************************************
     //   this.mCtypeArrangedCourse = mPlayerList.get(0).playingCourse.get(mTabIdx);
        //스코어뷰를 재활용하므로 반드시 이전 뷰들을 모두 제거해야 현생성되는 뷰가 보여진다.
        this.removeAllViewsInLayout();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.tab_course, this, false);
        mHolderLinear = v.findViewById(R.id.scoreColumn).findViewById(R.id.holderInfoLinear);
        mDistanceSpinner = v.findViewById(R.id.spinn_distance);
        arrayAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.spinn_array));
        mDistanceSpinner.setAdapter(arrayAdapter);
        mDistanceSpinner.setSelection(0);
        //거리 환산 적용
        mDistanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((parent.getItemAtPosition(position)).toString().equals("yard")) {
                    Global.isYard = true;
                    mDistanceSpinner.setSelection(2);
                    createHoleInfoLinear(mContext, mCtypeArrangedCourse);
                } else if ((parent.getItemAtPosition(position)).toString().equals("meter")) {
                    Global.isYard = false;
                    mDistanceSpinner.setSelection(1);
                    createHoleInfoLinear(mContext, mCtypeArrangedCourse);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        createHoleInfoLinear(mContext, mCtypeArrangedCourse);

        mScoreRecyclerView = v.findViewById(R.id.scoreRecylerView);
        initRecyclerView(mPlayerList, mTabIdx);
        addView(v);

    }

    public void setOnScoreInputFinishListener(ScoreInputFinishListener listener) {
        if (listener != null)
            this.inputFinishListener = listener;
    }


    private void initRecyclerView(List<Player> playerList, int tabIdx) {
        LinearLayoutManager mManager;

        mScoreRecyclerView.setHasFixedSize(true);
        mManager = new LinearLayoutManager(mContext);
        mScoreRecyclerView.setLayoutManager(mManager);
        mScoreAdapter = new ScoreAdapter(mContext, playerList, playerList.get(0).playingCourse.get(tabIdx));
        mScoreRecyclerView.setAdapter(mScoreAdapter);
        mScoreAdapter.notifyDataSetChanged();
    }

    /*
     *  최상단 홀정보 보여주는 뷰생성
     *
     */
    private void createHoleInfoLinear(Context context, Course course) {
        mHolderLinear.removeAllViewsInLayout();

        List<Hole> holes = course.holes;

        int totalPar = 0;
        int totalMeter = 0;
        for (int k = 0; holes.size() > k; k++) {
            holeInfoLinear[k] = new HoleInfoLinear(context, holes.get(k));
            holeInfoLinear[k].setCourse(course);
            holeInfoLinear[k].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mHolderLinear.addView(holeInfoLinear[k]);
            if (holes.get(k).par != null && Util.isInteger(holes.get(k).par))
                totalPar = totalPar + Integer.parseInt(holes.get(k).par);
            if (holes.get(k).hole_total_size != null && Util.isInteger(holes.get(k).hole_total_size))
                totalMeter = totalMeter + Integer.parseInt(holes.get(k).hole_total_size);
        }
        //홀인포 전체를 나타내는 마지막 셀정보 입력
        Hole totalHole = new Hole();
        totalHole.par = String.valueOf(totalPar);
        totalHole.hole_total_size = String.valueOf(totalMeter);
        holeInfoLinear[holeInfoLinear.length - 1] = new HoleInfoLinear(context, totalHole);
        TextView tvCourseName = holeInfoLinear[holeInfoLinear.length - 1].findViewById(R.id.course_name);
        TextView tvCoursId = holeInfoLinear[holeInfoLinear.length - 1].findViewById(R.id.hole_no);
        tvCoursId.setVisibility(View.GONE);
        tvCourseName.setVisibility(View.VISIBLE);

        tvCourseName.setText(course.courseName);
        mHolderLinear.addView(holeInfoLinear[holeInfoLinear.length - 1]);

    }


    private class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreItemViewHolder> {
        List<Player> playerList;
        Course mCurrentCourse;
        Context mContext;

        ScoreAdapter(Context context, List<Player> playerList, Course mCourse) {
            this.playerList = playerList;
            this.mCurrentCourse = mCourse;
            this.mContext = context;
        }


        class ScoreItemViewHolder extends RecyclerView.ViewHolder {
            TextView tvRank, tvName;
            LinearLayout[] holeScoreLayout = new LinearLayout[9];
            LinearLayout[] courseTotal = new LinearLayout[2]; //동적생성전 임시처리
            LinearLayout wholeTotalLinear ;
            LinearLayout ll_score_row;

            ScoreItemViewHolder(View view) {
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
                courseTotal[1] = view.findViewById(R.id.ll_course1_total);

                wholeTotalLinear = view.findViewById(R.id.whole_total_linear);

                for (int i = 0; NUM_OF_HOLES > i; i++) {
                    holeScoreLayout[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            switch (v.getId()) {
                                case R.id.hole1_ll:
                                    mHoleScoreLayoutIdx = 0;
                                    break;
                                case R.id.hole2_ll:
                                    mHoleScoreLayoutIdx = 1;
                                    break;
                                case R.id.hole3_ll:
                                    mHoleScoreLayoutIdx = 2;
                                    break;
                                case R.id.hole4_ll:
                                    mHoleScoreLayoutIdx = 3;
                                    break;
                                case R.id.hole5_ll:
                                    mHoleScoreLayoutIdx = 4;
                                    break;
                                case R.id.hole6_ll:
                                    mHoleScoreLayoutIdx = 5;
                                    break;
                                case R.id.hole7_ll:
                                    mHoleScoreLayoutIdx = 6;
                                    break;
                                case R.id.hole8_ll:
                                    mHoleScoreLayoutIdx = 7;
                                    break;
                                case R.id.hole9_ll:
                                    mHoleScoreLayoutIdx = 8;
                                    break;
                                default:

                            }
                            Global.viewPagerPosition = mHoleScoreLayoutIdx;
                            if (isPreviousHoleScoreFilledUp(playerList, mTabIdx, mHoleScoreLayoutIdx)) {
                                notifyDataSetChanged();
                                sDialog = new ScoreDialog(mContext, "저장", "취소", null, null, playerList, mTabIdx, mHoleScoreLayoutIdx);
                                sDialog.setOnScoreInputFinishListener(listener);
                                sDialog.show();
                            } else {
                                Toast.makeText(mContext, "이전 플레이어 점수를 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }


            }

//            private View.OnClickListener leftListener = new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    inputFinishListener.OnScoreInputFinished(mPlayerList);
//                    sDialog.dismiss();
//
//                }
//            };
//
//            private View.OnClickListener rightListener = new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //
//                }
//            };

            ScoreInputFinishListener listener = new ScoreInputFinishListener() {
                @Override
                public void OnScoreInputFinished(List<Player> playerList) {
                    mPlayerList = playerList;
                    //scoreFragment 로 화면갱신을위한 observer 적용
                    inputFinishListener.OnScoreInputFinished(mPlayerList);
//                    notifyDataSetChanged();
                }
            };
        }


        // RecyclerView 에 새로운 데이터를 보여주기 위해 필요한 ViewHolder 를 생성해야 할 때 호출됩니다.
        @NonNull
        @Override
        public ScoreAdapter.ScoreItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.score_row, viewGroup, false);
            return new ScoreItemViewHolder(view);
        }

        /*
         * holeScoreLayout[] :홀스코어뷰
         * course.holes[].playedScore: 홀별스코어 점수데이터
         */
        @Override
        public void onBindViewHolder(@NonNull ScoreAdapter.ScoreItemViewHolder scoreItemViewHolder, int i) {
            Course course = playerList.get(i).playingCourse.get(mTabIdx);

            if (playerList.get(i).Ranking.equals("1")) {
                scoreItemViewHolder.tvRank.setTextColor(Color.parseColor("#00abc5"));
                scoreItemViewHolder.tvName.setTextColor(Color.parseColor("#00abc5"));
            }
            scoreItemViewHolder.tvRank.setText(playerList.get(i).Ranking);
            scoreItemViewHolder.tvName.setText(playerList.get(i).name);

            for (int j = 0; scoreItemViewHolder.holeScoreLayout.length > j; j++) {
                if (j == mHoleScoreLayoutIdx) {
                    scoreItemViewHolder.holeScoreLayout[j].setBackgroundColor(Color.GRAY);
                    holeInfoLinear[j].setBackgroundColor(0xff00abc5);
                    holeInfoLinear[j].tvHoleNo.setTextColor(0xffffffff);
                    holeInfoLinear[j].tvPar.setTextColor(0xfff5f7f8);
                    holeInfoLinear[j].tvMeter.setTextColor(0xfff5f7f8);
                } else {
                    scoreItemViewHolder.holeScoreLayout[j].setBackgroundColor(Color.WHITE);
                    holeInfoLinear[j].setBackgroundColor(0xffffffff);
                    holeInfoLinear[j].tvHoleNo.setTextColor(0xff999999);
                    holeInfoLinear[j].tvPar.setTextColor(0xff999999);
                    holeInfoLinear[j].tvMeter.setTextColor(0xff999999);

                    if (i % 2 == 0) {
                        scoreItemViewHolder.holeScoreLayout[j].setBackgroundColor(Color.parseColor("#EBEFF1"));
                        scoreItemViewHolder.itemView.setBackgroundColor(Color.parseColor("#EBEFF1"));
                    } else {
                        scoreItemViewHolder.holeScoreLayout[j].setBackgroundColor(Color.parseColor("#F5F7F8"));
                        scoreItemViewHolder.itemView.setBackgroundColor(Color.parseColor("#F5F7F8"));

                    }

                }
            }


            ((TextView) scoreItemViewHolder.holeScoreLayout[0].findViewById(R.id.hole1_hit)).setText(AppDef.Par_Tar(course.holes.get(0).playedScore, AppDef.isTar));
            ((TextView) scoreItemViewHolder.holeScoreLayout[0].findViewById(R.id.hole1_putt)).setText(course.holes.get(0).playedScore.putting);
            setBadge(((TextView) scoreItemViewHolder.holeScoreLayout[0].findViewById(R.id.hole1_hit)), course.holes.get(0));

            ((TextView) scoreItemViewHolder.holeScoreLayout[1].findViewById(R.id.hole2_hit)).setText(AppDef.Par_Tar(course.holes.get(1).playedScore, AppDef.isTar));
            ((TextView) scoreItemViewHolder.holeScoreLayout[1].findViewById(R.id.hole2_putt)).setText(course.holes.get(1).playedScore.putting);
            setBadge(((TextView) scoreItemViewHolder.holeScoreLayout[1].findViewById(R.id.hole2_hit)), course.holes.get(1));

            ((TextView) scoreItemViewHolder.holeScoreLayout[2].findViewById(R.id.hole3_hit)).setText(AppDef.Par_Tar(course.holes.get(2).playedScore, AppDef.isTar));
            ((TextView) scoreItemViewHolder.holeScoreLayout[2].findViewById(R.id.hole3_putt)).setText(course.holes.get(2).playedScore.putting);
            setBadge(((TextView) scoreItemViewHolder.holeScoreLayout[2].findViewById(R.id.hole3_hit)), course.holes.get(2));

            ((TextView) scoreItemViewHolder.holeScoreLayout[3].findViewById(R.id.hole4_hit)).setText(AppDef.Par_Tar(course.holes.get(3).playedScore, AppDef.isTar));
            ((TextView) scoreItemViewHolder.holeScoreLayout[3].findViewById(R.id.hole4_putt)).setText(course.holes.get(3).playedScore.putting);
            setBadge(((TextView) scoreItemViewHolder.holeScoreLayout[3].findViewById(R.id.hole4_hit)), course.holes.get(3));

            ((TextView) scoreItemViewHolder.holeScoreLayout[4].findViewById(R.id.hole5_hit)).setText(AppDef.Par_Tar(course.holes.get(4).playedScore, AppDef.isTar));
            ((TextView) scoreItemViewHolder.holeScoreLayout[4].findViewById(R.id.hole5_putt)).setText(course.holes.get(4).playedScore.putting);
            setBadge(((TextView) scoreItemViewHolder.holeScoreLayout[4].findViewById(R.id.hole5_hit)), course.holes.get(4));

            ((TextView) scoreItemViewHolder.holeScoreLayout[5].findViewById(R.id.hole6_hit)).setText(AppDef.Par_Tar(course.holes.get(5).playedScore, AppDef.isTar));
            ((TextView) scoreItemViewHolder.holeScoreLayout[5].findViewById(R.id.hole6_putt)).setText(course.holes.get(5).playedScore.putting);
            setBadge(((TextView) scoreItemViewHolder.holeScoreLayout[5].findViewById(R.id.hole6_hit)), course.holes.get(5));

            ((TextView) scoreItemViewHolder.holeScoreLayout[6].findViewById(R.id.hole7_hit)).setText(AppDef.Par_Tar(course.holes.get(6).playedScore, AppDef.isTar));
            ((TextView) scoreItemViewHolder.holeScoreLayout[6].findViewById(R.id.hole7_putt)).setText(course.holes.get(6).playedScore.putting);
            setBadge(((TextView) scoreItemViewHolder.holeScoreLayout[6].findViewById(R.id.hole7_hit)), course.holes.get(6));

            ((TextView) scoreItemViewHolder.holeScoreLayout[7].findViewById(R.id.hole8_hit)).setText(AppDef.Par_Tar(course.holes.get(7).playedScore, AppDef.isTar));
            ((TextView) scoreItemViewHolder.holeScoreLayout[7].findViewById(R.id.hole8_putt)).setText(course.holes.get(7).playedScore.putting);
            setBadge(((TextView) scoreItemViewHolder.holeScoreLayout[7].findViewById(R.id.hole8_hit)), course.holes.get(7));

            ((TextView) scoreItemViewHolder.holeScoreLayout[8].findViewById(R.id.hole9_hit)).setText(AppDef.Par_Tar(course.holes.get(8).playedScore, AppDef.isTar));
            ((TextView) scoreItemViewHolder.holeScoreLayout[8].findViewById(R.id.hole9_putt)).setText(course.holes.get(8).playedScore.putting);
            setBadge(((TextView) scoreItemViewHolder.holeScoreLayout[8].findViewById(R.id.hole9_hit)), course.holes.get(8));

            ((TextView) scoreItemViewHolder.courseTotal[0].findViewById(R.id.ll_course0_total).findViewById(R.id.course0_total_tar)).setText(Par_Tar_Total(course, AppDef.isTar));
            ((TextView) scoreItemViewHolder.courseTotal[0].findViewById(R.id.ll_course0_total).findViewById(R.id.course0_toatal_putt)).setText(Putt_Total(course));
            //코스토탈
            if(mTabIdx == 0 ) {
                Course theOtherCourse =  playerList.get(i).playingCourse.get(1);
                ((TextView) scoreItemViewHolder.courseTotal[1].findViewById(R.id.ll_course1_total).findViewById(R.id.course1_total_tar)).setText(Par_Tar_Total(theOtherCourse, AppDef.isTar));
            ((TextView) scoreItemViewHolder.courseTotal[1].findViewById(R.id.ll_course1_total).findViewById(R.id.course1_total_putt)).setText(Putt_Total(theOtherCourse));
            }else if(mTabIdx == 1){
                Course theOtherCourse =  playerList.get(i).playingCourse.get(0);
                ((TextView) scoreItemViewHolder.courseTotal[1].findViewById(R.id.ll_course1_total).findViewById(R.id.course1_total_tar)).setText(Par_Tar_Total(theOtherCourse, AppDef.isTar));
                ((TextView) scoreItemViewHolder.courseTotal[1].findViewById(R.id.ll_course1_total).findViewById(R.id.course1_total_putt)).setText(Putt_Total(theOtherCourse));
            }
            //전체토탈
           // playerList.get(i).totalRankingPutting을 totalPutt으로
            Score wholeTotalScore = new Score(playerList.get(i).totalPar, playerList.get(i).totalRankingPutting, playerList.get(i).totalTar);
            ((TextView) scoreItemViewHolder.wholeTotalLinear.findViewById(R.id.whole_total_tar)).setText(AppDef.Par_Tar(wholeTotalScore, AppDef.isTar));
            ((TextView) scoreItemViewHolder.wholeTotalLinear.findViewById(R.id.whole_total_put)).setText( playerList.get(i).totalRankingPutting);

        }

        @Override
        public int getItemCount() {
            return playerList.size();
        }
    }



    private void setBadge(TextView tv, Hole playedHole) {

        if (!Util.isInteger(playedHole.playedScore.tar) || "-".equals(playedHole.playedScore.tar)) {
            return;
        }

        switch (playedHole.par) {
            case "3":
                switch (Integer.parseInt(playedHole.playedScore.tar)) {
                    case 1:
                        tv.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.holeinone, null));
                        break;
                    case 2:
                        tv.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.birdie, null));
                        break;
                }
                break;
            case "4":
                switch (Integer.parseInt(playedHole.playedScore.tar)) {
                    case 1:
                        tv.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.holeinone, null));
                        break;
                    case 2:
                        tv.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.eagle, null));
                        break;
                    case 3:
                        tv.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.birdie, null));
                        break;
                }
                break;

            case "5":
                switch (Integer.parseInt(playedHole.playedScore.tar)) {
                    case 1:
                        tv.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.holeinone, null));
                        break;
                    case 2:
                        tv.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.alba, null));
                        break;
                    case 3:
                        tv.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.eagle, null));
                        break;
                    case 4:
                        tv.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.birdie, null));
                        break;

                }
                break;

            default:
        }

    }


    private String Par_Tar_Total(Course course, boolean istar) {
        int total_score = 0;
        if (istar) {
            for (int i = 0; course.holes.size() > i; i++) {
                if (Util.isIntegerNumber(course.holes.get(i).playedScore.tar))
                    total_score = total_score + Integer.parseInt(course.holes.get(i).playedScore.tar);
            }
        } else {
            for (int i = 0; course.holes.size() > i; i++) {
                if (Util.isIntegerNumber(course.holes.get(i).playedScore.par))
                    total_score = total_score + Integer.parseInt(course.holes.get(i).playedScore.par);
            }
        }
        //다시 스트링으로 변환해 리턴
        return String.valueOf(total_score);
    }

    private String Putt_Total(Course course) {
        int total_score = 0;

        for (int i = 0; course.holes.size() > i; i++) {
            if (Util.isIntegerNumber(course.holes.get(i).playedScore.putting))
                total_score = total_score + Integer.parseInt(course.holes.get(i).playedScore.putting);
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
                    totalDistance = totalDistance + Integer.parseInt(holes[i].hole_total_size);
            }
        }
        return totalDistance;
    }

}

