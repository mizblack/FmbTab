package com.eye3.golfpay.fmb_tab.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.AppDef;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.order.Category;
import com.eye3.golfpay.fmb_tab.model.order.Category2;
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
import com.eye3.golfpay.fmb_tab.view.SnappingLinearLayoutManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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
    private ImageView mFoodImage;
    private int mSelectedRestaurantTabIdx = 0;
    //탭홀더
    private LinearLayout mTabLinear, mOrderBrowserLinearLayout, mLinearSubMenu;
    LinearLayout mGuestContainer;
    public static LinearLayout mTabsRootLinear;
    private ShadeOrder mShadeOrders;
    private OrderedMenuItem mOrderedMenuItem = null;
    private String mOrderedGuestId = "";
    // 최상위 카테고리 이름
    private TextView mTVCateName, mTvSubCateName, infoTextView, mTvTheRestaurant;
    private TextView mTotalPriceTextView;
    private Button mCancelButton;
    private Button mTempSaveButton;
    //**********************************************************
    List<OrderDetail> mOrderDetailList = new ArrayList<>();//먼저 생성해야 아래리스트에 renew됨.
    List<OrderItemInvoice> mOrderItemInvoiceArrayList = new ArrayList<>();
    //**********************************************************

    private ImageView mArrowToApply;
    static TextView preSelectedGuestView;
    RelativeLayout mRelOrderHistory, mRelSendOrder;
    Button mBtnHistory, mBtnAdd;
    AppCompatSpinner mSpinnSubMenu;
    ArrayAdapter mSpinnAdapter;
    TextView mTvCaddy;

    List<RestaurantMenu> mWholeMenuList;
    MenuAdapter mMenuAdapter;

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
        mTvSubCateName = v.findViewById(R.id.tv_sub_cate_name);
        infoTextView = v.findViewById(R.id.infoTextView);
        mArrowToApply = v.findViewById(R.id.arrow_to_apply);
        mOrderBrowserLinearLayout = v.findViewById(R.id.orderBrowserLinearLayout);
        mTabsRootLinear = v.findViewById(R.id.tabsRootLinearLayout);
        mTotalPriceTextView = v.findViewById(R.id.totalPriceTextView);
        createGuestList(mGuestContainer);
        mRecyclerCategory = v.findViewById(R.id.recycler_category);

        mFoodImage = v.findViewById(R.id.img_food);
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
                if (mOrderedMenuItem != null) {
                    //캐디주문을 표기한다.
                    mOrderedMenuItem.caddy_id = Global.CaddyNo;
                    //  if (v.getTag().equals(mOrderDetailList.get(0).reserve_guest_id)) {
                    infoTextView.setVisibility(View.GONE);
                    ((TextView) v).setTextColor(getResources().getColor(R.color.white, Objects.requireNonNull(getActivity()).getTheme()));
                    (v).setBackgroundResource(R.drawable.shape_ebony_black_background_and_edge);
                    mOrderDetailList.get(0).addOrPlusOrderedMenuItem(mOrderedMenuItem);
                    mOrderDetailList.get(0).setTotalPaidAmount(mOrderDetailList.get(0).getPaid_total_amount());
                    makeOrderItemsForCaddy();
                    makeOrderItemInvoiceArrViews();
                    setTheTotalInvoice();
//                    } else {
//                        Toast.makeText(mContext, "주문상세 주문자가 불일치합니다.", Toast.LENGTH_SHORT).show();
//                        mOrderedMenuItem = null;
//                        refreshCategory();
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
        initFoodImage();
    }

    private void initSelectedRestaurantTabColor() {

        mRestaurantTabBarArr[0].setTextColor(Color.BLACK);
    }

    private void initFoodImage() {
        if (mRestaurantList == null) {
            Toast.makeText(getActivity(), "존재하는 식당 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
//        if (mRestaurantList.get(0).mCategory1List == null || mRestaurantList.get(0).mCategory1List.size() == 0) {
//            Toast.makeText(getActivity(), "존재하는 식당 메뉴 카테고리가 없습니다.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (mRestaurantList.get(0).mCategory1List.get(0).subCategoryList.get(0)== null || mRestaurantList.get(0).mCategory1List.get(0).Menus.size() == 0) {
//            //    Toast.makeText(getActivity(), " 식당 메뉴가 없습니다.", Toast.LENGTH_SHORT).show();
//            return;
//        }

//        if (mRestaurantList.get(0).mCategory1List.get(0).subCategoryList.size() > 0) {

        //  if (mSelectedRestaurantTabIdx < 0)
        //  setFoodImage(mFoodImage, (((Restaurant) mTvTheRestaurant.getTag()).mCategory1List.get(0).subCategoryList.get(0).Menus.get(0).image));
        //  else
        //  setFoodImage(mFoodImage, (mRestaurantList.get(mSelectedRestaurantTabIdx).mCategory1List.get(0).subCategoryList.get(0).Menus.get(0).image));
        //    }
    }

    //레스토랑바 선택시 보여주는 함수
    private void selectRestaurant(int selectedTabIdx) {
        refresh();
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
        mSpinnSubMenu.setVisibility(View.INVISIBLE);
        if (mSelectedRestaurantIdx == -1) {
            initRecyclerView(mRecyclerCategory, ((Restaurant) mTvTheRestaurant.getTag()));
            return;
        }
        initRecyclerView(mRecyclerCategory, mRestaurantList.get(mSelectedRestaurantIdx));
    }

    private int getIndexByName(String guestName) {
        for (int i = 0; mOrderDetailList.size() > i; i++) {
            if (getGuestName(mOrderDetailList.get(i).reserve_guest_id).equals(guestName))
                return i;
        }
        return -1;

    }

    private void openTempSavedOrderList(){
        //임시저장정보가 있다면
        if (AppDef.orderItemInvoiceArrayList.size() > 0) {
            Toast.makeText(mContext, "임시저장 메뉴정보가 존재합니다.", Toast.LENGTH_SHORT).show();
            selectRestaurant(AppDef.mTempSaveRestaurantIdx);
            //초기화로 인해 clear되지않게 할것
            mOrderItemInvoiceArrayList = AppDef.orderItemInvoiceArrayList;
            mOrderDetailList = AppDef.orderDetailList;
         //   makeOrderItems();
            makeOrderItemInvoiceArrViews();
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
                bundle.putString("ani_direction", "down");
                GoNativeScreen(new OrderDetailHistoryFragment(), bundle);
            }
        });

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppDef.orderDetailList.size() > 0)
                    sendShadeOrders();
                else
                    Toast.makeText(getActivity(), "먼저 주문을 해주십시요.", Toast.LENGTH_SHORT).show();
            }
        });

        mLinearSubMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedItem = -1;
                List<Category2> spinnSubCate2MenuList = getSubMenuList("", mTVCateName.getText().toString().trim());
                if (spinnSubCate2MenuList == null) {
                    Toast.makeText(mContext, "서브카테고리가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<String> spinnSubMenuList = new ArrayList<>();

                if (mSpinnAdapter != null)
                    mSpinnAdapter.clear();

                for (int i = 0; spinnSubCate2MenuList.size() > i; i++) {
                    spinnSubMenuList.add(spinnSubCate2MenuList.get(i).catergory2_name);
                }

                mSpinnAdapter = new ArrayAdapter<String>(mContext, R.layout.sub_menu_spinner_item, spinnSubMenuList) {
                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View v = null;
                        v = super.getDropDownView(position, null, parent);
                        // If this is the selected item position
                        if (position == selectedItem) {
                            v.setBackgroundColor(Color.BLUE);

                        } else {
                            // for other views
                            v.setBackgroundColor(Color.BLACK);

                        }
                        return v;
                    }
                };
                mSpinnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinnSubMenu.setVisibility(View.VISIBLE);
                mSpinnSubMenu.setAdapter(mSpinnAdapter);
                mSpinnSubMenu.setSelection(0);
                mSpinnSubMenu.setGravity(Gravity.CENTER);
                mSpinnSubMenu.setBackgroundColor(Color.BLACK);
                mSpinnSubMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position > 0) {
                            int subMenuZeroIdx = getPositionForSubMenuZeroItem(mTVCateName.getText().toString().trim(), spinnSubMenuList.get(position).toString());
                            mTvSubCateName.setText(spinnSubMenuList.get(position));
                            if (subMenuZeroIdx != -1) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mRecyclerCategory.smoothScrollToPosition(subMenuZeroIdx);
                                    }
                                }, 100);

                            } else
                                Toast.makeText(mContext, "해당 서브카테고리에 메뉴가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }

        });
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
        mOrderedMenuItem = null;
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
        mOrderedMenuItem = null;
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
            mGuestContainer.getChildAt(i).setBackgroundResource(R.drawable.shape_gray_edge);
            ((TextView) mGuestContainer.getChildAt(i)).setTextColor(Color.parseColor("#999999"));
        }
    }

