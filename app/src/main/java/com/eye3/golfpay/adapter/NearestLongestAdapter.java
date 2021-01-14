package com.eye3.golfpay.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.R;

import java.util.ArrayList;


public class NearestLongestAdapter extends RecyclerView.Adapter<NearestLongestAdapter.ItemHolder> {


    public enum Unit {
        Meters,
        Centimeters
    }

    public interface IOnClickAdapter {
        void onAdapterItemClicked(Integer id);
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        public TextView tv_item;

        public ItemHolder(View itemView) {
            super(itemView);

            tv_item = itemView.findViewById(R.id.tv_item);
        }
    }

    public class Item {
        Item(int n) {
            this.num = n;
        }

        public Integer num;
        public boolean selected = false;
    }

    protected static final String TAG = "LearnOnlineItemAdapter";

    private final ArrayList<Item> items;
    private final Context context;
    private final IOnClickAdapter onClickAdapter;
    private final Unit distansceUnit;

    public NearestLongestAdapter(Context context, Unit unit, IOnClickAdapter listener) {
        this.context = context;
        this.onClickAdapter = listener;
        items = new ArrayList<>();
        distansceUnit = unit;
        if (unit == Unit.Meters) {
            for (int i = 15; i <= 35; i++) {
                Item item = new Item(i * 10);
                items.add(item);
            }
        } else {
            for (int i = 0; i <= 30; i++) {
                Item item = new Item(i);
                items.add(item);
            }
        }
    }

    public void setScore(float score) {

        if (score == -1)
            return;

        int position = 0;
        for (int i = 0; i < items.size(); i++) {

            position = i;
            Item item = items.get(i);
            if (item.num == score) {
                item.selected = true;
                break;
            }
        }

        if (onClickAdapter != null)
            onClickAdapter.onAdapterItemClicked(position);
    }

    public Integer getItem(int position) {
        return items.get(position).num;
    }

    public void clearData() {
        items.clear();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearest_longest_item, parent, false);
        ItemHolder rcv = new ItemHolder(view);

        return rcv;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {

        try {

            if (items.get(position).selected == true) {
                holder.tv_item.setTextAppearance(context, R.style.GlobalTextView_24SP_Black_NotoSans_Bold);
            } else {
                holder.tv_item.setTextAppearance(context, R.style.GlobalTextView_22SP_ebonyBlack_NotoSans_Regular);
            }

            holder.tv_item.setText(items.get(position).num.toString());

            holder.tv_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allSelected(false);
                    items.get(position).selected = true;
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
        for (Item item : items) {
            item.selected = select;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

