package com.eye3.golfpay.fmb_tab.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.adapter.ShortCutPagerAdapter;

public class ControlPanelView extends ConstraintLayout {


    public interface OnClickListener {
        public void onShortcutMessage(String msg);
        public void onShowShortcutDlg();
    }

    private ViewPager vp_shortcut;
    private ShortCutPagerAdapter shortCutPagerAdapter;
    private OnClickListener listener;
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

        shortCutPagerAdapter = new ShortCutPagerAdapter(context, new ShortCutPagerAdapter.OnAdapterClickListener() {
            @Override
            public void onClicked(int position) {
                ShortCutPagerAdapter.ShortcutItem item = shortCutPagerAdapter.getShortcutItem(position);
                if (item.multi == true)
                    listener.onShowShortcutDlg();
                else
                    listener.onShortcutMessage(item.descr);
            }
        });
        vp_shortcut.setAdapter(shortCutPagerAdapter);

        addView(v);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }
}
