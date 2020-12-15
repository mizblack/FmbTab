package com.eye3.golfpay.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
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

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.R;
import com.eye3.golfpay.adapter.RestaurantListAdapter;
import com.eye3.golfpay.common.AppDef;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.model.order.Menu;
import com.eye3.golfpay.model.order.PersonalOrder;
import com.eye3.golfpay.model.order.PosPersonalOrder;
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


    private void getStoreOrder() {
        showProgress("주문내역 정보를 가져오는 중입니다.");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getStoreOrder(getActivity(), Global.selectedReservation.getReserveNo(), new DataInterface.ResponseCallback<ResponseData<StoreOrder>>() {
            @Override
            public void onSuccess(ResponseData<StoreOrder> response) {
                hideProgress();
                systemUIHide();
                if (response.getResultCode().equals("ok")) {

                    AppDef.storeOrderArrayList = (ArrayList<StoreOrder>) response.getList();
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
                response.getError();
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
        getStoreOrder();
        mParentActivity.hideMainBottomBar();
        return v;
    }

    private View createReceiptOrderView(ReceiptUnit receiptUnit, String storeName) {
        TextView total, orderTime;
        TextView orderStatus;
        LinearLayout linearPersonalOrderContainer, linearTotalPrice;
        Button btnOrderCancel;
        View receptUnitView = LayoutInflater.from(mContext).inflate(R.layout.recept_order_item, null, false);
        btnOrderCancel = receptUnitView.findViewById(R.id.btn_extra_order_cancel);
        btnOrderCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView) v).setText("주문취소");
                ((TextView) v).setTextColor(Color.RED);
                Toast.makeText(mContext, "주문이 취소되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        //total = receptUnitView.findViewById(R.id.tvTotal);
        //total.setText("총계" + "   " + AppDef.priceMapper(getTotalReceptUnit(receiptUnit.recept_list)));
        orderTime = receptUnitView.findViewById(R.id.order_time);
        orderTime.setText(String.format("%s (%s)", storeName, receiptUnit.order_time));
        linearPersonalOrderContainer = receptUnitView.findViewById(R.id.linear_personal_order_container);
        linearTotalPrice = receptUnitView.findViewById(R.id.view_total_price);
        //게스트중 한명만이라도 주문완료면 주문완료로 표시
        orderStatus = receptUnitView.findViewById(R.id.order_status);
        orderStatus.setText(receiptUnit.recept_list.get(0).order_status);

        int itemWidth = 238; //4개
        int itemHeight = 400;
        //int w = 197; //5개

        if (receiptUnit.recept_list.size() == 5) {
            itemWidth = 198;
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, itemWidth, getResources().getDisplayMetrics());
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(width, 0);
            params.bottomToTop = R.id.btn_extra_order_cancel;
            params.endToEnd = R.id.view_root;
            params.topToTop = R.id.linear_personal_order_container;
            params.bottomMargin = 6;
            linearTotalPrice.setLayoutParams(params);
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
            View view = createPersonalPayBillView(personalReceiptsOrder);
            final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, itemWidth, getResources().getDisplayMetrics());
            final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, itemHeight, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
            params.rightMargin = 10;
            view.setLayoutParams(params);
            linearPersonalOrderContainer.addView(view);
        }
        return receptUnitView;
    }

    private int getOrderMaxCount(ReceiptUnit receiptUnit) {

        ArrayList<Integer> array = new ArrayList<>();

        for (int i = 0; receiptUnit.recept_list.size() > i; i++) {
            PersonalOrder personalReceiptsOrder = receiptUnit.recept_list.get(i);
            array.add(personalReceiptsOrder.menuList.size());
        }

        return Collections.max(array);
    }

    private View createPosOrderView(List<PosPersonalOrder> posPersonalOrderList) {
        int total = 0;
        if (posPersonalOrderList == null || posPersonalOrderList.size() == 0)
            return null;
        LinearLayout posUnitLinear = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.pos_order_item_linear, null, false);
        TextView tvPosTotal = posUnitLinear.findViewById(R.id.tvPosTotal);
        LinearLayout linearPersonalOrderContainer = null;
        for (int i = 0; posPersonalOrderList.size() > i; i++) {
            total += Integer.valueOf(posPersonalOrderList.get(i).total_price);
            View personalOrderDetailView = createPosPersonalPayBillView(posPersonalOrderList.get(i));
            final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 230, getResources().getDisplayMetrics());
            final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            params.rightMargin = 100;
            personalOrderDetailView.setLayoutParams(params);

            linearPersonalOrderContainer = posUnitLinear.findViewById(R.id.linear_pos__personal_order_container);
            linearPersonalOrderContainer.addView(personalOrderDetailView);
        }
        tvPosTotal.setText(AppDef.priceMapper(total) + " 원");
        return posUnitLinear;

    }

    private void setOrderHistory(StoreOrder storeOrder) {
        mOrderHistoryLinear.removeAllViewsInLayout();

        for (int i = 0; storeOrder.tablet_order_list.size() > i; i++) {

            mOrderHistoryLinear.addView(createReceiptOrderView(storeOrder.tablet_order_list.get(i), storeOrder.store_name));
        }

        //pos 오더 뷰 생성
        View view = createPosOrderView(storeOrder.pos_order_list);
        if (view != null)
            mOrderHistoryLinear.addView(createPosOrderView(storeOrder.pos_order_list));
    }

    //레스토랑바 선택시 보여주는 함수
    private void selectRestaurantAndShowHistory() {
        showProgress("주문 내역을 조회중입니다.");
        setOrderHistory(findStoreOrderByRestaurantName(AppDef.storeOrderArrayList, mRestaurantList.get(mSelectedRestaurantTabIdx).name) );
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
            LayoutInflater inflater2 = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View childView = inflater2.inflate(R.layout.view_history_personal_bill_item, null, false);

            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            params.topMargin = 30;
            childView.setLayoutParams(params);

            TextView tvBillItemName = childView.findViewById(R.id.tv_bill_item_name);
            TextView tvBillItemCount = childView.findViewById(R.id.tv_bill_item_count);
            tvBillItemName.setText(a_menu.item_name);
            tvBillItemCount.setText(a_menu.qty + "개");

            memberOrderLinearLayout.addView(childView);
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

