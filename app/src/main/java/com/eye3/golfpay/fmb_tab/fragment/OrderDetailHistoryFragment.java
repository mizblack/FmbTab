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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.constraintlayout.widget.ConstraintLayout;
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
    private ArrayList<Restaurant> mRestaurantList = new ArrayList<>();
    //해당 아이디를 가진 전표만 리스트에 나옴
    String mSelectedRestaurantId = "";
    Button mBtnTopAdd;
    ScrollView mOrderHistorySv;
    LinearLayout mOrderHistoryLinear;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        if (bundle != null) {
            mRestaurantList = (ArrayList<Restaurant>) bundle.getSerializable("restaurantList");
            NUM_OF_RESTAURANT = mRestaurantList.size();
            mRestaurantTabBarArr = new TextView[NUM_OF_RESTAURANT];
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
                    //createTabBar(mRestaurantTabBarArr, mRestaurantList);
                    setTagTheRestaurant();
                    selectRestaurantAndShowHistory(mSelectedRestaurantTabIdx);
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
                selectRestaurantAndShowHistory(mSelectedRestaurantTabIdx);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_order_history, container, false);

        mOrderHistorySv = v.findViewById(R.id.order_history_sv);
        mOrderHistoryLinear = v.findViewById(R.id.order_history_linear);
        mTabLinearOrderDetail = v.findViewById(R.id.restaurantTabLinearLayout);
        mTvTheRestaurant = v.findViewById(R.id.tvTheRestaurantOrderHistory);
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

    private View createReceptOrderView(ReceiptUnit receiptUnit) {
        TextView total, orderTime;
        TextView orderStatus;
        LinearLayout linearPersonalOrderContainer;
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
        total = receptUnitView.findViewById(R.id.tvTotal);
        total.setText("총계" + "   " + AppDef.priceMapper(getTotalReceptUnit(receiptUnit.recept_list)));
        orderTime = receptUnitView.findViewById(R.id.order_time);
        orderTime.setText(receiptUnit.order_time);
        linearPersonalOrderContainer = receptUnitView.findViewById(R.id.linear_personal_order_container);
        //게스트중 한명만이라도 주문완료면 주문완료로 표시
        orderStatus = receptUnitView.findViewById(R.id.order_status);
        orderStatus.setText(receiptUnit.recept_list.get(0).order_status);

        for (int i = 0; receiptUnit.recept_list.size() > i; i++) {
            PersonalOrder personalReceiptsOrder = receiptUnit.recept_list.get(i);
            linearPersonalOrderContainer.addView(createPersonalPayBillView(personalReceiptsOrder));
        }
        return receptUnitView;
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
            linearPersonalOrderContainer = posUnitLinear.findViewById(R.id.linear_pos__personal_order_container);
            linearPersonalOrderContainer.addView(personalOrderDetailView);
        }
        tvPosTotal.setText(AppDef.priceMapper(total) + " 원");
        return posUnitLinear;

    }

    private void setOrderHistory(StoreOrder storeOrder) {
        mOrderHistoryLinear.removeAllViewsInLayout();

        for (int i = 0; storeOrder.tablet_order_list.size() > i; i++) {
            mOrderHistoryLinear.addView(createReceptOrderView(storeOrder.tablet_order_list.get(i)));
        }
        //pos 오더 뷰 생성
        View view = createPosOrderView(storeOrder.pos_order_list);
        if (view != null)
            mOrderHistoryLinear.addView(createPosOrderView(storeOrder.pos_order_list));
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
                        selectRestaurantAndShowHistory(mSelectedRestaurantTabIdx);
                    }
                });
                isTheRestaurant = true;
                mRestaurantList.remove(i);
                i--;
            } else {
                final int idx = i;
                tvRestTabBar[i] = new TextView(new ContextThemeWrapper(getActivity(), R.style.GlobalTextView_18SP_C9D7DC_NotoSans_Bold), null, 0);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                param.rightMargin = 80;
                tvRestTabBar[i].setLayoutParams(param);
                tvRestTabBar[i].setGravity(Gravity.CENTER);
                tvRestTabBar[i].setText(mRestaurantList.get(i).name);
                tvRestTabBar[i].setTag(findStoreOrderByRestaurantName(AppDef.storeOrderArrayList, mRestaurantList.get(i).name));
                tvRestTabBar[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSelectedRestaurantTabIdx = idx;
                        selectRestaurantAndShowHistory(mSelectedRestaurantTabIdx);
                    }
                });
                mTabLinearOrderDetail.addView(tvRestTabBar[i]);
            }
        }
    }

    //레스토랑바 선택시 보여주는 함수
    private void selectRestaurantAndShowHistory(int selectedTabIdx) {
        showProgress("주문 내역을 조회중입니다.");
        mTvTheRestaurant.setTextColor(Color.GRAY);
        for (int i = 0; mRestaurantTabBarArr.length > i; i++) {
            TextView textView = mRestaurantTabBarArr[i];
            textView.setTextColor(Color.GRAY);
        }
        if (selectedTabIdx != -1) {
            mRestaurantTabBarArr[selectedTabIdx].setTextColor(Color.BLACK);
            mRestaurantTabBarArr[selectedTabIdx].setVisibility(View.VISIBLE);
        } else {
            mTvTheRestaurant.setTextColor(Color.BLACK);
            mTvTheRestaurant.setVisibility(View.VISIBLE);
        }
        showStoreOrderHistory(selectedTabIdx);
        hideProgress();
    }

    private void showStoreOrderHistory(int selectedRestaurantTabIdx) {
        //retaurantlist와 storeArraylist의 식당 인덱스가 대식당으로 일치하지 않다
        if (selectedRestaurantTabIdx != -1) {
          //  StoreOrder storeOrder = (StoreOrder) mRestaurantTabBarArr[selectedRestaurantTabIdx].getTag();
            setOrderHistory(findStoreOrderByRestaurantName(AppDef.storeOrderArrayList, mRestaurantList.get(mSelectedRestaurantTabIdx).name) );
        } else {   //selectedTabIdx == -1 대식당일경우
            StoreOrder storeOrder = (StoreOrder) mTvTheRestaurant.getTag();
            setOrderHistory(storeOrder);
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

