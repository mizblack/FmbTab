package com.eye3.golfpay.fmb_tab.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.activity.MainActivity;
import com.eye3.golfpay.fmb_tab.listener.OnEditorFinishListener;
import com.eye3.golfpay.fmb_tab.model.guest.Guest;
import com.eye3.golfpay.fmb_tab.util.EditorDialogFragment;

import java.util.Objects;

import static com.eye3.golfpay.fmb_tab.util.Logger.TAG;


public class CaddieViewBasicGuestItem extends RelativeLayout {
    private static int sendCountNum = 0;
    protected static ProgressDialog pd; // 프로그레스바 선언
    Guest mGuest;
    Context mContext;
    TextView mGuestMemoContentTextView;
    EditText mCarNumEditTextView, mPhoneNumberEditText;
    LinearLayout mGuestMemoLinearLayout;

//    public CaddieViewGuestItem(Context context) {
//        super(context);
//
//        init(context);
//    }

    public CaddieViewBasicGuestItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public CaddieViewBasicGuestItem(Context context, Guest guest) {
        super(context);
        this.mContext = context;
        this.mGuest = guest;
        init(context, guest);
    }


    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_caddie_view_basic_guest, this, false);

        mGuestMemoLinearLayout = v.findViewById(R.id.guestMemoLinearLayout);

        addView(v);
    }

    public void init(Context context, Guest guest) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_caddie_view_basic_guest, this, false);

        mCarNumEditTextView = v.findViewById(R.id.carNumberEditText);
        mCarNumEditTextView.setText(guest.getCarNumber());
        mCarNumEditTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mGuest.setCarNumber(s.toString());
            }
        });

        mPhoneNumberEditText = v.findViewById(R.id.phoneNumberEditText);
        mPhoneNumberEditText.setText(guest.getPhoneNumber());
        mPhoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mGuest.setPhoneNumber(s.toString());
            }
        });

        mGuestMemoLinearLayout = v.findViewById(R.id.guestMemoLinearLayout);
        guestMemoLinearLayoutOnClick(mGuestMemoLinearLayout);
        mGuestMemoContentTextView = v.findViewById(R.id.guestMemoContentTextView);
        mGuestMemoContentTextView.setText(guest.getMemo());
        addView(v);
    }

    private void guestMemoLinearLayoutOnClick(final View guestMemoLinearLayout) {
        guestMemoLinearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                EditorDialogFragment guestMemoEditorDialogFragment = new EditorDialogFragment();

                guestMemoEditorDialogFragment.setGuestId(mGuest.getGuestName());
                guestMemoEditorDialogFragment.setMemoContent(mGuest.getMemo());
                guestMemoEditorDialogFragment.setOnEditorFinishListener(new OnEditorFinishListener() {
                    @Override
                    public void OnEditorInputFinished(String memoContent) {
                        closeKeyboard();
                        mGuest.setMemo(memoContent);
                        mGuestMemoContentTextView.setText(memoContent);
                    }
                });
                showDialogFragment(guestMemoEditorDialogFragment);
            }
        });
    }

    public void closeKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(mContext).getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void closeKeyboard() {
        closeKeyboard(mCarNumEditTextView);
        closeKeyboard(mPhoneNumberEditText);
    }

    private void showDialogFragment(DialogFragment dialogFragment) {
        closeKeyboard();
        FragmentManager supportFragmentManager = ((MainActivity) (mContext)).getSupportFragmentManager();
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        dialogFragment.show(transaction, TAG);
        assert dialogFragment.getFragmentManager() != null;
        dialogFragment.getFragmentManager().executePendingTransactions();
    }
}
