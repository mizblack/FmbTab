package com.eye3.golfpay.fmb_tab.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.order.Category;
import com.eye3.golfpay.fmb_tab.model.order.NameOrder;
import com.eye3.golfpay.fmb_tab.model.order.OrderDetail;
import com.eye3.golfpay.fmb_tab.model.order.OrderItemInvoice;
import com.eye3.golfpay.fmb_tab.model.order.OrderedMenuItem;
import com.eye3.golfpay.fmb_tab.model.order.Restaurant;
import com.eye3.golfpay.fmb_tab.model.order.RestaurantMenu;
import com.eye3.golfpay.fmb_tab.model.order.ShadeOrder;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.net.ResponseData;
import com.eye3.golfpay.fmb_tab.view.NameOrderView;
import com.eye3.golfpay.fmb_tab.view.OrderItemInvoiceView;

import java.util.ArrayList;
import java.util.Objects;

public class OrderFragment extends BaseFragment {
    private View v;
    private static int NUM_OF_RESTAURANT;
    protected String TAG = getClass().getSimpleName();
    private Button orderOrApplyBtn;
    private ArrayList<Restaurant> mRestaurantList = new ArrayList<>();
    private RecyclerView mRecyclerCategory;
    private CategoryAdapter mCateAdapter;
    private TextView[] mRestaurantTabBarArr;
    private ImageView mFoodImage;
    private int mSelectedRestaurantTabIdx = 0;
    //탭홀더
    private LinearLayout mTabLinear, mGuestContainer, mOrderBrowserLinearLayout;
    private ShadeOrder mShadeOrders;
    private OrderedMenuItem mOrderedMenuItem = null;
    // 최상위 카테고리 이름
    private TextView mTVCateName, infoTextView, mTvTheRestaurant;
    private TextView mTotalPriceTextView;
    private Button mResetButton;
    ArrayList<OrderItemInvoice> mOrderItemInvoiceArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getRestaurantMenu();
    }

    @Override
    public void onResume() {
        super.onResume();
        systemUIHide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fr_restaurant_order, container, false);
        mTabLinear = v.findViewById(R.id.tabLinearLayout);
        mTvTheRestaurant = v.findViewById(R.id.tvTheRestaurant);
        mGuestContainer = v.findViewById(R.id.guest_container);
        mTVCateName = v.findViewById(R.id.tv_cate_name);
        infoTextView = v.findViewById(R.id.infoTextView);
        mOrderBrowserLinearLayout = v.findViewById(R.id.orderBrowserLinearLayout);
        mTotalPriceTextView = v.findViewById(R.id.totalPriceTextView);
        createGuestList(mGuestContainer);
        mRecyclerCategory = v.findViewById(R.id.recycler_category);
        mFoodImage = v.findViewById(R.id.img_food);
        mParentActivity.showMainBottomBar();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                mTabLinear.addView(tvRestTabBar[i]);
            }
        }
        initSelectedRestaurantTabColor();
    }

    private void initSelectedRestaurantTabColor() {
        mRestaurantTabBarArr[0].setTextColor(Color.BLACK);
    }

    //레스토랑바 선택시 보여주는 함수
    private void selectRestaurant(int selectedTabIdx) {
        refreshCategory();
        mCateAdapter.mMenuAdapter.notifyDataSetChanged();
        resetGuestList();

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

    private void setSelectedRestaurant(int mSelectedRestaurantIdx) {
        if (mSelectedRestaurantIdx == -1) {
            initRecyclerView(mRecyclerCategory, mCateAdapter, ((Restaurant) mTvTheRestaurant.getTag()).categoryList);
            return;
        }
        initRecyclerView(mRecyclerCategory, mCateAdapter, mRestaurantList.get(mSelectedRestaurantIdx).categoryList);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        orderOrApplyBtn = Objects.requireNonNull(getView()).findViewById(R.id.orderOrApplyTextView);
        mResetButton = Objects.requireNonNull(getView()).findViewById(R.id.resetButton);

        orderOrApplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderOrApplyBtn.getText().toString().equals("주문하기")) {
                    //    tabsLinearLayout.setVisibility(View.GONE);
                    //   applyTabLinearLayout.setVisibility(View.VISIBLE);
                    //   orderOrApplyBtn.setText("적용하기");
                    sendShadeOrders();
                } else if (orderOrApplyBtn.getText().toString().equals("적용하기")) {
                    // GoNativeScreenAdd(new ShadePaymentFragment(), null);
                }
            }
        });

        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init();
            }
        });
    }


    void initOrderDetailList() {
        Global.orderDetailList.clear();
        int Size = Global.selectedReservation.getGuestData().size();
        //최초주문시 사이즈가 0이면
        if (Global.orderDetailList.size() == 0) {
            for (int i = 0; Size > i; i++) {
                Global.orderDetailList.add(i, new OrderDetail(Global.selectedReservation.getGuestData().get(i).getId()));
            }

        }
    }

    private void init() {
        initOrderDetailList();
        orderOrApplyBtn.setText("주문하기");
        mOrderedMenuItem = null;


        mShadeOrders = null;
        mOrderBrowserLinearLayout.removeAllViewsInLayout();
        mTotalPriceTextView.setText("");
        mFoodImage.setImageResource(R.drawable.ic_noimage);
        refreshCategory();
        mCateAdapter.mMenuAdapter.notifyDataSetChanged();
        resetGuestList();
    }

    private void resetGuestList() {
        for (int i = 0; mGuestContainer.getChildCount() > i; i++) {
            mGuestContainer.getChildAt(i).setBackgroundResource(R.drawable.shape_gray_edge);
            ((TextView) mGuestContainer.getChildAt(i)).setTextColor(Color.parseColor("#999999"));
        }
    }

    private void refreshCategory() {
        mCateAdapter.setAllRestaurantMenuUnSelected();
        for (int i = 0; mRestaurantList.size() > i; i++)
            mCateAdapter.notifyDataSetChanged();

        mCateAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView(RecyclerView recycler, CategoryAdapter cateAdapter, ArrayList<Category> categoryList) {
        mCateAdapter = new CategoryAdapter(mContext, categoryList);
        recycler.setAdapter(mCateAdapter);
        recycler.setHasFixedSize(true);
        LinearLayoutManager mManager = new LinearLayoutManager(mContext);
        recycler.setLayoutManager(mManager);
        cateAdapter.notifyDataSetChanged();
    }

    private void getRestaurantMenu() {
        showProgress("식당 메뉴 정보를 가져오는 중입니다.");
        DataInterface.getInstance().getRestaurantMenu(getActivity(), Global.CaddyNo, Global.selectedReservation.getReserveNo(), new DataInterface.ResponseCallback<ResponseData<Restaurant>>() {
            @Override
            public void onSuccess(ResponseData<Restaurant> response) {
                hideProgress();
                systemUIHide();
                if (response.getResultCode().equals("ok")) {
                    response.getData();
                    mRestaurantList = (ArrayList<Restaurant>) response.getList();
                    NUM_OF_RESTAURANT = mRestaurantList.size();
                    mRestaurantTabBarArr = new TextView[NUM_OF_RESTAURANT];
                    createTabBar(mRestaurantTabBarArr, mRestaurantList);
                    mCateAdapter = new CategoryAdapter(mContext, mRestaurantList.get(0).categoryList);
                    mRecyclerCategory.setAdapter(mCateAdapter);
                    mRecyclerCategory.setHasFixedSize(true);
                    LinearLayoutManager mManager = new LinearLayoutManager(mContext);
                    mRecyclerCategory.setLayoutManager(mManager);
                    mCateAdapter.notifyDataSetChanged();
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

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryItemViewHolder> {
        Context mContext;

        ArrayList<Category> mCategoryList;
        MenuAdapter mMenuAdapter;

        CategoryAdapter(Context context, ArrayList<Category> categoryList) {

            mContext = context;
            mCategoryList = categoryList;
        }

        @NonNull
        @Override
        //recyclerView 가 parent 임
        public CategoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.restaurant_cate_row, parent, false);
            return new CategoryItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final CategoryItemViewHolder holder,
                                     int position) {
            ArrayList<RestaurantMenu> menuList = mCategoryList.get(position).Menus;
            mTVCateName.setText(mCategoryList.get(position).catergory_name);
            mMenuAdapter = new MenuAdapter(mContext, menuList);
            holder.mRecyclerMenu.setAdapter(mMenuAdapter);
            holder.mRecyclerMenu.setHasFixedSize(true);
            LinearLayoutManager mManager = new LinearLayoutManager(mContext);
            holder.mRecyclerMenu.setLayoutManager(mManager);
        }


        @Override
        public int getItemCount() {
            return mCategoryList.size();
        }

        class CategoryItemViewHolder extends RecyclerView.ViewHolder {
            RecyclerView mRecyclerMenu;

            //onCreateViewHolder 의 view 임()
            CategoryItemViewHolder(@NonNull final View itemView) {
                super(itemView);
                mRecyclerMenu = itemView.findViewById(R.id.recycler_menu);

            }

        }

        void setAllRestaurantMenuUnSelected() {
            for (int i = 0; mCategoryList.size() > i; i++) {
                ArrayList<RestaurantMenu> tempList = mCategoryList.get(i).Menus;
                for (int j = 0; tempList.size() > j; j++) {
                    tempList.get(j).isSelected = false;

                }
            }
            notifyDataSetChanged();
        }

        private class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuItemViewHolder> {
            Context mContext;
            ArrayList<RestaurantMenu> mMenuList;

            MenuAdapter(Context context, ArrayList<RestaurantMenu> menuList) {
                mContext = context;
                mMenuList = menuList;
            }

            @NonNull
            @Override
            // recyclerView 가 parent 임
            public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.restaurant_menu_row, parent, false);
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
            public void onBindViewHolder(@NonNull final MenuAdapter.MenuItemViewHolder holder, int position) {
                final int idx = position;
                if (mMenuList.get(idx).isSelected) {
                    holder.itemView.setBackgroundResource(R.drawable.shape_gray_edge);
                    holder.tvMenuName.setTextColor(getResources().getColor(R.color.black, Objects.requireNonNull(getActivity()).getTheme()));
                    holder.tvPrice.setTextColor(getResources().getColor(R.color.black, Objects.requireNonNull(getActivity()).getTheme()));
                } else {
                    holder.itemView.setBackgroundColor(getResources().getColor(R.color.lightAliceBlue, Objects.requireNonNull(getActivity()).getTheme()));
                    holder.tvMenuName.setTextColor(getResources().getColor(R.color.gray, Objects.requireNonNull(getActivity()).getTheme()));
                    holder.tvPrice.setTextColor(getResources().getColor(R.color.gray, Objects.requireNonNull(getActivity()).getTheme()));
                }

                holder.tvMenuName.setText(mMenuList.get(idx).name);
                holder.tvPrice.setText(priceMapper(Integer.parseInt(mMenuList.get(idx).price)));
                holder.tvMenuId.setText(mMenuList.get(idx).id);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setAllRestaurantMenuUnSelected();
                        resetGuestList();
                        mMenuList.get(idx).isSelected = true;
                        setFoodImage(mFoodImage, mMenuList.get(idx).image);
                        mOrderedMenuItem = new OrderedMenuItem(holder.tvMenuId.getText().toString().trim(), "1", mMenuList.get(idx).price.trim(), mMenuList.get(idx).name);
                        //category  전체를 refresh 해야한다.
                        CategoryAdapter.this.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public int getItemCount() {
                return mMenuList.size();
            }

            class MenuItemViewHolder extends RecyclerView.ViewHolder {
                TextView tvMenuName, tvPrice, tvMenuId;

                //onCreateViewHolder 의 view 임()
                MenuItemViewHolder(@NonNull final View itemView) {
                    super(itemView);
                    tvMenuName = itemView.findViewById(R.id.tv_menu_name);
                    tvPrice = itemView.findViewById(R.id.tv_menu_price);
                    tvMenuId = itemView.findViewById(R.id.tv_menu_id);

                }
            }
        }

        void setFoodImage(ImageView img, String url) {
            if (img != null) {
                Glide.with(mContext)
                        .load("http://testerp.golfpay.co.kr" + url)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.ic_noimage)
                        .into(mFoodImage);
            }
        }
    }

    private void createGuestList(LinearLayout container) {
        initOrderDetailList();
        for (int i = 0; Global.orderDetailList.size() > i; i++) {
            final int idx = i;
            TextView tv = new TextView(new ContextThemeWrapper(getActivity(), R.style.ShadeGuestNameTextView), null, 0);
            final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
            final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width, height);
            tv.setLayoutParams(param);
            tv.setTag(Global.selectedReservation.getGuestData().get(idx).getId());
            tv.setText(Global.selectedReservation.getGuestData().get(idx).getGuestName());
            tv.setBackgroundResource(R.drawable.shape_gray_edge);

            tv.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    //주문된 음식이 있다면
                    if (mOrderedMenuItem != null) {
                        mOrderBrowserLinearLayout.removeAllViewsInLayout();
                        if (v.getTag().equals(Global.orderDetailList.get(idx).reserve_guest_id)) {
                            infoTextView.setVisibility(View.GONE);
                            ((TextView) v).setTextColor(getResources().getColor(R.color.white, Objects.requireNonNull(getActivity()).getTheme()));
                            (v).setBackgroundResource(R.drawable.shape_ebony_black_background_and_edge);
                            Global.orderDetailList.get(idx).addOrPlusOrderedMenuItem(mOrderedMenuItem);
                            Global.orderDetailList.get(idx).setTotalPaidAmount(Global.orderDetailList.get(idx).getPaid_total_amount());
                            makeOrderItems(Global.orderDetailList);
                            makeOrderItemInvoiceArrViews();

                            setTheTotalInvoice();
                        } else {
                            Toast.makeText(mContext, "주문상세 주문자가 불일치합니다.", Toast.LENGTH_SHORT).show();
                        }

                    } else
                        Toast.makeText(mContext, "주문한 음식이 없습니다. 먼저 음식을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                }
            });
            container.addView(tv);
        }
    }

    private void makeOrderItems(ArrayList<OrderDetail> orderDetailList) {
        mOrderItemInvoiceArrayList = null;
        mOrderItemInvoiceArrayList = new  ArrayList<OrderItemInvoice>();
        OrderItemInvoice a_ItemInvoice = null;
        for (int i = 0; orderDetailList.size() > i; i++) {
            OrderDetail a_orderDetail = orderDetailList.get(i);
          final int Size =  mOrderItemInvoiceArrayList.size();
            if (Size == 0) {
                a_ItemInvoice = new OrderItemInvoice();
                for (int k = 0; a_orderDetail.getOrderedMenuItemList().size() > k; k++) {
                    if (a_ItemInvoice.mMenunName.equals(a_orderDetail.getOrderedMenuItemList().get(k).name)) {

                        //메뉴이름이 같을때
                        a_ItemInvoice.mQty += Integer.valueOf(a_orderDetail.getOrderedMenuItemList().get(k).getQty());
                        a_ItemInvoice.mNameOrders.add(new NameOrder(a_orderDetail.reserve_guest_id, Integer.valueOf(a_orderDetail.getOrderedMenuItemList().get(k).getQty())));
                    } else {


                        a_ItemInvoice.mMenunName = a_orderDetail.getOrderedMenuItemList().get(k).name;
                        a_ItemInvoice.mQty = Integer.valueOf(a_orderDetail.getOrderedMenuItemList().get(0).getQty());
                        a_ItemInvoice.mNameOrders.add((new NameOrder(a_orderDetail.reserve_guest_id, Integer.valueOf(a_orderDetail.getOrderedMenuItemList().get(0).getQty()))));
                        mOrderItemInvoiceArrayList.add(a_ItemInvoice);
                    }
                }

            } else {
               final int Size2 =  mOrderItemInvoiceArrayList.size();
                for (int j = 0; Size2 > j; j++) {
                    a_ItemInvoice = mOrderItemInvoiceArrayList.get(j);
                   final int Size3 = a_orderDetail.getOrderedMenuItemList().size();
                    for (int k = 0; Size3 > k; k++) {
                        if (a_ItemInvoice.mMenunName.equals(a_orderDetail.getOrderedMenuItemList().get(k).name)) {

                            //메뉴이름이 같을때
                            a_ItemInvoice.mQty += Integer.valueOf(a_orderDetail.getOrderedMenuItemList().get(k).getQty());
                            a_ItemInvoice.mNameOrders.set(k, new NameOrder(a_orderDetail.reserve_guest_id, a_ItemInvoice.mQty));
                        } else {

                            a_ItemInvoice = new OrderItemInvoice();
                            a_ItemInvoice.mMenunName = a_orderDetail.getOrderedMenuItemList().get(k).name;
                            a_ItemInvoice.mQty = Integer.valueOf(a_orderDetail.getOrderedMenuItemList().get(k).getQty());
                            a_ItemInvoice.mNameOrders.add((new NameOrder(a_orderDetail.reserve_guest_id, Integer.valueOf(a_orderDetail.getOrderedMenuItemList().get(k).getQty()))));
                            mOrderItemInvoiceArrayList.add(a_ItemInvoice);

                        }
                    }
                }
            }
        }
    }


    private OrderItemInvoiceView makeOrderItemInvoiceView(OrderItemInvoice orderItemInvoice) {
        OrderItemInvoiceView a_InvoiceView;
        if (orderItemInvoice == null || orderItemInvoice.mMenunName == null)
            return null;
        else
            a_InvoiceView = new OrderItemInvoiceView(mContext);

        a_InvoiceView.setTag(orderItemInvoice);
        for (int i = 0; orderItemInvoice.mNameOrders.size() > i; i++) {
            NameOrderView a_NameOrderview = new NameOrderView(mContext);
            a_NameOrderview.setTag(orderItemInvoice.mNameOrders.get(i));
            a_NameOrderview.mNameTv.setText(((NameOrder) a_NameOrderview.getTag()).name);
            a_NameOrderview.mQtyTv.setText(String.valueOf(((NameOrder) a_NameOrderview.getTag()).qty));
            a_InvoiceView.mLinearNameOrder.addView(a_NameOrderview);
        }
        a_InvoiceView.mTvMenuName.setText(((OrderItemInvoice) a_InvoiceView.getTag()).mMenunName);
        int _qty = ((OrderItemInvoice) a_InvoiceView.getTag()).mQty;
        a_InvoiceView.mTvQty.setText(String.valueOf(_qty));
        return a_InvoiceView;
    }

    //최종적으로 Orderfragment 인보이스 레이아웃에 add..
    private void makeOrderItemInvoiceArrViews() {
        mOrderBrowserLinearLayout = null;
        mOrderBrowserLinearLayout = v.findViewById(R.id.orderBrowserLinearLayout);
        for (int i = 0; mOrderItemInvoiceArrayList.size() > i; i++) {

            OrderItemInvoiceView an_OrderItemInvoiceView = makeOrderItemInvoiceView(mOrderItemInvoiceArrayList.get(i));
            if (an_OrderItemInvoiceView != null)
                mOrderBrowserLinearLayout.addView(an_OrderItemInvoiceView);

        }

    }

    //인보이스상에 동일한 메뉴가 있으면 인덱스 a를 리턴한다.
    private int isAlreadyExist(OrderItemInvoice a_invoice, OrderDetail a_orderDetail) {
        for (int i = 0; mOrderItemInvoiceArrayList.size() > i; i++)
            if (a_invoice.mMenunName.equals(a_orderDetail.getOrderedMenuItemList().get(i).name)) {
                return i;
            }

        return -1;
    }

    @SuppressLint("SetTextI18n")
    private void setTheTotalInvoice() {
        int theTotal = 0;
        for (int i = 0; Global.orderDetailList.size() > i; i++)
            theTotal += Integer.parseInt(Global.orderDetailList.get(i).getPaid_total_amount());
        mTotalPriceTextView.setText(theTotal + "원");
    }

    private void sendShadeOrders() {
        //여기 다시수정 mSelectedRestaurantTabIdx 가-1일경우
        if (mSelectedRestaurantTabIdx < 0)
            mShadeOrders = new ShadeOrder(((Restaurant) mTvTheRestaurant.getTag()).id, Global.reserveId, Global.orderDetailList);
        else
            mShadeOrders = new ShadeOrder(mRestaurantList.get(mSelectedRestaurantTabIdx).id, Global.reserveId, Global.orderDetailList);
        showProgress("주문을 전송하고 있습니다.");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).sendShadeOrders(mContext, mShadeOrders, new DataInterface.ResponseCallback<ResponseData<Object>>() {
            @Override
            public void onSuccess(ResponseData<Object> response) {
                if (response.getResultCode().equals("ok")) {
                    hideProgress();
                    orderOrApplyBtn.setText("적용하기");
                    Toast.makeText(getActivity(), "주문이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    init();
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
}