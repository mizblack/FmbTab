package com.eye3.golfpay.fmb_tab.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.model.order.OrderItemInvoice;

import java.util.ArrayList;
import java.util.Objects;

public class OrderItemInvoiceView extends RelativeLayout {
    public TextView mTvMenuName , mTvQty;
   public LinearLayout mLinearNameOrder , mExtendAndFoldingLinearLayout;

//    public OrderItemView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//
//        init(context);
//    }

    public OrderItemInvoiceView(Context context) {
        super(context);

        init(context);
    }

    private void init(Context context ) {

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_shade_order, this, false);
        mTvMenuName = v.findViewById(R.id.menuNameTextView);
        mTvQty = v.findViewById(R.id.menuQuantityTextView);
        mLinearNameOrder =   v.findViewById(R.id.linear_name_order);
        mExtendAndFoldingLinearLayout = v.findViewById(R.id.extendAndFoldingLinearLayout);
        mExtendAndFoldingLinearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLinearNameOrder.setSystemUiVisibility(View.VISIBLE);
            }
        });

        addView(v);

    }



}
