package com.eye3.golfpay.fmb_tab.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.eye3.golfpay.fmb_tab.R;

import java.util.Objects;

public class SignatureDialog extends Dialog {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        Objects.requireNonNull(getWindow()).setAttributes(lpWindow);

        setContentView(R.layout.signature_dlg);
    }

    public SignatureDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

    }

}
