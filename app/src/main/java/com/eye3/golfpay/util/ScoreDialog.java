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
import com.eye3.golfpay.model.score.ScoreSend;
import com.eye3.golfpay.model.teeup.Player;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;
import com.eye3.golfpay.view.ScoreInserter;

import java.util.ArrayList;
import java.util.List;


public class ScoreDialog extends Dialog {

    private Button mLeftButton;
    private Button mRightButton;
    private final String mLeftTitle;
    private final String mRightTitle;

    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;
    private ScoreInserter score;
    private ScoreInserter putt;
    private ScoreInserter teeShot;

    RecyclerView recycler;
    List<Player> mPlayerList;
    Course mCurrentCourseInfo;
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
        mReserveScore = new ReserveScore(mPlayerList, Global.reserveId, mCurrentCourseInfo.holes.get(mHoleScoreLayoutIdx).id, mTabIdx, mHoleScoreLayoutIdx);
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

        score.makeScoreBoard(mReserveScore.guest_score_list);
        putt.makeScoreBoard(mReserveScore.guest_score_list);
        teeShot.makeScoreBoard(mReserveScore.guest_score_list);

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
    }

    public void setOnScoreInputFinishListener(ScoreInputFinishListener listener) {
        this.inputFinishListener = listener;
    }

    private void sendPlayersScores(final Context mContext, ReserveScore reserveScore) {


        for (int i = 0; i < reserveScore.guest_score_list.size(); i++) {
            reserveScore.guest_score_list.get(i).tar="-";
        }

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

