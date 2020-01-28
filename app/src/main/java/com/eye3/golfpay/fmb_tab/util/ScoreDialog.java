package com.eye3.golfpay.fmb_tab.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Spanned;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eye3.golfpay.fmb_tab.R;


public class ScoreDialog extends Dialog {


   //    private TextView mTitleView;
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
    // private boolean isThemePink = false;
    private ScoreDialog dialog = this;

    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;
  //  private View.OnClickListener mSingleClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.fr_score_input);

      //     mTitleView = findViewById(R.id.dlg_title);
     //       mContentView = findViewById(R.id.dlg_msg);

        mLeftButton = findViewById(R.id.btnLeft);
        mRightButton = findViewById(R.id.btnRight);
    //    mSingleButton = findViewById(R.id.btnSingle);
        mLayoutButtons = findViewById(R.id.layoutButtons);


        //   mClosButton = findViewById(R.id.btn_dlg_close);
//        mClosButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
 //        제목과 내용을 생성자에서 셋팅한다.
//             mTitleView.setText(mTitle);
//
//        if (mSpannedContent != null) {
//            mContentView.setText(mSpannedContent);
//        } else {
//            mContentView.setText(mContent);
//        }


        // 클릭 이벤트 셋팅
        if (mLeftClickListener != null && mRightClickListener != null) {
            //       mTitleView.setTextColor(getContext().getResources().getColor(R.color.txtPink));

            mLayoutButtons.setVisibility(View.VISIBLE);
      //      mSingleButton.setVisibility(View.GONE);
            mLeftButton.setOnClickListener(mLeftClickListener);
            mLeftButton.setText(mLeftTitle);
            mRightButton.setOnClickListener(mRightClickListener);
            mRightButton.setText(mRightTitle);
        }
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
     //   this.mSingleClickListener = singleListener;
        this.mSingleTitle = btnTitle;
        //   this.isThemePink = isThemePink;
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
                       View.OnClickListener rightListener, boolean isThemePink) {
        super(context, android.R.style.Theme_DeviceDefault_Light_Dialog);

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
        //    this.isThemePink = isThemePink;
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
        //  this.isThemePink = isThemePink;
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

}

