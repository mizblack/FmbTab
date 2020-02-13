package com.eye3.golfpay.fmb_tab.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class ScoreDialog extends Dialog {


    private TextView mTitleView;
    private Button mLeftButton;
    private Button mRightButton;
    private ImageButton mClosButton;
    private LinearLayout mLayoutButtons;
    private String mTitle;
    private String mContent;
    private String mSingleTitle;
    private String mLeftTitle;
    private String mRightTitle;
    private Spanned mSpannedContent;
    private ScoreDialog dialog = this;

    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;

    RecyclerView recycler;
    ArrayList<Player> mPlayerList;
    Course mCurrentCourse;
    ScoreInputAdapter mScoreInputAdapter;
    Context mContext;
    /*hole_no와 동일 zero base
    *hole_id= mCurrentCourse.holes[mHoleScoreLayoutIdx ].id
    */
    int mHoleScoreLayoutIdx;

    ScoreInputFinishListener inputFinishListener;
    //여기 수정할것
    TextView tvHoleId;
    TextView tvPar;
    TextView tvCourseName;
    TextView tvIN_OUT;
    //서버로 스코어등록을 하기위한 객체
    ReserveScore mReserveScore;

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

        mTitleView = findViewById(R.id.dlg_title);
        //       mContentView = findViewById(R.id.dlg_msg);

        mLeftButton = findViewById(R.id.btnLeft);
        mRightButton = findViewById(R.id.btnRight);
        mLayoutButtons = findViewById(R.id.layoutButtons);

        tvHoleId = findViewById(R.id.hole_id);
        tvHoleId.setText("Hole-" + mCurrentCourse.holes[mHoleScoreLayoutIdx].id);
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

                    inputFinishListener.OnScoreInputFinished(mPlayerList);
                    sendPlayersScores(mContext, mReserveScore);
                    // dismiss();
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

        mReserveScore = new ReserveScore(mPlayerList, mCurrentCourse, Global.reserveId , mCurrentCourse.holes[mHoleScoreLayoutIdx ].id, mHoleScoreLayoutIdx);

//        mReserveScore.setReserve_id(Global.reserveId);
//        mReserveScore.setHole_id(String.valueOf(mCurrentCourse.holes[mHoleScoreLayoutIdx +1].id));

    }

    public void setOnScoreInputFinishListener(ScoreInputFinishListener listener) {
        this.inputFinishListener = listener;
    }


    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public ScoreDialog(Context context, String title, String content, String leftBtnTitle, String rightBtnTitle,
                       View.OnClickListener leftListener,
                       View.OnClickListener rightListener, ArrayList<Player> mPlayerList, Course currentCourse, int mHoleScoreLayoutIdx) {
        super(context, android.R.style.Theme_DeviceDefault_Light_Dialog);
        this.mContext = context;

        if (title != null) {
            this.mTitle = title;
        } else {
            this.mTitle = context.getResources().getString(R.string.app_name);
        }

        this.mContent = content;
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
        this.mLeftTitle = leftBtnTitle;
        this.mRightTitle = rightBtnTitle;

        this.mPlayerList = mPlayerList;
        this.mCurrentCourse = currentCourse;
        this.mHoleScoreLayoutIdx = mHoleScoreLayoutIdx;

    }

    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public ScoreDialog(Context context, String title, Spanned content, String leftBtnTitle, String rightBtnTitle,
                       View.OnClickListener leftListener,
                       View.OnClickListener rightListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        if (title != null) {
            this.mTitle = title;
        } else {
            this.mTitle = context.getResources().getString(R.string.app_name);
        }

        this.mSpannedContent = content;
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
        public void onBindViewHolder(@NonNull ScoreInputAdapter.ScoreInputItemViewHolder holder, int position) {
            final int playerIdx = position;
            //*******
            Course a_course = mPlayerList.get(playerIdx).playingCourse.get((Integer.valueOf(mCurrentCourse.id) - 1));
            holder.playerName.setText(mPlayerList.get(position).name);

            holder.etInputTar.setText(AppDef.Par_Tar(a_course.holes[mHoleScoreLayoutIdx].playedScore, AppDef.isTar));
            holder.etInputTar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    String s = ((EditText) v).getText().toString();
                    //    if ("-".equals(s)) {
                    ((EditText) v).setText("");
                    //     }
                    return false;
                }
            });
            holder.etInputTar.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (!TextUtils.isEmpty(s) && Util.isInteger(s.toString().trim())) {
                        //현재 선택되어진 홀
                        Hole selected_hole = mCurrentCourse.holes[mHoleScoreLayoutIdx];

                        if (AppDef.isTar) {

                            setTar(mPlayerList.get(playerIdx).playingCourse.get((Integer.valueOf(mCurrentCourse.id) - 1)).holes[mHoleScoreLayoutIdx].playedScore, s.toString());
                            setPar(mPlayerList.get(playerIdx).playingCourse.get((Integer.valueOf(mCurrentCourse.id) - 1)).holes[mHoleScoreLayoutIdx].playedScore, String.valueOf(Integer.valueOf(s.toString()) - Integer.valueOf(selected_hole.par)));


                            setTar(mReserveScore.guest_score_list.get(playerIdx), s.toString());
                            setPar(mReserveScore.guest_score_list.get(playerIdx), String.valueOf(Integer.valueOf(s.toString()) - Integer.valueOf(selected_hole.par)));

                        } else {  //AppDaf.istar == false (파로 넣을때)
                            setPar(mPlayerList.get(playerIdx).playingCourse.get((Integer.valueOf(mCurrentCourse.id) - 1)).holes[mHoleScoreLayoutIdx].playedScore, s.toString());
                            setTar(mPlayerList.get(playerIdx).playingCourse.get((Integer.valueOf(mCurrentCourse.id) - 1)).holes[mHoleScoreLayoutIdx].playedScore, String.valueOf(Integer.valueOf(s.toString()) + Integer.valueOf(selected_hole.par)));

                            setPar(mReserveScore.guest_score_list.get(playerIdx), s.toString());
                            setTar(mReserveScore.guest_score_list.get(playerIdx), String.valueOf(Integer.valueOf(s.toString()) + Integer.valueOf(selected_hole.par)));

                        }

                        mReserveScore.guest_score_list.get(playerIdx).guest_id = mPlayerList.get(playerIdx).guest_id;

                    }
