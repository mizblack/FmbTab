package com.eye3.golfpay.fmb_tab.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.eye3.golfpay.fmb_tab.R;

import java.util.Objects;

public class NearestLongestInputItem extends RelativeLayout {

    public NearestLongestInputItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public NearestLongestInputItem(Context context) {
        super(context);

        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Objects.requireNonNull(inflater).inflate(R.layout.item_nearest_longest_input, this, true);
    }

}

