package com.eye3.golfpay.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.eye3.golfpay.R;
import com.eye3.golfpay.common.AppDef;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.fragment.ControlFragment;

import java.util.ArrayList;

public class ScoreInserter extends RelativeLayout {

    Context mContext;
    private boolean teeShot = false;
    private String[] teeShotItem = {"Fairway", "Bunker"};
    private int start;
    private int end;
    private LinearLayout mScoreInserterContainer;
    ArrayList<LinearLayout> scores = new ArrayList<>();
    int itemHeightDP = 92;
    public ScoreInserter(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public ScoreInserter(Context context, String type) {
        super(context);
        init(context, type);
    }

    public ScoreInserter(Context context, AttributeSet attrs) {
        super(context, attrs);

        teeShot = context.obtainStyledAttributes(attrs, R.styleable.ScoreInserter).getBoolean(R.styleable.ScoreInserter_teeshot, false);
        start = context.obtainStyledAttributes(attrs, R.styleable.ScoreInserter).getInteger(R.styleable.ScoreInserter_start, 0);
        end = context.obtainStyledAttributes(attrs, R.styleable.ScoreInserter).getInteger(R.styleable.ScoreInserter_end, 10);
        init(context, "type");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void init(Context context, String type) {
        this.mContext = context;

        if (Global.guestList.size() == 4)
            itemHeightDP = 112;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.inserter_layout, this, false);
        mScoreInserterContainer = v.findViewById(R.id.score_insert_container);

        if (teeShot)
            createTeeShot();
        else
            createScore();

        addView(v);

    }

    void createScore() {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < Global.guestList.size(); i++) {
            final int position = i;
            View childView = inflater.inflate(R.layout.inserter_item, null, false);
            LinearLayout linearLayout = childView.findViewById(R.id.view_item);

            for (int j = start; j < end; j++) {
                FrameLayout item = (FrameLayout) inflater.inflate(R.layout.item_score, null, false);

                final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, getResources().getDisplayMetrics());
                final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, itemHeightDP, getResources().getDisplayMetrics());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                item.setLayoutParams(params);

                TextView tvItem = item.findViewById(R.id.tv_item);
                tvItem.setText(j + "");

                View view = item.findViewById(R.id.view_select);

                item.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        unSelectAllRow(position);
                        view.setVisibility(View.VISIBLE);
                    }
                });

                linearLayout.addView(item);
            }

            scores.add(linearLayout);

            View separator = inflater.inflate(R.layout.item_separator, null, false);
            final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, RelativeLayout.LayoutParams.MATCH_PARENT, getResources().getDisplayMetrics());
            final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            separator.setLayoutParams(params);
            mScoreInserterContainer.addView(separator);

            mScoreInserterContainer.addView(linearLayout);
        }
    }

    void createTeeShot() {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < Global.guestList.size(); i++) {
            final int position = i;
            View childView = inflater.inflate(R.layout.inserter_item, null, false);
            LinearLayout linearLayout = childView.findViewById(R.id.view_item);

            for (int j = 0; j < 2; j++) {
                FrameLayout item = (FrameLayout) inflater.inflate(R.layout.item_score, null, false);

                final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 134, getResources().getDisplayMetrics());
                final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, itemHeightDP, getResources().getDisplayMetrics());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                item.setLayoutParams(params);

                TextView tvItem = item.findViewById(R.id.tv_item);
                tvItem.setText(teeShotItem[j]);

                View view = item.findViewById(R.id.view_select);

                item.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        unSelectAllRow(position);
                        view.setVisibility(View.VISIBLE);
                    }
                });

                linearLayout.addView(item);
            }

            scores.add(linearLayout);

            View separator = inflater.inflate(R.layout.item_separator, null, false);
            final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, RelativeLayout.LayoutParams.MATCH_PARENT, getResources().getDisplayMetrics());
            final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            separator.setLayoutParams(params);
            mScoreInserterContainer.addView(separator);
            mScoreInserterContainer.addView(linearLayout);
        }
    }

    private void unSelectAllRow(final int position) {
        LinearLayout view = scores.get(position);
        for (int i = 0; i < view.getChildCount(); i++) {
            View childView = view.getChildAt(i);
            childView.findViewById(R.id.view_select).setVisibility(View.GONE);
        }
    }

}
