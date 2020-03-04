package com.eye3.golfpay.fmb_tab.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.model.order.OrderItemInvoice;

import java.util.ArrayList;
import java.util.Objects;

public class OrderItemInvoiceView extends RelativeLayout {
    public TextView mTvMenuName, mTvQty;
    public LinearLayout mLinearNameOrder, mExtendAndFoldingLinearLayout;
    public ImageView mIvExtending;
    public boolean isExtended = false;

//    public OrderItemView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//
//        init(context);
//    }

    public int getmLinearNameOrderCount(){
       return mLinearNameOrder.getChildCount();
    }

    public OrderItemInvoiceView(Context context) {
        super(context);

        init(context);
    }

    private void init(Context context) {

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_shade_order, this, false);
        mTvMenuName = v.findViewById(R.id.menuNameTextView);
        mTvQty = v.findViewById(R.id.menuQuantityTextView);
        mLinearNameOrder = v.findViewById(R.id.linear_name_order);
        mIvExtending = v.findViewById(R.id.extendAndFoldingImageView);
        mExtendAndFoldingLinearLayout = v.findViewById(R.id.extendAndFoldingLinearLayout);
        mExtendAndFoldingLinearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isExtended) {
                    mLinearNameOrder.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    mIvExtending.setImageResource(R.drawable.up_arrow);
                    isExtended = true;
                } else {
                    mLinearNameOrder.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0));
                    mIvExtending.setImageResource(R.drawable.down_arrow);
                    isExtended = false;
                }
            }
        });
            addView(v);
    }
}


