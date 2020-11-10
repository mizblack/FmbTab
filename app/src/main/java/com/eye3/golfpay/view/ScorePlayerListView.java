package com.eye3.golfpay.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.eye3.golfpay.R;
import com.eye3.golfpay.common.AppDef;
import com.eye3.golfpay.common.Global;

import java.util.ArrayList;

public class ScorePlayerListView extends RelativeLayout {
    Context mContext;
    LinearLayout mScoreInserterContainer;
    public ScorePlayerListView(Context context) {
        super(context);
    }
    int itemHeightDP = 92;
    @RequiresApi(api = Build.VERSION_CODES.M)
    public ScorePlayerListView(Context context, String type) {
        super(context);
        init(context, type);
    }

    public ScorePlayerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        addPlayerList(v);
        addView(v);
    }

    void addPlayerList(View view) {

        for (int i = 0; i < Global.guestList.size(); i++) {
            String name = Global.guestList.get(i).getGuestName();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            TextView player = (TextView)inflater.inflate(R.layout.item_player, null, false);
            final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDisplayMetrics());
            final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, itemHeightDP, getResources().getDisplayMetrics());
            player.setText(name);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            player.setLayoutParams(params);
            mScoreInserterContainer.addView(player);
        }
    }
}
