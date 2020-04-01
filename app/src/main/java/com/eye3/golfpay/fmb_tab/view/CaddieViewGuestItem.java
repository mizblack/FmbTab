package com.eye3.golfpay.fmb_tab.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.eye3.golfpay.fmb_tab.R;

public class CaddieViewGuestItem extends RelativeLayout {
     LinearLayout mGuestMemoLinearLayout;
    public CaddieViewGuestItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public CaddieViewGuestItem(Context context) {
        super(context);

        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_caddie_view_guest, this, false);

        mGuestMemoLinearLayout = v.findViewById(R.id.guestMemoLinearLayout);

        addView(v);

    }

}

