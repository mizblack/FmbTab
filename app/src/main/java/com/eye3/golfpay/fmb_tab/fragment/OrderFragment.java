package com.eye3.golfpay.fmb_tab.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.eye3.golfpay.fmb_tab.model.order.RestaurantOrder;
import com.eye3.golfpay.fmb_tab.model.order.ShadeOrder;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.net.ResponseData;
import com.eye3.golfpay.fmb_tab.view.NameOrderView;
import com.eye3.golfpay.fmb_tab.view.OrderItemInvoiceView;

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
    private CategoryAdapter mCateAdapter;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOrderItemInvoiceArrayList = AppDef.orderItemInvoiceArrayList;
        mOrderDetailList = AppDef.orderDetailList;

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
        if (mSelectedRestaurantIdx == -1) {
            initRecyclerView(mRecyclerCategory, mCateAdapter, ((Restaurant) mTvTheRestaurant.getTag()).categoryList);
            return;
        }
        initRecyclerView(mRecyclerCategory, mCateAdapter, mRestaurantList.get(mSelectedRestaurantIdx).categoryList);
    }

    private int getIndexByName(String guestName) {
        for (int i = 0; mOrderDetailList.size() > i; i++) {
            if (getGuestName(mOrderDetailList.get(i).reserve_guest_id).equals(guestName))
                return i;
        }
        return -1;

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
                    mBtnHistory.setVisibility(View.VISIBLE);
                    orderOrApplyBtn.setVisibility(View.INVISIBLE);
                } else {
                    refresh();
                    AppDef.orderDetailList.clear();
                    initOrderDetailList();

                }
            }
        });


        //임시저장
        mTempSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDef.orderItemInvoiceArrayList = mOrderItemInvoiceArrayList;
                AppDef.orderDetailList = mOrderDetailList;
                //임시저장시 RestaurantOrder에 식당별로 add시키고
                Toast.makeText(getActivity(), "임시저장이 완료되었습니다.", Toast.LENGTH_SHORT).show();
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("restaurantList", mRestaurantList);
//                GoNativeScreen(new OrderDetailHistoryFragment(), bundle);
            }
        });

//        mTVCateName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mRestaurantList != null) {
//
//                }
//            }
//        });

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
                sendShadeOrders();
            }
        });
    }


    void initOrderDetailList() {
        mOrderDetailList.clear();
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

    private void refreshCategory() {
        mCateAdapter.setAllRestaurantMenuUnSelected();
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
        ArrayList<Category> categoryList;
        //서브메뉴 전시시 필요함.
        ArrayList<Category2> mSubCategoryList;
        SubCategoryAdapter mSubCategoryAdapter;

        CategoryAdapter(Context context, ArrayList<Category> categoryList) {

            mContext = context;
            this.categoryList = categoryList;
        }


        @NonNull
        @Override
        //recyclerView 가 parent 임
        public CategoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Log.d(TAG, "onCreateViewHolder");
            View view = LayoutInflater.from(mContext).inflate(R.layout.restaurant_cate_row, parent, false);
            return new CategoryItemViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull final CategoryItemViewHolder holder,
                                     int position_category) {
            Log.d(TAG, "onBindViewHolder    " + categoryList.get(position_category).catergory1_name);

            List<Category2> a_sub_cate_list = categoryList.get(position_category).subCategoryList;

            mSubCategoryAdapter = new SubCategoryAdapter(mContext, a_sub_cate_list, categoryList.get(position_category).catergory1_name);
            holder.subCategoyryRecyclerView.setAdapter(mSubCategoryAdapter);
            holder.subCategoyryRecyclerView.setHasFixedSize(true);
            LinearLayoutManager mManager = new LinearLayoutManager(mContext);
            holder.subCategoyryRecyclerView.setLayoutManager(mManager);
            holder.itemView.setTag(position_category);

            mLinearSubMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selectedItem = -1;
                    mSubCategoryList = categoryList.get((int) holder.itemView.getTag()).subCategoryList;
                    ArrayList<String> submenu = new ArrayList<>();
                    //   submenu.add("서브메뉴 선택");
                    for (int i = 0; mSubCategoryList.size() > i; i++) {
                        submenu.add(mSubCategoryList.get(i).catergory2_name);
                    }
                    //    arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.spinn_array)) ;
                    mSpinnAdapter = new ArrayAdapter<String>(mContext, R.layout.sub_menu_spinner_item, submenu) {
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
//                            if (position > 0)
//                                mRecyclerCategory.smoothScrollToPosition( );

//                            if ((parent.getItemAtPosition(position)).toString().equals("yard")) {
//
//                            } else if ((parent.getItemAtPosition(position)).toString().equals("meter")) {
//
//                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                }

            });

        }


