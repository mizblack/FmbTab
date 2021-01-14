package com.eye3.golfpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.eye3.golfpay.R;

public class SelectedOrderAdapter extends PagerAdapter {

    private final Context context;
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