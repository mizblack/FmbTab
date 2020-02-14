package com.eye3.golfpay.fmb_tab.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.teeup.GuestDatum;

import java.util.ArrayList;
import java.util.Objects;

public class EditorDialog extends Dialog {

    private String guestId;
    private String memoContent;
    private ArrayList<GuestDatum> guestArrayList = Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getGuestData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        Objects.requireNonNull(getWindow()).setAttributes(lpWindow);

        setContentView(R.layout.editor_dlg);

        final EditText memoEditText = findViewById(R.id.memoEditText);
        showSoftKeyboard(memoEditText);

        findViewById(R.id.saveTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard(memoEditText);
                dismiss();
            }
        });

        findViewById(R.id.cancelTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard(memoEditText);
                dismiss();
            }
        });

        setTextMemoTitle();
        setTextMemoContent();

    }

    public EditorDialog(Context context, String memoContent) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.memoContent = memoContent;
    }

    public EditorDialog(Context context, String guestId, String memoContent) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.guestId = guestId;
        this.memoContent = memoContent;
    }

    private void setTextMemoTitle() {
        TextView memoTitleTextView = findViewById(R.id.memoTitleTextView);
        if (guestId == null) {
            memoTitleTextView.setText("팀메모");
        } else {
            memoTitleTextView.setText("개인메모");
        }
    }

    private void setTextTeamMemoContent() {
        EditText memoEditText = findViewById(R.id.memoEditText);
        memoEditText.setText(memoContent);
    }

    private int traversalByGuestId() {
        int index = 0;
        for (int i = 0; i < guestArrayList.size(); i++) {
            if (guestId.equals(guestArrayList.get(i).getId())) {
                index = i;
            }
        }
        return index;
    }

    private void setTextGuestMemoContent() {
        View caddieViewGuestItem = Global.caddieViewGuestItemArrayList.get(traversalByGuestId());
        TextView guestMemoContentTextView = caddieViewGuestItem.findViewById(R.id.guestMemoContentTextView);
        guestMemoContentTextView.setText(memoContent);
    }

    private void setTextMemoContent() {
        if (guestId == null) {
            setTextTeamMemoContent();
        } else {
            setTextGuestMemoContent();
        }
    }

    private void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void systemUIHide() {
        View decorView = Objects.requireNonNull(getWindow()).getDecorView();
        final int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LOW_PROFILE;
        decorView.setSystemUiVisibility(uiOptions);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    // TODO: The system bars are visible. Make any desired
                    // adjustments to your UI, such as showing the action bar or
                    // other navigational controls.
                    systemUIHide();
                }
//                } else {
                // TODO: The system bars are NOT visible. Make any desired
                // adjustments to your UI, such as hiding the action bar or
                // other navigational controls.
//                }
            }
        });
    }

    private void closeKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        systemUIHide();
    }

}
