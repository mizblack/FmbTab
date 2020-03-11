package com.eye3.golfpay.fmb_tab.util;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.teeup.GuestDatum;
import com.eye3.golfpay.fmb_tab.view.CaddieViewGuestItem;
import com.github.gcacace.signaturepad.views.SignaturePad;

import java.util.ArrayList;

public class SignatureDialogFragment extends DialogFragment {

    private String guestId;
    private ArrayList<GuestDatum> guestArrayList = Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getGuestData();
    private ArrayList<CaddieViewGuestItem> caddieViewGuestItemArrayList;
    private SignaturePad signaturePad;

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public void setCaddieViewGuestItemArrayList(ArrayList<CaddieViewGuestItem> caddieViewGuestItemArrayList) {
        this.caddieViewGuestItemArrayList = caddieViewGuestItemArrayList;
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
        View view = inflater.inflate(R.layout.signature_dlg, container, false);

        signaturePad = view.findViewById(R.id.signaturePad);

        view.findViewById(R.id.saveTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CaddieViewGuestItem caddieViewGuestItem = caddieViewGuestItemArrayList.get(traversalByGuestId());
                TextView signatureTextView = caddieViewGuestItem.findViewById(R.id.signatureTextView);
                if (signaturePad.getTransparentSignatureBitmap() != null) {
                    Global.signatureBitmapArrayList.set(traversalByGuestId(), signaturePad.getTransparentSignatureBitmap());
                    ImageView imgView = caddieViewGuestItemArrayList.get(traversalByGuestId()).findViewById(R.id.signatureImageView);
                    setImageWithGlide(imgView, signaturePad.getTransparentSignatureBitmap());
                    signatureTextView.setVisibility(View.GONE);
                }
                dismiss();
            }
        });

        view.findViewById(R.id.cancelTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signaturePad.clear();
                dismiss();
            }
        });

        return view;
    }
    void setImageWithGlide(ImageView img, Bitmap bitmap) {
        if (img != null) {
            Glide.with(getActivity())
                     .asBitmap()
                    .load( bitmap)
              //      .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.ic_noimage)
                    .into(img);
        }
    }

}
