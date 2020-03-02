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

public class OrderItemView extends RelativeLayout {
    public TextView mTvMenuName , mTvQty;
   public LinearLayout linear_name_order;

//    public OrderItemView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//
//        init(context);
//    }

    public OrderItemView(Context context) {
        super(context);

        init(context);
    }

    private void init(Context context ) {

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_shade_order, this, false);
        mTvMenuName = v.findViewById(R.id.menuNameTextView);
        mTvQty = v.findViewById(R.id.menuQuantityTextView);
        linear_name_order =   v.findViewById(R.id.linear_name_order);

        mTvMenuName.setText(((OrderItemInvoice) getTag()).mMenunName);
        mTvQty.setText(((OrderItemInvoice) getTag()).mTvQty);
        addView(v);

    }



}
