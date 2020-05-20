package com.eye3.golfpay.fmb_tab.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.model.order.Restaurant;

import java.util.ArrayList;


public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ItemHolder> {

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
    ArrayList<Restaurant> mRestaurantList;

    public RestaurantListAdapter(Context context, IOnClickAdapter listener) {
        this.context = context;
        this.onClickAdapter = listener;
    }

    public void setData(ArrayList<Restaurant> items) {
        mRestaurantList = items;
    }

    public Restaurant getItem(int position) {
        return mRestaurantList.get(position);
    }

    public void clearData() {
        mRestaurantList.clear();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item, parent, false);
        ItemHolder rcv = new ItemHolder(view);

        return rcv;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {

        try {

            if (mRestaurantList.get(position).select == true) {
                holder.tv_item.setTextAppearance(context, R.style.GlobalTextView_22SP_Black_NotoSans_Bold);
            } else {
                holder.tv_item.setTextAppearance(context, R.style.GlobalTextView_22SP_zumthor_NotoSans_Regular);
            }

            holder.tv_item.setText(mRestaurantList.get(position).name);

            holder.tv_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allSelected(false);
                    mRestaurantList.get(position).select = true;
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
        for (Restaurant item : mRestaurantList) {
            item.select = select;
        }
    }

    @Override
    public int getItemCount() {
        return mRestaurantList.size();
    }
}

