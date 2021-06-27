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
import com.eye3.golfpay.common.AppDef;
import com.eye3.golfpay.common.Global;

public class TeeShotSpotView extends ConstraintLayout {

    Context mContext;
    int color;
    String mText;
    String mMeter;

    private TextView tvText;
    private TextView tvMeter;
    private ImageView ivOval;
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
        mText = context.obtainStyledAttributes(attrs, R.styleable.TeeShotSpotView).getString(R.styleable.TeeShotSpotView_title);
        mMeter = context.obtainStyledAttributes(attrs, R.styleable.TeeShotSpotView).getString(R.styleable.TeeShotSpotView_meter);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void init(Context context) {
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.teeshot_spot_layout, this, false);
        ivOval = v.findViewById(R.id.iv_oval);
        tvText = v.findViewById(R.id.tv_text);
        tvText.setText(mText);
        tvMeter = v.findViewById(R.id.tv_meter);
        tvMeter.setText(mMeter);

        addView(v);
    }

    public void setValue(String text, String meter, String colorText) {


        if (Global.isYard) {
            int nMeter = Integer.parseInt(meter);
            Integer yard = AppDef.MeterToYard(nMeter);
            tvMeter.setText(yard.toString());
        } else {
            tvMeter.setText(meter);
        }

        tvText.setText(text);

        try {
            colorText = "#" + colorText;
            color = Color.parseColor(colorText);
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.shape_oval);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            ivOval.setBackground(drawable);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
