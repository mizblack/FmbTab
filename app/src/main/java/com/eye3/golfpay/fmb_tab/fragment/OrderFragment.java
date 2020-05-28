package com.eye3.golfpay.fmb_tab.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.activity.MainActivity;
import com.eye3.golfpay.fmb_tab.adapter.RestaurantListAdapter;
import com.eye3.golfpay.fmb_tab.common.AppDef;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.dialog.MenuCategoryDialog;
import com.eye3.golfpay.fmb_tab.dialog.PopupDialog;
import com.eye3.golfpay.fmb_tab.dialog.RestaurantsPopupDialog;
import com.eye3.golfpay.fmb_tab.model.notice.NoticeItem;
import com.eye3.golfpay.fmb_tab.model.order.Category;
import com.eye3.golfpay.fmb_tab.model.order.Category2;
import com.eye3.golfpay.fmb_tab.model.order.GuestNameOrder;
import com.eye3.golfpay.fmb_tab.model.order.OrderDetail;
import com.eye3.golfpay.fmb_tab.model.order.OrderItemInvoice;
import com.eye3.golfpay.fmb_tab.model.order.OrderedMenuItem;
import com.eye3.golfpay.fmb_tab.model.order.Restaurant;
import com.eye3.golfpay.fmb_tab.model.order.RestaurantMenu;
import com.eye3.golfpay.fmb_tab.model.order.RestaurantMenuOrder;
import com.eye3.golfpay.fmb_tab.model.order.ShadeOrder;
import com.eye3.golfpay.fmb_tab.model.order.StoreOrder;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.net.ResponseData;
import com.eye3.golfpay.fmb_tab.util.Util;
import com.eye3.golfpay.fmb_tab.view.NameOrderView;
import com.eye3.golfpay.fmb_tab.view.OrderItemInvoiceView;
import com.eye3.golfpay.fmb_tab.view.SnappingLinearLayoutManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.prefs.PreferenceChangeListener;


public class OrderFragment extends BaseFragment {
    private View v;
    private static int NUM_OF_RESTAURANT;
    private static int NUM_OF_SINGLE_ORDERED_ITEM = 1;
    private static String STRING_CADDY = "(캐디)";

    public static final int NUM_MENU_NAME_KEY = 1 + 2 << 24;
    public static final int NUM_NAMEORDER_KEY = 2 + 2 << 24;
    public static final int NUM_INVOICEORDER_KEY = 3 + 2 << 24;

    public String TAG = getClass().getSimpleName();
    private Button orderOrApplyBtn;
    private ArrayList<Restaurant> mRestaurantList = new ArrayList<>();
    private RecyclerView mRecyclerCategory;
    private TextView[] mRestaurantTabBarArr;
    private ImageView mFoodImage, mFoodNoImage;
    private int mSelectedRestaurantTabIdx = 0;
    //탭홀더
    private LinearLayout mOrderBrowserLinearLayout, mLinearSubMenu;
    private RecyclerView rv_restaurant;
    LinearLayout mGuestContainer;
    public static LinearLayout mTabsRootLinear;
    private ShadeOrder mShadeOrders;
    private ArrayList<OrderedMenuItem> mOrderedMenuItems = new ArrayList<>();
    private String mOrderedGuestId = "";


    // 최상위 카테고리 이름
    private TextView mTVCateName, mTvSubCateName, infoTextView;
    private TextView mTotalPriceTextView;
    private TextView mCancelButton;
    private TextView mTempSaveButton;
    //**********************************************************
    //임시저장을 제외한 주문은 mOrderDetailList으로 처리함 (AppDef.mOrderDetailList 사용안함)
    List<OrderDetail> mOrderDetailList = new ArrayList<>();//먼저 생성해야 아래리스트에 renew됨.
    List<OrderItemInvoice> mOrderItemInvoiceArrayList = new ArrayList<>();
    //**********************************************************

    private ImageView mArrowToApply;
    static TextView preSelectedGuestView;
    RelativeLayout mRelOrderHistory;
    ConstraintLayout mRelSendOrder;
    Button mBtnHistory, mBtnAdd;
    AppCompatSpinner mSpinnSubMenu;
    ArrayAdapter mSpinnAdapter;
    TextView mTvCaddy;
    RestaurantMenuOrder mRestaurantMenuOrder;

