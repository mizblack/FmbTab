package com.eye3.golfpay.util;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.eye3.golfpay.R;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.listener.OnSignatureFinishListener;
import com.eye3.golfpay.model.teeup.GuestDatum;
import com.github.gcacace.signaturepad.views.SignaturePad;

import java.util.ArrayList;

import static com.eye3.golfpay.common.Global.signatureBitmapArrayList;

public class SignatureDialogFragment extends DialogFragment {

    private String guestId;
    private final ArrayList<GuestDatum> guestArrayList = Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getGuestData();
    private SignaturePad signaturePad;
    OnSignatureFinishListener onSignatureFinishListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (signatureBitmapArrayList.size() == 0) {
            //signatureBitmapArrayList.add(new Bitmap());
        }
    }

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
        View view = inflater.inflate(R.layout.signature_dlg, container, false);

        signaturePad = view.findViewById(R.id.signaturePad);

        view.findViewById(R.id.saveTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (signaturePad.getTransparentSignatureBitmap() != null) {
                    onSignatureFinishListener.OnSignatureInputFinished(signaturePad.getSignatureBitmap());
                    dismiss();
                }
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
                    .load(bitmap)
                    //      .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.ic_noimage)
                    .into(img);
        }
    }

    public void setOnSignatureFinishListener(OnSignatureFinishListener listener) {
        if (listener != null)
            this.onSignatureFinishListener = listener;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

}
