package com.eye3.golfpay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import com.eye3.golfpay.R;

/**
 * Created by Administrator on 2017-09-22.
 */

public class LogoutDialog extends Dialog {

    private TextView mBtnClose;
    private boolean mIsChecked = false;
    private CheckBox mCheckBox;

    private String imgscr;
    private String target;
    private String Idx;
    private IListenerLogout mListener;

    public interface  IListenerLogout {
        void onLogout();
    }

    public LogoutDialog() {
        super(null);
    }

    public LogoutDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setListener(IListenerLogout listener){
        mListener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_logout);

        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mListener.onLogout();
            }
        });

        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
