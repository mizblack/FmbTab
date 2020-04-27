package com.eye3.golfpay.fmb_tab.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.AppDef;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.order.Menu;
import com.eye3.golfpay.fmb_tab.model.order.OrderDetail;
import com.eye3.golfpay.fmb_tab.model.order.OrderedMenuItem;
import com.eye3.golfpay.fmb_tab.model.order.PersonalOrder;
import com.eye3.golfpay.fmb_tab.model.order.ReceiptUnit;
import com.eye3.golfpay.fmb_tab.model.order.Restaurant;
import com.eye3.golfpay.fmb_tab.model.order.RestaurantOrder;
import com.eye3.golfpay.fmb_tab.model.order.StoreOrder;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.net.ResponseData;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailHistoryFragment extends BaseFragment {
    private static int NUM_OF_RESTAURANT;
    TextView mTvTheRestaurant;
    int mSelectedRestaurantTabIdx = 0;
    private TextView[] mRestaurantTabBarArr ;
    Restaurant mSelectedRestaurant;
    LinearLayout mTabLinearOrderDetail;
    private RecyclerView mOrderHistoryRecyclerView;
    private ArrayList<Restaurant> mRestaurantList = new ArrayList<>();
    //해당 아이디를 가진 전표만 리스트에 나옴
    String mSelectedRestaurantId = "";
    ArrayList<OrderDetail> mOrderDetailList = new ArrayList<>();
    List<RestaurantOrder>  mRestaurantOrderDetailList = new ArrayList<>();
     Button mBtnTopAdd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  Global.orderDetailList로 초기화할것
        Bundle bundle = getArguments();
        if (bundle != null) {
            mRestaurantList = (ArrayList<Restaurant>) bundle.getSerializable("restaurantList");
            mOrderDetailList = (ArrayList<OrderDetail>) bundle.getSerializable("orderdetailList");
            NUM_OF_RESTAURANT = mRestaurantList.size();
            mRestaurantTabBarArr = new TextView[NUM_OF_RESTAURANT];
        }

       // storeOrderArrayList
        getStoreOrder();
    }

    private void getStoreOrder() {
        showProgress("주문내역 정보를 가져오는 중입니다.");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getStoreOrder(getActivity(), Global.selectedReservation.getReserveNo(), new DataInterface.ResponseCallback<ResponseData<StoreOrder>>() {
            @Override
            public void onSuccess(ResponseData<StoreOrder> response) {
                hideProgress();
                systemUIHide();
                if (response.getResultCode().equals("ok")) {

                    AppDef.storeOrderArrayList = (ArrayList<StoreOrder>) response.getList();
                    initRecyclerView(0, AppDef.storeOrderArrayList);

                } else if (response.getResultCode().equals("fail")) {
                    Toast.makeText(getActivity(), response.getResultMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(ResponseData<StoreOrder> response) {
                hideProgress();
                systemUIHide();
                response.getError();
            }

            @Override
            public void onFailure(Throwable t) {
                hideProgress();
                systemUIHide();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_order_history, container, false);
        mOrderHistoryRecyclerView = v.findViewById(R.id.orderHistoryRecycleView);
        mTabLinearOrderDetail = v.findViewById(R.id.restaurantTabLinearLayout);
        mTvTheRestaurant = v.findViewById(R.id.tvTheRestaurantOrderHistory);
        mBtnTopAdd = v.findViewById(R.id.topAddButton);
        mBtnTopAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new OrderFragment(), null);
            }
        });

        init();
        createTabBar(mRestaurantTabBarArr, mRestaurantList);
        mParentActivity.hideMainBottomBar();
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
                tvRestTabBar[i].setTag(mRestaurantList.get(i));
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


        mTvTheRestaurant.setTextColor(Color.GRAY);
        for (int i = 0; mRestaurantTabBarArr.length  > i; i++) {
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
        initRecyclerView(selectedTabIdx, AppDef.storeOrderArrayList);
    }


    private void initSelectedRestaurantTabColor() {

        mRestaurantTabBarArr[0].setTextColor(Color.BLACK);
    }

    private void setSelectedRestaurant(int mSelectedRestaurantIdx) {
        //각 식당에 따른 전표 내역을 보여준다.
      mSelectedRestaurant =  (Restaurant) mRestaurantTabBarArr[mSelectedRestaurantIdx].getTag();
       mSelectedRestaurantId =  mSelectedRestaurant.id;
    }

    private void init() {
//        RestaurantOrder restaurantOrder = new RestaurantOrder();
//        restaurantOrder.setOrderDetailList(mOrderDetailList);
//        mRestaurantOrderDetailList.add(restaurantOrder);
        initRecyclerView(0, AppDef.storeOrderArrayList);
    }




    private void initRecyclerView(int mSelectedRestaurantTabIdx , List<StoreOrder> storeOrderArrayList) {

        OrderHistoryAdapter  historyAdapter = new OrderHistoryAdapter(getActivity(), storeOrderArrayList);
        mOrderHistoryRecyclerView.setAdapter(historyAdapter);
        mOrderHistoryRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mManager = new LinearLayoutManager(mContext);
        mOrderHistoryRecyclerView.setLayoutManager(mManager);
        historyAdapter.notifyDataSetChanged();
    }



    private class OrderHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context context;
 //       List<RestaurantOrder> restaurantOrderArrayList;
         List<StoreOrder> storeOrderArrayList;

//        public OrderHistoryAdapter(Context context, List<RestaurantOrder> restaurantOrderArrayList) {
//            this.context = context;
//            this.restaurantOrderArrayList = restaurantOrderArrayList;
//
//        }

        public OrderHistoryAdapter(Context context, List<StoreOrder> storeOrderArrayList) {
            this.context = context;
            this.storeOrderArrayList = storeOrderArrayList;

        }


        private int getTotalReceptUnit( List<PersonalOrder> personalOrderList){
            //4personal total
            int total_price = 0;
               for(int i =0; personalOrderList.size() > i ; i++){
                   total_price +=  Integer.valueOf(personalOrderList.get(i).total_price);
               }
               return total_price;
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
//           if(! mSelectedRestaurantId.equals(restaurantOrderArrayList.get(position).restaurant_id )){
//               return;
//            }
//            viewHolder.tvTotal.setText(storeOrderArrayList.get(position).wholeTotalAmount);
//            viewHolder.tvOrderComplete.setText(storeOrderArrayList.get(position).order_state);
            //주문번호 전시
            viewHolder.tvOderNum.setText(storeOrderArrayList.get(position).store_no);
//            viewHolder.btnExtraOrder.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    GoNativeScreen(new OrderFragment(), null);
//                }
//            });



            List<ReceiptUnit> tabletOrderList = storeOrderArrayList.get(position).tablet_order_list;
            for (int i = 0; tabletOrderList.size() > i; i++) {
                List<PersonalOrder> personalOrderList =  tabletOrderList.get(i).recept_list;

                LinearLayout receptUnitLinear= (LinearLayout) LayoutInflater.from(context).inflate(R.layout.recept_order_item, null, false);
                Button btnExtraOrderCancel = receptUnitLinear.findViewById(R.id.btn_extra_order_cancel);
                TextView total = receptUnitLinear.findViewById(R.id.tvTotal);

                total.setText("총계" +"   " + getTotalReceptUnit(personalOrderList));

                btnExtraOrderCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      //  storeOrderArrayList.get(position).order_state = "주문취소";
                        Toast.makeText(mContext, "주문이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                for(int j =0; personalOrderList.size() > j ;j++ ) {
                    View personalOrderDetailView = createPersonalDutchPayBillView2(personalOrderList.get(j));
                     LinearLayout linearPersonalOrderContainer = receptUnitLinear.findViewById(R.id.linear_personal_order_container);
                    linearPersonalOrderContainer.addView(personalOrderDetailView);
                }
                viewHolder.tabletOrderLinear.addView(receptUnitLinear);

            }


        }

        @Override
        public int getItemCount() {
            return storeOrderArrayList.size();
        }


        class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
            TextView tvTotal, tvOderNum, tvOrderComplete;
            LinearLayout tabletOrderLinear, posOrderLinear ;
            Button btnExtraOrder, btnExtraOrderCancel;

            OrderHistoryViewHolder(View view) {
                super(view);
                tvTotal = view.findViewById(R.id.tvTotal);
                tvOderNum = view.findViewById(R.id.tv_order_num);
                tvOrderComplete = view.findViewById(R.id.tv_order_complete);
               //recept unit저장장소
                tabletOrderLinear = view.findViewById(R.id.linear_recept_unit_container);
                posOrderLinear = view.findViewById(R.id.linear_pos_unit);
              //  btnExtraOrderCancel = view.findViewById(R.id.btn_extra_order_cancel);

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
            LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50);

            tvOrderItem.setLayoutParams(lllp);
            tvOrderItem.setPadding(8, 0, 0, 0);
            String str = a_item.name  + "     " + a_item.qty +"개";
            tvOrderItem.setText(str);
            memberOrderLinearLayout.addView(tvOrderItem);
        }
        return v;

    }

    private View createPersonalDutchPayBillView2(PersonalOrder personalOrder) {
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.history_personal_bill, null, false);

        TextView tvName = v.findViewById(R.id.memberNameTextView);
        TextView tvPrice = v.findViewById(R.id.memberPriceTextView);
        LinearLayout memberOrderLinearLayout = v.findViewById(R.id.personalOrderLinearLayout);

        tvName.setText(personalOrder.guest_name);
        for (int i = 0; personalOrder.menuList.size()  > i; i++) {
            Menu a_menu = personalOrder.menuList.get(i);
            TextView tvOrderItem = new TextView(getActivity(), null, 0, R.style.ItemOrderBillTextStyle);
            LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50);
            tvOrderItem.setLayoutParams(lllp);
            tvOrderItem.setPadding(8, 0, 0, 0);
            String str = a_menu.item_name + "       " + a_menu.qty +"개";
            tvOrderItem.setText(str);
            memberOrderLinearLayout.addView(tvOrderItem);
        }
        String strPrice = AppDef.priceMapper(Integer.valueOf(personalOrder.total_price));
        tvPrice.setText(strPrice + "원");
        return v;

    }


}

