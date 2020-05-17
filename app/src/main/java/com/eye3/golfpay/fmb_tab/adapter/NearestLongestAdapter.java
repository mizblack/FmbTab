package com.eye3.golfpay.fmb_tab.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.fmb_tab.R;

import java.util.ArrayList;


public class NearestLongestAdapter extends RecyclerView.Adapter<NearestLongestAdapter.ItemHolder> {

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

    public class Item {
        Item(int n) {
            this.num = n;
        }

        public Integer num;
        public boolean selected = false;
    }

    protected static final String TAG = "LearnOnlineItemAdapter";

    private ArrayList<Item> items;
    private Context context;
    private IOnClickAdapter onClickAdapter;


    public NearestLongestAdapter(Context context, IOnClickAdapter listener) {
        this.context = context;
        this.onClickAdapter = listener;
        items = new ArrayList<>();

        for (int i = 10; i <= 50; i++) {
            Item item = new Item(i * 10);
            items.add(item);
        }
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

