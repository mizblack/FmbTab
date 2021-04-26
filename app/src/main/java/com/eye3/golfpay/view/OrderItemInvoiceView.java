package com.eye3.golfpay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.eye3.golfpay.R;
import com.eye3.golfpay.common.SingleClickListener;
import com.eye3.golfpay.model.order.OrderItemInvoice;

import java.util.ArrayList;
import java.util.Objects;

public class OrderItemInvoiceView extends RelativeLayout {
    public static final int NUM_MENU_NAME_KEY = 1 + 2 << 24;
    public static final int NUM_NAMEORDER_KEY = 2 + 2 << 24;
    public static final int NUM_INVOICEORDER_KEY = 3 + 2 << 24;

    public TextView mTvMenuName, mTvQty;
    public ImageButton btn_plus, btn_minus;
    public LinearLayout mLinearNameOrder, mExtendAndFoldingLinearLayout;
    public ImageView mIvExtending;
    private View.OnClickListener deletelistener;
    private View.OnClickListener pluslistener;
    private View.OnClickListener minuslistener;
    OrderItemInvoice orderItemInvoice;

//    public OrderItemView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//
//        init(context);
//    }

    public int getLinearNameOrderCount(){
       return mLinearNameOrder.getChildCount();
    }

    public OrderItemInvoiceView(Context context, View.OnClickListener deletelistener, View.OnClickListener pluslistener, View.OnClickListener minuslistener, OrderItemInvoice orderItemInvoice) {
        super(context);


        this.deletelistener = deletelistener;
        this.pluslistener = pluslistener;
        this.minuslistener = minuslistener;
        this.orderItemInvoice = orderItemInvoice;
        init(context);
    }

    private void init(Context context) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_shade_order, this, false);
        mTvMenuName = v.findViewById(R.id.menuNameTextView);
        mTvQty = v.findViewById(R.id.menuQuantityTextView);
        mLinearNameOrder = v.findViewById(R.id.linear_name_order);
        mIvExtending = v.findViewById(R.id.extendAndFoldingImageView);
        btn_plus = v.findViewById(R.id.tv_plus);
        btn_minus = v.findViewById(R.id.tv_minus);
        mExtendAndFoldingLinearLayout = v.findViewById(R.id.extendAndFoldingLinearLayout);
        mExtendAndFoldingLinearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(NUM_MENU_NAME_KEY, mTvMenuName.getText().toString());
                deletelistener.onClick(v);
            }
        });

        btn_plus.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {

                v.setTag(orderItemInvoice);
                v.setTag(NUM_INVOICEORDER_KEY, orderItemInvoice);
                v.setTag(NUM_NAMEORDER_KEY, orderItemInvoice.mGuestNameOrders.get(0));
                pluslistener.onClick(v);
            }
        });
        btn_minus.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {

                v.setTag(orderItemInvoice);
                v.setTag(NUM_INVOICEORDER_KEY, orderItemInvoice);
                v.setTag(NUM_NAMEORDER_KEY, orderItemInvoice.mGuestNameOrders.get(0));
                minuslistener.onClick(v);
            }
        });

        addView(v);
    }
}


