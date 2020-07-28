package com.eye3.golfpay.fmb_tab.adapter;

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

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.util.Util;

import java.util.ArrayList;

public class ShortCutPagerAdapter extends PagerAdapter {


    public class ShortcutItem {

        public ShortcutItem(String title, String descr, boolean multi) {
            this.title = title; this.descr = descr; this.multi = multi;
        }
        public String title;
        public String descr;
        public boolean multi;
    }

    public interface OnAdapterClickListener {
        public void onClicked(int position);
    }

    Context mContext;
    private ArrayList<ShortcutItem> shortcutList = new ArrayList<>();
    OnAdapterClickListener onAdapterClickListener;

    @SuppressLint("MissingPermission")
    public ShortCutPagerAdapter(Context context, OnAdapterClickListener listener) {
        mContext = context;
        onAdapterClickListener = listener;

        shortcutList.add(new ShortcutItem("9홀추가", "9홀추가", false));
        shortcutList.add(new ShortcutItem("경기중단", "경기중단", false));
        shortcutList.add(new ShortcutItem("앞팀진행", "앞팀진행", false));
        shortcutList.add(new ShortcutItem("티샷완료", "티샷완료", false));
        shortcutList.add(new ShortcutItem("세컨완료", "세컨완료", false));
        shortcutList.add(new ShortcutItem("홀아웃", "홀아웃", true));
        shortcutList.add(new ShortcutItem("파3싸인", "파3싸인", false));
        shortcutList.add(new ShortcutItem("홀인원", "홀인원", false));
        shortcutList.add(new ShortcutItem("이글", "이글", false));
        shortcutList.add(new ShortcutItem("뒷팀진행", "뒷팀진행", false));
        shortcutList.add(new ShortcutItem("환자발생", "환자발생", true));
        shortcutList.add(new ShortcutItem("물품요청", "물품요청", true));
        shortcutList.add(new ShortcutItem("", "", false));
        shortcutList.add(new ShortcutItem("", "", false));
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_shortcut_view, container, false);


        GridLayout gridView = view.findViewById(R.id.grid_view);
        container.addView(view);
        createItem(gridView);
        return view;
    }

    private void createItem(GridLayout gridView) {

        for (int i = 0; i < shortcutList.size(); i++) {

            final int position = i;
            ShortcutItem shortcut = shortcutList.get(i);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_shortcut, null, false);

            LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams((int) Util.convertDpToPixel(89.5f, mContext), (int)Util.convertDpToPixel(60.f, mContext));
            lllp.gravity = Gravity.CENTER_VERTICAL;
            lllp.rightMargin = (int)Util.convertDpToPixel(6.f, mContext);
            lllp.bottomMargin = (int)Util.convertDpToPixel(6.f, mContext);
            view.setLayoutParams(lllp);

            TextView tv = view.findViewById(R.id.tv_item);
            String str = shortcut.title;
            ImageView multi = view.findViewById(R.id.multi);
            tv.setText(str);

            if (shortcut.multi == true) {
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
                    onAdapterClickListener.onClicked(position);
                }
            });

            gridView.addView(view);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        // 뷰페이저에서 삭제.
        container.removeView((View) object);
    }

    @Override
    public int getCount() {

        return 2;
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
