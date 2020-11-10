package com.eye3.golfpay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.eye3.golfpay.R;

import java.util.Objects;

public class VisitorsGuestItem extends RelativeLayout {

    public VisitorsGuestItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public VisitorsGuestItem(Context context) {
        super(context);

        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Objects.requireNonNull(inflater).inflate(R.layout.item_visitors_guest, this, true);
    }

}