package com.eye3.golfpay.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.eye3.golfpay.R;

public class NumberkeypadDialogFragment extends DialogFragment {

    public interface INumberkeypadListener {
        void onConfirm(String number);
        void onCancel();
    }

    protected String TAG = getClass().getSimpleName();
    TextView tvNumber;
    INumberkeypadListener iNumberkeypadListener;

    public void setiNumberkeypadListener(INumberkeypadListener listener) {
        iNumberkeypadListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frd_number_keypad, container, false);

        tvNumber = view.findViewById(R.id.tv_number);
        view.findViewById(R.id.btn_0).setOnClickListener(onClickListener);
        view.findViewById(R.id.btn_1).setOnClickListener(onClickListener);
        view.findViewById(R.id.btn_2).setOnClickListener(onClickListener);
        view.findViewById(R.id.btn_3).setOnClickListener(onClickListener);
        view.findViewById(R.id.btn_4).setOnClickListener(onClickListener);
        view.findViewById(R.id.btn_5).setOnClickListener(onClickListener);
        view.findViewById(R.id.btn_6).setOnClickListener(onClickListener);
        view.findViewById(R.id.btn_7).setOnClickListener(onClickListener);
        view.findViewById(R.id.btn_8).setOnClickListener(onClickListener);
        view.findViewById(R.id.btn_9).setOnClickListener(onClickListener);
        view.findViewById(R.id.btn_point).setOnClickListener(onClickListener);
        view.findViewById(R.id.btn_clear).setOnClickListener(onClickListener);
        view.findViewById(R.id.btn_confirm).setOnClickListener(onClickListener);
        view.findViewById(R.id.btn_cancel).setOnClickListener(onClickListener);


        return view;
    }


    Button.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_0:
                    setNumber("0");
                    break;
                case R.id.btn_1:
                    setNumber("1");
                    break;
                case R.id.btn_2:
                    setNumber("2");
                    break;
                case R.id.btn_3:
                    setNumber("3");
                    break;
                case R.id.btn_4:
                    setNumber("4");
                    break;
                case R.id.btn_5:
                    setNumber("5");
                    break;
                case R.id.btn_6:
                    setNumber("6");
                    break;
                case R.id.btn_7:
                    setNumber("7");
                    break;
                case R.id.btn_8:
                    setNumber("8");
                    break;
                case R.id.btn_9:
                    setNumber("9");
                    break;
                case R.id.btn_point:
                    setNumber(".");
                    break;
                case R.id.btn_clear:
                    tvNumber.setText("");
                    break;
                case R.id.btn_confirm :
                    iNumberkeypadListener.onConfirm(tvNumber.getText().toString());
                    dismiss();
                    break;
                case R.id.btn_cancel:
                    dismiss();
                    break;
            }
        }
    };

    private void setNumber(String s) {
        tvNumber.append(s);
    }
}
