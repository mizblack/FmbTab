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

import com.eye3.golfpay.dialog.ClubInfoDialog;
import com.eye3.golfpay.R;
import com.eye3.golfpay.model.order.Restaurant;


import java.util.ArrayList;


public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ClubHolder> {

    public interface IOnClickAdapter {
        public void onAdapterItemClicked(ClubInfoDialog.ClubType clubType, int count);
    }

    public class ClubHolder extends RecyclerView.ViewHolder {

        public TextView tv_item;

        public ClubHolder(View itemView) {
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

    protected static final String TAG = "ClubAdapter";

    private ArrayList<Item> items;
    private Context context;
    private IOnClickAdapter onClickAdapter;
    private ClubInfoDialog.ClubType clubType;
    boolean isMultiSelect;

    public ClubAdapter(Context context, ClubInfoDialog.ClubType clubType, boolean isMultiSelect, IOnClickAdapter listener) {
        this.context = context;
        this.onClickAdapter = listener;
        this.clubType = clubType;
        this.isMultiSelect = isMultiSelect;
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
    public ClubHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_club_item, parent, false);
        ClubHolder rcv = new ClubHolder(view);

        return rcv;
    }

    @Override
    public void onBindViewHolder(ClubHolder holder, final int position) {

        try {

            if (items.get(position).selected == true) {
                holder.tv_item.setBackgroundColor(ContextCompat.getColor(context, R.color.irisBlue));
                holder.tv_item.setTextAppearance(context, R.style.GlobalTextView_16SP_White_NotoSans_Medium);
            } else {
                holder.tv_item.setBackgroundColor(Color.WHITE);
                holder.tv_item.setTextAppearance(context, R.style.GlobalTextView_16SP_A3A5A7_NotoSans_Medium);
            }

            holder.tv_item.setText(items.get(position).item);

            holder.tv_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //allSelected(false);


                    if (isMultiSelect == true) {
                        items.get(position).selected ^= true;
                        onClickAdapter.onAdapterItemClicked(clubType, getSelectedCount());
                    } else {
                        allSelected(false);
                        items.get(position).selected = true;

                        //멀티 셀렉트가 안 되는 항목들은 선택한 인덱스가 갯수가 된다.
                        onClickAdapter.onAdapterItemClicked(clubType, getSelectedCoverCount());
                    }

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

    private int getSelectedCount() {
        int count = 0;
        for (Item item : items) {
            if (item.selected == true)
                count++;
        }

        return count;
    }

    public int getSelectedCoverCount() {
        for (Item item : items) {
            if (item.selected == true) {
                String a = item.item.substring(0, 1);
                return Integer.parseInt(a);
            }
        }

        return 0;
    }

    public ArrayList<String> getSelectedItems() {

        ArrayList<String> selectedItms = new ArrayList<>();
        for (Item item : items) {
            if (item.selected == true)
                selectedItms.add(item.item);
        }

        return selectedItms;
    }

    public void setSelectItem(ArrayList<String> selectItem) {
        for (Item item : items) {

            for (String str : selectItem) {
                if (item.item.equals(str))
                    item.selected = true;
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

