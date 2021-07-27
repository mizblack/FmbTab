package com.eye3.golfpay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.eye3.golfpay.R;

/**
 * Created by Administrator on 2017-09-22.
 */

public class PopupDialog extends Dialog {

    private TextView tvMessage;
    private TextView tvSender;
    private TextView tvTime;
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

    public void setData(String message, String sender, String time) {

        tvMessage.setText(message);
        tvSender.setText(sender);
        tvTime.setText(time);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_popup);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        tvMessage = findViewById(R.id.tv_message);
        tvSender = findViewById(R.id.tv_sender);
        tvTime = findViewById(R.id.tv_time);

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
