package com.eye3.golfpay.fmb_tab.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.AppDef;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.fragment.ScoreFragment;
import com.eye3.golfpay.fmb_tab.listener.ScoreInputFinishListener;
import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.eye3.golfpay.fmb_tab.model.teeup.Player;

import java.util.ArrayList;


public class ScoreDialog extends Dialog  {


    private TextView mTitleView;
    //    private TextView mContentView;
    private Button mLeftButton;
    private Button mRightButton;
    private ImageButton mClosButton;
    //    private Button mSingleButton;
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
    ScoreInputAdapter mScoreInputAdapter;
    Context mContext;
    int mTabIdx;
    int mHoleScoreLayoutIdx;
     ScoreInputFinishListener inputFinishListener ;
     //여기 수정할것
     TextView tvHoleId;
     TextView tvPar;
     TextView tvCourseName;
     TextView tvIN_OUT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.fr_score_input);

        mTitleView = findViewById(R.id.dlg_title);
        //       mContentView = findViewById(R.id.dlg_msg);

        mLeftButton = findViewById(R.id.btnLeft);
        mRightButton = findViewById(R.id.btnRight);
        mLayoutButtons = findViewById(R.id.layoutButtons);

        tvHoleId = findViewById(R.id.hole_id);
        tvPar = findViewById(R.id.par_num);
        tvCourseName = findViewById(R.id.course_name);


        // 클릭 이벤트 셋팅
        if (mLeftClickListener != null && mRightClickListener != null) {

            mLayoutButtons.setVisibility(View.VISIBLE);
            mLeftButton.setOnClickListener(mLeftClickListener);
            mLeftButton.setText(mLeftTitle);
            mRightButton.setOnClickListener(mRightClickListener);
            mRightButton.setText(mRightTitle);
        }else{
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
                    dismiss();
                }
            });
            mRightButton.setText(mRightTitle);

        }

        recycler = findViewById(R.id.player_score_list);
        recycler.setHasFixedSize(true);
        LinearLayoutManager mManager = new LinearLayoutManager(mContext);
        recycler.setLayoutManager(mManager);
        mScoreInputAdapter = new ScoreInputAdapter(mContext, mPlayerList);
        recycler.setAdapter(mScoreInputAdapter);
        mScoreInputAdapter.notifyDataSetChanged();
    }

    public void setOnScoreInputFinishListener(ScoreInputFinishListener listener){
        this.inputFinishListener = listener;
    }

    // 클릭버튼이 하나일때 생성자 함수로 클릭이벤트를 받는다.
    public ScoreDialog(Context context, String title, String content, String btnTitle,
                       View.OnClickListener singleListener, boolean isThemePink) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        if (title != null) {
            this.mTitle = title;
        } else {
            this.mTitle = context.getResources().getString(R.string.app_name);
        }

        this.mContent = content;
        this.mSingleTitle = btnTitle;


    }

    // 클릭버튼이 하나일때 생성자 함수로 클릭이벤트를 받는다.
    public ScoreDialog(Context context, String title, String content, String btnTitle,
                       View.OnClickListener singleListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        if (title != null) {
            this.mTitle = title;
        } else {
            this.mTitle = context.getResources().getString(R.string.app_name);
        }

        this.mContent = content;
        //    this.mSingleClickListener = singleListener;
        this.mSingleTitle = btnTitle;
    }

    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public ScoreDialog(Context context, String title, String content, String leftBtnTitle, String rightBtnTitle,
                       View.OnClickListener leftListener,
                       View.OnClickListener rightListener, ArrayList<Player> mPlayerList, int mTabIdx, int mHoleScoreLayoutIdx) {
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
        this.mTabIdx = mTabIdx;
        this.mHoleScoreLayoutIdx = mHoleScoreLayoutIdx;

    }

    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public ScoreDialog(Context context, String title, String content, String leftBtnTitle, String rightBtnTitle,
                       View.OnClickListener leftListener,
                       View.OnClickListener rightListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

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
    }

    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public ScoreDialog(Context context, String title, Spanned content, String leftBtnTitle, String rightBtnTitle,
                       View.OnClickListener leftListener,
                       View.OnClickListener rightListener, boolean isThemePink) {
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
        ArrayList<Player> mPlayerList = new ArrayList<Player>();

        public ScoreInputAdapter(Context context, ArrayList<Player> playerList) {

            this.mPlayerList = playerList;
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
        public void onBindViewHolder(@NonNull ScoreInputAdapter.ScoreInputItemViewHolder holder, final int position) {
            holder.playerName.setText(mPlayerList.get(position).name);
            Course course =  mPlayerList.get(position).playingCourse.get(mTabIdx);
            holder.etInputTar.setText(AppDef.Par_Tar(course.holes[mHoleScoreLayoutIdx].playedScore , AppDef.isTar));
            holder.etInputTar.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s == null || s.equals("") )
                        return;
                    if(Util.isIntegerNumber(s.toString()))
                       ( mPlayerList.get(position).playingCourse.get(mTabIdx).holes[mHoleScoreLayoutIdx]).playedScore.par = s.toString();
//                    else
//                        Toast.makeText(mContext , "올바른 숫자가 아닙니다.", Toast.LENGTH_SHORT).show();
                }
            });
            holder.etInputPutt.setText((mPlayerList.get(position).playingCourse.get(mTabIdx).holes[mHoleScoreLayoutIdx]).playedScore.putting);
            holder.etInputPutt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                   if(s == null || s.equals(""))
                       return;
                    if(Util.isIntegerNumber(s.toString())  )
                        ( mPlayerList.get(position).playingCourse.get(mTabIdx).holes[mHoleScoreLayoutIdx]).playedScore.putting = s.toString();
                       //   mPlayerList.get(position).getGreenfee();
//                    else
//                        Toast.makeText(mContext , "올바른 숫자가 아닙니다.", Toast.LENGTH_SHORT).show();
                }
            });
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

   // private void sendPlayersScores()



}

