package com.eye3.golfpay.fmb_tab.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.eye3.golfpay.fmb_tab.R;

import java.util.Objects;

public class EditorDialog extends Dialog {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        Objects.requireNonNull(getWindow()).setAttributes(lpWindow);

        setContentView(R.layout.editor_dlg);

        EditText memoEditText = findViewById(R.id.memoEditText);
        showSoftKeyboard(memoEditText);
    }

    public EditorDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

    }

    private void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

//    void getKeyBoard() {
////        context.memoEditText.post(new Runnable() {
////            @Override
////            public void run() {
//                memoEditText.setFocusableInTouchMode(true);
//                memoEditText.requestFocus();
//                showSoftKeyboard(((MainActivity) mContext).mBinding.editorView.memoEditText);
////            }
////        });
//    }

}
