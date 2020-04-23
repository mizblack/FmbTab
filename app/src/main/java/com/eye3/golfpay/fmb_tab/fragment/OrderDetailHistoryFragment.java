package com.eye3.golfpay.fmb_tab.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.AppDef;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.order.Category;
import com.eye3.golfpay.fmb_tab.model.order.OrderDetail;
import com.eye3.golfpay.fmb_tab.model.order.OrderedMenuItem;
import com.eye3.golfpay.fmb_tab.model.order.Restaurant;
import com.eye3.golfpay.fmb_tab.model.order.RestaurantOrder;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailHistoryFragment extends BaseFragment {
    private static int NUM_OF_RESTAURANT;
    TextView mTvTheRestaurant;
    int mSelectedRestaurantTabIdx = 0;
    private TextView[] mRestaurantTabBarArr ;

    LinearLayout mTabLinearOrderDetail;
    private RecyclerView mOrderHistoryRecyclerView;
    private ArrayList<Restaurant> mRestaurantList = new ArrayList<>();
    //해당 아이디를 가진 전표만 리스트에 나옴
    String mSelectedRestaurantId = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  Global.orderDetailList로 초기화할것
        Bundle bundle = getArguments();
        if (bundle != null) {
            mRestaurantList = (ArrayList<Restaurant>) bundle.getSerializable("restaurantList");
            NUM_OF_RESTAURANT = mRestaurantList.size();
            mRestaurantTabBarArr = new TextView[NUM_OF_RESTAURANT];
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_order_history, container, false);
        mOrderHistoryRecyclerView = v.findViewById(R.id.orderHistoryRecycleView);
        mTabLinearOrderDetail = v.findViewById(R.id.tabLinearLayout);
        init();
        createTabBar(mRestaurantTabBarArr, mRestaurantList);
        mParentActivity.showMainBottomBar();
        return v;
    }

    //최상위 레스토랑 선택바
    private void createTabBar(final TextView[] tvRestTabBar, ArrayList<Restaurant> mRestaurantList) {

        boolean isTheRestaurant = false;
        for (int i = 0; mRestaurantList.size() > i; i++) {
            if (mRestaurantList.get(i).store_type.equals("대식당") && !isTheRestaurant) {
                mTvTheRestaurant.setText((mRestaurantList.get(i).name));
                mTvTheRestaurant.setTag(mRestaurantList.get(i));
                mTvTheRestaurant.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSelectedRestaurantTabIdx = -1;
                        selectRestaurant(-1);
                    }
                });
                isTheRestaurant = true;
                mRestaurantList.remove(i);
                i--;
            } else {
                final int idx = i;
                tvRestTabBar[i] = new TextView(new ContextThemeWrapper(getActivity(), R.style.ShadeTabTitleTextView), null, 0);
                tvRestTabBar[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                tvRestTabBar[i].setText(mRestaurantList.get(i).name);
                tvRestTabBar[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSelectedRestaurantTabIdx = idx;
                        selectRestaurant(idx);

                    }
                });
                mTabLinearOrderDetail.addView(tvRestTabBar[i]);
            }
        }
        initSelectedRestaurantTabColor();
    }

    //레스토랑바 선택시 보여주는 함수
    private void selectRestaurant(int selectedTabIdx) {
        init();

        mTvTheRestaurant.setTextColor(Color.GRAY);
        for (int i = 0; mRestaurantTabBarArr.length - 1 > i; i++) {
            TextView textView = mRestaurantTabBarArr[i];
            textView.setTextColor(Color.GRAY);
        }
        if (selectedTabIdx < 0) {
            mTvTheRestaurant.setTextColor(Color.BLACK);
            mTvTheRestaurant.setVisibility(View.VISIBLE);
        } else {
            mRestaurantTabBarArr[selectedTabIdx].setTextColor(Color.BLACK);
            mRestaurantTabBarArr[selectedTabIdx].setVisibility(View.VISIBLE);
        }
        setSelectedRestaurant(selectedTabIdx);
    }

    private void initSelectedRestaurantTabColor() {

        mRestaurantTabBarArr[0].setTextColor(Color.BLACK);
    }

    private void setSelectedRestaurant(int mSelectedRestaurantIdx) {
        //각 식당에 따른 전표 내역을 보여준다.
    Restaurant  mSelectedRestaurant =  (Restaurant)mRestaurantTabBarArr[mSelectedRestaurantIdx].getTag();
       mSelectedRestaurantId =  mSelectedRestaurant.id;
    }

    private void init() {
        initRecyclerView();
    }

    private void initRecyclerView() {

        OrderHistoryAdapter  historyAdapter = new OrderHistoryAdapter(getActivity(), Global.restaurantOrderArrayList);
        mOrderHistoryRecyclerView.setAdapter(historyAdapter);
        mOrderHistoryRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mManager = new LinearLayoutManager(mContext);
        mOrderHistoryRecyclerView.setLayoutManager(mManager);
        historyAdapter.notifyDataSetChanged();
    }


    private class OrderHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context context;
        ArrayList<RestaurantOrder> restaurantOrderArrayList;

        OrderHistoryAdapter(Context context, ArrayList<RestaurantOrder> restaurantOrderArrayList) {
            this.context = context;
            this.restaurantOrderArrayList = restaurantOrderArrayList;

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.history_order_item, parent, false);
            OrderHistoryViewHolder viewHolder = new OrderHistoryViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            OrderHistoryViewHolder viewHolder = (OrderHistoryViewHolder) holder;
           if(! mSelectedRestaurantId.equals(restaurantOrderArrayList.get(position).restaurant_id )){
               return;
            }
            viewHolder.tvTotal.setText(restaurantOrderArrayList.get(position).wholeTotalAmount);
            viewHolder.tvOrderComplete.setText(restaurantOrderArrayList.get(position).orderState);
            //주문번호 전시
            viewHolder.tvOderNum.setText(restaurantOrderArrayList.get(position).order_id);
            viewHolder.btnExtraOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoNativeScreen(new OrderFragment(), null);
                }
            });

            viewHolder.btnExtraOrderCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    restaurantOrderArrayList.get(position).orderState = "주문취소";
                }
            });

            List<OrderDetail> orderDetailList = restaurantOrderArrayList.get(position).mOrderDetailList;
            for (int i = 0; orderDetailList.size() > i; i++) {
                View personalOrderDetailView = createPersonalDutchPayBillView(orderDetailList.get(i));
                viewHolder.linearOrderDetail.addView(personalOrderDetailView);
            }
        }

        @Override
        public int getItemCount() {
            return restaurantOrderArrayList.size();
        }


        class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
            TextView tvTotal, tvOderNum, tvOrderComplete;
            LinearLayout linearOrderDetail;
            Button btnExtraOrder, btnExtraOrderCancel;

            OrderHistoryViewHolder(View view) {
                super(view);
                tvTotal = view.findViewById(R.id.tvTotal);
                tvOderNum = view.findViewById(R.id.tv_order_num);
                tvOrderComplete = view.findViewById(R.id.tv_order_complete);
                linearOrderDetail = view.findViewById(R.id.linear_order_detail);
                btnExtraOrder = view.findViewById(R.id.btn_extra_order);
                btnExtraOrderCancel = view.findViewById(R.id.btn_extra_order_cancel);

            }

        }
    }

    private View createPersonalDutchPayBillView(OrderDetail orderDetail) {
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.history_personal_bill, null, false);

        TextView tvName = v.findViewById(R.id.memberNameTextView);
        tvName.setText(OrderFragment.getGuestName(orderDetail.reserve_guest_id));
        TextView tvPrice = v.findViewById(R.id.memberPriceTextView);

        String strPrice = AppDef.priceMapper(Integer.valueOf(orderDetail.paid_total_amount));
        tvPrice.setText(strPrice + "원");
        LinearLayout memberOrderLinearLayout = v.findViewById(R.id.personalOrderLinearLayout);

        for (int i = 0; orderDetail.mOrderedMenuItemList.size() > i; i++) {
            OrderedMenuItem a_item = orderDetail.mOrderedMenuItemList.get(i);
            TextView tvOrderItem = new TextView(getActivity(), null, 0, R.style.ItemOrderBillTextStyle);
            LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(250, 50);
            lllp.gravity = Gravity.CENTER_VERTICAL;
            lllp.leftMargin = 10;
            tvOrderItem.setLayoutParams(lllp);
            tvOrderItem.setPadding(8, 0, 0, 0);
            String str = a_item.name + "x" + a_item.qty;
            tvOrderItem.setText(str);
            memberOrderLinearLayout.addView(tvOrderItem);
        }
        return v;

    }

}