//        @Override
//        public void onViewAttachedToWindow(@NonNull CategoryItemViewHolder holder) {
//            super.onViewAttachedToWindow(holder);
//
//            if (categoryList == null)
//                return;                                                                                                                               //index0 수정할것
//
//            //     mTVCateName.setText(categoryList.get((int) holder.itemView.getTag()).catergory1_name + "/" + categoryList.get((int) holder.itemView.getTag()).subCategoryList.get(0).catergory2_name);
//            //Category cate = (Category) holder.itemView.getTag();
//            int idx = (int) holder.itemView.getTag();
//            mTVCateName.setText(categoryList.get(idx).catergory1_name);
//
//        }
//
//        @Override
//        public void onViewDetachedFromWindow(@NonNull CategoryItemViewHolder holder) {
//            super.onViewDetachedFromWindow(holder);
//            if (categoryList == null)
//                return;
//            int idx = (int) holder.itemView.getTag();
//            if (idx > 0) {
//
//                mTVCateName.setText(categoryList.get(idx - 1).catergory1_name);
//            }

            //수정할것
            //   mTVCateName.setText(categoryList.get((int) holder.itemView.getTag() - 1).catergory1_name);
            //   mTVCateName.setText(categoryList.get((int) holder.itemView.getTag()).catergory1_name + "/" + categoryList.get((int) holder.itemView.getTag()).subCategoryList.get(0).catergory2_name);
    //    }

        @Override
        public int getItemCount() {
            return categoryList.size();
        }

        class CategoryItemViewHolder extends RecyclerView.ViewHolder {
            public RecyclerView subCategoyryRecyclerView;

            //onCreateViewHolder 의 mMenuView 임()
            CategoryItemViewHolder(@NonNull final View itemView) {
                super(itemView);

                subCategoyryRecyclerView = itemView.findViewById(R.id.recycler_category);

            }

        }

        private void setAllRestaurantMenuUnSelected() {
            for (int i = 0; categoryList.size() > i; i++) {
                for (int k = 0; categoryList.size() > k; k++) {
                    ArrayList<Category2> tempList = categoryList.get(i).subCategoryList;
                    for (int j = 0; tempList.size() > j; j++) {
                        for (int l = 0; tempList.get(j).Menus.size() > l; l++)
                            tempList.get(j).Menus.get(l).isSelected = false;

                    }
                }
            }
        }

        private class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.SubCategoryItemViewHolder> {
            Context mContext;
            List<Category2> subCategoryList;
            String categoryName;
            MenuAdapter mMenuAdapter;

            public SubCategoryAdapter(Context context, List<Category2> subCategoryList, String categoryName) {
                this.mContext = context;
                this.subCategoryList = subCategoryList;
                this.categoryName = categoryName;

            }


            @NonNull
            @Override
            public SubCategoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.restaurant_sub_cate_row, parent, false);
                return new SubCategoryItemViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull SubCategoryItemViewHolder holder, int position) {
                mMenuAdapter = new MenuAdapter(mContext, categoryName, subCategoryList.get(position).Menus, position);
                holder.menuRecyclerView.setAdapter(mMenuAdapter);
                holder.menuRecyclerView.setHasFixedSize(true);
                LinearLayoutManager mManager = new LinearLayoutManager(mContext);
                holder.menuRecyclerView.setLayoutManager(mManager);
                holder.itemView.setTag(position); //서브 커테인덱스를 일괄 tag로 붙인다.
            }

            @Override
            public int getItemCount() {
                return subCategoryList.size();
            }

            class SubCategoryItemViewHolder extends RecyclerView.ViewHolder {
                public RecyclerView menuRecyclerView;

                //onCreateViewHolder 의 mMenuView 임()
                SubCategoryItemViewHolder(@NonNull final View itemView) {
                    super(itemView);
                    menuRecyclerView = itemView.findViewById(R.id.recycler_menu);

                }

            }


            private class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuItemViewHolder> {
                public static final int NUM_CATEGORY_NAME= 1 + 2 << 24;
                public static final int NUM_SUB_CATEGORY_NAME = 2 + 2 << 24;
                Context mContext;
                List<RestaurantMenu> mMenuList;
                int mSubCategoryPosition;
                String mCategory1Name;
                public MenuItemViewHolder preSelectedViewHolder;

                MenuAdapter(Context context, String categoryName, List<RestaurantMenu> menuList, int subCategoryPosition) {
                    Log.d(TAG,  "  메뉴 사이즈   " + String.valueOf(menuList.size()));
                    if(mSubCategoryList != null && mSubCategoryList.get(subCategoryPosition) != null && menuList != null)
                        Log.d(TAG,  mSubCategoryList.get(subCategoryPosition).catergory2_name + "  메뉴 사이즈"+ menuList.size());
                    mContext = context;
                    mMenuList = menuList;
                    mSubCategoryPosition = subCategoryPosition;
                    mCategory1Name = categoryName;

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

                    if (mMenuList.get(idx).isSelected) {
                        holder.itemView.setBackgroundResource(R.drawable.shape_gray_edge);
                        holder.tvMenuName.setTextColor(getResources().getColor(R.color.black, Objects.requireNonNull(getActivity()).getTheme()));
                        holder.tvPrice.setTextColor(getResources().getColor(R.color.black, Objects.requireNonNull(getActivity()).getTheme()));
                    } else {
                        holder.itemView.setBackgroundColor(getResources().getColor(R.color.lightAliceBlue, Objects.requireNonNull(getActivity()).getTheme()));
                        holder.tvMenuName.setTextColor(getResources().getColor(R.color.gray, Objects.requireNonNull(getActivity()).getTheme()));
                        holder.tvPrice.setTextColor(getResources().getColor(R.color.gray, Objects.requireNonNull(getActivity()).getTheme()));
                    }

                    //              holder.tvMenuName.setText(mMenuList.get(idx).name + " " + mSubCategoryList.get(idx).catergory2_name);
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
                public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
                    super.onAttachedToRecyclerView(recyclerView);
                    if (categoryList == null || subCategoryList == null)
                        return;
                    else {
                        mTVCateName.setText(mCategory1Name);
                        mTvSubCateName.setText(subCategoryList.get(mSubCategoryPosition).catergory2_name);
                        Log.d(TAG, "onViewAttachedToREcycler Menu    " + mCategory1Name + " " + subCategoryList.get(mSubCategoryPosition).catergory2_name);
                    }

                }

//                @Override
//                public void onViewAttachedToWindow(@NonNull MenuItemViewHolder holder) {
//                    super.onViewAttachedToWindow(holder);
//
//                    if (categoryList == null || subCategoryList == null)
//                        return;
//                    else {
//                        mTVCateName.setText(mCategory1Name);
//                        mTvSubCateName.setText(subCategoryList.get(mSubCategoryPosition).catergory2_name);
//                        Log.d(TAG, "onViewAttachedToWindow Menu    " + mCategory1Name + " " + subCategoryList.get(mSubCategoryPosition).catergory2_name);
//                    }
//
//                }

//                @Override
//                public void onViewDetachedFromWindow(@NonNull MenuItemViewHolder holder) {
//                    super.onViewDetachedFromWindow(holder);
//                    mTVCateName.setText(mCategory1Name);
//                    mTvSubCateName.setText(subCategoryList.get(mSubCategoryPosition ).catergory2_name);
//                }


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
//                        if (mOrderDetailList.size() == 0) {
//                            Toast.makeText(mContext, "주문오더가 없습니다.", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
                        if (v.getTag().equals(mOrderDetailList.get(idx).reserve_guest_id)) {
                            infoTextView.setVisibility(View.GONE);
                            ((TextView) v).setTextColor(getResources().getColor(R.color.white, Objects.requireNonNull(getActivity()).getTheme()));
                            (v).setBackgroundResource(R.drawable.shape_ebony_black_background_and_edge);
                            mOrderDetailList.get(idx).addOrPlusOrderedMenuItem(mOrderedMenuItem);
                            mOrderDetailList.get(idx).setTotalPaidAmount(mOrderDetailList.get(idx).getPaid_total_amount());
                            makeOrderItems();
                            makeOrderItemInvoiceArrViews();
                            setTheTotalInvoice();
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
                    //여기에선  mOrderedMenuItem qty가이 항상 1이어야 한다..
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
                    RestaurantOrder restaurantOrder = new RestaurantOrder();
                    restaurantOrder.setOrderDetailList(mOrderDetailList);
                    AppDef.restaurantOrderArrayList.add(restaurantOrder);
                    //refresh();

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