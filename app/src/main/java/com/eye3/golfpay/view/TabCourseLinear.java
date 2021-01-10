package com.eye3.golfpay.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.R;
import com.eye3.golfpay.common.AppDef;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.listener.ScoreInputFinishListener;
import com.eye3.golfpay.model.field.Course;
import com.eye3.golfpay.model.field.Hole;
import com.eye3.golfpay.model.score.Score;
import com.eye3.golfpay.model.teeup.Player;
import com.eye3.golfpay.util.ScoreDialog;
import com.eye3.golfpay.util.Util;
import com.google.zxing.client.result.TextParsedResult;

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

    TextView tvCourseName1;
    TextView tvCourseName2;
    TextView tvTotalPar1;
    TextView tvTotalPar2;
    TextView tvTotalMeters1;
    TextView tvTotalMeters2;
    RelativeLayout inOrOutLinearLayout;
    RelativeLayout inOrOutLinearLayout00;

    public TabCourseLinear(Context context) {
        super(context);

    }

    public static int getHoleScoreLayoutIdx() {
        return mHoleScoreLayoutIdx;
    }

    public static void setHoleScoreLayoutIdx(int Idx) {
        mHoleScoreLayoutIdx = Idx;
    }

    private boolean isPreviousHoleScoreFilledUp(List<Player> playerList, int mTabIdx) {

        if (mTabIdx == 0 && mHoleScoreLayoutIdx == 0)
            return true;

        int previousHoleScoreLayoutIdx = mHoleScoreLayoutIdx-1;
        if (playerList == null)
            return false;
        //최초 홀은 무조건 통과

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

    public void init(final Context context, final List<Player> playerList, int tabIdx, int nearest, int longest) {

        this.mContext = context;
        this.mTabIdx = tabIdx;
        this.mPlayerList = playerList;
        this.removeAllViewsInLayout();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.tab_course, this, false);
        mHolderLinear = v.findViewById(R.id.scoreColumn).findViewById(R.id.holderInfoLinear);
        mDistanceSpinner = v.findViewById(R.id.spinn_distance);
        arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.spinn_array)) ;
        tvCourseName1 = v.findViewById(R.id.tvCourseName1);
        tvCourseName2 = v.findViewById(R.id.tvCourseName2);
        tvTotalPar1 = v.findViewById(R.id.tvTotalPar1);
        tvTotalPar2 = v.findViewById(R.id.tvTotalPar2);

        tvTotalMeters1 = v.findViewById(R.id.tvTotalMeters1);
        tvTotalMeters2 = v.findViewById(R.id.tvTotalMeters2);

        inOrOutLinearLayout = v.findViewById(R.id.inOrOutLinearLayout);
        inOrOutLinearLayout00 = v.findViewById(R.id.inOrOutLinearLayout00);

        mDistanceSpinner.setAdapter(arrayAdapter);
        mDistanceSpinner.setSelection(0);
        //거리 환산 적용
        mDistanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((parent.getItemAtPosition(position)).toString().equals("yard")) {
                    Global.isYard = true;
                    mDistanceSpinner.setSelection(2);
                    createHoleInfoLinear(mContext, nearest, longest);
                } else if ((parent.getItemAtPosition(position)).toString().equals("meter")) {
                    Global.isYard = false;
                    mDistanceSpinner.setSelection(1);
                    createHoleInfoLinear(mContext, nearest, longest);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        createHoleInfoLinear(mContext, nearest, longest);
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

    private int getTotalPar(List<Hole> holes) {
        int totalPar = 0;
        for (int i = 0; i < holes.size(); i++) {
            if (holes.get(i).par != null && Util.isInteger(holes.get(i).par))
                totalPar = totalPar + Integer.parseInt(holes.get(i).par);
        }
        return totalPar;
    }

    private int getTotalMeter(List<Hole> holes) {
        int totalMeter = 0;
        for (int i = 0; i < holes.size(); i++) {
            if (holes.get(i).hole_total_size != null && Util.isInteger(holes.get(i).hole_total_size))
                totalMeter = totalMeter + Integer.parseInt(holes.get(i).hole_total_size);
        }
        return totalMeter;
    }

    /*
     *  최상단 홀정보 보여주는 뷰생성
     *
     */
    private void createHoleInfoLinear(Context context, int nearest_hole, int longest_hole) {
        mHolderLinear.removeAllViewsInLayout();

        List<Hole> holes = Global.courseInfoList.get(mTabIdx).holes;

        int totalPar = 0;
        int totalMeter = 0;
        for (int i = 0; i < holes.size(); i++) {
            holeInfoLinear[i] = new HoleInfoLinear(context, holes.get(i));
            holeInfoLinear[i].setCourse(Global.courseInfoList.get(mTabIdx));
            holeInfoLinear[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mHolderLinear.addView(holeInfoLinear[i]);

            if (i == longest_hole) {
                holeInfoLinear[i].findViewById(R.id.hole_longest).setVisibility(View.VISIBLE);
            } else if (i == nearest_hole) {
                holeInfoLinear[i].findViewById(R.id.hole_nearest).setVisibility(View.VISIBLE);
            }
        }

        //홀인포 전체를 나타내는 마지막 셀정보 입력
        Hole totalHole = new Hole();
        totalHole.par = String.valueOf(totalPar);
        totalHole.hole_total_size = String.valueOf(totalMeter);
        holeInfoLinear[holeInfoLinear.length - 1] = new HoleInfoLinear(context, totalHole);

        TextView tvCoursId = holeInfoLinear[holeInfoLinear.length - 1].findViewById(R.id.hole_no);
        tvCoursId.setVisibility(View.GONE);

        String cType1 = Global.courseInfoList.get(0).ctype;
        String cType2 = Global.courseInfoList.get(1).ctype;
        tvCourseName1.setText(cType1);
        tvCourseName2.setText(cType2);

        tvTotalPar1.setText(String.valueOf(getTotalPar(Global.courseInfoList.get(0).holes)));
        tvTotalPar2.setText(String.valueOf(getTotalPar(Global.courseInfoList.get(1).holes)));
        tvTotalMeters1.setText(String.valueOf(getTotalMeter(Global.courseInfoList.get(0).holes)));
        tvTotalMeters2.setText(String.valueOf(getTotalMeter(Global.courseInfoList.get(1).holes)));
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
            RelativeLayout[] holeScoreLayout = new RelativeLayout[9];
            RelativeLayout[] courseTotal = new RelativeLayout[2]; //동적생성전 임시처리
            RelativeLayout wholeTotalLinear;
            LinearLayout ll_score_row;
            int[] holeIds = { R.id.hole1_ll,R.id.hole2_ll,R.id.hole3_ll,R.id.hole4_ll,R.id.hole5_ll,R.id.hole6_ll,R.id.hole7_ll,R.id.hole8_ll,R.id.hole9_ll};
            ScoreItemViewHolder(View view) {
                super(view);
                ll_score_row = view.findViewById(R.id.ll_score_row);
                tvRank = view.findViewById(R.id.rank);
                tvName = view.findViewById(R.id.name);

                final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, LayoutParams.MATCH_PARENT, getResources().getDisplayMetrics());
                final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 112, getResources().getDisplayMetrics());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                ll_score_row.setLayoutParams(params);

                for (int i = 0; i < 9; i++) {
                    holeScoreLayout[i] = view.findViewById(holeIds[i]);
                }

                courseTotal[0] = view.findViewById(R.id.ll_course0_total);
                courseTotal[1] = view.findViewById(R.id.ll_course1_total);
                wholeTotalLinear = view.findViewById(R.id.whole_total_linear);

                for (int i = 0; NUM_OF_HOLES > i; i++) {
                    holeScoreLayout[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            mHoleScoreLayoutIdx = getFindHoleId(v.getId());
                            Global.viewPagerPosition = mHoleScoreLayoutIdx;
                            if (isPreviousHoleScoreFilledUp(playerList, mTabIdx)) {
                                notifyDataSetChanged();
                                sDialog = new ScoreDialog(mContext, "저장", "취소", null, null, playerList, mTabIdx, mHoleScoreLayoutIdx);
                                WindowManager.LayoutParams wmlp = sDialog.getWindow().getAttributes();
                                wmlp.gravity = Gravity.CENTER_VERTICAL;

                                sDialog.getWindow().setAttributes(wmlp);
                                sDialog.setOnScoreInputFinishListener(listener);
                                sDialog.getWindow().
                                        setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

                                sDialog.getWindow().getDecorView().setSystemUiVisibility(Util.DlgUIFalg);
                                sDialog.show();
                            } else {
                                Toast.makeText(mContext, "이전 플레이어 점수를 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            private int getFindHoleId(int id) {
                for (int i = 0; i < holeIds.length; i++) {
                    if (holeIds[i] == id)
                        return i;
                }

                return -1;
            }

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
        public void onBindViewHolder(@NonNull ScoreAdapter.ScoreItemViewHolder scoreItemViewHolder, int index) {
            Course course = playerList.get(index).playingCourse.get(mTabIdx);

            if (playerList.get(index).Ranking.equals("1")) {
                scoreItemViewHolder.tvRank.setTextColor(Color.parseColor("#00abc5"));
                scoreItemViewHolder.tvName.setTextColor(Color.parseColor("#00abc5"));
            }
            scoreItemViewHolder.tvRank.setText(playerList.get(index).Ranking);
            scoreItemViewHolder.tvName.setText(playerList.get(index).name);

            for (int i = 0; scoreItemViewHolder.holeScoreLayout.length > i; i++) {
                if (i == mHoleScoreLayoutIdx) {

                    if (index % 2 == 0) {
                        scoreItemViewHolder.holeScoreLayout[i].setBackgroundColor(Color.parseColor("#d7dcde"));
                        //scoreItemViewHolder.itemView.setBackgroundColor(Color.parseColor("#EBEFF1"));
                    } else {
                        scoreItemViewHolder.holeScoreLayout[i].setBackgroundColor(Color.parseColor("#e0e3e4"));
                        //scoreItemViewHolder.itemView.setBackgroundColor(Color.parseColor("#F5F7F8"));
                    }

                    holeInfoLinear[i].setBackgroundColor(0xff00abc5);
                    holeInfoLinear[i].tvHoleNo.setTextColor(0xffffffff);
                    holeInfoLinear[i].tvPar.setTextColor(0xfff5f7f8);
                    holeInfoLinear[i].tvMeter.setTextColor(0xfff5f7f8);
                } else {
                    scoreItemViewHolder.holeScoreLayout[i].setBackgroundColor(Color.WHITE);
                    holeInfoLinear[i].setBackgroundColor(0xffffffff);
                    holeInfoLinear[i].tvHoleNo.setTextColor(0xff999999);
                    holeInfoLinear[i].tvPar.setTextColor(0xff999999);
                    holeInfoLinear[i].tvMeter.setTextColor(0xff999999);

                    if (index % 2 == 0) {
                        scoreItemViewHolder.holeScoreLayout[i].setBackgroundColor(Color.parseColor("#EBEFF1"));
                        //scoreItemViewHolder.itemView.setBackgroundColor(Color.parseColor("#EBEFF1"));
                    } else {
                        scoreItemViewHolder.holeScoreLayout[i].setBackgroundColor(Color.parseColor("#F5F7F8"));
                        //scoreItemViewHolder.itemView.setBackgroundColor(Color.parseColor("#F5F7F8"));
                    }
                }
            }

            int[] tvIds = {R.id.hole1_hit, R.id.hole2_hit, R.id.hole3_hit,R.id.hole4_hit, R.id.hole5_hit ,R.id.hole6_hit ,R.id.hole7_hit ,R.id.hole8_hit ,R.id.hole9_hit};
            int[] tvHoleIds = {R.id.hole1_putt, R.id.hole2_putt, R.id.hole3_putt,R.id.hole4_putt, R.id.hole5_putt ,R.id.hole6_putt ,R.id.hole7_putt ,R.id.hole8_putt ,R.id.hole9_putt};
            int[] ivBadgeIds = {R.id.iv_badge1, R.id.iv_badge2, R.id.iv_badge3,R.id.iv_badge4, R.id.iv_badge5 ,R.id.iv_badge6 ,R.id.iv_badge7 ,R.id.iv_badge8 ,R.id.iv_badge9};

            for (int j = 0; j < 9; j++) {
                TextView tvScore = scoreItemViewHolder.holeScoreLayout[j].findViewById(tvIds[j]);
                TextView tvPutt = scoreItemViewHolder.holeScoreLayout[j].findViewById(tvHoleIds[j]);
                tvScore.setText(AppDef.Par_Tar(course.holes.get(j).playedScore, AppDef.isTar));
                tvPutt.setText(getScorePuttAndTesShot(course.holes.get(j).playedScore));

                setBadge(((ImageView) scoreItemViewHolder.holeScoreLayout[j].findViewById(ivBadgeIds[j])), course.holes.get(j), tvScore);
                setTextStyle(tvScore);
            }


            if (index % 2 == 0) {
                scoreItemViewHolder.courseTotal[0].setBackgroundColor(Color.parseColor("#EBEFF1"));
                scoreItemViewHolder.courseTotal[1].setBackgroundColor(Color.parseColor("#EBEFF1"));
            } else {
                scoreItemViewHolder.courseTotal[0].setBackgroundColor(Color.parseColor("#F5F7F8"));
                scoreItemViewHolder.courseTotal[1].setBackgroundColor(Color.parseColor("#F5F7F8"));
            }

            //코스토탈
            if (mTabIdx == 0) {
                TextView tvCourseTotalTar = scoreItemViewHolder.courseTotal[0].findViewById(R.id.course0_total_tar);
                TextView tvCourseTotalPutt = scoreItemViewHolder.courseTotal[0].findViewById(R.id.course0_total_putt);
                tvCourseTotalTar.setText(Par_Tar_Total(course, AppDef.isTar));
                tvCourseTotalPutt.setText(Putt_Total(course));

                Course theOtherCourse = playerList.get(index).playingCourse.get(1);
                TextView tvCourseTotalTar2 = scoreItemViewHolder.courseTotal[1].findViewById(R.id.course1_total_tar);
                TextView tvCourseTotalPutt2 = scoreItemViewHolder.courseTotal[1].findViewById(R.id.course1_total_putt);
                tvCourseTotalTar2.setText(Par_Tar_Total(theOtherCourse, AppDef.isTar));
                tvCourseTotalPutt2.setText(Putt_Total(theOtherCourse));
            } else if (mTabIdx == 1) {
                TextView tvCourseTotalTar = scoreItemViewHolder.courseTotal[1].findViewById(R.id.course1_total_tar);
                TextView tvCourseTotalPutt = scoreItemViewHolder.courseTotal[1].findViewById(R.id.course1_total_putt);
                tvCourseTotalTar.setText(Par_Tar_Total(course, AppDef.isTar));
                tvCourseTotalPutt.setText(Putt_Total(course));

                Course theOtherCourse = playerList.get(index).playingCourse.get(0);
                TextView tvCourseTotalTar2 = scoreItemViewHolder.courseTotal[0].findViewById(R.id.course0_total_tar);
                TextView tvCourseTotalPutt2 = scoreItemViewHolder.courseTotal[0].findViewById(R.id.course0_total_putt);
                tvCourseTotalTar2.setText(Par_Tar_Total(theOtherCourse, AppDef.isTar));
                tvCourseTotalPutt2.setText(Putt_Total(theOtherCourse));
            }



            //전체토탈
            // playerList.get(i).totalRankingPutting을 totalPutt으로
            Score wholeTotalScore = new Score(playerList.get(index).totalPar, playerList.get(index).totalRankingPutting, playerList.get(index).totalTar, "teeShot");
            ((TextView) scoreItemViewHolder.wholeTotalLinear.findViewById(R.id.whole_total_tar)).setText(AppDef.Par_Tar(wholeTotalScore, AppDef.isTar));
            ((TextView) scoreItemViewHolder.wholeTotalLinear.findViewById(R.id.whole_total_put)).setText(playerList.get(index).totalRankingPutting);
        }

        private void setTextStyle(TextView tvScore) {
            if (tvScore.getText().equals("-")) {
                tvScore.setTextAppearance(R.style.GlobalTextView_40SP_Gray_NotoSans_Regular);
            }
        }

        @Override
        public int getItemCount() {
            return playerList.size();
        }
    }

    private String getScorePuttAndTesShot(Score score) {
        if (score.teeShot.isEmpty())
            return score.putting;

        return score.putting + "/" + score.teeShot.substring(0, 1).toUpperCase();
    }

    private void setBadge(ImageView iv, Hole playedHole, TextView tvScore) {

        if (!Util.isInteger(playedHole.playedScore.tar) || "-".equals(playedHole.playedScore.tar)) {
            return;
        }

        switch (playedHole.par) {
            case "3":
                switch (Integer.parseInt(playedHole.playedScore.tar)) {
                    case 1:
                        iv.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.holeinone, null));
                        tvScore.setTextAppearance(R.style.GlobalTextView_40SP_Gray_NotoSans_Regular);
                        break;
                    case 2:
                        iv.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.birdie, null));
                        tvScore.setTextAppearance(R.style.GlobalTextView_40SP_Gray_NotoSans_Regular);
                        break;
                }
                break;
            case "4":
                switch (Integer.parseInt(playedHole.playedScore.tar)) {
                    case 1:
                        iv.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.holeinone, null));
                        tvScore.setTextAppearance(R.style.GlobalTextView_40SP_Gray_NotoSans_Regular);
                        break;
                    case 2:
                        iv.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.eagle, null));
                        tvScore.setTextAppearance(R.style.GlobalTextView_40SP_Gray_NotoSans_Regular);
                        break;
                    case 3:
                        iv.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.birdie, null));
                        tvScore.setTextAppearance(R.style.GlobalTextView_40SP_Gray_NotoSans_Regular);
                        break;
                }
                break;

            case "5":
                switch (Integer.parseInt(playedHole.playedScore.tar)) {
                    case 1:
                        iv.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.holeinone, null));
                        tvScore.setTextAppearance(R.style.GlobalTextView_40SP_Gray_NotoSans_Regular);
                        break;
                    case 2:
                        iv.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.alba, null));
                        tvScore.setTextAppearance(R.style.GlobalTextView_40SP_Gray_NotoSans_Regular);
                        break;
                    case 3:
                        iv.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.eagle, null));
                        tvScore.setTextAppearance(R.style.GlobalTextView_40SP_Gray_NotoSans_Regular);
                        break;
                    case 4:
                        iv.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.birdie, null));
                        tvScore.setTextAppearance(R.style.GlobalTextView_40SP_Gray_NotoSans_Regular);
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

        return String.valueOf(total_score);
    }

    private String Putt_Total(Course course) {
        int total_score = 0;

        for (int i = 0; course.holes.size() > i; i++) {
            if (Util.isIntegerNumber(course.holes.get(i).playedScore.putting))
                total_score = total_score + Integer.parseInt(course.holes.get(i).playedScore.putting);
        }

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