package com.eye3.golfpay.fmb_tab.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.model.field.Hole;

public class HoleInfoLinear extends LinearLayout {
    TextView tvHoleId;
    TextView tvPar;
    TextView tvMeter;

//    public HoleInfoLinear(Context context) {
//        super(context);
//        init(context);
//    }

    public HoleInfoLinear(Context context, Hole hole) {
        super(context);
        init(context, hole);
    }

    public HoleInfoLinear(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    private void init(Context context, Hole hole) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.hole_info_linear, this, false);
        tvHoleId = v.findViewById(R.id.hole_id);
        tvHoleId.setText(hole.id);
        tvPar = v.findViewById(R.id.hole_par);
        tvPar.setText("Par" + hole.par);
        tvMeter = v.findViewById(R.id.hole_meter);
        tvMeter.setText(hole.hole_total_size);
        addView(v);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.hole_info_linear, this, false);

        addView(v);
        if (attrs != null) {
            //attrs.xml에 정의한 스타일을 가져온다
//            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyCustomView);
//            mSelected = a.getInteger(0, 0);
//            a.recycle(); // 이용이 끝났으면 recycle() 호출
        }

    }

}
