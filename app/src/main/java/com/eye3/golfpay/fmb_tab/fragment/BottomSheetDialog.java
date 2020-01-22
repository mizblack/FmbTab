package com.eye3.golfpay.fmb_tab.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.eye3.golfpay.fmb_tab.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;



public class BottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener{

    public static BottomSheetDialog getInstance() { return new BottomSheetDialog(); }

    private LinearLayout msgLo;
    private LinearLayout emailLo;
    private LinearLayout cloudLo;
    private LinearLayout bluetoothLo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_dialog, container,false);
        msgLo = (LinearLayout) view.findViewById(R.id.msgLo);
        emailLo = (LinearLayout) view.findViewById(R.id.emailLo);
        cloudLo = (LinearLayout) view.findViewById(R.id.cloudLo);
        bluetoothLo = (LinearLayout) view.findViewById(R.id.bluetoothLo);

        msgLo.setOnClickListener(this);
        emailLo.setOnClickListener(this);
        cloudLo.setOnClickListener(this);
        bluetoothLo.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.msgLo:
                Toast.makeText(getContext(),"Message", Toast.LENGTH_SHORT).show();
                break;
            case R.id.emailLo:
                Toast.makeText(getContext(),"Email", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cloudLo:
                Toast.makeText(getContext(),"Cloud", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bluetoothLo:
                Toast.makeText(getContext(),"Bluetooth", Toast.LENGTH_SHORT).show();
                break;
        }
        dismiss();
    }
}
