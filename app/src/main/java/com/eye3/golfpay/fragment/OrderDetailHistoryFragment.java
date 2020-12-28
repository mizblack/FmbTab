package com.eye3.golfpay.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.R;
import com.eye3.golfpay.adapter.RestaurantListAdapter;
import com.eye3.golfpay.common.AppDef;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.model.order.CancelOrder;
import com.eye3.golfpay.model.order.Menu;
import com.eye3.golfpay.model.order.PersonalOrder;
import com.eye3.golfpay.model.order.ReceiptUnit;
import com.eye3.golfpay.model.order.Restaurant;
import com.eye3.golfpay.model.order.StoreOrder;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderDetailHistoryFragment extends BaseFragment {
    private static int NUM_OF_RESTAURANT;
    int mSelectedRestaurantTabIdx = 0;

    Restaurant mSelectedRestaurant;
    LinearLayout mTabLinearOrderDetail;
    private ArrayList<Restaurant> mRestaurantList = new ArrayList<>();
    //해당 아이디를 가진 전표만 리스트에 나옴
    String mSelectedRestaurantId = "";
    Button mBtnTopAdd;
    ScrollView mOrderHistorySv;
    LinearLayout mOrderHistoryLinear;
    private RecyclerView rv_restaurant;
    private ArrayList<StoreOrder> storeOrderArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        if (bundle != null) {
            mRestaurantList = (ArrayList<Restaurant>) bundle.getSerializable("restaurantList");
            mSelectedRestaurantTabIdx = bundle.getInt("selectedRestaurantTabIdx");
        }
    }

    public static StoreOrder findStoreOrderByRestaurantName(List<StoreOrder> storeOrderList, String targetRestaurantName) {
        for (int i = 0; storeOrderList.size() > i; i++) {
            if (storeOrderList.get(i).store_name.equals(targetRestaurantName))
                return storeOrderList.get(i);
        }
        return null;
    }

    private void showGpsFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("ani_direction", "down");
        GoNativeScreen(new CourseFragment(), bundle);
    }

    private void getStoreOrder() {
        showProgress("주문내역 정보를 가져오는 중입니다.");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getStoreOrder(getActivity(), Global.selectedReservation.getReserveNo(), new DataInterface.ResponseCallback<ResponseData<StoreOrder>>() {
            @Override
            public void onSuccess(ResponseData<StoreOrder> response) {
                hideProgress();
                systemUIHide();
                if (response.getResultCode().equals("ok")) {

                    storeOrderArrayList = (ArrayList<StoreOrder>) response.getList();
                    //여기서 실행해야함.
                    initRestaurantRecyclerView();
                    selectRestaurantAndShowHistory();
                } else if (response.getResultCode().equals("fail")) {
                    Toast.makeText(getActivity(), response.getResultMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(ResponseData<StoreOrder> response) {
                hideProgress();
                systemUIHide();
            }

            @Override
            public void onFailure(Throwable t) {
                hideProgress();
                systemUIHide();
            }
        });
    }

    private void cancelOrder(CancelOrder cancelOrder) {
        showProgress("주문을 취소하는 중입니다.");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).cancelOrder(getActivity(), cancelOrder, new DataInterface.ResponseCallback<ResponseData<StoreOrder>>() {
            @Override
            public void onSuccess(ResponseData<StoreOrder> response) {
                hideProgress();
                systemUIHide();
                if (response.getResultCode().equals("ok")) {
                    getStoreOrder();
                    Toast.makeText(mContext, "주문이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                } else if (response.getResultCode().equals("fail")) {

                }
            }

            @Override
            public void onError(ResponseData<StoreOrder> response) {
                hideProgress();
                systemUIHide();
            }

            @Override
            public void onFailure(Throwable t) {
                hideProgress();
                systemUIHide();
            }
        });
    }

    private void initRestaurantRecyclerView() {
        LinearLayoutManager mManager;

        mManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        rv_restaurant.setLayoutManager(mManager);

        RestaurantListAdapter adapter = new RestaurantListAdapter(mContext, new RestaurantListAdapter.IOnClickAdapter() {
            @Override
            public void onAdapterItemClicked(Integer id) {
                mSelectedRestaurantTabIdx = id;
                selectRestaurantAndShowHistory();
            }
        });

        rv_restaurant.setAdapter(adapter);
        adapter.setData(mRestaurantList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_order_history, container, false);

        rv_restaurant = v.findViewById(R.id.rv_restaurant);
        mOrderHistorySv = v.findViewById(R.id.order_history_sv);
        mOrderHistoryLinear = v.findViewById(R.id.order_history_linear);
        mBtnTopAdd = v.findViewById(R.id.topAddButton);
        mBtnTopAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new OrderFragment(), null);
            }
        });

        v.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGpsFragment();
            }
        });

        getStoreOrder();
        mParentActivity.hideMainBottomBar();
        return v;
    }

    @SuppressLint("CutPasteId")
    private View createReceiptOrderView(ReceiptUnit receiptUnit, String storeNo, String storeName) {
        TextView total, orderTime, tv_total_price;
        TextView orderStatus;
        LinearLayout linearPersonalOrderContainer;
        Button btnOrderCancel;
        LinearLayout view_cancel;
        ConstraintLayout view_total_price;

        View receptUnitView = LayoutInflater.from(mContext).inflate(R.layout.recept_order_item, null, false);
        btnOrderCancel = receptUnitView.findViewById(R.id.btn_extra_order_cancel);
        view_cancel = receptUnitView.findViewById(R.id.view_cancel);
        view_total_price = receptUnitView.findViewById(R.id.view_total_price);
        tv_total_price = receptUnitView.findViewById(R.id.tv_total_price);
        String strPrice = AppDef.priceMapper(getTotalReceptUnit(receiptUnit.recept_list));
        tv_total_price.setText(strPrice + "원");

        btnOrderCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelOrder cancelOrder = new CancelOrder();
                cancelOrder.reserve_id = Global.selectedReservation.getReserveNo();
                cancelOrder.shade_id = storeNo;
                cancelOrder.cancel_order_detail = new ArrayList<>();
                for (PersonalOrder list : receiptUnit.recept_list) {
                    CancelOrder.CancelOrderDetail data = new CancelOrder.CancelOrderDetail();
                    data.order_bills_id = list.pos_bill_id;
                    cancelOrder.cancel_order_detail.add(data);
                }

                cancelOrder(cancelOrder);
            }
        });
        //total = receptUnitView.findViewById(R.id.tvTotal);
        //total.setText("총계" + "   " + AppDef.priceMapper(getTotalReceptUnit(receiptUnit.recept_list)));
        orderTime = receptUnitView.findViewById(R.id.order_time);
        orderTime.setText(String.format("%s (%s)", storeName, receiptUnit.order_time));
        linearPersonalOrderContainer = receptUnitView.findViewById(R.id.linear_personal_order_container);
        //게스트중 한명만이라도 주문완료면 주문완료로 표시
        orderStatus = receptUnitView.findViewById(R.id.order_status);
        orderStatus.setText(receiptUnit.recept_list.get(0).order_status);

        int itemWidth = 238; //기본 4개
        int itemHeight = 400;

        boolean isCancelOrder = isCancelOrder(receiptUnit);

        if (isCancelOrder) {
            view_cancel.setVisibility(View.GONE);
            view_total_price.setBackgroundColor(Color.parseColor("#f1f5f6"));
        }

        if (receiptUnit.recept_list.size() == 5) {
            itemWidth = 198;
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, itemWidth, getResources().getDisplayMetrics());
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(width, 0);
            params.bottomToTop = R.id.btn_extra_order_cancel;
            params.endToEnd = R.id.view_root;
            params.topToTop = R.id.linear_personal_order_container;
            params.bottomMargin = 6;
            view_total_price.setLayoutParams(params);
        }

        int maxCount = getOrderMaxCount(receiptUnit);
        if (maxCount > 7) {
            itemHeight = maxCount * 60;
            final int width = 0;
            final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, itemHeight, getResources().getDisplayMetrics());
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(width, height);
            params.setMarginEnd(6);
            params.topMargin = 20;
            params.bottomToBottom = R.id.view_root;
            params.endToStart = R.id.view_total_price;
            params.startToStart = R.id.view_root;
            params.topToBottom = R.id.linearLayout8;
            linearPersonalOrderContainer.setLayoutParams(params);
        }

        for (int i = 0; receiptUnit.recept_list.size() > i; i++) {
            PersonalOrder personalReceiptsOrder = receiptUnit.recept_list.get(i);
            View view = createPersonalPayBillView(personalReceiptsOrder, isCancelOrder);
            final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, itemWidth, getResources().getDisplayMetrics());
            final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, itemHeight, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
            params.rightMargin = 10;
            view.setLayoutParams(params);
            linearPersonalOrderContainer.addView(view);
        }
        return receptUnitView;
    }

    @SuppressLint("CutPasteId")
    private View createPosOrderView(List<PersonalOrder> personalOrders, String storeNo, String storeName) {
        TextView tvTotal, tvOrderTime, tv_total_price;
        TextView orderStatus;
        LinearLayout linearPersonalOrderContainer;
        Button btnOrderCancel;
        LinearLayout view_cancel;
        ConstraintLayout view_root, view_total_price;

        View receptUnitView = LayoutInflater.from(mContext).inflate(R.layout.recept_order_item, null, false);
        view_root = receptUnitView.findViewById(R.id.view_root);
        btnOrderCancel = receptUnitView.findViewById(R.id.btn_extra_order_cancel);
        view_cancel = receptUnitView.findViewById(R.id.view_cancel);
        view_total_price = receptUnitView.findViewById(R.id.view_total_price);
        tv_total_price = receptUnitView.findViewById(R.id.tv_total_price);
        //String strPrice = AppDef.priceMapper(getTotalReceptUnit(personalOrders));
        //tv_total_price.setText(strPrice + "원");

        view_root.setBackgroundColor(Color.parseColor("#2d2e31"));
        btnOrderCancel.setVisibility(View.GONE);
        tvOrderTime = receptUnitView.findViewById(R.id.order_time);
        tvOrderTime.setText(String.format("%s (%s)", storeName, personalOrders.get(0).order_time));
        tvOrderTime.setTextColor(Color.parseColor("#ffffff"));
        linearPersonalOrderContainer = receptUnitView.findViewById(R.id.linear_personal_order_container);
        //게스트중 한명만이라도 주문완료면 주문완료로 표시
        orderStatus = receptUnitView.findViewById(R.id.order_status);
        orderStatus.setVisibility(View.GONE);

        int itemWidth = 238; //기본 4개
        int itemHeight = 400;

        if (personalOrders.size() == 5) {
            itemWidth = 198;
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, itemWidth, getResources().getDisplayMetrics());
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(width, 0);
            params.bottomToTop = R.id.btn_extra_order_cancel;
            params.endToEnd = R.id.view_root;
            params.topToTop = R.id.linear_personal_order_container;
            params.bottomMargin = 6;
            view_total_price.setLayoutParams(params);
        }

        int maxCount = getOrderMaxCount(personalOrders);
        if (maxCount > 7) {
            itemHeight = maxCount * 60;
            final int width = 0;
            final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, itemHeight, getResources().getDisplayMetrics());
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(width, height);
            params.setMarginEnd(6);
            params.topMargin = 20;
            params.bottomToBottom = R.id.view_root;
            params.endToStart = R.id.view_total_price;
            params.startToStart = R.id.view_root;
            params.topToBottom = R.id.linearLayout8;
            linearPersonalOrderContainer.setLayoutParams(params);
        }

        for (int i = 0; personalOrders.size() > i; i++) {
            PersonalOrder personalReceiptsOrder = personalOrders.get(i);
            View view = createPersonalPayBillView(personalReceiptsOrder, false);
            final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, itemWidth, getResources().getDisplayMetrics());
            final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, itemHeight, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
            params.rightMargin = 10;
            view.setLayoutParams(params);
            linearPersonalOrderContainer.addView(view);
        }
        return receptUnitView;
    }

    private boolean isCancelOrder(ReceiptUnit receiptUnit) {
        for (PersonalOrder item : receiptUnit.recept_list) {
            if (item.order_status.equals("주문취소"))
                return true;
        }

        return false;
    }

    private int getOrderMaxCount(List<PersonalOrder> personalOrders) {

        ArrayList<Integer> array = new ArrayList<>();

        for (int i = 0; personalOrders.size() > i; i++) {
            PersonalOrder personalReceiptsOrder = personalOrders.get(i);
            array.add(personalReceiptsOrder.menuList.size());
        }

        return Collections.max(array);
    }

    private int getOrderMaxCount(ReceiptUnit receiptUnit) {

        ArrayList<Integer> array = new ArrayList<>();

        for (int i = 0; receiptUnit.recept_list.size() > i; i++) {
            PersonalOrder personalReceiptsOrder = receiptUnit.recept_list.get(i);
            array.add(personalReceiptsOrder.menuList.size());
        }

        return Collections.max(array);
    }

    private void setOrderHistory(StoreOrder storeOrder) {
        mOrderHistoryLinear.removeAllViewsInLayout();

        for (int i = 0; storeOrder.tablet_order_list.size() > i; i++) {

            mOrderHistoryLinear.addView(createReceiptOrderView(storeOrder.tablet_order_list.get(i), storeOrder.store_no, storeOrder.store_name));
        }

        //pos 오더 뷰 생성

        if (!storeOrder.pos_order_list.isEmpty()) {

            TextView tvHeader = new TextView(getContext(), null, 0, R.style.ItemPosOrderBillHeaderTextStyle);
            LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120);
            tvHeader.setLayoutParams(lllp);
            tvHeader.setPadding(60, 0, 0, 0);
            mOrderHistoryLinear.addView(tvHeader);

            View view = createPosOrderView(storeOrder.pos_order_list, storeOrder.store_no, storeOrder.store_name);
            if (view != null)
                mOrderHistoryLinear.addView(view);
        }
    }

    //레스토랑바 선택시 보여주는 함수
    private void selectRestaurantAndShowHistory() {
        showProgress("주문 내역을 조회중입니다.");
        setOrderHistory(findStoreOrderByRestaurantName(storeOrderArrayList, mRestaurantList.get(mSelectedRestaurantTabIdx).name));
        hideProgress();
    }

    private int getTotalReceptUnit(List<PersonalOrder> personalOrderList) {
        //4personal total
        int total_price = 0;
        for (int i = 0; personalOrderList.size() > i; i++) {
            total_price += Integer.valueOf(personalOrderList.get(i).total_price);
        }
        return total_price;
    }

    private View createPersonalPayBillView(PersonalOrder personalOrder, boolean isCancelOrder) {
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.history_personal_bill, null, false);

        TextView tvName = v.findViewById(R.id.memberNameTextView);
        TextView tvPrice = v.findViewById(R.id.memberPriceTextView);
        TextView tvSum = v.findViewById(R.id.tv_sum);
        ConstraintLayout rootView = v.findViewById(R.id.personal_bill_root);
        LinearLayout memberOrderLinearLayout = v.findViewById(R.id.personalOrderLinearLayout);

        if (isCancelOrder) {
            rootView.setBackgroundColor(Color.parseColor("#f1f5f6"));
            tvName.setTextColor(Color.parseColor("#acacad"));
            tvPrice.setTextColor(Color.parseColor("#acacad"));
            tvSum.setTextColor(Color.parseColor("#acacad"));
        }

        tvName.setText(personalOrder.guest_name);
        for (int i = 0; personalOrder.menuList.size() > i; i++) {
            Menu a_menu = personalOrder.menuList.get(i);
            LayoutInflater inflater2 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View childView = inflater2.inflate(R.layout.view_history_personal_bill_item, null, false);

            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            params.topMargin = 30;
            childView.setLayoutParams(params);

            TextView tvBillItemName = childView.findViewById(R.id.tv_bill_item_name);
            TextView tvBillItemCount = childView.findViewById(R.id.tv_bill_item_count);
            tvBillItemName.setText(a_menu.item_name);
            tvBillItemCount.setText(a_menu.qty + "개");

            if (isCancelOrder) {
                tvBillItemName.setTextColor(Color.parseColor("#acacad"));
                tvBillItemCount.setTextColor(Color.parseColor("#acacad"));
            }

            memberOrderLinearLayout.addView(childView);
        }

        int totalPrice = 0;
        if (personalOrder.total_price == null) {
            totalPrice = Integer.parseInt(personalOrder.totalPrice);
        } else
            totalPrice = Integer.parseInt(personalOrder.total_price);

        String strPrice = AppDef.priceMapper(totalPrice);
        tvPrice.setText(strPrice + "원");
        return v;
    }
}

