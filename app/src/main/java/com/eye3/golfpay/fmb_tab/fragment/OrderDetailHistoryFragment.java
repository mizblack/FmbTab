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
import com.eye3.golfpay.fmb_tab.model.order.PersonalOrder;
import com.eye3.golfpay.fmb_tab.model.order.PosPersonalOrder;
import com.eye3.golfpay.fmb_tab.model.order.ReceiptUnit;
import com.eye3.golfpay.fmb_tab.model.order.Restaurant;
import com.eye3.golfpay.fmb_tab.model.order.StoreOrder;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.net.ResponseData;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailHistoryFragment extends BaseFragment {
    private static int NUM_OF_RESTAURANT;
    TextView mTvTheRestaurant;
    int mSelectedRestaurantTabIdx = 0;
    private TextView[] mRestaurantTabBarArr;
    Restaurant mSelectedRestaurant;
    LinearLayout mTabLinearOrderDetail;
    private RecyclerView mOrderHistoryRecyclerView;
    private ArrayList<Restaurant> mRestaurantList = new ArrayList<>();
    //해당 아이디를 가진 전표만 리스트에 나옴
    String mSelectedRestaurantId = "";
    Button mBtnTopAdd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        if (bundle != null) {
            mRestaurantList = (ArrayList<Restaurant>) bundle.getSerializable("restaurantList");
            NUM_OF_RESTAURANT = mRestaurantList.size();
            mRestaurantTabBarArr = new TextView[NUM_OF_RESTAURANT];
        }
        getStoreOrder();
    }

    private StoreOrder findStoreOrderByRestaurantName(List<StoreOrder> storeOrderList, String targetRestaurantName) {
        for (int i = 0; storeOrderList.size() > i; i++) {
            if (storeOrderList.get(i).store_name.equals(targetRestaurantName))
                return storeOrderList.get(i);
        }
        return null;
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
                    //    mTheRestaurantStoreOrder =   findStoreOrderByRestaurantName(AppDef.storeOrderArrayList, "대식당");
                    //여기서 실행해야함.
                    createTabBar(mRestaurantTabBarArr, mRestaurantList);
                    setTagTheRestaurant();
                    selectRestaurant(0);

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

    private void setTagTheRestaurant() {
        mTvTheRestaurant.setTag(findStoreOrderByRestaurantName(AppDef.storeOrderArrayList, "대식당"));
        mTvTheRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedRestaurantTabIdx = -1;
                selectRestaurant(-1);
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
        mParentActivity.hideMainBottomBar();
        return v;
    }

    //최상위 레스토랑 선택바
    private void createTabBar(final TextView[] tvRestTabBar, ArrayList<Restaurant> mRestaurantList) {

        boolean isTheRestaurant = false;
        for (int i = 0; mRestaurantList.size() > i; i++) {
            if (mRestaurantList.get(i).store_type.equals("대식당") && !isTheRestaurant) {
                mTvTheRestaurant.setText((mRestaurantList.get(i).name));
                mTvTheRestaurant.setTag(findStoreOrderByRestaurantName(AppDef.storeOrderArrayList, mRestaurantList.get(i).name));
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
                tvRestTabBar[i].setTag(findStoreOrderByRestaurantName(AppDef.storeOrderArrayList, mRestaurantList.get(i).name));
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
        for (int i = 0; mRestaurantTabBarArr.length > i; i++) {
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
        //retaurantlist와 storeArraylist의 식당 인덱스가 대식당으로 일치하지 않다
        if (selectedTabIdx >= 0) {
            StoreOrder storeOrder = (StoreOrder) mRestaurantTabBarArr[selectedTabIdx].getTag();
            initRecyclerView(storeOrder);
        }
        else {   //selectedTabIdx == -1 대식당일경우
            StoreOrder storeOrder = (StoreOrder) mTvTheRestaurant.getTag();
            initRecyclerView(storeOrder);
        }

    }


    private void initSelectedRestaurantTabColor() {

        mRestaurantTabBarArr[0].setTextColor(Color.BLACK);
    }

    private void setSelectedRestaurant(int mSelectedRestaurantIdx) {
        //각 식당에 따른 전표 내역을 보여준다.
        mSelectedRestaurant = (Restaurant) mRestaurantTabBarArr[mSelectedRestaurantIdx].getTag();
        mSelectedRestaurantId = mSelectedRestaurant.id;
    }


    private void initRecyclerView( StoreOrder selectedStoreOrder) {
        if (selectedStoreOrder.tablet_order_list.size() > 0) {

            //*****************************
            LinearLayoutManager mManager = new LinearLayoutManager(mContext);
            mOrderHistoryRecyclerView.setHasFixedSize(true);
            mOrderHistoryRecyclerView.setLayoutManager(mManager);

            OrderHistoryAdapter historyAdapter = new OrderHistoryAdapter(getActivity(), selectedStoreOrder);
            mOrderHistoryRecyclerView.setAdapter(historyAdapter);
            historyAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getActivity(), "주문내역이 없습니다.", Toast.LENGTH_SHORT).show();
             mOrderHistoryRecyclerView.setAdapter(null);
        }
    }


    private class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {

        Context context;
        StoreOrder storeOrder;

        public OrderHistoryAdapter(Context context, StoreOrder storeOrder) {
            this.context = context;
            this.storeOrder = storeOrder;

        }

        @NonNull
        @Override
        public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.history_order_item, parent, false);
            return new OrderHistoryAdapter.OrderHistoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderHistoryViewHolder viewHolder, int position) {
           //   OrderHistoryViewHolder viewHolder = (OrderHistoryViewHolder) viewHolder;

            if (position < storeOrder.tablet_order_list.size()) {

                for (int i = 0; storeOrder.tablet_order_list.size() > i; i++) {

                    ReceptUnitAdapter receptUnitAdapter = new ReceptUnitAdapter(mContext, (List<ReceiptUnit>) storeOrder.tablet_order_list);
                    viewHolder.receptunitRecyclerView.setAdapter(receptUnitAdapter);
                    viewHolder.receptunitRecyclerView.setHasFixedSize(true);
                    LinearLayoutManager mManager = new LinearLayoutManager(mContext);
                    viewHolder.receptunitRecyclerView.setLayoutManager(mManager);
                    viewHolder.itemView.setTag(position);
                }

            }
            //*****************************************************************************************************************************
            LinearLayout receptUnitLinear = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.pos_order_item_linear, null, false);
            LinearLayout linearPersonalOrderContainer = null;
            for (int j = 0; storeOrder.pos_order_list.size() > j; j++) {

                View personalOrderDetailView = createPosPersonalPayBillView(storeOrder.pos_order_list.get(j));
                linearPersonalOrderContainer = receptUnitLinear.findViewById(R.id.linear_pos__personal_order_container);
                linearPersonalOrderContainer.addView(personalOrderDetailView);
            }
           // if (linearPersonalOrderContainer != null)
              //  viewHolder.tabletOrderLinear.addView(linearPersonalOrderContainer);

        }

        @Override
        public int getItemCount() {
//            if (storeOrder == null || storeOrder.tablet_order_list == null)
//                return 0;
//            else
                return storeOrder.tablet_order_list.size();
        }


        class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
         //  LinearLayout tabletOrderLinear, posOrderLinear;
            RecyclerView receptunitRecyclerView;
            OrderHistoryViewHolder(View view) {
                super(view);
                //recept unit저장장소
              //  tabletOrderLinear = view.findViewById(R.id.linear_recept_unit_container);
                receptunitRecyclerView = view.findViewById(R.id.recept_unit_recyclerView);
                //  posOrderLinear = view.findViewById(R.id.linear_pos_unit);
                //  btnExtraOrderCancel = view.findViewById(R.id.btn_extra_order_cancel);

            }

        }
    }

    private int getTotalReceptUnit(List<PersonalOrder> personalOrderList) {
        //4personal total
        int total_price = 0;
        for (int i = 0; personalOrderList.size() > i; i++) {
            total_price += Integer.valueOf(personalOrderList.get(i).total_price);
        }
        return total_price;
    }

    public class ReceptUnitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context mContext;
        List<ReceiptUnit> mReceiptUnitOrderList;

        public ReceptUnitAdapter(Context context, List<ReceiptUnit> mReceiptUnitOrderList) {
            mContext = context;
            this.mReceiptUnitOrderList = mReceiptUnitOrderList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            //  RecyclerView.ViewHolder viewHolder = null;
            view = LayoutInflater.from(mContext).inflate(R.layout.recept_order_item, parent, false);
            return new ReceptUnitViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            ReceptUnitViewHolder viewHolder = (ReceptUnitViewHolder) holder;
            viewHolder.total.setText("총계" + "   " + AppDef.priceMapper(getTotalReceptUnit(mReceiptUnitOrderList.get(position).recept_list)));
            viewHolder.tvTime.setText(mReceiptUnitOrderList.get(position).order_time);
            //게스트중 한명만이라도 주문완료면 주문완료로 표시
            viewHolder.tvStatus.setText(mReceiptUnitOrderList.get(position).recept_list.get(0).order_status);

            ReceiptUnit receiptUnit =  mReceiptUnitOrderList.get(position);
            for(int i=0; receiptUnit.recept_list.size() >i ;i++) {
                PersonalOrder personalReceiptsOrder = receiptUnit.recept_list.get(i);
                viewHolder.linearPersonalOrderContainer.addView(createPersonalPayBillView(personalReceiptsOrder));
            }


        }

        @Override
        public int getItemCount() {
            return mReceiptUnitOrderList.size();
        }

        public class ReceptUnitViewHolder extends RecyclerView.ViewHolder {
            TextView total, tvTime, tvStatus;
            Button btnExtraOrderCancel;
            LinearLayout linearPersonalOrderContainer;
            public ReceptUnitViewHolder(@NonNull View itemView) {
                super(itemView);
                total = itemView.findViewById(R.id.tvTotal);
                tvTime = itemView.findViewById(R.id.order_time);
                tvStatus = itemView.findViewById(R.id.order_status);
                linearPersonalOrderContainer = itemView.findViewById(R.id.linear_personal_order_container);
                btnExtraOrderCancel = itemView.findViewById(R.id.btn_extra_order_cancel);
                btnExtraOrderCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvStatus.setText("주문취소");
                        tvStatus.setTextColor(Color.RED);
                        Toast.makeText(mContext, "주문이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }

    private View createPersonalPayBillView(PersonalOrder personalOrder) {
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.history_personal_bill, null, false);

        TextView tvName = v.findViewById(R.id.memberNameTextView);
        TextView tvPrice = v.findViewById(R.id.memberPriceTextView);
        LinearLayout memberOrderLinearLayout = v.findViewById(R.id.personalOrderLinearLayout);

        tvName.setText(personalOrder.guest_name);
        for (int i = 0; personalOrder.menuList.size() > i; i++) {
            Menu a_menu = personalOrder.menuList.get(i);
            TextView tvOrderItem = new TextView(getActivity(), null, 0, R.style.ItemOrderBillTextStyle);
            LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50);
            tvOrderItem.setLayoutParams(lllp);
            tvOrderItem.setPadding(8, 0, 0, 0);
            String str = a_menu.item_name + "       " + a_menu.qty + "개";
            tvOrderItem.setText(str);
            memberOrderLinearLayout.addView(tvOrderItem);
        }
        String strPrice = AppDef.priceMapper(Integer.valueOf(personalOrder.total_price));
        tvPrice.setText(strPrice + "원");
        return v;

    }

    private View createPosPersonalPayBillView(PosPersonalOrder posPersonalOrder) {
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.history_personal_bill, null, false);

        TextView tvName = v.findViewById(R.id.memberNameTextView);
        TextView tvPrice = v.findViewById(R.id.memberPriceTextView);
        LinearLayout memberOrderLinearLayout = v.findViewById(R.id.personalOrderLinearLayout);

        tvName.setText(posPersonalOrder.order_name);
        for (int i = 0; posPersonalOrder.menuList.size() > i; i++) {
            Menu a_menu = posPersonalOrder.menuList.get(i);
            TextView tvOrderItem = new TextView(getActivity(), null, 0, R.style.ItemOrderBillTextStyle);
            LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50);
            tvOrderItem.setLayoutParams(lllp);
            tvOrderItem.setPadding(8, 0, 0, 0);
            String str = a_menu.item_name + "       " + a_menu.qty + "개";
            tvOrderItem.setText(str);
            memberOrderLinearLayout.addView(tvOrderItem);
        }
        String strPrice = AppDef.priceMapper(Integer.valueOf(posPersonalOrder.total_price));
        tvPrice.setText(strPrice + "원");
        return v;

    }


}

