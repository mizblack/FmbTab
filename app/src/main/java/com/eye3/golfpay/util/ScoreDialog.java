package com.eye3.golfpay.util;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
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

import com.eye3.golfpay.R;
import com.eye3.golfpay.adapter.ScoreAdapter;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.listener.ScoreInputFinishListener;
import com.eye3.golfpay.model.field.Course;
import com.eye3.golfpay.model.score.ReserveScore;
import com.eye3.golfpay.model.teeup.Player;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;
import com.eye3.golfpay.view.ScoreInserter;

import java.util.List;


public class ScoreDialog extends Dialog {

    private Button mLeftButton;
    private Button mRightButton;
    private String mLeftTitle;
    private String mRightTitle;
    private ScoreDialog dialog = this;

    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;
    private ScoreInserter score;
    private ScoreInserter putt;
    private ScoreInserter teeShot;

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
        this.mTabIdx = mTabIdx;
    }

    public ScoreDialog(Context context, String leftBtnTitle, String rightBtnTitle) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mContext = context;

        this.mLeftTitle = leftBtnTitle;
        this.mRightTitle = rightBtnTitle;
    }

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
        }
        mRightButton.setText(mRightTitle);


        findViewById(R.id.view_disalbe).setVisibility(View.GONE);
        findViewById(R.id.view_disalbe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        score = findViewById(R.id.scoreInserter);
        putt = findViewById(R.id.putt);
        teeShot = findViewById(R.id.teeshot);

        score.setIScoreInserterListenr(new ScoreInserter.IScoreInserterListenr() {
            @Override
            public void onClickedScore(int row, int cal) {
                mReserveScore.guest_score_list.get(row).par = score.getScore(cal).toString();
            }
        });

        putt.setIScoreInserterListenr(new ScoreInserter.IScoreInserterListenr() {
            @Override
            public void onClickedScore(int row, int cal) {
                mReserveScore.guest_score_list.get(row).putting = score.getScore(cal).toString();
            }
        });

        teeShot.setIScoreInserterListenr(new ScoreInserter.IScoreInserterListenr() {
            @Override
            public void onClickedScore(int row, int cal) {
                mReserveScore.guest_score_list.get(row).teeShot = score.getTeeShot(cal);
            }
        });

        mReserveScore = new ReserveScore(mPlayerList, Global.reserveId, mCurrentCourseInfo.holes.get(mHoleScoreLayoutIdx).id, mTabIdx, mHoleScoreLayoutIdx);
    }

    public void setOnScoreInputFinishListener(ScoreInputFinishListener listener) {
        this.inputFinishListener = listener;
    }

    private class ScoreInputAdapter extends RecyclerView.Adapter<ScoreInputAdapter.ScoreInputItemViewHolder> {

        class ScoreInputItemViewHolder extends RecyclerView.ViewHolder {
            TextView playerName;
            RecyclerView rv_score;
            RecyclerView rv_putt;

            public ScoreInputItemViewHolder(@NonNull final View itemView) {
                super(itemView);
                playerName = itemView.findViewById(R.id.playerName);
                rv_score = itemView.findViewById(R.id.rv_score);
                rv_putt = itemView.findViewById(R.id.rv_putt);
            }
        }

        List<Player> mPlayerList;
        int mTabIdx;
        int mHoleLayoutIdx;

        public ScoreInputAdapter(Context context, List<Player> playerList, int mTabIdx, int mHoleLayoutIdx) {

            this.mPlayerList = playerList;
            this.mTabIdx = mTabIdx;
            this.mHoleLayoutIdx = mHoleLayoutIdx;
        }

        @NonNull
        @Override
        //recyclerview가 parent임
        public ScoreInputItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.score_input_row, parent, false);
            ScoreInputAdapter.ScoreInputItemViewHolder viewHolder = new ScoreInputAdapter.ScoreInputItemViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final ScoreInputItemViewHolder holder, int position) {

            if (holder.rv_score.getAdapter() == null) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                holder.rv_score.setHasFixedSize(true);
                holder.rv_score.setLayoutManager(layoutManager);

                ScoreAdapter adapter = new ScoreAdapter(getContext(), new ScoreAdapter.IOnClickAdapter() {
                    @Override
                    public void onAdapterItemClicked(Integer index, Integer value) {
                        mReserveScore.guest_score_list.get(position).par = value.toString();
                        mReserveScore.guest_score_list.get(position).tar = index.toString();
                    }
                });

                holder.playerName.setText(mPlayerList.get(position).name);

                int score = -3;
                for (int i = 0; i < 18; i++) {
                    adapter.addItem(score++ + "");
                }

                try {
                    adapter.select(mPlayerList.get(position).playingCourse.get(mTabIdx).holes.get(mHoleScoreLayoutIdx).playedScore.par);
                } catch (Exception e) {

                }

                holder.rv_score.setAdapter(adapter);
            }

            if (holder.rv_putt.getAdapter() == null) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                holder.rv_putt.setHasFixedSize(true);
                holder.rv_putt.setLayoutManager(layoutManager);
                ScoreAdapter adapter = new ScoreAdapter(getContext(), new ScoreAdapter.IOnClickAdapter() {
                    @Override
                    public void onAdapterItemClicked(Integer index, Integer value) {
                        mReserveScore.guest_score_list.get(position).putting = value.toString();
                        mReserveScore.guest_score_list.get(position).tar = index.toString();
                    }
                });

                for (int i = 0; i < 15; i++) {
                    adapter.addItem(i++ + "");
                }

                adapter.select(mPlayerList.get(position).playingCourse.get(mTabIdx).holes.get(mHoleScoreLayoutIdx).playedScore.putting);
                holder.rv_putt.setAdapter(adapter);
            }
        }


        @Override
        public int getItemCount() {
            return mPlayerList.size();
        }
    }

    private void sendPlayersScores(final Context mContext, ReserveScore reserveScore) {

        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).setScore(mContext, reserveScore, new DataInterface.ResponseCallback<ResponseData<Object>>() {
            @Override
            public void onSuccess(ResponseData<Object> response) {
                if ("ok".equals(response.getResultCode())) {
                    //    Toast.makeText(mContext, response.getResultMessage(), Toast.LENGTH_SHORT).show();
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

