package com.eye3.golfpay.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eye3.golfpay.R;
import com.eye3.golfpay.common.AppDef;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.model.order.RestaurantMenu;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class RestaurantMenuAdapter extends RecyclerView.Adapter<RestaurantMenuAdapter.MenuItemViewHolder> {

    public interface IRestaurantMenuListener {
        void onMenuSelected(RestaurantMenu menu);
    }

    Context mContext;
    public String TAG = getClass().getSimpleName();
    private RestaurantMenu selectedMenu = null;
    private final List<RestaurantMenu> mMenuList;
    private final IRestaurantMenuListener iRestaurantMenuListener;

    static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_food_image;
        TextView tv_food_name;
        TextView tv_food_price;
        ConstraintLayout viewFoodInfo;


        //onCreateViewHolder 의 mMenuView 임()
        MenuItemViewHolder(@NonNull final View itemView) {
            super(itemView);
            iv_food_image = itemView.findViewById(R.id.iv_food_image);
            tv_food_name = itemView.findViewById(R.id.tv_food_name);
            tv_food_price = itemView.findViewById(R.id.tv_food_price);
            viewFoodInfo = itemView.findViewById(R.id.view_food_info);
        }
    }

    public RestaurantMenuAdapter(Context context, List<RestaurantMenu> menuList, IRestaurantMenuListener listener) {
        Log.d(TAG, "  메뉴 사이즈   " + menuList.size());
        mContext = context;
        mMenuList = menuList;
        this.iRestaurantMenuListener = listener;
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

        holder.tv_food_name.setText(mMenuList.get(position).name);
        String price = AppDef.priceMapper(Integer.parseInt(mMenuList.get(position).price));
        holder.tv_food_price.setText(price + "원");

        if (mMenuList.get(idx).isSelected) {
            holder.viewFoodInfo.setBackgroundColor(ContextCompat.getColor(mContext, R.color.irisBlue));
            holder.tv_food_name.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            holder.tv_food_price.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            //holder.tvPrice.setTextColor(getResources().getColor(R.color.black, Objects.requireNonNull(getActivity()).getTheme()));
        } else {
            holder.viewFoodInfo.setBackgroundColor(Color.parseColor("#ccffffff"));
            holder.tv_food_name.setTextColor(ContextCompat.getColor(mContext, R.color.gray));
            holder.tv_food_price.setTextColor(ContextCompat.getColor(mContext, R.color.gray));
        }

        String url = "";
        if (mMenuList.get(position).image != null)
            url = mMenuList.get(position).image.replace("public", "storage");

        Glide.with(mContext)
                .load(Global.HOST_BASE_ADDRESS_AWS + url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                //.placeholder(R.drawable.ic_noimage)
                .into(holder.iv_food_image);



        holder.itemView.setOnClickListener(new View.OnClickListener() {

            View preView = null;
            @Override
            public void onClick(View v) {

                if (mMenuList.get(idx).isSelected)
                    return;

                holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.lightAliceBlue, Objects.requireNonNull(mContext).getTheme()));

                clearOrder();
                mMenuList.get(idx).isSelected = true;
                selectedMenu = mMenuList.get(idx);
                //resetGuestList();
                notifyDataSetChanged();
                //moveSmoothScroll(position);
                preView = v;
                iRestaurantMenuListener.onMenuSelected(mMenuList.get(idx));
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
            if (item.isSelected)
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

    public List<RestaurantMenu> getRestaurantMenus() {
        return mMenuList;
    }
}
