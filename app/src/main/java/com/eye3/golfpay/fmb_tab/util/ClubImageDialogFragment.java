package com.eye3.golfpay.fmb_tab.util;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.teeup.GuestDatum;

import java.util.ArrayList;

public class ClubImageDialogFragment extends DialogFragment {

    private String guestId;
    private ArrayList<GuestDatum> guestArrayList = Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getGuestData();
    private ArrayList<Bitmap> clubBitmapArrayList;

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public void setClubBitmapArrayList(ArrayList<Bitmap> clubBitmapArrayList) {
        this.clubBitmapArrayList = clubBitmapArrayList;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_club_image_viewer, container, false);

        if (clubBitmapArrayList.get(traversalByGuestId()) != null) {
            ImageView clubImageView = view.findViewById(R.id.clubImageView);
            clubImageView.setImageBitmap(clubBitmapArrayList.get(traversalByGuestId()));
        }

        view.findViewById(R.id.cameraTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        view.findViewById(R.id.closeTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }
}