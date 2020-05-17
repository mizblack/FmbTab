package com.eye3.golfpay.fmb_tab.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import com.eye3.golfpay.fmb_tab.R;

/**
 * Created by Administrator on 2017-09-22.
 */

public class RestaurantsPopupDialog extends Dialog {

    private TextView mBtnClose;
    private boolean mIsChecked = false;
    private CheckBox mCheckBox;

    private String imgscr;
    private String target;
    private String Idx;
    private IListenerDialogTouch mListener;

    public interface  IListenerDialogTouch {
        public void onTouch();
    }

    public RestaurantsPopupDialog(Context context) {
        super(context);
    }

    public RestaurantsPopupDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setListener(IListenerDialogTouch listener){
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_restaurant_popup);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
