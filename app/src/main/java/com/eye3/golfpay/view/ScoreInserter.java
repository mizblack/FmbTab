package com.eye3.golfpay.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Handler;
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
import com.eye3.golfpay.model.score.ScoreSend;

import java.util.ArrayList;

public class ScoreInserter extends RelativeLayout {

    public interface IScoreInserterListenr {
        void onClickedScore(Integer row, Integer cal);
        void onEraseScore(Integer row);
    }

    Context mContext;
    private boolean teeShot = false;
    private final String[] teeShotItem = {"Fairway", "Bunker", "Rough", "OB", "Hazard"};
    private int start;
    private int end;
    private String type;
    private LinearLayout mScoreInserterContainer;
    ArrayList<LinearLayout> scores = new ArrayList<>();
    int itemHeightDP = 92;
    public ScoreInserter(Context context) {
        super(context);
    }
    private IScoreInserterListenr iScoreInserterListenr;
    ArrayList<ScoreSend> oldScore = null;
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
        type = context.obtainStyledAttributes(attrs, R.styleable.ScoreInserter).getString(R.styleable.ScoreInserter_type);
        init(context, type);
    }

    public void setIScoreInserterListenr(IScoreInserterListenr listener) {
        this.iScoreInserterListenr = listener;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void init(Context context, String type) {
        this.mContext = context;

        if (Global.guestList.size() == 4)
            itemHeightDP = 112;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.inserter_layout, this, false);
        mScoreInserterContainer = v.findViewById(R.id.score_insert_container);
        addView(v);
    }

    public void makeScoreBoard(ArrayList<ScoreSend> score) {
        oldScore = score;

        if (teeShot)
            createTeeShot();
        else
            createScore(type);
    }

    void createScore(String type) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        boolean isVisible = false;
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

                ////////////////////////////////////////////////////
                //*이미 입력된 스코어 선택
                if (!isVisible) {
                    if (type.equals("par")) {
                        isVisible = selectPar(i, j, view);
                    } else if (type.equals("putt")) {
                        isVisible = selectPutt(i, j, view);
                    }
                }

                //시작값이 없거나 '-' 이거면 0으로 선택
                if ((oldScore.get(i).par.isEmpty() || oldScore.get(i).par.equals("-")) && j == 0 && type.equals("par")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (view.getVisibility() == View.GONE)
                                item.performClick();
                        }
                    }, 30);
                }
                //시작값이 없거나 '-' 이거면 0으로 선택
                if ((oldScore.get(i).putting.isEmpty() || oldScore.get(i).putting.equals("-")) && j == 0 && type.equals("putt")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (view.getVisibility() == View.GONE)
                                item.performClick();
                        }
                    }, 30);
                }

                final int cal = j;
                item.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int oldSelectIdx = unSelectAllRow(position);
                        int score = getScore(oldSelectIdx);
                        if (score != cal) {
                            view.setVisibility(View.VISIBLE);
                            iScoreInserterListenr.onClickedScore(position, cal);
                        } else {
                            iScoreInserterListenr.onEraseScore(position);
                        }
                    }
                });

                linearLayout.addView(item);
            }

            isVisible = false;

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

    private boolean selectPar(int index, int value, View view) {

        if (oldScore == null)
            return false;

        ScoreSend score = oldScore.get(index);
        if (score.par.isEmpty() || score.par.equals("-"))
            return false;

        if (Integer.parseInt(score.par) == value) {
            view.setVisibility(View.VISIBLE);
            return true;
        }

        view.setVisibility(View.GONE);
        return false;
    }

    private boolean selectPutt(int index, int value, View view) {

        if (oldScore == null)
            return false;

        ScoreSend score = oldScore.get(index);
        if (score.putting.isEmpty() || score.putting.equals("-"))
            return false;

        if (Integer.parseInt(score.putting) == value) {
            view.setVisibility(View.VISIBLE);
            return true;
        }

        view.setVisibility(View.GONE);
        return false;
    }

    private boolean selectTeeShot(int index, String teeShot, View view) {

        if (oldScore == null)
            return false;

        ScoreSend score = oldScore.get(index);
        if (score.teeShot.isEmpty() || score.teeShot.equals("-"))
            return false;

        if (score.teeShot.equalsIgnoreCase(teeShot)) {
            view.setVisibility(View.VISIBLE);
            return true;
        }

        view.setVisibility(View.GONE);
        return false;
    }

    public Integer getScore(int index) {
        for (int i = start, j = 0; i < end; i++, j++) {
            if (j == index)
                return i;
        }
        return -100;
    }

    public String getTeeShot(int index) {
        return teeShotItem[index];
    }

    void createTeeShot() {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        boolean isVisible = false;
        for (int i = 0; i < Global.guestList.size(); i++) {
            final int position = i;
            View childView = inflater.inflate(R.layout.inserter_item, null, false);
            LinearLayout linearLayout = childView.findViewById(R.id.view_item);

            int j = 0;
            for (String teeShot : teeShotItem) {
                FrameLayout item = (FrameLayout) inflater.inflate(R.layout.item_score, null, false);

                final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 134, getResources().getDisplayMetrics());
                final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, itemHeightDP, getResources().getDisplayMetrics());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                item.setLayoutParams(params);

                TextView tvItem = item.findViewById(R.id.tv_item);
                tvItem.setText(teeShot);
                View view = item.findViewById(R.id.view_select);

                ////////////////////////////////////////////////////
                //*이미 입력된 스코어 선택
                if (!isVisible) {
                    isVisible = selectTeeShot(i, teeShot, view);
                }

                final int cal = j;
                item.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int oldSelectIdx = unSelectAllRow(position);
                        if (oldSelectIdx != cal) {
                            view.setVisibility(View.VISIBLE);
                            iScoreInserterListenr.onClickedScore(position, cal);
                        } else {
                            iScoreInserterListenr.onEraseScore(position);
                        }
                    }
                });

                linearLayout.addView(item);
                j++;
            }

            isVisible = false;
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

    private int unSelectAllRow(final int position) {
        int oldSelectValue = -100;
        LinearLayout view = scores.get(position);
        for (int i = 0; i < view.getChildCount(); i++) {
            View childView = view.getChildAt(i);
            if (childView.findViewById(R.id.view_select).getVisibility() == View.VISIBLE) {
                oldSelectValue = i;
            }
            childView.findViewById(R.id.view_select).setVisibility(View.GONE);
        }

        return oldSelectValue;
    }
}
