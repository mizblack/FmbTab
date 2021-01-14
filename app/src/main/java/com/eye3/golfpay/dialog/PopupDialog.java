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

public class PopupDialog extends Dialog {

    private TextView mBtnClose;
    private final boolean mIsChecked = false;
    private CheckBox mCheckBox;

    private String imgscr;
    private String target;
    private String Idx;
    private IListenerDialogTouch mListener;

    public interface  IListenerDialogTouch {
        void onTouch();
    }

    public PopupDialog() {
        super(null);
    }

    public PopupDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setListener(IListenerDialogTouch listener){
        mListener = listener;
    }

    public void setData(String imgscr, String target, String idx) {

        setContentView(R.layout.dialog_popup);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_popup);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        findViewById(R.id.layout_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mListener != null) {
                    mListener.onTouch();
                }

                dismiss();
            }
        });
    }
}