//                    else
//                        Toast.makeText(mContext , "올바른 숫자가 아닙니다.", Toast.LENGTH_SHORT).show();
                }
            });
            // holder.etInputPutt.setText((mCurrentCourse.holes[mHoleScoreLayoutIdx].playedScore.putting));
            holder.etInputPutt.setText(a_course.holes[mHoleScoreLayoutIdx].playedScore.putting);

            holder.etInputPutt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    String s = ((EditText) v).getText().toString();
                    //    if ("-".equals(s)) {
                    ((EditText) v).setText("");
                    //    }
                    return false;
                }
            });
            holder.etInputPutt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s == null || s.equals(""))
                        return;
                    if (Util.isInteger(s.toString())) {
                        mPlayerList.get(playerIdx).playingCourse.get((Integer.valueOf(mCurrentCourse.id) - 1)).holes[mHoleScoreLayoutIdx].playedScore.putting = s.toString();
                        mReserveScore.guest_score_list.get(playerIdx).putting = s.toString();
                    }


//                    else
//                        Toast.makeText(mContext , "올바른 숫자가 아닙니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void setTar(Score score, String scoreStr) {
//            if (s == null || s.toString() == null)
//                return;
            score.tar = scoreStr;
        }

        private void setPar(Score score, String scoreStr) {
            if (scoreStr == null)
                return;
            else
                score.par = scoreStr;
        }

        private void setPutting(Score score, String scoreStr) {
            if (scoreStr == null)
                return;
            else
                score.putting = scoreStr;
        }


        @Override
        public int getItemCount() {
            return mPlayerList.size();
        }

        public class ScoreInputItemViewHolder extends RecyclerView.ViewHolder {
            TextView playerName;
            EditText etInputTar;
            EditText etInputPutt;

            //onCreate의 view임(score_input_row)
            public ScoreInputItemViewHolder(@NonNull View itemView) {
                super(itemView);
                playerName = itemView.findViewById(R.id.playerName);
                etInputTar = itemView.findViewById(R.id.et_tar_input);
                etInputPutt = itemView.findViewById(R.id.et_putt_input);
            }
        }
    }

    private void sendPlayersScores(final Context mContext, ReserveScore reserveScore) {
        int a = 0;
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).setScore(mContext, reserveScore, new DataInterface.ResponseCallback<ResponseData<Object>>() {
            @Override
            public void onSuccess(ResponseData<Object> response) {
                if ("ok".equals(response.getResultCode())) {
                    Toast.makeText(mContext, response.getResultMessage(), Toast.LENGTH_SHORT).show();
                    mReserveScore = null; //다음 입력을 위해 clear한다.
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

    ArrayList<Integer> incrementsLoop(int a, int b, int increments) {
        Log.d(TAG, "incrementsLoop(" + a + ", " + b + ", " + increments + ")");
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        for (int i = a; i <= b; ) {
            integerArrayList.add(i);
            i = i + increments;
        }
        return integerArrayList;
    }

    void createIntegerArrayList() {
        ArrayList<Integer> parScoreIntegerArrayList = incrementsLoop(3 * (-1), 8, 1);
        ArrayList<Integer> strokesScoreIntegerArrayList = incrementsLoop(1, 10, 1);
        ArrayList<Integer> puttIntegerArrayList = incrementsLoop(0, 10, 1);
        ArrayList<Integer> nearestIntegerArrayList = incrementsLoop(0, 20, 1);
        ArrayList<Integer> longestIntegerArrayList = incrementsLoop(100, 300, 10);
    }


}

