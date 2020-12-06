package com.eye3.golfpay.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eye3.golfpay.R;

public class NameOrderView extends LinearLayout {
    public TextView mNameTv, mQtyTv,  mCaddyTv;
    public ImageButton mPlusTv , mMinusTv;
    public LinearLayout deleteLinear;
    public String menuPrice;

    public NameOrderView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context ) {

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.linear_name_order, this, false);

        mNameTv =  v.findViewById(R.id.NameTextView);
        mQtyTv = v.findViewById(R.id.MenuQuantityTextView);
        mPlusTv= v.findViewById(R.id.tv_plus) ;
        mCaddyTv = v.findViewById(R.id.CaddyTextView);
        mCaddyTv.setVisibility(View.INVISIBLE);
        mMinusTv= v.findViewById(R.id.tv_minus) ;
        deleteLinear = v.findViewById(R.id.deleteLinearLayout);

        addView(v);

    }

    public void setmCaddyTvVisible() {
        this.mCaddyTv.setVisibility(View.VISIBLE);
    }
}
