package com.eye3.golfpay.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.R;
import com.eye3.golfpay.adapter.RestaurantCategoryAdapter;
import com.eye3.golfpay.adapter.RestaurantListAdapter;
import com.eye3.golfpay.adapter.RestaurantMenuAdapter;
import com.eye3.golfpay.common.AppDef;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.databinding.FrRestaurantOrderBinding;
import com.eye3.golfpay.model.order.Category;
import com.eye3.golfpay.model.order.Category2;
import com.eye3.golfpay.model.order.GuestNameOrder;
import com.eye3.golfpay.model.order.OrderDetail;
import com.eye3.golfpay.model.order.OrderItemInvoice;
import com.eye3.golfpay.model.order.OrderedMenuItem;
import com.eye3.golfpay.model.order.Restaurant;
import com.eye3.golfpay.model.order.RestaurantMenu;
import com.eye3.golfpay.model.order.RestaurantMenuOrder;
import com.eye3.golfpay.model.order.ShadeOrder;
import com.eye3.golfpay.model.order.StoreOrder;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;
import com.eye3.golfpay.view.NameOrderView;
import com.eye3.golfpay.view.OrderItemInvoiceView;
import com.eye3.golfpay.view.SnappingLinearLayoutManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderFragment extends BaseFragment {

    private FrRestaurantOrderBinding binding;
    public static final int NUM_MENU_NAME_KEY = 1 + 2 << 24;
    public static final int NUM_NAMEORDER_KEY = 2 + 2 << 24;
    public static final int NUM_INVOICEORDER_KEY = 3 + 2 << 24;

    public String TAG = getClass().getSimpleName();
    private ArrayList<Restaurant> mRestaurantList = new ArrayList<>();
    private int mSelectedRestaurantIdx = 0;
    private ShadeOrder mShadeOrders;
    //**********************************************************
    //임시저장을 제외한 주문은 mOrderDetailList으로 처리함 (AppDef.mOrderDetailList 사용안함)
    List<OrderDetail> mOrderDetailList = new ArrayList<>();//먼저 생성해야 아래리스트에 renew됨.
    List<OrderItemInvoice> mOrderItemInvoiceArrayList = new ArrayList<>();
    //**********************************************************
    public static LinearLayout mTabsRootLinear;
    private View preSelectedGuestView;
    RestaurantMenuOrder mRestaurantMenuOrder;
    List<RestaurantMenu> mWholeMenuList;
    RestaurantMenuAdapter mMenuAdapter;
    RestaurantCategoryAdapter mCategoryAdapter;
    int mNumOfOrderHistory = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRestaurantMenuOrder = new RestaurantMenuOrder(getActivity(), mOrderDetailList, mOrderItemInvoiceArrayList);
        getRestaurantMenu();
    }

    @Override
    public void onResume() {
        super.onResume();
        systemUIHide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fr_restaurant_order, container, false);
        View mainView = binding.getRoot();

        mTabsRootLinear = mainView.findViewById(R.id.tabsRootLinearLayout);
        createGuestList(binding.guestContainer);
        initOrderItemInvoiceView();
        mParentActivity.showMainBottomBar();
        return mainView;
    }

    private View.OnClickListener CaddieClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mMenuAdapter.haveOrder()) {

                for (RestaurantMenu item : mMenuAdapter.getRestaurantMenus()) {

                    if (!item.isSelected)
                        continue;


                    OrderedMenuItem menu = new OrderedMenuItem(item.id, "1", item.price, item.name, Global.CaddyNo);

                    //캐디주문을 표기한다.
                    binding.infoTextView.setVisibility(View.GONE);
                    ((TextView) v).setTextColor(getResources().getColor(R.color.white, Objects.requireNonNull(getActivity()).getTheme()));
                    (v).setBackgroundResource(R.drawable.shape_ebony_black_background_and_edge);
                    OrderedMenuItem orderedMenuItemForCaddy = new OrderedMenuItem(menu.id, "1", menu.price, menu.menuName, Global.CaddyNo);
                    mRestaurantMenuOrder.setmCurrentOrderedMenuItem(orderedMenuItemForCaddy);
                    // 캐디주문시 주문자 아이디는 첫 내장객으로 지정한다.
                    mRestaurantMenuOrder.setOrderedGuestId(mOrderDetailList.get(0).reserve_guest_id);
                    mRestaurantMenuOrder.addRestaurantMenuOrder(orderedMenuItemForCaddy, mOrderDetailList.get(0).reserve_guest_id);
                    makeOrderItemInvoiceArrViews(mRestaurantMenuOrder.getmOrderItemInvoiceArrayList());
                    setTheTotalInvoice();
                }

            } else
                Toast.makeText(mContext, "주문한 음식이 없습니다. 먼저 음식을 선택해 주세요.", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initFoodImage() {
        if (mRestaurantList == null) {
            Toast.makeText(getActivity(), "존재하는 식당 정보가 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openTempSavedOrderList() {
        //임시저장정보가 있다면
        if (AppDef.orderItemInvoiceArrayList.size() > 0) {
            Toast.makeText(mContext, "임시저장 메뉴정보가 존재합니다.", Toast.LENGTH_SHORT).show();

            refresh();
            //초기화로 인해 clear되지않게 할것
            mOrderItemInvoiceArrayList = AppDef.orderItemInvoiceArrayList;
            mOrderDetailList = AppDef.orderDetailList;
            //   makeOrderItems();
            makeOrderItemInvoiceArrViews(mRestaurantMenuOrder.getmOrderItemInvoiceArrayList());
            setTheTotalInvoice();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding.arrowToApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tabsRootLinearLayout.setVisibility(View.INVISIBLE);
                GoOrderLeftBoard(new OrderApplyFragment(), null);
            }
        });

        binding.orderOrApplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendShadeOrders();


            }
        });

        binding.resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mMenuAdapter.clearOrder();
                mRestaurantMenuOrder.clearRestaurantMenuOrder();

                if (AppDef.restaurantOrderArrayList.size() > 0) {
                    //  mBtnHistory.setVisibility(View.VISIBLE);
                    binding.orderOrApplyBtn.setVisibility(View.VISIBLE);
                } else {
                    refresh();
                    AppDef.orderDetailList.clear();
                    initOrderDetailList();
                    binding.orderOrApplyBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        //임시저장
        binding.btnTempSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDef.orderItemInvoiceArrayList = mOrderItemInvoiceArrayList;
                AppDef.orderDetailList = mOrderDetailList;
                AppDef.mTempSaveRestaurantIdx = mSelectedRestaurantIdx;
                //임시저장시 RestaurantOrder에 식당별로 add시키고
                Toast.makeText(getActivity(), "임시저장이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("restaurantList", mRestaurantList);
                bundle.putSerializable("orderdetailList", (Serializable) mOrderDetailList);
                bundle.putSerializable("selectedRestaurantTabIdx", mSelectedRestaurantIdx);
                bundle.putString("ani_direction", "down");
                GoNativeScreen(new OrderDetailHistoryFragment(), bundle);
            }
        });

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOrderDetailList.size() > 0)
                    sendShadeOrders();
                else
                    Toast.makeText(getActivity(), "먼저 주문을 해주십시요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //
    private List<RestaurantMenu> makeTotalMenuList(Restaurant selectedRestaurant) {

        int position = 0; //각 메뉴아이템당 새로운 index값 적용
        mWholeMenuList = new ArrayList<>();


        return mWholeMenuList;
    }

    //orderdetail과 주문내역데이리스트도 같이 삭제한다.
    void initOrderDetailList() {
        mOrderDetailList.clear();
        mOrderItemInvoiceArrayList.clear();
        int Size = Global.selectedReservation.getGuestData().size();
        //최초주문시 사이즈가 0이면
        if (mOrderDetailList.size() == 0) {
            for (int i = 0; Size > i; i++) {
                mOrderDetailList.add(i, new OrderDetail(Global.selectedReservation.getGuestData().get(i).getId()));
            }
        }
    }

    private void refresh() {
        initOrderDetailList();

        if (mMenuAdapter != null)
            mMenuAdapter.clearOrder();

        mOrderItemInvoiceArrayList = new ArrayList<OrderItemInvoice>();
        mShadeOrders = null;
        binding.orderBrowserLinearLayout.removeAllViewsInLayout();
        binding.totalPriceTextView.setText("");
        initFoodImage();
        refreshCategory();
//        if (mCateAdapter != null && mCateAdapter.mMenuAdapter != null)
//            mCateAdapter.mMenuAdapter.notifyDataSetChanged();
        resetGuestList();
    }

    private void resetGuestList() {

        if (mMenuAdapter == null)
            return;

        preSelectedGuestView = null;

        for (int i = 0; binding.guestContainer.getChildCount() > i; i++) {

            View view = binding.guestContainer.getChildAt(i);
            TextView tvCount = view.findViewById(R.id.tv_count);
            TextView tvName = view.findViewById(R.id.tv_name);

            tvName.setTextAppearance(R.style.GlobalTextView_20SP_999999_NotoSans_Medium);
            tvCount.setTextAppearance(R.style.GlobalTextView_17SP_999999_NotoSans_Medium);
            view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

            RestaurantMenu item = mMenuAdapter.getSelectedMenu();
            if (item == null)
                return;

            OrderedMenuItem findMenu = mOrderDetailList.get(i).findOrderMenu(item.id);
            if (findMenu == null) {
                tvCount.setText("+");
            } else {
                if (findMenu.qty.equals("0"))
                    tvCount.setText("+");
                 else
                    tvCount.setText(findMenu.qty + "");
            }
        }
    }

    public String getGuestName(String reserveId) {
        for (int i = 0; Global.selectedReservation.getGuestData().size() > i; i++) {
            if (Global.selectedReservation.getGuestData().get(i).getId().equals(reserveId)) {
                return Global.selectedReservation.getGuestData().get(i).getGuestName();
            }
        }
        return "";
    }

    public String getGuestId(String guestName) {
        for (int i = 0; Global.selectedReservation.getGuestData().size() > i; i++) {
            if (Global.selectedReservation.getGuestData().get(i).getGuestName().equals(guestName)) {
                return Global.selectedReservation.getGuestData().get(i).getId();
            }
        }
        return "";
    }

    private void createGuestList(LinearLayout container) {
        initOrderDetailList();
        for (int i = 0; mOrderDetailList.size() > i; i++) {
            final int idx = i;

            View viewGuest = LayoutInflater.from(mContext).inflate(R.layout.restaurant_guest_item, container, false);

            final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
            final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 94, getResources().getDisplayMetrics()) - 1;
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width, height);
            param.leftMargin = 0;
            param.topMargin = 1;

            if (i == 0) {
                param.topMargin = 2;
            }

            viewGuest.setLayoutParams(param);
            viewGuest.setTag(Global.selectedReservation.getGuestData().get(idx).getId());

            TextView tvName = viewGuest.findViewById(R.id.tv_name);
            tvName.setText(Global.selectedReservation.getGuestData().get(idx).getGuestName());
            setGuestClickListener(idx, viewGuest);
            container.addView(viewGuest);
        }
    }

    private void setGuestClickListener(final int index, View viewGuest) {
        viewGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvName;
                TextView tvCount;

                //이미선택된 게스트가 있다면 초기화하자
                if (preSelectedGuestView != null) {
                    tvName = preSelectedGuestView.findViewById(R.id.tv_name);
                    tvCount = preSelectedGuestView.findViewById(R.id.tv_count);

                    tvName.setTextAppearance(R.style.GlobalTextView_20SP_999999_NotoSans_Medium);
                    tvCount.setTextAppearance(R.style.GlobalTextView_17SP_999999_NotoSans_Medium);
                    preSelectedGuestView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                }

                preSelectedGuestView = v;
                tvName = v.findViewById(R.id.tv_name);
                tvCount = v.findViewById(R.id.tv_count);
                tvName.setTextAppearance(R.style.GlobalTextView_20SP_White_NotoSans_Medium);
                tvCount.setTextAppearance(R.style.GlobalTextView_17SP_white_NotoSans_Medium);
                preSelectedGuestView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.ebonyBlack));

                //주문된 음식이 있다면
                if (mMenuAdapter.haveOrder()) {
                    RestaurantMenu item = mMenuAdapter.getSelectedMenu();
                    OrderedMenuItem menu = new OrderedMenuItem(item.id, "1", item.price, item.name, "");

                    binding.infoTextView.setVisibility(View.GONE);
                    mRestaurantMenuOrder.setOrderedGuestId((String) v.getTag());
                    mRestaurantMenuOrder.setmCurrentOrderedMenuItem(menu);
                    mRestaurantMenuOrder.addRestaurantMenuOrder(new OrderedMenuItem(menu.id, "1", menu.price, menu.menuName, ""), (String) v.getTag());

                    OrderedMenuItem findMenu = mOrderDetailList.get(index).findOrderMenu(menu.id);
                    tvCount.setText(findMenu.qty + "");

                    makeOrderItemInvoiceArrViews(mRestaurantMenuOrder.getmOrderItemInvoiceArrayList());
                    setTheTotalInvoice();
                } else {
                    Toast.makeText(mContext, "음식을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void initOrderItemInvoiceView() {
        makeOrderItemInvoiceArrViews(mRestaurantMenuOrder.getmOrderItemInvoiceArrayList());
        setTheTotalInvoice();
    }


    //우측 주문표 뷰화면 생성함수
    private OrderItemInvoiceView makeOrderItemInvoiceView(final OrderItemInvoice orderItemInvoice) {
        OrderItemInvoiceView a_InvoiceView;
        if (orderItemInvoice == null || orderItemInvoice.mMenunName == "" || orderItemInvoice.mGuestNameOrders.size() == 0)
            return null;
        else
            a_InvoiceView = new OrderItemInvoiceView(mContext);

        a_InvoiceView.setTag(orderItemInvoice);
        //네임오더가 없으면 뷰를 생성하지 않고 null 리턴
        if (orderItemInvoice.mGuestNameOrders.size() == 0) {
            return null;
        }
        for (int i = 0; orderItemInvoice.mGuestNameOrders.size() > i; i++) {
            NameOrderView a_NameOrderview = new NameOrderView(mContext);
            a_NameOrderview.setTag(orderItemInvoice.mGuestNameOrders.get(i));
            a_NameOrderview.deleteLinear.setTag(NUM_MENU_NAME_KEY, orderItemInvoice.mMenunName);
            a_NameOrderview.deleteLinear.setTag(NUM_NAMEORDER_KEY, orderItemInvoice.mGuestNameOrders.get(i));
            a_NameOrderview.mNameTv.setText(((GuestNameOrder) a_NameOrderview.getTag()).mGuestName);

            a_NameOrderview.mQtyTv.setText(String.valueOf(((GuestNameOrder) a_NameOrderview.getTag()).qty) + "개");
            a_NameOrderview.deleteLinear.setOnClickListener(deletelistener);

            if (orderItemInvoice.mGuestNameOrders.get(i).caddy_id != null && orderItemInvoice.mGuestNameOrders.get(i).caddy_id != "")
                a_NameOrderview.setmCaddyTvVisible();

            a_NameOrderview.mPlusTv.setOnClickListener(listenerPlus);
            a_NameOrderview.mPlusTv.setTag(NUM_INVOICEORDER_KEY, orderItemInvoice);
            a_NameOrderview.mPlusTv.setTag(NUM_NAMEORDER_KEY, orderItemInvoice.mGuestNameOrders.get(i));

            a_NameOrderview.mMinusTv.setOnClickListener(listenerMinus);
            a_NameOrderview.mMinusTv.setTag(NUM_INVOICEORDER_KEY, orderItemInvoice);
            a_NameOrderview.mMinusTv.setTag(NUM_NAMEORDER_KEY, orderItemInvoice.mGuestNameOrders.get(i));

            a_InvoiceView.mLinearNameOrder.addView(a_NameOrderview);
        }

        a_InvoiceView.mTvMenuName.setText(((OrderItemInvoice) a_InvoiceView.getTag()).mMenunName);
        int _qty = ((OrderItemInvoice) a_InvoiceView.getTag()).mQty;
        a_InvoiceView.mTvQty.setText(_qty + "개");

        return a_InvoiceView;
    }


    View.OnClickListener deletelistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String menuName = (String) v.getTag(NUM_MENU_NAME_KEY);
            GuestNameOrder deletedGuestNameOrder = (GuestNameOrder) v.getTag(NUM_NAMEORDER_KEY);

            for (int i = 0; mOrderDetailList.size() > i; i++) {
                ArrayList<OrderedMenuItem> a_orderedMenuItemList = mOrderDetailList.get(i).mOrderedMenuItemList;
                for (int j = 0; a_orderedMenuItemList.size() > j; j++) {
                    if (menuName.equals(a_orderedMenuItemList.get(j).menuName) && getGuestName(mOrderDetailList.get(i).reserve_guest_id).equals(deletedGuestNameOrder.mGuestName)) {

                        a_orderedMenuItemList.remove(a_orderedMenuItemList.get(j));
                        break;
                    }
                }
            }

            for (OrderItemInvoice order : mRestaurantMenuOrder.getmOrderItemInvoiceArrayList()) {
                for (GuestNameOrder guestNameOrder : order.mGuestNameOrders) {
                    if (guestNameOrder.mGuestName.equals(deletedGuestNameOrder.mGuestName) && guestNameOrder.mMenuName.equals(menuName)) {
                        order.mGuestNameOrders.remove(guestNameOrder);

                        setTheTotalInvoice();
                        makeOrderItemInvoiceArrViews(mRestaurantMenuOrder.getmOrderItemInvoiceArrayList());
                        resetGuestList();
                        return;
                    }
                }
            }

            setTheTotalInvoice();
            makeOrderItemInvoiceArrViews(mRestaurantMenuOrder.getmOrderItemInvoiceArrayList());
            resetGuestList();
//            deleteNameOrderFromInvoiceArrayList(menuName, deletedGuestNameOrder);
//            makeOrderItemInvoiceArrViews(mRestaurantMenuOrder.getmOrderItemInvoiceArrayList());
        }
    };

    View.OnClickListener listenerPlus = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            OrderItemInvoice plusOrderItemInvoice = (OrderItemInvoice) v.getTag(NUM_INVOICEORDER_KEY);
            GuestNameOrder plusNameOrder = (GuestNameOrder) v.getTag(NUM_NAMEORDER_KEY);
            RestaurantMenu restaurantMenu = findMenuByName(plusOrderItemInvoice.mMenunName);
            if (restaurantMenu != null) {
                mRestaurantMenuOrder.setOrderedGuestId(getGuestId(plusNameOrder.mGuestName));
                mRestaurantMenuOrder.addRestaurantMenuOrder(new OrderedMenuItem(getMenuIdByMenuName(plusNameOrder.mMenuName), "1", getMenuPriceByMenuName(plusNameOrder.mMenuName), plusNameOrder.mMenuName, plusNameOrder.caddy_id), getGuestId(plusNameOrder.mGuestName));
                makeOrderItemInvoiceArrViews(mRestaurantMenuOrder.getmOrderItemInvoiceArrayList());
                setTheTotalInvoice();
                resetGuestList();
            }
        }
    };

    View.OnClickListener listenerMinus = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            OrderItemInvoice minusOrderItemInvoice = (OrderItemInvoice) v.getTag(NUM_INVOICEORDER_KEY);
            GuestNameOrder minusNameOrder = (GuestNameOrder) v.getTag(NUM_NAMEORDER_KEY);
            RestaurantMenu restaurantMenu = findMenuByName(minusOrderItemInvoice.mMenunName);
            if (restaurantMenu != null) {

                mRestaurantMenuOrder.setOrderedGuestId(getGuestId(minusNameOrder.mGuestName));
                mRestaurantMenuOrder.addRestaurantMenuOrder(new OrderedMenuItem(getMenuIdByMenuName(minusNameOrder.mMenuName),
                        "-1", getMenuPriceByMenuName(minusNameOrder.mMenuName),
                        minusNameOrder.mMenuName,
                        minusNameOrder.caddy_id),
                        getGuestId(minusNameOrder.mGuestName));

                if (minusNameOrder.qty == 1) {

                    String menuName = restaurantMenu.name;
                    GuestNameOrder deletedGuestNameOrder = (GuestNameOrder) v.getTag(NUM_NAMEORDER_KEY);

                    for (OrderItemInvoice order : mRestaurantMenuOrder.getmOrderItemInvoiceArrayList()) {

                        for (GuestNameOrder guestNameOrder : order.mGuestNameOrders) {
                            if (guestNameOrder.mGuestName.equals(deletedGuestNameOrder.mGuestName) && guestNameOrder.mMenuName.equals(menuName)) {
                                order.mGuestNameOrders.remove(guestNameOrder);

                                makeOrderItemInvoiceArrViews(mRestaurantMenuOrder.getmOrderItemInvoiceArrayList());
                                setTheTotalInvoice();
                                resetGuestList();
                                return;
                            }
                        }
                    }
                }

                makeOrderItemInvoiceArrViews(mRestaurantMenuOrder.getmOrderItemInvoiceArrayList());
                setTheTotalInvoice();
                resetGuestList();
            }
        }
    };

    public String getMenuIdByMenuName(String menuName) {
        Restaurant selectedRestaurant = mRestaurantList.get(mSelectedRestaurantIdx);

        return null;
    }

    public String getMenuPriceByMenuName(String menuName) {
        Restaurant selectedRestaurant = mRestaurantList.get(mSelectedRestaurantIdx);

        return null;
    }

    //최종적으로 Orderfragment 인보이스 레이아웃에 add..
    private void makeOrderItemInvoiceArrViews(List<OrderItemInvoice> mOrderItemInvoiceArrayList) {
        binding.orderBrowserLinearLayout.removeAllViewsInLayout();

        int totalCount = 0;
        if (mOrderItemInvoiceArrayList.size() == 0) {
            binding.infoTextView.setVisibility(View.VISIBLE);
            return;
        }
        for (int i = 0; mOrderItemInvoiceArrayList.size() > i; i++) {

            totalCount += mOrderItemInvoiceArrayList.get(i).mGuestNameOrders.size();
            OrderItemInvoiceView an_OrderItemInvoiceView = makeOrderItemInvoiceView(mOrderItemInvoiceArrayList.get(i));
            if (an_OrderItemInvoiceView != null)
                binding.orderBrowserLinearLayout.addView(an_OrderItemInvoiceView);
        }

        if (totalCount == 0) {
            binding.infoTextView.setVisibility(View.VISIBLE);
        }
    }

    private RestaurantMenu findMenuByName(String targetMenuName) {
        List<Category> categoryList;


        return null;
    }

    @SuppressLint("SetTextI18n")
    private void setTheTotalInvoice() {
        int theTotal = 0;
        for (int i = 0; mOrderDetailList.size() > i; i++)
            theTotal += Integer.parseInt(mOrderDetailList.get(i).getPaid_total_amount());
        binding.totalPriceTextView.setText(AppDef.priceMapper(theTotal) + "원");
    }

    private void sendShadeOrders() {
        //여기 다시수정 mSelectedRestaurantTabIdx 가-1일경우

        mShadeOrders = new ShadeOrder(mRestaurantList.get(mSelectedRestaurantIdx).id, Global.reserveId, mOrderDetailList);

        showProgress("주문을 전송하고 있습니다.");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).sendShadeOrders(mContext, mShadeOrders, new DataInterface.ResponseCallback<ResponseData<Object>>() {
            @Override
            public void onSuccess(ResponseData<Object> response) {
                if (response.getResultCode().equals("ok")) {
                    hideProgress();
                    binding.relSendOrder.setVisibility(View.INVISIBLE);
                    binding.relOrderHistory.setVisibility(View.VISIBLE);
                    mNumOfOrderHistory++;
                    refresh();

                    Toast.makeText(getActivity(), "주문이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(ResponseData<Object> response) {
                hideProgress();
            }

            @Override
            public void onFailure(Throwable t) {
                hideProgress();
            }
        });
    }

    //******************************************************************************************************
    private void initRecyclerView(RecyclerView recycler, List<RestaurantMenu>storeMenus) {
        //mWholeMenuList = makeTotalMenuList(selectedRestaurant);
        mMenuAdapter = new RestaurantMenuAdapter(mContext, storeMenus);

        GridLayoutManager mManager = new GridLayoutManager(getActivity(), 2);
        mManager.setOrientation(RecyclerView.VERTICAL);
        recycler.setLayoutManager(mManager);

        recycler.setAdapter(mMenuAdapter);
        recycler.setHasFixedSize(true);
        mMenuAdapter.notifyDataSetChanged();
        recycler.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        //At this point the layout is complete and the
                        //dimensions of recyclerView and any child views are known.
                        //Remove listener after changed RecyclerView's height to prevent infinite loop
                        recycler.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }

    //******************************************************************************************************
    private void initCategoryRecyclerView(RecyclerView recycler) {

        ArrayList<Category> categoryList = mRestaurantList.get(mSelectedRestaurantIdx).categoryList;
        mCategoryAdapter = new RestaurantCategoryAdapter(mContext, categoryList, new RestaurantCategoryAdapter.IOnCategoryClickAdapter() {
            @Override
            public void onApplyCategory(String ct1Id, String ct2Id) {
                getStoreMenu(mRestaurantList.get(mSelectedRestaurantIdx).id, ct2Id);
            }
        });

        recycler.setAdapter(mCategoryAdapter);
        binding.recyclerCategory.setLayoutManager(new SnappingLinearLayoutManager(mContext, 1, false));
        recycler.setHasFixedSize(true);
        mCategoryAdapter.notifyDataSetChanged();

        recycler.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        //At this point the layout is complete and the
                        //dimensions of recyclerView and any child views are known.
                        //Remove listener after changed RecyclerView's height to prevent infinite loop
                        recycler.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

        getStoreMenu(mRestaurantList.get(0).id, mRestaurantList.get(0).categoryList.get(0).catergory1_id);
        mCategoryAdapter.firstSelect();
    }

    private void getRestaurantMenu() {
        showProgress("식당 메뉴 정보를 가져오는 중입니다.");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getRestaurantMenu(getActivity(), new DataInterface.ResponseCallback<ResponseData<Restaurant>>() {
            @Override
            public void onSuccess(ResponseData<Restaurant> response) {
                hideProgress();
                systemUIHide();
                if (response.getResultCode().equals("ok")) {
                    mRestaurantList = (ArrayList<Restaurant>) response.getList();
                    initRestaurantRecyclerView();
                    initFoodImage();
                    initCategoryRecyclerView(binding.recyclerCategory);
                    refresh();
                    openTempSavedOrderList();
                    getStoreOrder();
                } else if (response.getResultCode().equals("fail")) {
                    Toast.makeText(getActivity(), response.getResultMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(ResponseData<Restaurant> response) {
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

    private void getStoreMenu(String restaurantId, String categoryId) {
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getStoreMenu(getContext(), restaurantId, categoryId, new DataInterface.ResponseCallback<ResponseData<RestaurantMenu>>() {
            @Override
            public void onSuccess(ResponseData<RestaurantMenu> response) {

                if (response.getResultCode().equals("ok")) {
                    initRecyclerView(binding.recyclerMenu, response.getList());
                }
            }

            @Override
            public void onError(ResponseData<RestaurantMenu> response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void initRestaurantRecyclerView() {
        LinearLayoutManager mManager;

        mManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        binding.rvRestaurant.setLayoutManager(mManager);

        RestaurantListAdapter adapter = new RestaurantListAdapter(mContext, new RestaurantListAdapter.IOnClickAdapter() {
            @Override
            public void onAdapterItemClicked(Integer id) {
                mSelectedRestaurantIdx = id;
                initCategoryRecyclerView(binding.recyclerCategory);
            }
        });

        binding.rvRestaurant.setAdapter(adapter);

        //처음엔 무조건 첫번째 식당이 선택되도록
        mRestaurantList.get(0).select = true;
        adapter.setData(mRestaurantList);
    }

    private void refreshCategory() {
        //   mMenuAdapter.setAllRestaurantMenuUnSelected();
        if (mMenuAdapter != null)
            mMenuAdapter.notifyDataSetChanged();
    }

    private void getStoreOrder() {
        setProgressMessage("주문내역 정보를 가져오는 중입니다.");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getStoreOrder(getActivity(), Global.selectedReservation.getReserveNo(), new DataInterface.ResponseCallback<ResponseData<StoreOrder>>() {
            @Override
            public void onSuccess(ResponseData<StoreOrder> response) {
                hideProgress();
                systemUIHide();
                if (response.getResultCode().equals("ok")) {

                    AppDef.storeOrderArrayList = (ArrayList<StoreOrder>) response.getList();
                    if (mSelectedRestaurantIdx > 0) {
                        mNumOfOrderHistory = AppDef.storeOrderArrayList.get(mSelectedRestaurantIdx).tablet_order_list.size();
                    } else { //대식당 주문내역 찾기
                        mNumOfOrderHistory = OrderDetailHistoryFragment.findStoreOrderByRestaurantName(AppDef.storeOrderArrayList, "대식당").tablet_order_list.size();
                    }

                    //       ((Restaurant) mTvTheRestaurant.getTag()
                    if (mNumOfOrderHistory > 0) {
                        binding.relSendOrder.setVisibility(View.INVISIBLE);
                        binding.relOrderHistory.setVisibility(View.VISIBLE);
                        binding.btnOrderHistory.setText(mNumOfOrderHistory + "건 주문내역 보기");
                    }
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
}