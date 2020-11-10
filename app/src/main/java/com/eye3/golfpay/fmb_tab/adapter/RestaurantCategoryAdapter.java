package com.eye3.golfpay.fmb_tab.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.model.order.Category;

import java.util.ArrayList;
import java.util.Objects;



public class RestaurantCategoryAdapter extends RecyclerView.Adapter<RestaurantCategoryAdapter.ViewHolder> {


    public interface IOnCategoryClickAdapter {
        void onApplyCategory(String ct1Id, String ct2Id);
    }

    public String TAG = getClass().getSimpleName();
    private Category selectedCategory = null;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;

        //onCreateViewHolder 의 mMenuView 임()
        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tv_menu_category);
        }
    }

    Context mContext;
    ArrayList<Category> categoryList;
    public ViewHolder preSelectedViewHolder;
    IOnCategoryClickAdapter iOnCategoryClickAdapter;

    public RestaurantCategoryAdapter(Context context, ArrayList<Category> categoryList, IOnCategoryClickAdapter listener) {
        mContext = context;
        this.categoryList = categoryList;
        iOnCategoryClickAdapter = listener;
    }

    @NonNull
    @Override
    // recyclerView 가 parent 임
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.restaurant_category_row, parent, false);
        //    Log.d(TAG, "onCreateViewHolder    " + "MenuItemViewHolder");
        return new ViewHolder(view);
    }

    private String priceMapper(int price) {
        String priceToString = "" + price;
        if (price >= 1000) {
            int length = priceToString.length();
            String string00 = priceToString.substring(0, priceToString.length() - 3);
            String string01 = priceToString.substring(priceToString.length() - 3, length);
            priceToString = string00 + "," + string01;
        }
        return priceToString;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final int idx = position;
        Log.d(TAG, "onBindViewHolder    " + "메뉴명: " + categoryList.get(idx).catergory1_name + "MenuItemViewHolder");
        holder.itemView.setTag(categoryList.get(position));
        if (categoryList.get(idx).isSelected) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.irisBlue));
            holder.tvCategoryName.setTextColor(mContext.getResources().getColor(R.color.white, Objects.requireNonNull(mContext).getTheme()));
        } else {
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.lightAliceBlue, Objects.requireNonNull(mContext).getTheme()));
            holder.tvCategoryName.setTextColor(mContext.getResources().getColor(R.color.gray, Objects.requireNonNull(mContext).getTheme()));
        }

        //              holder.tvMenuName.setText(mMenuList.get(idx).name + " " + mCategory2List.get(idx).catergory2_name);
        holder.tvCategoryName.setText(categoryList.get(idx).catergory1_name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            View preView = null;
            @Override
            public void onClick(View v) {

                if (categoryList.get(idx).isSelected == true)
                    return;

                if (preSelectedViewHolder != null) {

                    preSelectedViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.lightAliceBlue, Objects.requireNonNull(mContext).getTheme()));
                    preSelectedViewHolder.tvCategoryName.setTextColor(mContext.getResources().getColor(R.color.gray, Objects.requireNonNull(mContext).getTheme()));
                }

                preSelectedViewHolder = holder;

                clearOrder();
                categoryList.get(idx).isSelected = true;
                selectedCategory = categoryList.get(idx);
                notifyDataSetChanged();
                iOnCategoryClickAdapter.onApplyCategory(categoryList.get(idx).catergory1_name, categoryList.get(idx).catergory1_id);
                //moveSmoothScroll(position);
                preView = v;
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public boolean haveOrder() {
        for (Category item : categoryList) {
            if (item.isSelected == true)
                return true;
        }

        return false;
    }

    public void clearOrder() {
        for (Category item : categoryList) {
            item.isSelected = false;
        }

        notifyDataSetChanged();
    }

    public Category getSelectedMenu() {
        return selectedCategory;
    }
}

