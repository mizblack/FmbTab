package com.eye3.golfpay.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.R;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.model.guest.Guest;
import com.eye3.golfpay.model.order.Restaurant;

import java.util.ArrayList;
import java.util.List;


public class ClubGuestListAdapter extends RecyclerView.Adapter<ClubGuestListAdapter.ItemHolder> {

    public interface IOnClickAdapter {
        public void onAdapterItemClicked(Integer id);
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        public TextView tv_item;

        public ItemHolder(View itemView) {
            super(itemView);

            tv_item = itemView.findViewById(R.id.tv_item);
        }
    }

    protected static final String TAG = "RestaurantListAdapter";

    private Context context;
    private IOnClickAdapter onClickAdapter;
    public static List<Guest> guestList;

    public ClubGuestListAdapter(Context context, IOnClickAdapter listener) {
        this.context = context;
        this.onClickAdapter = listener;
    }

    public void setData(List<Guest> items) {
        guestList = items;
    }

    public void select(int position) {

        allSelected(false);
        guestList.get(position).selected = true;
        notifyDataSetChanged();
    }

    public Guest getItem(int position) {
        return guestList.get(position);
    }

    public void clearData() {
        guestList.clear();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guest_item, parent, false);
        ItemHolder rcv = new ItemHolder(view);

        return rcv;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {

        try {

            if (guestList.get(position).selected == true) {
                holder.tv_item.setTextAppearance(context, R.style.GlobalTextView_22SP_ebonyBlack_NotoSans_Regular);
            } else {
                holder.tv_item.setTextAppearance(context, R.style.GlobalTextView_22SP_zumthor_NotoSans_Regular);
            }

            holder.tv_item.setText(guestList.get(position).getGuestName());

            holder.tv_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allSelected(false);
                    guestList.get(position).selected = true;
                    onClickAdapter.onAdapterItemClicked(position);
                    notifyDataSetChanged();
                }
            });
        } catch (NullPointerException e) {
            Log.e(TAG, "NullPointerException : " + e);
        } catch (Exception e) {
            Log.e(TAG, "error : " + e);
        }
    }

    private void allSelected(boolean select) {
        for (Guest item : guestList) {
            item.selected = select;
        }
    }

    @Override
    public int getItemCount() {
        return guestList.size();
    }
}

