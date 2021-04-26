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
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.eye3.golfpay.R;
import com.eye3.golfpay.activity.BaseActivity;
import com.eye3.golfpay.common.AppDef;
import com.eye3.golfpay.common.SingleClickListener;
import com.eye3.golfpay.fragment.ScoreFragment;


public class SettingsCustomDialog extends Dialog {

    protected String TAG = getClass().getSimpleName();
    private ImageView ivTar, ivPar;
    private TextView tvTar, tvPar;
    private boolean isTar = false;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_custom_dlg);
        ivTar = findViewById(R.id.iv_tar);
        tvTar = findViewById(R.id.tv_tar);
        ivPar = findViewById(R.id.iv_par);
        tvPar = findViewById(R.id.tv_par);
        changeTarPar(AppDef.isTar);

        findViewById(R.id.view_tar).setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                changeTarPar(true);
            }
        });

        findViewById(R.id.view_par).setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                changeTarPar(false);
            }
        });

        findViewById(R.id.tv_save).setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                setPar();
                dismiss();
            }
        });

        findViewById(R.id.tv_cancel).setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                dismiss();
            }
        });
    }

    private void changeTarPar(boolean tar) {

        isTar = tar;
        if (tar) {
            ivTar.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.check_in_white));
            ivPar.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.uncheck));
            tvTar.setTextAppearance(R.style.GlobalTextView_18SP_irisBlue_NotoSans_Bold);
            tvPar.setTextAppearance(R.style.GlobalTextView_18SP_ebonyBlack_NotoSans_Bold);
        }
        else {
            ivPar.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.check_in_white));
            ivTar.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.uncheck));
            tvPar.setTextAppearance(R.style.GlobalTextView_18SP_irisBlue_NotoSans_Bold);
            tvTar.setTextAppearance(R.style.GlobalTextView_18SP_ebonyBlack_NotoSans_Bold);
        }
    }

    private void setPar() {
        if (isTar) {
            AppDef.isTar = true;
            //Toast.makeText(mContext, "타수식 점수제로 전환하였습니다.", Toast.LENGTH_SHORT).show();
            ((BaseActivity) mContext).GoNativeScreenBetweenParTar(new ScoreFragment(), null);

        } else {
            AppDef.isTar = false;
            //Toast.makeText(mContext, "Par식 점수제로 전환하였습니다.", Toast.LENGTH_SHORT).show();
            ((BaseActivity) mContext).GoNativeScreenBetweenParTar(new ScoreFragment(), null);
        }
    }

    public SettingsCustomDialog(Context context,  int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }


}