    List<RestaurantMenu> mWholeMenuList;
    MenuAdapter mMenuAdapter;
    int mNumOfOrderHistoryOfSelectedRestaurant = 0;

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
        v = inflater.inflate(R.layout.fr_restaurant_order, container, false);

        rv_restaurant = v.findViewById(R.id.rv_restaurant);
        mGuestContainer = v.findViewById(R.id.guest_container);
        mTVCateName = v.findViewById(R.id.tv_cate_name);
        mTvSubCateName = v.findViewById(R.id.tv_sub_cate_name);
        infoTextView = v.findViewById(R.id.infoTextView);
        mArrowToApply = v.findViewById(R.id.arrow_to_apply);
        mOrderBrowserLinearLayout = v.findViewById(R.id.orderBrowserLinearLayout);
        mTabsRootLinear = v.findViewById(R.id.tabsRootLinearLayout);
        mTotalPriceTextView = v.findViewById(R.id.totalPriceTextView);
        createGuestList(mGuestContainer);
        mRecyclerCategory = v.findViewById(R.id.recycler_category);
        mFoodImage = v.findViewById(R.id.img_food);
        mFoodNoImage = v.findViewById(R.id.img_no_img);
        mTempSaveButton = v.findViewById(R.id.btnTempSave);
        mLinearSubMenu = v.findViewById(R.id.linear_sub_menu);
        mRelOrderHistory = v.findViewById(R.id.rel_order_history);
        mRelSendOrder = v.findViewById(R.id.rel_send_order);
        mBtnHistory = v.findViewById(R.id.btn_order_history);
        mBtnAdd = v.findViewById(R.id.btn_add);
        mSpinnSubMenu = v.findViewById(R.id.spinn_sub_menu);
        mTvCaddy = v.findViewById(R.id.tvCaddy);
        mTvCaddy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mMenuAdapter.haveOrder()) {

                    for (RestaurantMenu item : mMenuAdapter.mMenuList) {

                        if (item.isSelected == false)
                            continue;

                        OrderedMenuItem menu = new OrderedMenuItem(item.id, "1", item.price, item.name, Global.CaddyNo);

                        //캐디주문을 표기한다.
                        infoTextView.setVisibility(View.GONE);
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
        });
        initOrderItemInvoiceView();
        mParentActivity.showMainBottomBar();
        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    private void initFoodImage() {
        if (mRestaurantList == null) {
            Toast.makeText(getActivity(), "존재하는 식당 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
//        if (mRestaurantList.get(0).categoryList == null || mRestaurantList.get(0).categoryList.size() == 0) {
//            Toast.makeText(getActivity(), "존재하는 식당 메뉴 카테고리가 없습니다.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (mRestaurantList.get(0).categoryList.get(0).subCategoryList.get(0)== null || mRestaurantList.get(0).categoryList.get(0).Menus.size() == 0) {
//            //    Toast.makeText(getActivity(), " 식당 메뉴가 없습니다.", Toast.LENGTH_SHORT).show();
//            return;
//        }

//        if (mRestaurantList.get(0).categoryList.get(0).subCategoryList.size() > 0) {

        //  if (mSelectedRestaurantTabIdx < 0)
        //  setFoodImage(mFoodImage, (((Restaurant) mTvTheRestaurant.getTag()).categoryList.get(0).subCategoryList.get(0).Menus.get(0).image));
        //  else
        //  setFoodImage(mFoodImage, (mRestaurantList.get(mSelectedRestaurantTabIdx).categoryList.get(0).subCategoryList.get(0).Menus.get(0).image));
        //    }
    }

    private int getIndexByName(String guestName) {
        for (int i = 0; mOrderDetailList.size() > i; i++) {
            if (getGuestName(mOrderDetailList.get(i).reserve_guest_id).equals(guestName))
                return i;
        }
        return -1;

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

        orderOrApplyBtn = Objects.requireNonNull(getView()).findViewById(R.id.orderOrApplyBtn);
        mCancelButton = Objects.requireNonNull(getView()).findViewById(R.id.resetButton);
        mTempSaveButton = Objects.requireNonNull(getView()).findViewById(R.id.btnTempSave);
        mArrowToApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTabsRootLinear.setVisibility(View.INVISIBLE);
                GoOrderLeftBoard(new OrderApplyFragment(), null);
            }
        });

        orderOrApplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendShadeOrders();


            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mMenuAdapter.clearOrder();
                mRestaurantMenuOrder.clearRestaurantMenuOrder();

                if (AppDef.restaurantOrderArrayList.size() > 0) {
                    //  mBtnHistory.setVisibility(View.VISIBLE);
                    orderOrApplyBtn.setVisibility(View.VISIBLE);
                } else {
                    refresh();
                    AppDef.orderDetailList.clear();
                    initOrderDetailList();
                    orderOrApplyBtn.setVisibility(View.VISIBLE);
                }
            }
        });


        //임시저장
        mTempSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDef.orderItemInvoiceArrayList = mOrderItemInvoiceArrayList;
                AppDef.orderDetailList = mOrderDetailList;
                AppDef.mTempSaveRestaurantIdx = mSelectedRestaurantTabIdx;
                //임시저장시 RestaurantOrder에 식당별로 add시키고
                Toast.makeText(getActivity(), "임시저장이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        mBtnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("restaurantList", mRestaurantList);
                bundle.putSerializable("orderdetailList", (Serializable) mOrderDetailList);
                bundle.putSerializable("selectedRestaurantTabIdx", mSelectedRestaurantTabIdx);
                bundle.putString("ani_direction", "down");
                GoNativeScreen(new OrderDetailHistoryFragment(), bundle);
            }
        });

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOrderDetailList.size() > 0)
                    sendShadeOrders();
                else
                    Toast.makeText(getActivity(), "먼저 주문을 해주십시요.", Toast.LENGTH_SHORT).show();
            }
        });

        mLinearSubMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int ui_flags =
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

                MenuCategoryDialog dlg = new MenuCategoryDialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dlg.getWindow().getDecorView().setSystemUiVisibility(ui_flags);

                dlg.getWindow().
                        setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

                dlg.show();

            }