//    private void setAllRestaurantMenuUnSelected() {
//        for (int i = 0; mCategory1List.size() > i; i++) {
//            for (int k = 0; mCategory1List.size() > k; k++) {
//                ArrayList<Category2> tempList = mCategory1List.get(i).subCategoryList;
//                for (int j = 0; tempList.size() > j; j++) {
//                    for (int l = 0; tempList.get(j).Menus.size() > l; l++)
//                        tempList.get(j).Menus.get(l).isSelected = false;
//
//                }
//            }
//        }
//    }

    private void refreshCategory() {
        //   mMenuAdapter.setAllRestaurantMenuUnSelected();
        if (mMenuAdapter != null)
            mMenuAdapter.notifyDataSetChanged();
    }

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
                        if (mSelectedRestaurantTabIdx > 0) {
                            mTVCateName.setText(mRestaurantList.get(mSelectedRestaurantTabIdx).categoryList.get(0).catergory1_name);
                            mTvSubCateName.setText(mRestaurantList.get(mSelectedRestaurantTabIdx).categoryList.get(0).subCategoryList.get(0).catergory2_name);
                        } else { //대식당이
                            mTVCateName.setText(((Restaurant) mTvTheRestaurant.getTag()).categoryList.get(0).catergory1_name);
                            mTvSubCateName.setText(((Restaurant) mTvTheRestaurant.getTag()).categoryList.get(0).subCategoryList.get(0).catergory2_name);
                        }
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
                    NUM_OF_RESTAURANT = mRestaurantList.size();
                    mRestaurantTabBarArr = new TextView[NUM_OF_RESTAURANT];
                    createTabBar(mRestaurantTabBarArr, mRestaurantList);
                    mMenuAdapter = new MenuAdapter(mContext, makeTotalMenuList(mRestaurantList.get(0)));
                    mRecyclerCategory.setAdapter(mMenuAdapter);
                    mRecyclerCategory.setHasFixedSize(true);

                    mRecyclerCategory.setLayoutManager(new SnappingLinearLayoutManager(mContext, 1, false));
                    mMenuAdapter.notifyDataSetChanged();
                    openTempSavedOrderList();
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

    private List<Category2> getSubMenuList(String Category1Id, String Category1Name) {
        List<Category> cate1List;
        if (mSelectedRestaurantTabIdx > 0)
            cate1List = mRestaurantList.get(mSelectedRestaurantTabIdx).categoryList;
        else
            cate1List = ((Restaurant) mTvTheRestaurant.getTag()).categoryList;

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

    private class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuItemViewHolder> {
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
                holder.itemView.setBackgroundResource(R.drawable.shape_gray_edge);
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
                    holder.itemView.setBackgroundResource(R.drawable.shape_gray_edge);
                    holder.tvMenuName.setTextColor(getResources().getColor(R.color.black, Objects.requireNonNull(getActivity()).getTheme()));
                    holder.tvPrice.setTextColor(getResources().getColor(R.color.black, Objects.requireNonNull(getActivity()).getTheme()));
                    mMenuList.get(idx).isSelected = true;
                    resetGuestList();
                    setFoodImage(mFoodImage, mMenuList.get(idx).image);
                    mOrderedMenuItem = new OrderedMenuItem(holder.tvMenuId.getText().toString().trim(), "1", mMenuList.get(idx).price.trim(), mMenuList.get(idx).name);
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
    }

    private void setFoodImage(ImageView img, String url) {
        if (img != null) {

            Glide.with(mContext)
                    .load(Global.HOST_BASE_ADDRESS_AWS + url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.ic_noimage)
                    .into(mFoodImage);
        }
    }

    public static String getGuestName(String reserveId) {
        for (int i = 0; Global.selectedReservation.getGuestData().size() > i; i++) {
            if (Global.selectedReservation.getGuestData().get(i).getId().equals(reserveId)) {
                return Global.selectedReservation.getGuestData().get(i).getGuestName();
            }
        }
        return "";
    }


    private void createGuestList(LinearLayout container) {
        initOrderDetailList();
        for (int i = 0; mOrderDetailList.size() > i; i++) {
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
                    //이미선택된 게스트가 있다면 초기화하자
                    if (preSelectedGuestView != null) {
                        preSelectedGuestView.setBackgroundResource(R.drawable.shape_gray_edge);
                        preSelectedGuestView.setTextAppearance(R.style.ShadeGuestNameTextView);
                    }
                    preSelectedGuestView = (TextView) v;
                    mOrderedGuestId = (String) v.getTag();
                    //주문된 음식이 있다면
                    if (mOrderedMenuItem != null) {
                        if (v.getTag().equals(mOrderDetailList.get(idx).reserve_guest_id)) {
                            infoTextView.setVisibility(View.GONE);
                            ((TextView) v).setTextColor(getResources().getColor(R.color.white, Objects.requireNonNull(getActivity()).getTheme()));
                            (v).setBackgroundResource(R.drawable.shape_ebony_black_background_and_edge);
                            mOrderDetailList.get(idx).addOrPlusOrderedMenuItem(mOrderedMenuItem);
                            mOrderDetailList.get(idx).setTotalPaidAmount(mOrderDetailList.get(idx).getPaid_total_amount());
                            makeOrderItems();
                            makeOrderItemInvoiceArrViews();
                            setTheTotalInvoice();
                            //********** 이부분 수정할것 중복 메뉴 추가
                            mOrderedMenuItem = null;
                            refreshCategory();
                            //***********
                        } else {
                            Toast.makeText(mContext, "주문상세 주문자가 불일치합니다.", Toast.LENGTH_SHORT).show();
                            mOrderedMenuItem = null;
                            refreshCategory();
                        }

                    } else
                        Toast.makeText(mContext, "주문한 음식이 없습니다. 먼저 음식을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                }
            });
            container.addView(tv);
        }
    }


    void initOrderItemInvoiceView() {
        makeOrderItemInvoiceArrViews();
        setTheTotalInvoice();
    }

    private void makeOrderItemsForCaddy() {
        OrderItemInvoice a_ItemInvoice = null;
        if (mOrderItemInvoiceArrayList.size() == 0) {
            a_ItemInvoice = new OrderItemInvoice();
            a_ItemInvoice.mMenunName = mOrderedMenuItem.name;
            a_ItemInvoice.mQty = 1;
            NameOrder nameOrder = new NameOrder(getGuestName(mOrderDetailList.get(0).reserve_guest_id) + STRING_CADDY, NUM_OF_SINGLE_ORDERED_ITEM);
            nameOrder.caddy_id = Global.CaddyNo;
            a_ItemInvoice.mNameOrders.add(nameOrder);

            mOrderItemInvoiceArrayList.add(a_ItemInvoice);

        } else {

            for (int j = 0; mOrderItemInvoiceArrayList.size() > j; j++) {
                a_ItemInvoice = mOrderItemInvoiceArrayList.get(j);

                if (a_ItemInvoice.mMenunName.equals(mOrderedMenuItem.name)) {
                    //메뉴이름이 같을때
                    a_ItemInvoice.mQty += NUM_OF_SINGLE_ORDERED_ITEM;
                    //여기에선  mOrderedMenuItem qty가이 항상 1이어야 한다..

                    putNameOrderForCaddy(a_ItemInvoice, getGuestName(mOrderDetailList.get(0).reserve_guest_id) + STRING_CADDY, NUM_OF_SINGLE_ORDERED_ITEM);

                    return;
                }
            }
            //메뉴이름이 업스면 새인보이스생성
            a_ItemInvoice = new OrderItemInvoice();
            a_ItemInvoice.mMenunName = mOrderedMenuItem.name;
            a_ItemInvoice.mQty = 1;
            a_ItemInvoice.mNameOrders.add((new NameOrder(getGuestName(mOrderDetailList.get(0).reserve_guest_id) + STRING_CADDY, NUM_OF_SINGLE_ORDERED_ITEM)));

            mOrderItemInvoiceArrayList.add(a_ItemInvoice);
            return;


        }
    }


    private void makeOrderItems() {

        OrderItemInvoice a_ItemInvoice = null;
        if (mOrderItemInvoiceArrayList.size() == 0) {
            a_ItemInvoice = new OrderItemInvoice();
            a_ItemInvoice.mMenunName = mOrderedMenuItem.name;
            a_ItemInvoice.mQty = Integer.valueOf(mOrderedMenuItem.qty);
            a_ItemInvoice.mNameOrders.add((new NameOrder(getGuestName(mOrderedGuestId), Integer.valueOf(mOrderedMenuItem.qty))));
            mOrderItemInvoiceArrayList.add(a_ItemInvoice);

        } else {

            for (int j = 0; mOrderItemInvoiceArrayList.size() > j; j++) {
                a_ItemInvoice = mOrderItemInvoiceArrayList.get(j);

                if (a_ItemInvoice.mMenunName.equals(mOrderedMenuItem.name)) {
                    //메뉴이름이 같을때
                    a_ItemInvoice.mQty += Integer.valueOf(mOrderedMenuItem.qty);
                    //여기에선  mOrderedMenuItem qty가 항상 1이어야 한다..
                    putNameOrder(a_ItemInvoice, getGuestName(mOrderedGuestId), Integer.valueOf(mOrderedMenuItem.qty));
                    return;
                }
            }
            //메뉴이름이 업스면 새인보이스생성
            a_ItemInvoice = new OrderItemInvoice();
            a_ItemInvoice.mMenunName = mOrderedMenuItem.name;
            a_ItemInvoice.mQty = 1;
            a_ItemInvoice.mNameOrders.add((new NameOrder(getGuestName(mOrderedGuestId), Integer.valueOf(mOrderedMenuItem.qty))));

            mOrderItemInvoiceArrayList.add(a_ItemInvoice);
            return;


        }
    }

    private void putNameOrderForCaddy(OrderItemInvoice a_iteminvoice, String guestName, int OrderedMenuItemQty) {
        if (a_iteminvoice.mNameOrders.size() < 0)
            return;
        for (int i = 0; a_iteminvoice.mNameOrders.size() > i; i++) {
            if (a_iteminvoice.mNameOrders.get(i).name.equals(guestName)) {
                int newQty = a_iteminvoice.mNameOrders.get(i).qty + OrderedMenuItemQty;
                a_iteminvoice.mNameOrders.set(i, new NameOrder(guestName, newQty));
                return;
            }
        }
        // a_iteminvoice.mNameOrders.add(new NameOrder(guestName, OrderedMenuItemQty));
        NameOrder nameOrder = new NameOrder(getGuestName(mOrderDetailList.get(0).reserve_guest_id) + STRING_CADDY, NUM_OF_SINGLE_ORDERED_ITEM);
        nameOrder.caddy_id = Global.CaddyNo;
        a_iteminvoice.mNameOrders.add(nameOrder);
    }

    private void putNameOrder(OrderItemInvoice a_iteminvoice, String guestName, int OrderedMenuItemQty) {
        if (a_iteminvoice.mNameOrders.size() < 0)
            return;
        for (int i = 0; a_iteminvoice.mNameOrders.size() > i; i++) {
            if (a_iteminvoice.mNameOrders.get(i).name.equals(guestName)) {
                int newQty = a_iteminvoice.mNameOrders.get(i).qty + OrderedMenuItemQty;
                a_iteminvoice.mNameOrders.set(i, new NameOrder(guestName, newQty));
                return;
            }
        }
        a_iteminvoice.mNameOrders.add(new NameOrder(guestName, OrderedMenuItemQty));
    }

    //우측 주문표 뷰화면 생성함수
    private OrderItemInvoiceView makeOrderItemInvoiceView(final OrderItemInvoice orderItemInvoice) {
        OrderItemInvoiceView a_InvoiceView;
        if (orderItemInvoice == null || orderItemInvoice.mMenunName == "" || orderItemInvoice.mNameOrders.size() == 0)
            return null;
        else
            a_InvoiceView = new OrderItemInvoiceView(mContext);

        a_InvoiceView.setTag(orderItemInvoice);
        //네임오더가 없으면 뷰를 생성하지 않고 null 리턴
        if (orderItemInvoice.mNameOrders.size() == 0) {
            return null;
        }
        for (int i = 0; orderItemInvoice.mNameOrders.size() > i; i++) {
            NameOrderView a_NameOrderview = new NameOrderView(mContext);
            a_NameOrderview.setTag(orderItemInvoice.mNameOrders.get(i));
            a_NameOrderview.deleteLinear.setTag(NUM_MENU_NAME_KEY, orderItemInvoice.mMenunName);
            a_NameOrderview.deleteLinear.setTag(NUM_NAMEORDER_KEY, orderItemInvoice.mNameOrders.get(i));
            a_NameOrderview.mNameTv.setText(((NameOrder) a_NameOrderview.getTag()).name);

            a_NameOrderview.mQtyTv.setText(String.valueOf(((NameOrder) a_NameOrderview.getTag()).qty) + "개");
            a_NameOrderview.deleteLinear.setOnClickListener(listener);

            a_NameOrderview.mPlusTv.setOnClickListener(listenerPlus);
            a_NameOrderview.mPlusTv.setTag(NUM_INVOICEORDER_KEY, orderItemInvoice);
            a_NameOrderview.mPlusTv.setTag(NUM_NAMEORDER_KEY, orderItemInvoice.mNameOrders.get(i));

            a_NameOrderview.mMinusTv.setOnClickListener(listenerMinus);
            a_NameOrderview.mMinusTv.setTag(NUM_INVOICEORDER_KEY, orderItemInvoice);
            a_NameOrderview.mMinusTv.setTag(NUM_NAMEORDER_KEY, orderItemInvoice.mNameOrders.get(i));

            a_InvoiceView.mLinearNameOrder.addView(a_NameOrderview);
        }

        a_InvoiceView.mTvMenuName.setText(((OrderItemInvoice) a_InvoiceView.getTag()).mMenunName);
        int _qty = ((OrderItemInvoice) a_InvoiceView.getTag()).mQty;
        a_InvoiceView.mTvQty.setText(String.valueOf(_qty));

        return a_InvoiceView;
    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String menuName = (String) v.getTag(NUM_MENU_NAME_KEY);
            NameOrder deletedNameOrder = (NameOrder) v.getTag(NUM_NAMEORDER_KEY);

            for (int i = 0; mOrderDetailList.size() > i; i++) {
                ArrayList<OrderedMenuItem> a_orderedMenuItemList = mOrderDetailList.get(i).mOrderedMenuItemList;
                for (int j = 0; a_orderedMenuItemList.size() > j; j++) {
                    if (menuName.equals(a_orderedMenuItemList.get(j).name) && getGuestName(mOrderDetailList.get(i).reserve_guest_id).equals(deletedNameOrder.name)) {

                        a_orderedMenuItemList.get(j).qty = String.valueOf(Integer.valueOf(a_orderedMenuItemList.get(j).qty) - deletedNameOrder.qty);
                        if (a_orderedMenuItemList.get(j).qty.equals("0")) {
                            mOrderDetailList.get(i).setTotalPaidAmount(mOrderDetailList.get(i).getPaid_total_amount());
                            a_orderedMenuItemList.remove(j);
                        }
                        break;
                    }
                }
            }
            deleteNameOrderFromInvoiceArrayList(menuName, deletedNameOrder);
            makeOrderItemInvoiceArrViews();
            setTheTotalInvoice();

        }
    };

    View.OnClickListener listenerPlus = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            OrderItemInvoice plusOrderItemInvoice = (OrderItemInvoice) v.getTag(NUM_INVOICEORDER_KEY);
            NameOrder plusNameOrder = (NameOrder) v.getTag(NUM_NAMEORDER_KEY);

            RestaurantMenu restaurantMenu = findMenuByName(plusOrderItemInvoice.mMenunName);
            if (restaurantMenu != null) {
                mOrderedMenuItem = new OrderedMenuItem(restaurantMenu.id, String.valueOf(NUM_OF_SINGLE_ORDERED_ITEM), restaurantMenu.price, restaurantMenu.name);
                if (getIndexByName(plusNameOrder.name) > 0) {

                    mOrderDetailList.get(getIndexByName(plusNameOrder.name)).addOrPlusOrderedMenuItem(mOrderedMenuItem);
                    mOrderDetailList.get(getIndexByName(plusNameOrder.name)).setTotalPaidAmount(mOrderDetailList.get(getIndexByName(plusNameOrder.name)).getPaid_total_amount());

                    makeOrderItems();
                    makeOrderItemInvoiceArrViews();
                    setTheTotalInvoice();
                }

            }

        }
    };

    View.OnClickListener listenerMinus = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            OrderItemInvoice minusOrderItemInvoice = (OrderItemInvoice) v.getTag(NUM_INVOICEORDER_KEY);
            NameOrder minusNameOrder = (NameOrder) v.getTag(NUM_NAMEORDER_KEY);
            int idx = getIndexByName(minusNameOrder.name);
            RestaurantMenu restaurantMenu = findMenuByName(minusOrderItemInvoice.mMenunName);
            if (restaurantMenu != null) {
                if (minusNameOrder.qty == 0) {
                    Toast.makeText(getActivity(), "수량이 0 입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                mOrderedMenuItem = new OrderedMenuItem(restaurantMenu.id, "-1", restaurantMenu.price, restaurantMenu.name);
                if (getIndexByName(minusNameOrder.name) > 0) {
                    mOrderDetailList.get(getIndexByName(minusNameOrder.name)).addOrPlusOrderedMenuItem(mOrderedMenuItem);
                    mOrderDetailList.get(getIndexByName(minusNameOrder.name)).setTotalPaidAmount(mOrderDetailList.get(getIndexByName(minusNameOrder.name)).getPaid_total_amount());

                    makeOrderItems();
                    makeOrderItemInvoiceArrViews();
                    setTheTotalInvoice();

                }
            }
        }
    };

    //mOrderedMenuItem을 추가/삭제시 아래함수로 array(데이터)와 우측뷰를 rewnew한다.
    private void renewItemInvoice() {
        makeOrderItems();
        makeOrderItemInvoiceArrViews();
        setTheTotalInvoice();
    }

    private void deleteNameOrderFromInvoiceArrayList(String menuName, NameOrder deletedNameOrder) {
        for (int i = 0; mOrderItemInvoiceArrayList.size() > i; i++) {
            OrderItemInvoice a_itemInvoice = mOrderItemInvoiceArrayList.get(i);
            if (a_itemInvoice.mMenunName.equals(menuName)) {
                for (int j = 0; a_itemInvoice.mNameOrders.size() > j; j++) {
                    if ((a_itemInvoice.mNameOrders.get(j).name).equals(deletedNameOrder.name) && a_itemInvoice.mNameOrders.get(j).qty == deletedNameOrder.qty) {
                        a_itemInvoice.mNameOrders.remove(a_itemInvoice.mNameOrders.get(j));
                        a_itemInvoice.mQty -= deletedNameOrder.qty;
                    }
                }
            }
        }
    }


    //최종적으로 Orderfragment 인보이스 레이아웃에 add..
    private void makeOrderItemInvoiceArrViews() {
        mOrderBrowserLinearLayout.removeAllViewsInLayout();
        if (mOrderItemInvoiceArrayList == null || mOrderItemInvoiceArrayList.size() == 0) {
            return;
        }
        for (int i = 0; mOrderItemInvoiceArrayList.size() > i; i++) {
            OrderItemInvoiceView an_OrderItemInvoiceView = makeOrderItemInvoiceView(mOrderItemInvoiceArrayList.get(i));
            if (an_OrderItemInvoiceView != null)
                mOrderBrowserLinearLayout.addView(an_OrderItemInvoiceView);

        }

    }

    private RestaurantMenu findMenuByName(String targetMenuName) {
        List<Category> categoryList;
        if (mSelectedRestaurantTabIdx < 0)
            categoryList = ((Restaurant) mTvTheRestaurant.getTag()).categoryList;
        else
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
        if (mSelectedRestaurantTabIdx < 0)
            mShadeOrders = new ShadeOrder(((Restaurant) mTvTheRestaurant.getTag()).id, Global.reserveId, mOrderDetailList);
        else
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