package com.eye3.golfpay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.eye3.golfpay.R;

/**
 * Created by Administrator on 2017-09-22.
 */

public class YesNoDialog extends Dialog {

    private TextView tvContent;
    private IListenerYesNo mListener;

    public interface  IListenerYesNo {
        void onConfirm();
    }

    public YesNoDialog() {
        super(null);
    }

    public YesNoDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setListener(IListenerYesNo listener){
        mListener = listener;
    }


    public void setContentTitle(String content) {
        tvContent.setText(content);
    }

    public void setDisableCancelButton() {
        findViewById(R.id.btn_cancel).setVisibility(View.GONE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_yesno);
        tvContent = findViewById(R.id.tv_content);
        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mListener.onConfirm();
            }
        });

        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