//            @Override
//            public void onClick(View v) {
//                int selectedItem = -1;
//                List<Category2> spinnSubCate2MenuList = getSubMenuList("", mTVCateName.getText().toString().trim());
//                if (spinnSubCate2MenuList == null) {
//                    Toast.makeText(mContext, "서브카테고리가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                List<String> spinnSubMenuList = new ArrayList<>();
//
//                if (mSpinnAdapter != null)
//                    mSpinnAdapter.clear();
//
//                for (int i = 0; spinnSubCate2MenuList.size() > i; i++) {
//                    spinnSubMenuList.add(spinnSubCate2MenuList.get(i).catergory2_name);
//                }
//
//                mSpinnAdapter = new ArrayAdapter<String>(mContext, R.layout.sub_menu_spinner_item, spinnSubMenuList) {
//                    @Override
//                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
//                        View v = null;
//                        v = super.getDropDownView(position, null, parent);
//                        // If this is the selected item position
//                        if (position == selectedItem) {
//                            v.setBackgroundColor(Color.BLUE);
//
//                        } else {
//                            // for other views
//                            v.setBackgroundColor(Color.BLACK);
//
//                        }
//                        return v;
//                    }
//                };
//                mSpinnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                mSpinnSubMenu.setVisibility(View.VISIBLE);
//                mSpinnSubMenu.setAdapter(mSpinnAdapter);
//                mSpinnSubMenu.setSelection(0);
//                mSpinnSubMenu.setGravity(Gravity.CENTER);
//                mSpinnSubMenu.setBackgroundColor(Color.BLACK);
//                mSpinnSubMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        if (position > 0) {
//                            int subMenuZeroIdx = getPositionForSubMenuZeroItem(mTVCateName.getText().toString().trim(), spinnSubMenuList.get(position).toString());
//                            mTvSubCateName.setText(spinnSubMenuList.get(position));
//                            if (subMenuZeroIdx != -1) {
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        mRecyclerCategory.smoothScrollToPosition(subMenuZeroIdx);
//                                    }
//                                }, 100);
//
//                            } else
//                                Toast.makeText(mContext, "해당 서브카테고리에 메뉴가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                });
//            }

        });
    }

    //
    private List<RestaurantMenu> makeTotalMenuList(Restaurant selectedRestaurant) {

        int position = 0; //각 메뉴아이템당 새로운 index값 적용
        mWholeMenuList = new ArrayList<>();

        List<Category> categoryList = selectedRestaurant.categoryList;
        for (int i = 0; categoryList.size() > i; i++) {
            List<Category2> subCateList = categoryList.get(i).subCategoryList;
            if (subCateList != null) {
                for (int j = 0; subCateList.size() > j; j++) {
                    List<RestaurantMenu> menuList = subCateList.get(j).Menus;
                    if (menuList != null) {
                        for (int k = 0; menuList.size() > k; k++) {
                            if (k == 0)
                                mWholeMenuList.add(new RestaurantMenu(menuList.get(k).id, menuList.get(k).name, menuList.get(k).price, menuList.get(k).image, categoryList.get(i).catergory1_name, subCateList.get(j).catergory2_name, position));
                            else
                                mWholeMenuList.add(new RestaurantMenu(menuList.get(k).id, menuList.get(k).name, menuList.get(k).price, menuList.get(k).image, categoryList.get(i).catergory1_name, subCateList.get(j).catergory2_name, -1));
                            position++;
                        }
                    }
                }
            }
        }
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

    //최초 화면 초기화
    private void init() {
        mMenuAdapter.clearOrder();
        mOrderItemInvoiceArrayList = new ArrayList<OrderItemInvoice>();
        mOrderedGuestId = null;
        mShadeOrders = null;
        mOrderBrowserLinearLayout.removeAllViewsInLayout();
        mTotalPriceTextView.setText("");
        initFoodImage();
        refreshCategory();
//        if (mCateAdapter != null && mCateAdapter.mMenuAdapter != null)
//            mCateAdapter.mMenuAdapter.notifyDataSetChanged();
        resetGuestList();
    }

    private void refresh() {
        initOrderDetailList();
        mMenuAdapter.clearOrder();
        mOrderItemInvoiceArrayList = new ArrayList<OrderItemInvoice>();
        mOrderedGuestId = null;
        mShadeOrders = null;
        mOrderBrowserLinearLayout.removeAllViewsInLayout();
        mTotalPriceTextView.setText("");
        initFoodImage();
        refreshCategory();
//        if (mCateAdapter != null && mCateAdapter.mMenuAdapter != null)
//            mCateAdapter.mMenuAdapter.notifyDataSetChanged();
        resetGuestList();
    }

    private void resetGuestList() {
        for (int i = 0; mGuestContainer.getChildCount() > i; i++) {
            //mGuestContainer.getChildAt(i).setBackgroundResource(R.drawable.shape_gray_edge);
            ((TextView) mGuestContainer.getChildAt(i)).setTextColor(Color.parseColor("#999999"));
        }
    }


    private void setFoodImage(ImageView img, String url) {
        if (img != null) {

            mFoodNoImage.setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(Global.HOST_BASE_ADDRESS_AWS + url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            mFoodNoImage.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(mFoodImage);
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
            TextView tv = new TextView(new ContextThemeWrapper(getActivity(), R.style.ShadeGuestNameTextView), null, 0);
            final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
            final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 94, getResources().getDisplayMetrics()) - 1;
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width, height);
            param.leftMargin = 0;
            param.topMargin = 1;

            if (i == 0) {
                param.topMargin = 2;
            }

            tv.setLayoutParams(param);
            tv.setTag(Global.selectedReservation.getGuestData().get(idx).getId());
            tv.setText(Global.selectedReservation.getGuestData().get(idx).getGuestName());

            tv.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    //이미선택된 게스트가 있다면 초기화하자
                    if (preSelectedGuestView != null) {
                        preSelectedGuestView.setTextAppearance(R.style.ShadeGuestNameTextView);
                        preSelectedGuestView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                    }
                    preSelectedGuestView = (TextView) v;
                    preSelectedGuestView.setTextAppearance(R.style.ShadeGuestNameSelectTextView);
                    preSelectedGuestView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.ebonyBlack));
                    mOrderedGuestId = (String) v.getTag();
                    //주문된 음식이 있다면

                    if (mMenuAdapter.haveOrder()) {
                        if (v.getTag().equals(mOrderDetailList.get(idx).reserve_guest_id)) {

                            for (RestaurantMenu item : mMenuAdapter.mMenuList) {

                                if (item.isSelected == false)
                                    continue;

                                OrderedMenuItem menu = new OrderedMenuItem(item.id, "1", item.price, item.name, "");

                                infoTextView.setVisibility(View.GONE);
                                ((TextView) v).setTextColor(getResources().getColor(R.color.white, Objects.requireNonNull(getActivity()).getTheme()));
                                //(v).setBackgroundResource(R.drawable.shape_ebony_black_background_and_edge);
                                mRestaurantMenuOrder.setOrderedGuestId((String) v.getTag());
                                mRestaurantMenuOrder.setmCurrentOrderedMenuItem(menu);
                                mRestaurantMenuOrder.addRestaurantMenuOrder(new OrderedMenuItem(menu.id, "1", menu.price, menu.menuName, ""), (String) v.getTag());
                                makeOrderItemInvoiceArrViews(mRestaurantMenuOrder.getmOrderItemInvoiceArrayList());
                                setTheTotalInvoice();
                            }

                            mMenuAdapter.clearOrder();

                        } else {
                            Toast.makeText(mContext, "주문상세 주문자가 불일치합니다.", Toast.LENGTH_SHORT).show();
                            mMenuAdapter.clearOrder();
                            refreshCategory();
                        }
                    } else {
                        Toast.makeText(mContext, "주문한 음식이 없습니다. 먼저 음식을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            container.addView(tv);
        }
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

//                        a_orderedMenuItemList.get(j).qty = String.valueOf(Integer.valueOf(a_orderedMenuItemList.get(j).qty) - deletedGuestNameOrder.qty);
//                        if (a_orderedMenuItemList.get(j).qty.equals("0")) {
//                            mOrderDetailList.get(i).setTotalPaidAmount(mOrderDetailList.get(i).getPaid_total_amount());
//                            a_orderedMenuItemList.remove(j);
//                        }
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
                        return;
                    }
                }
            }

            setTheTotalInvoice();
            makeOrderItemInvoiceArrViews(mRestaurantMenuOrder.getmOrderItemInvoiceArrayList());
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
                if (minusNameOrder.qty == 0) {
                    Toast.makeText(getActivity(), "수량이 0 입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                mRestaurantMenuOrder.setOrderedGuestId(getGuestId(minusNameOrder.mGuestName));
                mRestaurantMenuOrder.addRestaurantMenuOrder(new OrderedMenuItem(getMenuIdByMenuName(minusNameOrder.mMenuName),
                        "-1", getMenuPriceByMenuName(minusNameOrder.mMenuName),
                        minusNameOrder.mMenuName,
                        minusNameOrder.caddy_id),
                        getGuestId(minusNameOrder.mGuestName));

                makeOrderItemInvoiceArrViews(mRestaurantMenuOrder.getmOrderItemInvoiceArrayList());
                setTheTotalInvoice();
            }
        }
    };

    public String getMenuIdByMenuName(String menuName) {
        Restaurant selectedRestaurant = mRestaurantList.get(mSelectedRestaurantTabIdx);
        for (int i = 0; selectedRestaurant.categoryList.size() > i; i++) {
            if (selectedRestaurant.categoryList.get(i) == null)
                break;
            for (int j = 0; selectedRestaurant.categoryList.get(i).subCategoryList.size() > j; j++) {
                if (selectedRestaurant.categoryList.get(i).subCategoryList.get(j) == null)
                    break;
                for (int k = 0; selectedRestaurant.categoryList.get(i).subCategoryList.get(j).Menus.size() > k; k++) {
                    if (selectedRestaurant.categoryList.get(i).subCategoryList.get(j).Menus.get(k) == null)
                        break;
                    if (selectedRestaurant.categoryList.get(i).subCategoryList.get(j).Menus.get(k).name.equals(menuName)) {
                        return selectedRestaurant.categoryList.get(i).subCategoryList.get(j).Menus.get(k).id;
                    }
                }
            }
        }
        return null;
    }

    public String getMenuPriceByMenuName(String menuName) {
        Restaurant selectedRestaurant = mRestaurantList.get(mSelectedRestaurantTabIdx);
        for (int i = 0; selectedRestaurant.categoryList.size() > i; i++) {
            if (selectedRestaurant.categoryList.get(i) == null)
                break;
            for (int j = 0; selectedRestaurant.categoryList.get(i).subCategoryList.size() > j; j++) {
                if (selectedRestaurant.categoryList.get(i).subCategoryList.get(j) == null)
                    break;
                for (int k = 0; selectedRestaurant.categoryList.get(i).subCategoryList.get(j).Menus.size() > k; k++) {
                    if (selectedRestaurant.categoryList.get(i).subCategoryList.get(j).Menus.get(k) == null)
                        break;
                    if (selectedRestaurant.categoryList.get(i).subCategoryList.get(j).Menus.get(k).name.equals(menuName)) {
                        return selectedRestaurant.categoryList.get(i).subCategoryList.get(j).Menus.get(k).price;
                    }
                }
            }
        }
        return null;
    }


    private void deleteNameOrderFromInvoiceArrayList(String menuName, GuestNameOrder deletedGuestNameOrder) {
        for (int i = 0; mOrderItemInvoiceArrayList.size() > i; i++) {
            OrderItemInvoice a_itemInvoice = mOrderItemInvoiceArrayList.get(i);
            if (a_itemInvoice.mMenunName.equals(menuName)) {
                for (int j = 0; a_itemInvoice.mGuestNameOrders.size() > j; j++) {
                    if ((a_itemInvoice.mGuestNameOrders.get(j).mGuestName).equals(deletedGuestNameOrder.mGuestName) && a_itemInvoice.mGuestNameOrders.get(j).qty == deletedGuestNameOrder.qty) {
                        a_itemInvoice.mGuestNameOrders.remove(a_itemInvoice.mGuestNameOrders.get(j));
                        a_itemInvoice.mQty -= deletedGuestNameOrder.qty;
                    }
                }
            }
        }
    }

    //최종적으로 Orderfragment 인보이스 레이아웃에 add..
    private void makeOrderItemInvoiceArrViews(List<OrderItemInvoice> mOrderItemInvoiceArrayList) {
        mOrderBrowserLinearLayout.removeAllViewsInLayout();

        int totalCount = 0;
        if (mOrderItemInvoiceArrayList.size() == 0) {
            infoTextView.setVisibility(View.VISIBLE);
            return;
        }
        for (int i = 0; mOrderItemInvoiceArrayList.size() > i; i++) {

            totalCount += mOrderItemInvoiceArrayList.get(i).mGuestNameOrders.size();
            OrderItemInvoiceView an_OrderItemInvoiceView = makeOrderItemInvoiceView(mOrderItemInvoiceArrayList.get(i));
            if (an_OrderItemInvoiceView != null)
                mOrderBrowserLinearLayout.addView(an_OrderItemInvoiceView);
        }

        if (totalCount == 0) {
            infoTextView.setVisibility(View.VISIBLE);
        }
    }

    private RestaurantMenu findMenuByName(String targetMenuName) {
        List<Category> categoryList;

        categoryList = mRestaurantList.get(mSelectedRestaurantTabIdx).categoryList;

        if (categoryList == null)
            return null;
        for (int i = 0; categoryList.size() > i; i++) {
            List<Category2> subCategoryList = categoryList.get(i).subCategoryList;
            for (int j = 0; subCategoryList.size() > j; j++) {
                List<RestaurantMenu> menuList = subCategoryList.get(j).Menus;
                for (int k = 0; menuList.size() > k; k++) {
                    if (menuList.get(k).name.equals(targetMenuName)) {
                        return menuList.get(k);
                    }
                }
            }
        }
        return null;
    }

    @SuppressLint("SetTextI18n")
    private void setTheTotalInvoice() {
        int theTotal = 0;
        for (int i = 0; mOrderDetailList.size() > i; i++)
            theTotal += Integer.parseInt(mOrderDetailList.get(i).getPaid_total_amount());
        mTotalPriceTextView.setText(AppDef.priceMapper(theTotal) + "원");
    }

    private void sendShadeOrders() {
        //여기 다시수정 mSelectedRestaurantTabIdx 가-1일경우

        mShadeOrders = new ShadeOrder(mRestaurantList.get(mSelectedRestaurantTabIdx).id, Global.reserveId, mOrderDetailList);

        showProgress("주문을 전송하고 있습니다.");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).sendShadeOrders(mContext, mShadeOrders, new DataInterface.ResponseCallback<ResponseData<Object>>() {
            @Override
            public void onSuccess(ResponseData<Object> response) {
                if (response.getResultCode().equals("ok")) {
                    hideProgress();
                    mRelSendOrder.setVisibility(View.INVISIBLE);
                    mRelOrderHistory.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "주문이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    //전송한 retaurant order가 로컬에는 저장되지않음  임시처리
//                    RestaurantOrder restaurantOrder = new RestaurantOrder();
//                    restaurantOrder.setOrderDetailList(mOrderDetailList);
//                    AppDef.restaurantOrderArrayList.add(restaurantOrder);
                    refresh();
                    mNumOfOrderHistoryOfSelectedRestaurant++;
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
    private void initRecyclerView(RecyclerView recycler, Restaurant selectedRestaurant) {
        mWholeMenuList = makeTotalMenuList(selectedRestaurant);
        mMenuAdapter = new MenuAdapter(mContext, mWholeMenuList);
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

                        mTVCateName.setText(mRestaurantList.get(mSelectedRestaurantTabIdx).categoryList.get(0).catergory1_name);
                        mTvSubCateName.setText(mRestaurantList.get(mSelectedRestaurantTabIdx).categoryList.get(0).subCategoryList.get(0).catergory2_name);

                        recycler.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }

    private void getRestaurantMenu() {
        showProgress("식당 메뉴 정보를 가져오는 중입니다.");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getRestaurantMenu(getActivity(), Global.CaddyNo, Global.selectedReservation.getReserveNo(), new DataInterface.ResponseCallback<ResponseData<Restaurant>>() {
            @Override
            public void onSuccess(ResponseData<Restaurant> response) {
                hideProgress();
                systemUIHide();
                if (response.getResultCode().equals("ok")) {
                    mRestaurantList = (ArrayList<Restaurant>) response.getList();

                    initRestaurantRecyclerView();
                    initFoodImage();

                    mMenuAdapter = new MenuAdapter(mContext, makeTotalMenuList(mRestaurantList.get(0)));
                    mRecyclerCategory.setAdapter(mMenuAdapter);
                    mRecyclerCategory.setHasFixedSize(true);
                    mRecyclerCategory.setLayoutManager(new SnappingLinearLayoutManager(mContext, 1, false));
                    mMenuAdapter.notifyDataSetChanged();
                    //레스토랑 선택 초기화

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

    private void initRestaurantRecyclerView() {
        LinearLayoutManager mManager;

        mManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        rv_restaurant.setLayoutManager(mManager);

        RestaurantListAdapter adapter = new RestaurantListAdapter(mContext, new RestaurantListAdapter.IOnClickAdapter() {
            @Override
            public void onAdapterItemClicked(Integer id) {
                mSelectedRestaurantTabIdx = id;
                initRecyclerView(mRecyclerCategory, mRestaurantList.get(id));
            }
        });

        rv_restaurant.setAdapter(adapter);
        adapter.setData(mRestaurantList);
    }

    private void refreshCategory() {
        //   mMenuAdapter.setAllRestaurantMenuUnSelected();
        if (mMenuAdapter != null)
            mMenuAdapter.notifyDataSetChanged();
    }

    private List<Category2> getSubMenuList(String Category1Id, String Category1Name) {
        List<Category> cate1List;

        cate1List = mRestaurantList.get(mSelectedRestaurantTabIdx).categoryList;

        for (int i = 0; cate1List.size() > i; i++) {
            if (cate1List.get(i).catergory1_name.equals(Category1Name))
                return cate1List.get(i).subCategoryList;
        }
        return null;
    }

    //서브아아템의 wholemenulist의 position을 리턴한다. (smoothScrollto 사용시)
    private int getPositionForSubMenuZeroItem(String Category1Name, String Category2Name) {
        for (int i = 0; mWholeMenuList.size() > i; i++) {
            if (mWholeMenuList.get(i).category1.equals(Category1Name) && mWholeMenuList.get(i).category2.equals(Category2Name)) {
                if (mWholeMenuList.get(i).SubCateZeroIndex != -1) {
                    return mWholeMenuList.get(i).SubCateZeroIndex;
                }
            }
        }
        return -1;
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
                    if (mSelectedRestaurantTabIdx > 0)
                        mNumOfOrderHistoryOfSelectedRestaurant = AppDef.storeOrderArrayList.get(mSelectedRestaurantTabIdx).tablet_order_list.size();
                    else //대식당 주문내역 찾기
                        mNumOfOrderHistoryOfSelectedRestaurant = OrderDetailHistoryFragment.findStoreOrderByRestaurantName(AppDef.storeOrderArrayList, "대식당").tablet_order_list.size();
                    //       ((Restaurant) mTvTheRestaurant.getTag()
                    if (mNumOfOrderHistoryOfSelectedRestaurant > 0) {
                        mRelSendOrder.setVisibility(View.INVISIBLE);
                        mRelOrderHistory.setVisibility(View.VISIBLE);
                        mBtnHistory.setText(mNumOfOrderHistoryOfSelectedRestaurant + "건 주문내역 보기");
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

    private class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuItemViewHolder> {

        class MenuItemViewHolder extends RecyclerView.ViewHolder {
            TextView tvMenuName, tvPrice, tvMenuId;

            //onCreateViewHolder 의 mMenuView 임()
            MenuItemViewHolder(@NonNull final View itemView) {
                super(itemView);
                tvMenuName = itemView.findViewById(R.id.tv_menu_name);
                tvPrice = itemView.findViewById(R.id.tv_menu_price);
                tvMenuId = itemView.findViewById(R.id.tv_menu_id);
            }
        }

        public static final int NUM_CATEGORY_NAME = 1 + 2 << 24;
        public static final int NUM_SUB_CATEGORY_NAME = 2 + 2 << 24;
        Context mContext;
        List<RestaurantMenu> mMenuList;
        public MenuItemViewHolder preSelectedViewHolder;

        MenuAdapter(Context context, List<RestaurantMenu> menuList) {
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
        public void onBindViewHolder(@NonNull final MenuAdapter.MenuItemViewHolder holder, int position) {
            final int idx = position;
            Log.d(TAG, "onBindViewHolder    " + "메뉴명: " + mMenuList.get(idx).name + "MenuItemViewHolder");
            holder.itemView.setTag(mMenuList.get(position));
            if (mMenuList.get(idx).isSelected) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                holder.tvMenuName.setTextColor(getResources().getColor(R.color.black, Objects.requireNonNull(getActivity()).getTheme()));
                holder.tvPrice.setTextColor(getResources().getColor(R.color.black, Objects.requireNonNull(getActivity()).getTheme()));
            } else {
                holder.itemView.setBackgroundColor(getResources().getColor(R.color.lightAliceBlue, Objects.requireNonNull(getActivity()).getTheme()));
                holder.tvMenuName.setTextColor(getResources().getColor(R.color.gray, Objects.requireNonNull(getActivity()).getTheme()));
                holder.tvPrice.setTextColor(getResources().getColor(R.color.gray, Objects.requireNonNull(getActivity()).getTheme()));
            }

            //              holder.tvMenuName.setText(mMenuList.get(idx).name + " " + mCategory2List.get(idx).catergory2_name);
            holder.tvMenuName.setText(mMenuList.get(idx).name);
            if (mMenuList.get(idx).price != null)
                holder.tvPrice.setText(priceMapper(Integer.parseInt(mMenuList.get(idx).price)));
            holder.tvMenuId.setText(mMenuList.get(idx).id);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (preSelectedViewHolder != null) {
                        preSelectedViewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.lightAliceBlue, Objects.requireNonNull(getActivity()).getTheme()));
                        preSelectedViewHolder.tvMenuName.setTextColor(getResources().getColor(R.color.gray, Objects.requireNonNull(getActivity()).getTheme()));
                        preSelectedViewHolder.tvPrice.setTextColor(getResources().getColor(R.color.gray, Objects.requireNonNull(getActivity()).getTheme()));
                    }

                    preSelectedViewHolder = holder;

//                    holder.itemView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
//                    holder.tvMenuName.setTextColor(getResources().getColor(R.color.black, Objects.requireNonNull(getActivity()).getTheme()));
//                    holder.tvPrice.setTextColor(getResources().getColor(R.color.black, Objects.requireNonNull(getActivity()).getTheme()));

                    mMenuList.get(idx).isSelected ^= true;
                    resetGuestList();
                    setFoodImage(mFoodImage, mMenuList.get(idx).image);
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onViewAttachedToWindow(@NonNull MenuItemViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            if (mSpinnSubMenu.getVisibility() == View.VISIBLE)
                mSpinnSubMenu.setVisibility(View.INVISIBLE);
            else {
                String catergory1_name = ((RestaurantMenu) holder.itemView.getTag()).category1;
                String catergory2_name = ((RestaurantMenu) holder.itemView.getTag()).category2;
                mTVCateName.setText(catergory1_name);
                mTvSubCateName.setText(catergory2_name);

                //   mTvSubCateName.setText(subCategoryList.get(mSubCategoryPosition).catergory2_name);
                //   Log.d(TAG, "onViewAttachedToWindow Menu    " + mCategory1Name + " " + subCategoryList.get(mSubCategoryPosition).catergory2_name);
            }

        }

        @Override
        public int getItemCount() {
            // Toast.makeText(mContext, "메뉴 사이즈" +mMenuList.size(), Toast.LENGTH_SHORT ).show();
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
    }

}