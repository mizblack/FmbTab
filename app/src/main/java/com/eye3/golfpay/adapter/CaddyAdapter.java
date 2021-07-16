package com.eye3.golfpay.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.R;
import com.eye3.golfpay.model.teeup.Caddy;
import com.google.android.flexbox.FlexboxLayoutManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class CaddyAdapter extends RecyclerView.Adapter<CaddyAdapter.CaddyAdapterHolder> {

    public interface IAdapterClickListener {
        void onClickListener(int position);
    }
    private List<Caddy> caddyList;
    private int selectIdx;
    private Context context;
    private int startIndex;
    private IAdapterClickListener listener;

    @Override
    public CaddyAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_caddy_more, parent, false);
        CaddyAdapterHolder rcv = new CaddyAdapterHolder(view);

        return rcv;
    }

    public void setData(List<Caddy> list) {
        caddyList = list;
    }

    public int getItemCount() {

        return caddyList.size();
    }

    @SuppressLint({"SetTextI18n"})
    public void onBindViewHolder(@NotNull CaddyAdapterHolder holder, final int position) {
        if (selectIdx == position) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.tv_item.setTextAppearance(R.style.GlobalTextView_18SP_FF5353_NotoSans_Medium);
                holder.tv_item.setBackgroundResource(R.drawable.shape_tag_item_active_bg);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.tv_item.setTextAppearance(R.style.GlobalTextView_18SP_ebonyBlack_NotoSans_Reqular);
                holder.tv_item.setBackgroundResource(R.drawable.shape_tag_item_bg);
            }
        }

        FlexboxLayoutManager.LayoutParams flexboxLp = (FlexboxLayoutManager.LayoutParams)holder.itemView.getLayoutParams();
        flexboxLp.setFlexGrow(1.0f);

        holder.tv_item.setText(caddyList.get(position).name);
        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectIdx = position;
                notifyDataSetChanged();
                listener.onClickListener(position);
            }
        });
    }

    public CaddyAdapter(@Nullable Context context, int startIndex, @NotNull IAdapterClickListener listener) {
        super();
        this.context = context;
        this.startIndex = startIndex;
        this.listener = listener;
        this.selectIdx = this.startIndex;
    }

    public interface IOnClickAdapter {
        void onAdapterItemClicked(Integer id);
    }

    public class CaddyAdapterHolder extends RecyclerView.ViewHolder {

        public TextView tv_item;

        public CaddyAdapterHolder(View itemView) {
            super(itemView);

            tv_item = itemView.findViewById(R.id.tv_item);
        }
    }
}
