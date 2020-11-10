package com.eye3.golfpay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.eye3.golfpay.R;
import com.eye3.golfpay.adapter.HotKeyPagerAdapter;
import com.eye3.golfpay.model.control.ChatHotKeyItem;

import java.util.ArrayList;

public class ControlPanelView extends ConstraintLayout {


    public interface OnClickListener {
        public void onShortcutMessage(String msg);
        public void onShowShortcutDlg(String option);
    }

    private ViewPager vp_shortcut;
    private HotKeyPagerAdapter hotKeyPagerAdapter;
    private OnClickListener listener;
    private TextView[] textViews = new TextView[8];
    private LinearLayout viewIndicator;
    private ImageView indicator1;
    private ImageView indicator2;

    public ControlPanelView(Context context) {
        super(context);
    }

    public ControlPanelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(final Context context) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_control_panel, this, false);
        vp_shortcut = v.findViewById(R.id.vp_shortcut);
        viewIndicator = v.findViewById(R.id.view_indicator);
        indicator1 = v.findViewById(R.id.iv_indicator_1);
        indicator2 = v.findViewById(R.id.iv_indicator_2);

        hotKeyPagerAdapter = new HotKeyPagerAdapter(context, new HotKeyPagerAdapter.OnAdapterClickListener() {
            @Override
            public void onClicked(int position) {
                HotKeyPagerAdapter.ShortcutItem item = hotKeyPagerAdapter.getShortcutItem(position);
                if (item.multi == true)
                    listener.onShowShortcutDlg(item.option);
                else
                    listener.onShortcutMessage(item.descr);
            }
        });

        textViews[0] = v.findViewById(R.id.first_start);
        textViews[1] = v.findViewById(R.id.first_end);
        textViews[2] = v.findViewById(R.id.second_start);
        textViews[3] = v.findViewById(R.id.second_end);
        textViews[4] = v.findViewById(R.id.club_lost);
        textViews[5] = v.findViewById(R.id.club_get);
        textViews[6] = v.findViewById(R.id.belongings_lost);
        textViews[7] = v.findViewById(R.id.belongings_get);

        for (TextView tv : textViews) {
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onShortcutMessage(v.getTag().toString());
                }
            });
        }

        addView(v);
    }

    public void createHotkeyHeader(ArrayList<ChatHotKeyItem> headers) {

        for (int i = 0; i < headers.size(); i++) {
            ChatHotKeyItem item = headers.get(i);
            textViews[i].setText(item.getTitle());
            textViews[i].setTag(item.getMessage());
        }
    }

    public void createHotKey(ArrayList<ChatHotKeyItem> hotKeyItems) {
        hotKeyPagerAdapter.addHotkey(hotKeyItems);
        vp_shortcut.setAdapter(hotKeyPagerAdapter);

        if (hotKeyPagerAdapter.getCount() == 1) {
            viewIndicator.setVisibility(GONE);
        }

        vp_shortcut.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0 :
                        indicator1.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shape_oval_active_point));
                        indicator2.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shape_oval_point));
                        break;
                    case 1:
                        indicator2.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shape_oval_active_point));
                        indicator1.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.shape_oval_point));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }
}
