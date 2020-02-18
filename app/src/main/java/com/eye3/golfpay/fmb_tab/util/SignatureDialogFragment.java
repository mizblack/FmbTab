package com.eye3.golfpay.fmb_tab.util;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.teeup.GuestDatum;

import java.util.ArrayList;

public class SignatureDialogFragment extends DialogFragment {

    private String guestId;
    private ArrayList<GuestDatum> guestArrayList = Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getGuestData();
    private View view;

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public SignatureDialogFragment() {

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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.signature_dlg, container, false);

        view.findViewById(R.id.saveTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SignaturePad signaturePad = view.findViewById(R.id.signaturePad);
//                Global.guestArrayList.get(traversalByGuestId()).setSignImage();
//                signaturePad.getSignatureBitmap();
                dismiss();
            }
        });

        view.findViewById(R.id.cancelTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

}
