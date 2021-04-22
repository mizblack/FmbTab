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

import com.eye3.golfpay.common.SingleClickListener;
import com.eye3.golfpay.dialog.ClubInfoDialog;
import com.eye3.golfpay.R;
import com.eye3.golfpay.model.caddyNote.CaddyNoteInfo;
import com.eye3.golfpay.model.order.Restaurant;


import java.util.ArrayList;


public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ClubHolder> {

    public interface IOnClickAdapter {
        void onAdapterItemClicked(ClubInfoDialog.ClubType clubType, int count, int coverCount);
    }

    public class ClubHolder extends RecyclerView.ViewHolder {

        public TextView tv_item;
        public TextView tv_cover;

        public ClubHolder(View itemView) {
            super(itemView);

            tv_item = itemView.findViewById(R.id.tv_item);
            tv_cover = itemView.findViewById(R.id.tv_cover);
        }
    }

    public static class Item {
        public Item(String s) {
            item = s;
        }

        public String item;
        public SelectType selected = SelectType.eNone;
        public enum SelectType {
            eNone,
            eSelect,
            eNoneCover
        }
    }

    protected static final String TAG = "ClubAdapter";

    private final ArrayList<Item> items;
    private final Context context;
    private final IOnClickAdapter onClickAdapter;
    private final ClubInfoDialog.ClubType clubType;
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

            if (items.get(position).selected == Item.SelectType.eSelect) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.irisBlue));
                holder.tv_item.setTextAppearance(R.style.GlobalTextView_16SP_White_NotoSans_Medium);
                holder.tv_cover.setVisibility(View.GONE);
            } else if (items.get(position).selected == Item.SelectType.eNoneCover){
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.irisBlue));
                holder.tv_item.setTextAppearance(R.style.GlobalTextView_16SP_White_NotoSans_Medium);
                holder.tv_cover.setVisibility(View.VISIBLE);
            } else {
                holder.itemView.setBackgroundColor(Color.WHITE);
                holder.tv_item.setTextAppearance(R.style.GlobalTextView_16SP_A3A5A7_NotoSans_Medium);
                holder.tv_cover.setVisibility(View.GONE);
            }

            holder.tv_item.setText(items.get(position).item);

            holder.itemView.setOnClickListener(new SingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    //allSelected(false);

                    if (items.get(position).selected == Item.SelectType.eSelect) {
                        items.get(position).selected = Item.SelectType.eNoneCover;
                        onClickAdapter.onAdapterItemClicked(clubType, getSelectedCount(), getSelectedCoverCount());
                    } else if (items.get(position).selected == Item.SelectType.eNoneCover){
                        items.get(position).selected = Item.SelectType.eNone;
                        onClickAdapter.onAdapterItemClicked(clubType, getSelectedCount(), getSelectedCoverCount());
                    } else {
                        items.get(position).selected = Item.SelectType.eSelect;
                        onClickAdapter.onAdapterItemClicked(clubType, getSelectedCount(), getSelectedCoverCount());
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

    private int getSelectedCount() {
        int count = 0;
        for (Item item : items) {
            if (item.selected == Item.SelectType.eSelect)
                count++;
            if (item.selected == Item.SelectType.eNoneCover)
                count++;
        }

        return count;
    }

    private int getSelectedCoverCount() {

        int count = 0;
        for (Item item : items) {
            if (item.selected == Item.SelectType.eSelect)
                count++;
        }

        return count;
    }

    public ArrayList<CaddyNoteInfo.ClubInfo> getSelectedItems() {

        ArrayList<CaddyNoteInfo.ClubInfo> selectedItems = new ArrayList<>();
        int idx = 0;
        for (Item item : items) {
            if (item.selected == Item.SelectType.eSelect) {
                CaddyNoteInfo.ClubInfo ci = new CaddyNoteInfo.ClubInfo();
                ci.club = Integer.toString(idx);
                ci.cover = true;
                selectedItems.add(ci);
            }
            else if (item.selected == Item.SelectType.eNoneCover) {
                CaddyNoteInfo.ClubInfo ci = new CaddyNoteInfo.ClubInfo();
                ci.club = Integer.toString(idx);
                ci.cover = false;
                selectedItems.add(ci);
            }

            idx++;
        }

        return selectedItems;
    }

    public void setSelectItem(ArrayList<CaddyNoteInfo.ClubInfo> selectItem) {

        for (CaddyNoteInfo.ClubInfo ci : selectItem) {
            if (ci.cover)
                items.get(Integer.parseInt(ci.club)).selected = Item.SelectType.eSelect;
            else
                items.get(Integer.parseInt(ci.club)).selected = Item.SelectType.eNoneCover;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

