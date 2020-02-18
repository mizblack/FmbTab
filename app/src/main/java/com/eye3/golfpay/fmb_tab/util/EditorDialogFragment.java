package com.eye3.golfpay.fmb_tab.util;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.teeup.GuestDatum;
import com.eye3.golfpay.fmb_tab.view.CaddieViewGuestItem;

import java.util.ArrayList;
import java.util.Objects;

public class EditorDialogFragment extends DialogFragment implements View.OnClickListener {

    private String guestId;
    private String memoContent;
    private EditText memoEditText;
    private ArrayList<GuestDatum> guestArrayList = Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getGuestData();
    private ArrayList<CaddieViewGuestItem> caddieViewGuestItemArrayList;
    private View view;

    public EditorDialogFragment(String memoContent) {
        this.memoContent = memoContent;
    }

    public EditorDialogFragment(String guestId, String memoContent, ArrayList<CaddieViewGuestItem> caddieViewGuestItemArrayList) {
        this.guestId = guestId;
        this.memoContent = memoContent;
        this.caddieViewGuestItemArrayList = caddieViewGuestItemArrayList;
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

    private void setTeamMemoData() {
        Global.guestArrayList.get(0).setTeamMemo(memoEditText.getText().toString());
    }

    private void setGuestData() {
        Global.guestArrayList.get(traversalByGuestId()).setMemo(memoEditText.getText().toString());
    }

    private void setData() {
        setTeamMemoData();
        if (guestId != null) {
            setGuestData();
        }
    }

    private void setTextMemoTitle() {
        TextView memoTitleTextView = view.findViewById(R.id.memoTitleTextView);
        if (guestId == null) {
            memoTitleTextView.setText("팀메모");
        } else {
            memoTitleTextView.setText("개인메모");
        }
    }

    private void setTextTeamMemoContent() {
        memoEditText.setText(memoContent);
    }


    private void setTextGuestMemoContent() {
        View caddieViewGuestItem = caddieViewGuestItemArrayList.get(traversalByGuestId());
        TextView guestMemoContentTextView = caddieViewGuestItem.findViewById(R.id.guestMemoContentTextView);
        memoEditText.setText(guestMemoContentTextView.getText());
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
            InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void systemUIHide() {
        View decorView = Objects.requireNonNull(getActivity()).getWindow().getDecorView();
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
                    systemUIHide();
                }
            }
        });
    }

    private void closeKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        systemUIHide();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.editor_dlg, container, false);

        memoEditText = view.findViewById(R.id.memoEditText);
        showSoftKeyboard(memoEditText);

        view.findViewById(R.id.saveTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData();
                closeKeyboard(memoEditText);
                dismiss();
            }
        });

        view.findViewById(R.id.cancelTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard(memoEditText);
                dismiss();
            }
        });

        setTextMemoTitle();
        setTextMemoContent();


        return view;
    }

    @Override
    public void onClick(View view) {

    }
}