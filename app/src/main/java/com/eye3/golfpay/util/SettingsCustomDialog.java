package com.eye3.golfpay.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.eye3.golfpay.R;
import com.eye3.golfpay.activity.BaseActivity;
import com.eye3.golfpay.common.AppDef;
import com.eye3.golfpay.fragment.ScoreFragment;


public class SettingsCustomDialog extends Dialog {

    protected String TAG = getClass().getSimpleName();
    Switch mWifiSwitch;
    Switch mGPSSwitch;
    Switch mTarParSwitch;
    ImageView closeButtonView;
     Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        final int uiOptions =
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.settings_custom_dlg);
        closeButtonView = findViewById(R.id.closeImageView);
        closeButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mTarParSwitch = findViewById(R.id.puttSwitch);
        mTarParSwitch.setChecked(AppDef.isTar);
        mTarParSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppDef.isTar = true;
                    //Toast.makeText(mContext, "타수식 점수제로 전환하였습니다.", Toast.LENGTH_SHORT).show();
                    ((BaseActivity)mContext).GoNativeScreenBetweenParTar(new ScoreFragment(), null);

                }else {
                    AppDef.isTar = false;
                    //Toast.makeText(mContext, "Par식 점수제로 전환하였습니다.", Toast.LENGTH_SHORT).show();
                    ((BaseActivity)mContext).GoNativeScreenBetweenParTar(new ScoreFragment(), null);
                }
            }

        });

    }

    public SettingsCustomDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mContext = context;
    }



}
