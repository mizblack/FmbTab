package com.eye3.golfpay.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.R;

import java.util.ArrayList;


public class HotkeyCategoryAdapter extends RecyclerView.Adapter<HotkeyCategoryAdapter.ClubHolder> {

    public interface IOnClickAdapter {
        void onAdapterItemClicked(int count);
    }

    public class ClubHolder extends RecyclerView.ViewHolder {

        public TextView tv_item;
        public View iv_bar;
        public ImageView iv_select;

        public ClubHolder(View itemView) {
            super(itemView);

            tv_item = itemView.findViewById(R.id.tv_item);
            iv_bar = itemView.findViewById(R.id.iv_bar);
            iv_select = itemView.findViewById(R.id.iv_select);
        }
    }

    public class Item {
        public Item(String id, String name) {
            categoryId = id;
            categoryName = name;
        }

        public String categoryId;
        public String categoryName;
        public boolean selected = false;
    }

    protected static final String TAG = "ClubAdapter";

    private final ArrayList<Item> items;
    private final Context context;
    private final IOnClickAdapter onClickAdapter;

    public HotkeyCategoryAdapter(Context context, IOnClickAdapter listener) {
        this.context = context;
        this.onClickAdapter = listener;

        items = new ArrayList<>();
    }

    public void addItem(String id, String name) {
        Item item = new Item(id, name);
        items.add(item);
    }

    public String getItem(int position) {
        return items.get(position).categoryName;
    }

    public void clearData() {
        items.clear();
    }

    @Override
    public ClubHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_restaurant_category_item, parent, false);
        ClubHolder rcv = new ClubHolder(view);

        return rcv;
    }

    @Override
    public void onBindViewHolder(ClubHolder holder, final int position) {

        try {

            Item item = items.get(position);

            holder.iv_bar.setVisibility(View.GONE);
            holder.iv_select.setVisibility(View.GONE);
            holder.tv_item.setTextAppearance(R.style.GlobalTextView_18SP_505258_NotoSans_Bold);
            holder.tv_item.setText(item.categoryName);

            if (item.selected == true) {
                holder.iv_bar.setVisibility(View.VISIBLE);
                holder.iv_select.setVisibility(View.VISIBLE);
                holder.tv_item.setTextAppearance(R.style.GlobalTextView_18SP_White_NotoSans_Bold);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
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

    private int getSelectedCount() {
        int count = 0;
        for (Item item : items) {
            if (item.selected == true)
                count++;
        }

        return count;
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

