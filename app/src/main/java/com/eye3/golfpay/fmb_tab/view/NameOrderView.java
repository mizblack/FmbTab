package com.eye3.golfpay.fmb_tab.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.model.order.NameOrder;
import com.eye3.golfpay.fmb_tab.model.order.OrderItemInvoice;

public class NameOrderView extends LinearLayout {
    TextView mNameTv, mQtyTv;
    public LinearLayout deleteLinear;

    public NameOrderView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context ) {

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.linear_name_order, this, false);
        mNameTv =  v.findViewById(R.id.NameTextView);
        mNameTv.setText(((NameOrder)getTag()).name);
        mQtyTv = v.findViewById(R.id.MenuQuantityTextView);
        mQtyTv.setText(((NameOrder)getTag()).qty);
        deleteLinear = v.findViewById(R.id.deleteLinearLayout);
        addView(v);

    }
}
