package com.eye3.golfpay.util;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.eye3.golfpay.R;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.listener.OnEditorFinishListener;
import com.eye3.golfpay.model.teeup.GuestDatum;

import java.util.ArrayList;
import java.util.Objects;

public class EditorDialogFragment extends DialogFragment {

    private String guestId;
    private String memoContent;
    private EditText memoEditText;
    private final ArrayList<GuestDatum> guestArrayList = Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getGuestData();
    private View view;
    private String textType;
    private String textHint;
    OnEditorFinishListener onEditorFinishListener;

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public void setMemoContent(String memoContent) {
        this.memoContent = memoContent;
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
        for (int i = 0; i < Global.guestList.size(); i++) {
            Global.guestList.get(i).setTeamMemo(memoContent);
        }
    }

    private void setGuestDataMemo(String memoContent) {
        Global.guestList.get(traversalByGuestId()).setMemo(memoContent);
    }

    private void setGuestData() {
        Global.guestList.get(traversalByGuestId()).setMemo(memoContent);
    }

    private void setData(String memo) {
        if (memoEditText.getText() == null || "".equals(memoEditText.getText().toString())) {
            Toast.makeText(getActivity(), "텍스트 없슴", Toast.LENGTH_SHORT).show();
            return;
        }

        memoContent = memoEditText.getText().toString();

        if (guestId == null) {
            setTeamMemoData();
        } else {
            setGuestDataMemo(memo);
        }
    }

    private void setData() {
        if (memoEditText.getText() == null || "".equals(memoEditText.getText().toString())) {
            Toast.makeText(getActivity(), "텍스트 없슴", Toast.LENGTH_SHORT).show();
            return;
        }

        memoContent = memoEditText.getText().toString();

        if (guestId == null) {
            setTeamMemoData();
        } else {
            setGuestData();
        }
    }

    public void setTextType(String textType, String textHint) {
        this.textType = textType;
        this.textHint = textHint;
    }

    private void setTextMemoContent() {
        memoEditText.setText(memoContent);
    }

    private void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        setTextMemoContent();
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.editor_dlg, container, false);

        memoEditText = view.findViewById(R.id.memoEditText);
        memoEditText.setHint(textHint);
        showSoftKeyboard(memoEditText);

        view.findViewById(R.id.saveTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData();
            //    memoEditText.setText("");
                dismiss();
            }
        });

        view.findViewById(R.id.cancelTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   memoEditText.setText("");
                dismiss();
            }
        });

        setTextMemoContent();
        TextView memoTitleTextView = view.findViewById(R.id.memoTitleTextView);
        memoTitleTextView.setText(textType);
        return view;
    }

    public void setOnEditorFinishListener(OnEditorFinishListener listener) {
        if (listener != null)
            this.onEditorFinishListener = listener;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Util.hideSoftKey(memoEditText, getActivity());
        onEditorFinishListener.OnEditorInputFinished(memoContent);
    }


}
