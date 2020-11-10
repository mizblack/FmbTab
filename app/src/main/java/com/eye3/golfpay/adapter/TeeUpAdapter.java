package com.eye3.golfpay.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.R;
import com.eye3.golfpay.model.teeup.GuestDatum;
import com.eye3.golfpay.model.teeup.TodayReserveList;
import com.eye3.golfpay.view.VisitorsGuestItem;

import java.util.ArrayList;


public class TeeUpAdapter extends RecyclerView.Adapter<TeeUpAdapter.ItemHolder> {

    public interface IOnClickAdapter {
        public void onAdapterItemClicked(Integer id);
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        TextView tv_tee_up_time;
        TextView tv_name;
        TextView tv_course;
        TextView tv_group;
        LinearLayout visitorsGuestItemLinearLayout;
        View viewMask;
        public ItemHolder(View itemView) {
            super(itemView);

            tv_tee_up_time = itemView.findViewById(R.id.tv_tee_up_time);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_course = itemView.findViewById(R.id.tv_course);
            tv_group = itemView.findViewById(R.id.tv_group);
            visitorsGuestItemLinearLayout = itemView.findViewById(R.id.visitorsGuestItemLinearLayout);
            viewMask = itemView.findViewById(R.id.view_mask);
        }
    }

    protected static final String TAG = "RestaurantListAdapter";

    private Context context;
    private IOnClickAdapter onClickAdapter;

    ArrayList<TodayReserveList> todayReserveList;

    public TeeUpAdapter(Context context, IOnClickAdapter listener) {
        this.context = context;
        this.onClickAdapter = listener;
    }

    public void setData(ArrayList<TodayReserveList> items) {
        todayReserveList = items;
    }


    public TodayReserveList getItem(int position) {
        return todayReserveList.get(position);
    }

    public void clearData() {
        todayReserveList.clear();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teeup_item, parent, false);
        ItemHolder rcv = new ItemHolder(view);

        return rcv;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {

        String teeUpTime = todayReserveList.get(position).getTeeoff();
        String guestName = todayReserveList.get(position).getGuestName();
        String course = todayReserveList.get(position).getInoutCourse() + "코스";
        String group = todayReserveList.get(position).getGroup();

        holder.tv_tee_up_time.setText(teeUpTime);
        holder.tv_course.setText(course);
        holder.tv_name.setText(guestName);
        if (group != null)
            holder.tv_group.setText(group);

        try {
            for (int i = 0; i < todayReserveList.get(position).getGuestData().size(); i++) {
                VisitorsGuestItem visitorsGuestItem = new VisitorsGuestItem(context);
                TextView memberNameTextView = visitorsGuestItem.findViewById(R.id.memberNameTextView);
                ImageView ivCheckin = visitorsGuestItem.findViewById(R.id.iv_checkin);
                memberNameTextView.setText(todayReserveList.get(position).getGuestData().get(i).getGuestName());
                if ("N".equals(todayReserveList.get(position).getGuestData().get(i).getCheckin())) {
                    ivCheckin.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.check_out));
                } else {
                    ivCheckin.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.check_in));
                }
                holder.visitorsGuestItemLinearLayout.addView(visitorsGuestItem);
            }

            if (position == 0) {
                holder.viewMask.setVisibility(View.VISIBLE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickAdapter.onAdapterItemClicked(position);
                }
            });

        } catch (NullPointerException e) {
            Log.e(TAG, "NullPointerException : " + e);
        } catch (Exception e) {
            Log.e(TAG, "error : " + e);
        }
    }

    @Override
    public int getItemCount() {
        return todayReserveList.size();
    }
}

