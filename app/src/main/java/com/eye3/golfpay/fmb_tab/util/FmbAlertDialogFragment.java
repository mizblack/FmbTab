package com.eye3.golfpay.fmb_tab.util;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.eye3.golfpay.fmb_tab.R;

public class FmbAlertDialogFragment extends DialogFragment {
    TextView mMsg;
    Button mBtnConfirm, mBtnCancel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_fmb_alert_dialog, container, false);
        mMsg = view.findViewById(R.id.msg);
        mBtnConfirm = view.findViewById(R.id.btn_confirm);
        mBtnCancel = view.findViewById(R.id.btn_cancel);

        return view;
    }

    public void setMsg(String msg){
        mMsg.setText(msg);

    }

//    public void setButtonConfirm(){
//        mBtnConfirm
//    }

}
