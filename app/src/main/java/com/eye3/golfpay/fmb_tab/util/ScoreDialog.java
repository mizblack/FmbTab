package com.eye3.golfpay.fmb_tab.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
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

import java.util.ArrayList;


public class ScoreDialog extends Dialog {


    private Button mLeftButton;
    private Button mRightButton;
    private LinearLayout mLayoutButtons;
    private String mLeftTitle;
    private String mRightTitle;
    private ScoreDialog dialog = this;

    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;

    RecyclerView recycler;
    ArrayList<Player> mPlayerList;
    Course mCurrentCourse;
    ScoreInputAdapter mScoreInputAdapter;
    Context mContext;
    //선정된 코스텝 인덱스
    int mTabIdx;
    //hole_no와 동일 zero base
    int mHoleScoreLayoutIdx;

    TextView tvHoleId;
    TextView tvPar;
    TextView tvCourseName;
    //서버로 스코어등록을 하기위한 객체
    ReserveScore mReserveScore;
    ScoreInputFinishListener inputFinishListener;

    View cancelLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.score_dlg);

        cancelLinearLayout = findViewById(R.id.cancelLinearLayout);
        cancelLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

//        mTitleView = findViewById(R.id.dlg_title);
        //       mContentView = findViewById(R.id.dlg_msg);

        mLeftButton = findViewById(R.id.btnLeft);
        mRightButton = findViewById(R.id.btnRight);
        mLayoutButtons = findViewById(R.id.layoutButtons);

        tvHoleId = findViewById(R.id.hole_id);
        tvHoleId.setText("Hole ID-" + mCurrentCourse.holes[mHoleScoreLayoutIdx].id);
        tvPar = findViewById(R.id.par_num);
        tvPar.setText("Par-" + mCurrentCourse.holes[mHoleScoreLayoutIdx].par);
        tvCourseName = findViewById(R.id.course_name);
        tvCourseName.setText("Course(" + mCurrentCourse.courseName + ")");

        // 클릭 이벤트 셋팅
        if (mLeftClickListener != null && mRightClickListener != null) {

            mLayoutButtons.setVisibility(View.VISIBLE);
            mLeftButton.setOnClickListener(mLeftClickListener);
            mLeftButton.setText(mLeftTitle);
            mRightButton.setOnClickListener(mRightClickListener);
            mRightButton.setText(mRightTitle);
        } else {
            mLayoutButtons.setVisibility(View.VISIBLE);
            mLeftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            mLeftButton.setText(mLeftTitle);

            mRightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //  inputFinishListener.OnScoreInputFinished(mPlayerList);
                    sendPlayersScores(mContext, mReserveScore);

                }
            });
            mRightButton.setText(mRightTitle);

        }

        recycler = findViewById(R.id.player_score_list);
        recycler.setHasFixedSize(true);
        LinearLayoutManager mManager = new LinearLayoutManager(mContext);
        recycler.setLayoutManager(mManager);
        mScoreInputAdapter = new ScoreInputAdapter(mContext, mPlayerList, mCurrentCourse);
        recycler.setAdapter(mScoreInputAdapter);
        mScoreInputAdapter.notifyDataSetChanged();

        mReserveScore = new ReserveScore(mPlayerList, mCurrentCourse, Global.reserveId, mCurrentCourse.holes[mHoleScoreLayoutIdx].id, mTabIdx, mHoleScoreLayoutIdx);

    }

    public void setOnScoreInputFinishListener(ScoreInputFinishListener listener) {
        this.inputFinishListener = listener;
    }


    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public ScoreDialog(Context context, String title, String content, String leftBtnTitle, String rightBtnTitle,
                       View.OnClickListener leftListener,
                       View.OnClickListener rightListener, ArrayList<Player> mPlayerList, Course currentCourse, int tabIdx, int mHoleScoreLayoutIdx) {
        super(context, android.R.style.Theme_DeviceDefault_Light_Dialog);
        this.mContext = context;
        this.mTabIdx = tabIdx;
        this.mHoleScoreLayoutIdx = mHoleScoreLayoutIdx;
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
        this.mLeftTitle = leftBtnTitle;
        this.mRightTitle = rightBtnTitle;

        this.mPlayerList = mPlayerList;
        this.mCurrentCourse = currentCourse;
        this.mHoleScoreLayoutIdx = mHoleScoreLayoutIdx;

    }

    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public ScoreDialog(Context context, String 타이틀, String s, String 취소, String 확인, String title, Spanned content, String leftBtnTitle, String rightBtnTitle,
                       View.OnClickListener leftListener,
                       View.OnClickListener rightListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
        this.mLeftTitle = leftBtnTitle;
        this.mRightTitle = rightBtnTitle;
    }


    private class ScoreInputAdapter extends RecyclerView.Adapter<ScoreInputAdapter.ScoreInputItemViewHolder> {
        ArrayList<Player> mPlayerList;
        Course mCurrentCourse;

        public ScoreInputAdapter(Context context, ArrayList<Player> playerList, Course currentCourse) {

            this.mPlayerList = playerList;
            this.mCurrentCourse = currentCourse;


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
            final Hole selected_hole = mCurrentCourse.holes[mHoleScoreLayoutIdx];
            if (Global.isTar) {
               final TextView[] viewArr = holder.inserter.mStrokeScoreTextViewArr;
                for(int i=0; viewArr.length > i ;i++) {
                    viewArr[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mReserveScore.guest_score_list.get(playerIdx).tar = ((TextView) v).getText().toString().trim();
                            ScoreInserter.setAllBackGroundResourceBack(viewArr, R.drawable.score_inserter_bg);
                            v.getBackground().setColorFilter(Color.parseColor("#00AEC9"), PorterDuff.Mode.SRC);
                            //타수 파수 둘다 넣어줌
                            setTar(mReserveScore.guest_score_list.get(playerIdx), ((TextView) v).getText().toString().trim());
                            setPar(mReserveScore.guest_score_list.get(playerIdx), String.valueOf( Integer.valueOf(((TextView) v).getText().toString().trim()) - Integer.valueOf(selected_hole.par)));

                        }
                    });
                }
                int selectedIdx = holder.inserter.mSelectedStrokeScoreTvIdx;
           //     TextView selectedTv = holder.inserter.mSelectedStrokeInserterTv;
                holder.inserter.setBackGroundColorSelected(viewArr, AppDef.Par_Tar(mCurrentCourse.holes[mHoleScoreLayoutIdx].playedScore, AppDef.isTar), selectedIdx);

            } else {
               final TextView[] viewArr = holder.inserter.mParScoreTextViewArr;
                for(int i=0; viewArr.length > i ;i++) {
                    viewArr[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          //  mReserveScore.guest_score_list.get(playerIdx).par = ((TextView) v).getText().toString().trim();
                            ScoreInserter.setAllBackGroundResourceBack(viewArr, R.drawable.score_inserter_bg);
                            v.getBackground().setColorFilter(Color.parseColor("#00AEC9"), PorterDuff.Mode.SRC);
                            setPar(mReserveScore.guest_score_list.get(playerIdx), ((TextView) v).getText().toString().trim());
                            setTar(mReserveScore.guest_score_list.get(playerIdx), String.valueOf( Integer.valueOf(((TextView)v).getText().toString().trim()) + Integer.valueOf(selected_hole.par)));
                        }

                    });
                }
                int selectedIdx = holder.inserter.mSelectedParScoreTvIdx;
           //     TextView selectedTv = holder.inserter.mSelectedParInserterTv;
                holder.inserter.setBackGroundColorSelected(viewArr, AppDef.Par_Tar(mCurrentCourse.holes[mHoleScoreLayoutIdx].playedScore, AppDef.isTar), selectedIdx);

            }

            holder.inserterPutt.setBackGroundColorSelected(holder.inserterPutt.mPuttScoreTextViewArr, mCurrentCourse.holes[mHoleScoreLayoutIdx].playedScore.putting, holder.inserterPutt.mSelectedPuttScoreTvIdx);
            for(int i=0; holder.inserterPutt.mPuttScoreTextViewArr.length > i ;i++) {
                holder.inserterPutt.mPuttScoreTextViewArr[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mReserveScore.guest_score_list.get(playerIdx).putting = ((TextView) v).getText().toString().trim();
                        ScoreInserter.setAllBackGroundResourceBack(holder.inserterPutt.mPuttScoreTextViewArr, R.drawable.score_inserter_bg);
                        v.getBackground().setColorFilter(Color.parseColor("#00AEC9"), PorterDuff.Mode.SRC);
                    }
                });
            }

            mReserveScore.guest_score_list.get(playerIdx).putting = holder.inserterPutt.mSelectedPuttInserterTv.getText().toString().trim();
        }

        private void setTar(Score score, String scoreStr) {
            if (scoreStr == null ||! Util.isIntegerNumber(scoreStr)) {
                Toast.makeText(mContext, "정수가 아닙니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            score.tar = scoreStr;
        }

        private void setPar(Score score, String scoreStr) {
            if (scoreStr == null || ! Util.isIntegerNumber(scoreStr)) {
                Toast.makeText(mContext, "정수가 아닙니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            score.par = scoreStr;
        }

        private void setPutting(Score score, String scoreStr) {
            if (scoreStr == null ||! Util.isIntegerNumber(scoreStr)) {
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
                if (Global.isTar) {
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

        DataInterface.getInstance().setScore(mContext, reserveScore, new DataInterface.ResponseCallback<ResponseData<Object>>() {
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

