package com.eye3.golfpay.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.R;
import com.eye3.golfpay.common.SingleClickListener;
import com.eye3.golfpay.dialog.ClubInfoDialog;
import com.eye3.golfpay.model.guest.Guest;

import java.util.ArrayList;


public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScorebHolder> {

    public interface IOnClickAdapter {
        void onAdapterItemClicked(Integer position, Integer value);
    }

    public class ScorebHolder extends RecyclerView.ViewHolder {

        public TextView tv_item;

        public ScorebHolder(View itemView) {
            super(itemView);

            tv_item = itemView.findViewById(R.id.tv_item);
        }
    }

    public class Item {
        public Item(String s) {
            item = s;
        }

        public String item;
        public boolean selected = false;
    }

    protected static final String TAG = "ScoreAdapter";

    private final ArrayList<Item> items;
    private final Context context;
    private final IOnClickAdapter onClickAdapter;

    public ScoreAdapter(Context context, IOnClickAdapter listener) {
        this.context = context;
        this.onClickAdapter = listener;
        items = new ArrayList<>();
    }

    public void addItem(String s) {
        Item item = new Item(s);
        items.add(item);
    }

    public String getItem(int position) {
        return items.get(position).item;
    }

    public void clearData() {
        items.clear();
    }

    @Override
    public ScorebHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_score_item, parent, false);
        ScorebHolder rcv = new ScorebHolder(view);

        return rcv;
    }

    @Override
    public void onBindViewHolder(ScorebHolder holder, final int position) {

        try {

            if (items.get(position).selected == true) {
                holder.tv_item.setBackgroundColor(ContextCompat.getColor(context, R.color.irisBlue));
                holder.tv_item.setTextAppearance(context, R.style.GlobalTextView_16SP_White_NotoSans_Medium);
            } else {
                holder.tv_item.setBackgroundColor(Color.WHITE);
                holder.tv_item.setTextAppearance(context, R.style.GlobalTextView_16SP_A3A5A7_NotoSans_Medium);
            }

            holder.tv_item.setText(items.get(position).item);

            holder.tv_item.setOnClickListener(new SingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    //allSelected(false);
                    allSelected(false);
                    items.get(position).selected = true;
                    onClickAdapter.onAdapterItemClicked(position, Integer.parseInt(items.get(position).item));
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

    public void select(String value) {
        for (Item item : items) {
            if (item.item.equals(value)) {
                item.selected = true;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

