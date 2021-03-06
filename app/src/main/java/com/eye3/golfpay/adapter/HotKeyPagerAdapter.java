package com.eye3.golfpay.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.eye3.golfpay.R;
import com.eye3.golfpay.model.control.ChatHotKeyItem;
import com.eye3.golfpay.util.Util;

import java.util.ArrayList;

public class HotKeyPagerAdapter extends PagerAdapter {

    public class ShortcutItem {

        public ShortcutItem(String title, String descr, String option) {
            this.title = title; this.descr = descr; this.option = option;
            multi = !(option == null || option.isEmpty() == true);
        }

        public String title;
        public String descr;
        public String option;
        public boolean multi;
    }

    public interface OnAdapterClickListener {
        void onClicked(int position);
    }

    Context mContext;
    private final ArrayList<ShortcutItem> shortcutList = new ArrayList<>();
    OnAdapterClickListener onAdapterClickListener;
    GridLayout gridView;

    @SuppressLint("MissingPermission")
    public HotKeyPagerAdapter(Context context, OnAdapterClickListener listener) {
        mContext = context;
        onAdapterClickListener = listener;
    }

    public void addHotkey(ArrayList<ChatHotKeyItem> hotKeyItems) {

        for (ChatHotKeyItem item : hotKeyItems) {
            shortcutList.add(new ShortcutItem(item.getTitle(), item.getMessage(), item.getOption()));
        }

        int flag = 20;
        if (shortcutList.size() > 20 && shortcutList.size() <= 40)
            flag = 40;

        int size = shortcutList.size();
        for (int i = 0; i < flag - size; i++) {
            shortcutList.add(new ShortcutItem("", "", ""));
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_shortcut_view, container, false);

        gridView = view.findViewById(R.id.grid_view);
        container.addView(view);
        createItem(gridView, position == 0 ? 0 : 20);
        return view;
    }

    private void createItem(GridLayout gridView, int startPos) {

        for (int i = startPos; i < shortcutList.size(); i++) {

            if (startPos == 0 && i >= 20) {
                break;
            }

            final int position = i;
            ShortcutItem shortcut = shortcutList.get(i);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_shortcut, null, false);

            LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams((int) Util.convertDpToPixel(93.6f, mContext), (int)Util.convertDpToPixel(60.f, mContext));
            lllp.gravity = Gravity.CENTER_VERTICAL;
            lllp.rightMargin = (int)Util.convertDpToPixel(6.f, mContext);
            lllp.bottomMargin = (int)Util.convertDpToPixel(6.f, mContext);
            view.setLayoutParams(lllp);

            TextView tv = view.findViewById(R.id.tv_item);
            String str = shortcut.title;
            ImageView multi = view.findViewById(R.id.multi);
            tv.setText(str);

            if (shortcut.multi) {
                multi.setVisibility(View.VISIBLE);
            }

            if (str.isEmpty()) {
                view.setBackgroundResource(R.drawable.shape_shortcut_empty_round_bg);
            } else {
                view.setBackgroundResource(R.drawable.shape_shortcut_round_bg);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (str.isEmpty())
                        return;

                    unSelectAll();
                    onAdapterClickListener.onClicked(position);
                    view.setBackgroundResource(R.drawable.shape_shortcut_select_round_bg);
                }
            });

            gridView.addView(view);
        }
    }

    public void unSelectAll() {
        for (int i = 0; i < shortcutList.size(); i++) {
            ShortcutItem shortcut = shortcutList.get(i);
            View view = gridView.getChildAt(i);

            if (shortcut.title.isEmpty()) {
                view.setBackgroundResource(R.drawable.shape_shortcut_empty_round_bg);
            } else {
                view.setBackgroundResource(R.drawable.shape_shortcut_round_bg);
            }
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        // ?????????????????? ??????.
        container.removeView((View) object);
    }

    @Override
    public int getCount() {

        int size = (shortcutList.size() / 20);
        return size;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);
    }

    @Override
    public int getItemPosition(@NonNull Object item) {
        return POSITION_NONE;
    }

    public ShortcutItem getShortcutItem(int position) {
        return shortcutList.get(position);
    }
}
