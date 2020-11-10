package com.eye3.golfpay.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.eye3.golfpay.R;
import com.eye3.golfpay.model.teeup.TodayReserveList;
import com.eye3.golfpay.util.Util;
import com.eye3.golfpay.view.VisitorsGuestItem;

import java.util.ArrayList;

public class TeeUpViewPagerAdapter extends PagerAdapter {

    public interface OnAdapterClickListener {
        public void onClicked(int position);
    }

    Context mContext;
    ArrayList<TodayReserveList> todayReserveList;
    LinearLayout visitorsGuestItemLinearLayout;
    ImageView mIvMap;
    TextView startTextView;
    OnAdapterClickListener onAdapterClickListener;

    @SuppressLint("MissingPermission")
    public TeeUpViewPagerAdapter(Context context, ArrayList<TodayReserveList> items, OnAdapterClickListener listener) {
        mContext = context;
        todayReserveList = items;
        onAdapterClickListener = listener;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_tee_up, container, false);
        TextView teeUpTimeTextView, reservationGuestNameTextView;

        teeUpTimeTextView = view.findViewById(R.id.teeUpTimeTextView);
        reservationGuestNameTextView = view.findViewById(R.id.reservationPersonNameTextView);
        startTextView = view.findViewById(R.id.startTextView);
        startTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAdapterClickListener.onClicked(position);
            }
        });
        visitorsGuestItemLinearLayout = view.findViewById(R.id.visitorsGuestItemLinearLayout);
        //view.setBackgroundResource(R.drawable.shape_black_edge);
//                divider.setVisibility(View.VISIBLE);
        startTextView.setText("대기");
        startTextView.setTextColor(0xff00abc5);
        //      }
        teeUpTimeTextView.setText(todayReserveList.get(position).getInoutCourse() + " " + Util.timeMapper(todayReserveList.get(position).getTeeoff()));
        reservationGuestNameTextView.setText(todayReserveList.get(position).getGuestName());
        for (int i = 0; i < todayReserveList.get(position).getGuestData().size(); i++) {
            VisitorsGuestItem visitorsGuestItem = new VisitorsGuestItem(mContext);
            TextView memberNameTextView = visitorsGuestItem.findViewById(R.id.memberNameTextView);
            ImageView ivCheckin = visitorsGuestItem.findViewById(R.id.iv_checkin);
            memberNameTextView.setText(todayReserveList.get(position).getGuestData().get(i).getGuestName());
            if ("N".equals(todayReserveList.get(position).getGuestData().get(i).getCheckin())) {
                //내장객이 전원 입장을 안했을때
                //  ivCheckin.setImageAlpha(50);
                ivCheckin.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.check_out));
                startTextView.setEnabled(false);
            } else {
                ivCheckin.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.check_in));
            }

            visitorsGuestItemLinearLayout.addView(visitorsGuestItem);
        }

        if (areAllGuestsEnteredToPlay(position)) {
            startTextView.setEnabled(true);
            startTextView.setText("시작");
        }

        container.addView(view);
        return view;
    }

    public void setListMode(boolean listMode) {
        if (listMode == true) {
            startTextView.setEnabled(false);
        } else
            startTextView.setEnabled(true);
    }


    private boolean areAllGuestsEnteredToPlay(int position) {
        for (int i = 0; i < todayReserveList.get(position).getGuestData().size(); i++) {

            if ("N".equals(todayReserveList.get(position).getGuestData().get(i).getCheckin())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        // 뷰페이저에서 삭제.
        container.removeView((View) object);
    }

    @Override
    public int getCount() {

        return todayReserveList.size();
    }

    public TodayReserveList getItem(int position) {
        return todayReserveList.get(position);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);
    }

    @Override
    public int getItemPosition(@NonNull Object item) {
        return POSITION_NONE;
    }

}
