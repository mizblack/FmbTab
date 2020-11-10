package com.eye3.golfpay.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.eye3.golfpay.R;
import com.eye3.golfpay.common.Global;

public class TeeShotSpotView extends ConstraintLayout {

    Context mContext;
    int color;
    String text;
    String meter;

    public TeeShotSpotView(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public TeeShotSpotView(Context context, String type) {
        super(context);
        init(context);
    }

    public TeeShotSpotView(Context context, AttributeSet attrs) {
        super(context, attrs);

        color = context.obtainStyledAttributes(attrs, R.styleable.TeeShotSpotView).getColor(R.styleable.TeeShotSpotView_oval_color, 0);
        text = context.obtainStyledAttributes(attrs, R.styleable.TeeShotSpotView).getString(R.styleable.TeeShotSpotView_title);
        meter = context.obtainStyledAttributes(attrs, R.styleable.TeeShotSpotView).getString(R.styleable.TeeShotSpotView_meter);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void init(Context context) {
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.teeshot_spot_layout, this, false);

        ImageView ivOval = v.findViewById(R.id.iv_oval);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.shape_oval);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        ivOval.setBackground(drawable);

        TextView tvText = v.findViewById(R.id.tv_text);
        tvText.setText(text);
        TextView tvMeter = v.findViewById(R.id.tv_meter);
        tvMeter.setText(meter);

        addView(v);

    }
}
