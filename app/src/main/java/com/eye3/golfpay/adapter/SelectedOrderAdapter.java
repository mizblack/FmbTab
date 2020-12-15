package com.eye3.golfpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.eye3.golfpay.R;
import com.eye3.golfpay.model.order.GuestNameOrder;
import com.eye3.golfpay.model.order.OrderItemInvoice;
import com.eye3.golfpay.view.NameOrderView;
import com.eye3.golfpay.view.OrderItemInvoiceView;

import java.util.List;

import static com.eye3.golfpay.fragment.OrderFragment.NUM_INVOICEORDER_KEY;
import static com.eye3.golfpay.fragment.OrderFragment.NUM_MENU_NAME_KEY;
import static com.eye3.golfpay.fragment.OrderFragment.NUM_NAMEORDER_KEY;

public class SelectedOrderAdapter extends PagerAdapter {

    private Context context;
    private int pagerCount = 0;
    public SelectedOrderAdapter(Context context, int count) {
        super();
        this.context = context;
        pagerCount = count;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_selected_order, null);

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return pagerCount;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (View)o);
    }
}