package com.eye3.golfpay.fmb_tab.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.model.order.RestaurantMenu;

import java.util.List;
import java.util.Objects;

public class RestaurantMenuAdapter extends RecyclerView.Adapter<RestaurantMenuAdapter.MenuItemViewHolder> {
    public String TAG = getClass().getSimpleName();
    private RestaurantMenu selectedMenu = null;

    class MenuItemViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_food_image;

        //onCreateViewHolder 의 mMenuView 임()
        MenuItemViewHolder(@NonNull final View itemView) {
            super(itemView);
            iv_food_image = itemView.findViewById(R.id.iv_food_image);
        }
    }

    Context mContext;
    public List<RestaurantMenu> mMenuList;
    public MenuItemViewHolder preSelectedViewHolder;

    public RestaurantMenuAdapter(Context context, List<RestaurantMenu> menuList) {
        Log.d(TAG, "  메뉴 사이즈   " + String.valueOf(menuList.size()));
        mContext = context;
        mMenuList = menuList;
    }

    @NonNull
    @Override
    // recyclerView 가 parent 임
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.restaurant_menu_row, parent, false);
        //    Log.d(TAG, "onCreateViewHolder    " + "MenuItemViewHolder");
        return new MenuItemViewHolder(view);
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
    public void onBindViewHolder(@NonNull final RestaurantMenuAdapter.MenuItemViewHolder holder, int position) {
        final int idx = position;
        Log.d(TAG, "onBindViewHolder    " + "메뉴명: " + mMenuList.get(idx).name + "MenuItemViewHolder");
        holder.itemView.setTag(mMenuList.get(position));
        if (mMenuList.get(idx).isSelected) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
            //holder.tvMenuName.setTextColor(getResources().getColor(R.color.black, Objects.requireNonNull(getActivity()).getTheme()));
            //holder.tvPrice.setTextColor(getResources().getColor(R.color.black, Objects.requireNonNull(getActivity()).getTheme()));
        } else {
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.lightAliceBlue, Objects.requireNonNull(mContext).getTheme()));
            //holder.tvMenuName.setTextColor(getResources().getColor(R.color.gray, Objects.requireNonNull(getActivity()).getTheme()));
            //holder.tvPrice.setTextColor(getResources().getColor(R.color.gray, Objects.requireNonNull(getActivity()).getTheme()));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            View preView = null;
            @Override
            public void onClick(View v) {

                if (mMenuList.get(idx).isSelected == true)
                    return;

                if (preSelectedViewHolder != null) {

                    preSelectedViewHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.lightAliceBlue, Objects.requireNonNull(mContext).getTheme()));
                }

                preSelectedViewHolder = holder;

                clearOrder();
                mMenuList.get(idx).isSelected = true;
                selectedMenu = mMenuList.get(idx);
                //resetGuestList();
                notifyDataSetChanged();
                //moveSmoothScroll(position);
                preView = v;
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MenuItemViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public int getItemCount() {
        return mMenuList.size();
    }

    public boolean haveOrder() {
        for (RestaurantMenu item : mMenuList) {
            if (item.isSelected == true)
                return true;
        }

        return false;
    }

    public void clearOrder() {
        for (RestaurantMenu item : mMenuList) {
            item.isSelected = false;
        }

        notifyDataSetChanged();
    }

    public RestaurantMenu getSelectedMenu() {
        return selectedMenu;
    }
}
