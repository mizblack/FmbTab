package com.eye3.golfpay.fmb_tab.util;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.AppDef;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.listener.ScoreInputFinishListener;
import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.eye3.golfpay.fmb_tab.model.field.Hole;
import com.eye3.golfpay.fmb_tab.model.score.ReserveScore;
import com.eye3.golfpay.fmb_tab.model.score.Score;
import com.eye3.golfpay.fmb_tab.model.teeup.Player;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.net.ResponseData;
import com.eye3.golfpay.fmb_tab.view.ScoreInserter;

import java.util.List;


public class ScoreDialog extends Dialog {


    private Button mLeftButton;
    private Button mRightButton;
    private String mLeftTitle;
    private String mRightTitle;
    private ScoreDialog dialog = this;

    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;

    RecyclerView recycler;
    List<Player> mPlayerList;
    Course mCurrentCourseInfo;
    ScoreInputAdapter mScoreInputAdapter;
    Context mContext;
    //선정된 코스텝 인덱스 반드시 있어야함.
    int mTabIdx;
    //hole_no와 동일 zero base
    int mHoleScoreLayoutIdx;

    TextView tvHoleId;
    TextView tvPar;
    TextView tvCourseName;
    //서버로 스코어등록을 하기위한 객체
    ReserveScore mReserveScore;
    ScoreInputFinishListener inputFinishListener;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.score_dlg);
       //코스정보만있고 스코어 정보는 없음
        mCurrentCourseInfo = Global.courseInfoList.get(mTabIdx);

        mLeftButton = findViewById(R.id.btnLeft);
        mRightButton = findViewById(R.id.btnRight);

        tvHoleId = findViewById(R.id.hole_id);
        tvHoleId.setText("Hole" + mCurrentCourseInfo.holes.get(mHoleScoreLayoutIdx).hole_no);
        tvPar = findViewById(R.id.par_num);
        tvPar.setText("Par" + mCurrentCourseInfo.holes.get(mHoleScoreLayoutIdx).par);
        tvCourseName = findViewById(R.id.dlg_course_name);
        tvCourseName.setText("    Course(" + mCurrentCourseInfo.courseName + ")");

        // 클릭 이벤트 셋팅
        if (mLeftClickListener != null && mRightClickListener != null) {
            mLeftButton.setOnClickListener(mLeftClickListener);
            mLeftButton.setText(mLeftTitle);
            mRightButton.setOnClickListener(mRightClickListener);
            mRightButton.setText(mRightTitle);
        } else {
            mLeftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendPlayersScores(mContext, mReserveScore);
                }
            });
            mLeftButton.setText(mLeftTitle);

            mRightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            mRightButton.setText(mRightTitle);

        }

        recycler = findViewById(R.id.player_score_list);
        recycler.setHasFixedSize(true);
        LinearLayoutManager mManager = new LinearLayoutManager(mContext);
        recycler.setLayoutManager(mManager);
        mScoreInputAdapter = new ScoreInputAdapter(mContext, mPlayerList, mTabIdx, mHoleScoreLayoutIdx); //?
        recycler.setAdapter(mScoreInputAdapter);
        mScoreInputAdapter.notifyDataSetChanged();

        mReserveScore = new ReserveScore(mPlayerList,  Global.reserveId, mCurrentCourseInfo.holes.get(mHoleScoreLayoutIdx).id, mTabIdx, mHoleScoreLayoutIdx);

    }

    public void setOnScoreInputFinishListener(ScoreInputFinishListener listener) {
        this.inputFinishListener = listener;
    }


    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public ScoreDialog(Context context, String leftBtnTitle, String rightBtnTitle,
                       View.OnClickListener leftListener,
                       View.OnClickListener rightListener, List<Player> mPlayerList, int mTabIdx, int mHoleScoreLayoutIdx) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mContext = context;
        this.mHoleScoreLayoutIdx = mHoleScoreLayoutIdx;
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
        this.mLeftTitle = leftBtnTitle;
        this.mRightTitle = rightBtnTitle;

        this.mPlayerList = mPlayerList;
        this.mHoleScoreLayoutIdx = mHoleScoreLayoutIdx;
        this.mTabIdx = mTabIdx;
    }



    private class ScoreInputAdapter extends RecyclerView.Adapter<ScoreInputAdapter.ScoreInputItemViewHolder> {
        List<Player> mPlayerList;
       int mTabIdx;
       int mHoleLayoutIdx ;

        public ScoreInputAdapter(Context context, List<Player> playerList, int mTabIdx, int mHoleLayoutIdx) {

            this.mPlayerList = playerList;
            this.mTabIdx = mTabIdx;
            this.mHoleLayoutIdx = mHoleLayoutIdx;


        }

        @NonNull
        @Override
        //recyclerview가 parent임
        public ScoreInputAdapter.ScoreInputItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.score_input_row, parent, false);
            ScoreInputAdapter.ScoreInputItemViewHolder viewHolder = new ScoreInputAdapter.ScoreInputItemViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final ScoreInputAdapter.ScoreInputItemViewHolder holder, int position) {
            final int playerIdx = position;
            //*******
            holder.playerName.setText(mPlayerList.get(position).name);
            Score a_playerScore = mPlayerList.get(position).playingCourse.get(mTabIdx).holes.get(mHoleScoreLayoutIdx).playedScore;
            final Hole selected_hole =  mPlayerList.get(position).playingCourse.get(mTabIdx).holes.get(mHoleScoreLayoutIdx);

            //각홀 점수를 통해 inserter 배경을 지정하여 초기화한다.
            holder.inserter.initScoreBackgroundSelected(AppDef.Par_Tar(a_playerScore, AppDef.isTar), AppDef.isTar);
            holder.inserterPutt.initPuttScoreBackgroundSelected(a_playerScore.putting);
            if (AppDef.isTar) {
                final TextView[] viewArr = holder.inserter.mStrokeScoreTextViewArr;
                for (int i = 0; viewArr.length > i; i++) {
                    viewArr[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ScoreInserter.initAllBackGroundResources(viewArr);

                            if (!(boolean) v.getTag(ScoreInserter.NUM_STROKE_SELLECTED_PREVIOUS_KEY)) {
                                v.getBackground().setColorFilter(Color.parseColor("#00AEC9"), PorterDuff.Mode.SRC);
                                ((TextView)v).setTextColor(Color.WHITE);
                                setPar(mReserveScore.guest_score_list.get(playerIdx), ((TextView) v).getText().toString().trim());
                                setTar(mReserveScore.guest_score_list.get(playerIdx), String.valueOf(Integer.valueOf(((TextView) v).getText().toString().trim()) + Integer.valueOf(selected_hole.par)));
                                v.setTag(ScoreInserter.NUM_STROKE_SELLECTED_PREVIOUS_KEY, false);
                            } else {
                                ((TextView)v).setTextColor(Color.GRAY);
                                setPar(mReserveScore.guest_score_list.get(playerIdx), "-");
                                setTar(mReserveScore.guest_score_list.get(playerIdx), "-");
                            }

                        }
                    });
                }
                int selectedIdx = holder.inserter.mSelectedStrokeScoreTvIdx;

                holder.inserter.setBackGroundColorSelected(viewArr, AppDef.Par_Tar(a_playerScore, AppDef.isTar), selectedIdx);

            } else {
                final TextView[] viewArr = holder.inserter.mParScoreTextViewArr;
                for (int i = 0; viewArr.length > i; i++) {
                    viewArr[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ScoreInserter.initAllBackGroundResources(viewArr);

                            if (!(boolean) v.getTag(ScoreInserter.NUM_PAR_SELLECTED_PREVIOUS_KEY)) {
                                v.getBackground().setColorFilter(Color.parseColor("#00AEC9"), PorterDuff.Mode.SRC);
                                ((TextView)v).setTextColor(Color.WHITE);
                                setPar(mReserveScore.guest_score_list.get(playerIdx), ((TextView) v).getText().toString().trim());
                                setTar(mReserveScore.guest_score_list.get(playerIdx), String.valueOf(Integer.valueOf(((TextView) v).getText().toString().trim()) + Integer.valueOf(selected_hole.par)));
                                v.setTag(ScoreInserter.NUM_PAR_SELLECTED_PREVIOUS_KEY, true);
                            } else { //같은 스코어블럭을 눌럿을때
                                ((TextView)v).setTextColor(Color.GRAY);
                                setPar(mReserveScore.guest_score_list.get(playerIdx), "-");
                                setTar(mReserveScore.guest_score_list.get(playerIdx), "-");
                                v.setTag(ScoreInserter.NUM_PAR_SELLECTED_PREVIOUS_KEY, false);
                            }
                        }

                    });
                }
                int selectedIdx = holder.inserter.mSelectedParScoreTvIdx;
                holder.inserter.setBackGroundColorSelected(viewArr, AppDef.Par_Tar(a_playerScore, AppDef.isTar), selectedIdx);

            }

            holder.inserterPutt.setBackGroundColorSelected(holder.inserterPutt.mPuttScoreTextViewArr,a_playerScore.putting, holder.inserterPutt.mSelectedPuttScoreTvIdx);
            for (int i = 0; holder.inserterPutt.mPuttScoreTextViewArr.length > i; i++) {
                holder.inserterPutt.mPuttScoreTextViewArr[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ScoreInserter.initAllBackGroundResources(holder.inserterPutt.mPuttScoreTextViewArr);
                        mReserveScore.guest_score_list.get(playerIdx).putting = ((TextView) v).getText().toString().trim();
                        if (!(boolean) v.getTag(ScoreInserter.NUM_PUTT_SELLECTED_PREVIOUS_KEY)) {
                            v.getBackground().setColorFilter(Color.parseColor("#00AEC9"), PorterDuff.Mode.SRC);
                            ((TextView)v).setTextColor(Color.WHITE);
                            mReserveScore.guest_score_list.get(playerIdx).putting = ((TextView) v).getText().toString().trim();
                            v.setTag(ScoreInserter.NUM_PUTT_SELLECTED_PREVIOUS_KEY, true);
                        } else {
                            ((TextView)v).setTextColor(Color.GRAY);
                            mReserveScore.guest_score_list.get(playerIdx).putting = "-";
                            v.setTag(ScoreInserter.NUM_PUTT_SELLECTED_PREVIOUS_KEY, false);
                        }


                        // ScoreInserter.initAllBackGroundResources(holder.inserterPutt.mPuttScoreTextViewArr);
                        // v.getBackground().setColorFilter(Color.parseColor("#00AEC9"), PorterDuff.Mode.SRC);
                    }
                });
            }
            mReserveScore.guest_score_list.get(playerIdx).putting = holder.inserterPutt.mSelectedPuttInserterTv.getText().toString().trim();
        }

        private void setTar(Score score, String scoreStr) {
            //점수 취소를 위한 -추가
            if (scoreStr.equals("-")) {
                score.tar = scoreStr;
                return;
            }
            if (scoreStr == null || !Util.isIntegerNumber(scoreStr)) {
                Toast.makeText(mContext, "정수가 아닙니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            score.tar = scoreStr;
        }

        private void setPar(Score score, String scoreStr) {
            if (scoreStr.equals("-")) {
                score.par = scoreStr;
                return;
            }
            if (scoreStr == null || !Util.isIntegerNumber(scoreStr)) {
                Toast.makeText(mContext, "정수가 아닙니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            score.par = scoreStr;
        }

        private void setPutting(Score score, String scoreStr) {
            if (scoreStr.equals("-")) {
                score.putting = scoreStr;
                return;
            }
            if (scoreStr == null || !Util.isIntegerNumber(scoreStr)) {
                Toast.makeText(mContext, "정수가 아닙니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            score.putting = scoreStr;
        }


        @Override
        public int getItemCount() {
            return mPlayerList.size();
        }

        public class ScoreInputItemViewHolder extends RecyclerView.ViewHolder {
            TextView playerName;
            ScoreInserter inserter;
            ScoreInserter inserterPutt;

            //onCreate의 view임(score_input_row)
            public ScoreInputItemViewHolder(@NonNull final View itemView) {
                super(itemView);
                playerName = itemView.findViewById(R.id.playerName);
                if (AppDef.isTar) {
                    inserter = itemView.findViewById(R.id.inserter);
                    inserter.init(mContext, AppDef.ScoreType.Tar);

                } else { //if Par
                    inserter = itemView.findViewById(R.id.inserter);
                    inserter.init(mContext, AppDef.ScoreType.Par);

                }

                inserterPutt = itemView.findViewById(R.id.inserter_putt);
                inserterPutt.init(mContext, AppDef.ScoreType.Putt);
            }
        }
    }

    private void sendPlayersScores(final Context mContext, ReserveScore reserveScore) {

        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).setScore(mContext, reserveScore, new DataInterface.ResponseCallback<ResponseData<Object>>() {
            @Override
            public void onSuccess(ResponseData<Object> response) {
                if ("ok".equals(response.getResultCode())) {
                    Toast.makeText(mContext, response.getResultMessage(), Toast.LENGTH_SHORT).show();
                    mReserveScore = null; //다음 입력을 위해 clear한다.
                    inputFinishListener.OnScoreInputFinished(mPlayerList);
                    dismiss();
                } else if ("fail".equals(response.getResultCode())) {
                    Toast.makeText(mContext, response.getResultMessage(), Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }

            @Override
            public void onError(ResponseData<Object> response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }

        });

    }


}

