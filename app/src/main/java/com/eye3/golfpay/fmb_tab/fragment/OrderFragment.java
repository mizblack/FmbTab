package com.eye3.golfpay.fmb_tab.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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


    public static final int NUM_MENU_NAME_KEY = 1 + 2 << 24;
    public static final int NUM_NAMEORDER_KEY = 2 + 2 << 24;


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
    private TextView mTVCateName, infoTextView, mTvTheRestaurant;
    private TextView mTotalPriceTextView;
    private Button mCancelButton;
    private Button mTempSaveButton;
    List<OrderItemInvoice> mOrderItemInvoiceArrayList = new ArrayList<>();
    List<OrderDetail> mOrderDetailList = new ArrayList<>();
    private ImageView mArrowToApply;
    static TextView preSelectedGuestView;
    RelativeLayout mRelOrderHistory, mRelSendOrder;
    Button mBtnHistory, mBtnAdd;

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
                if(AppDef.restaurantOrderArrayList.size() > 0){
                    mBtnHistory.setVisibility(View.VISIBLE);
                    orderOrApplyBtn.setVisibility(View.INVISIBLE);
                }else {
                    refresh();
                    AppDef.orderDetailList.clear();
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

        mTVCateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRestaurantList != null) {

                }
            }
        });

        mBtnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("restaurantList", mRestaurantList);
                bundle.putSerializable("orderdetailList", (Serializable)mOrderDetailList);
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
        ArrayList<Category> mCategoryList;
        //서브메뉴 전시시 필요함.
        ArrayList<Category2> mSubCategoryList;
        SubCategoryAdapter mSubCategoryAdapter;

        CategoryAdapter(Context context, ArrayList<Category> categoryList) {

            mContext = context;
            mCategoryList = categoryList;
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
            Log.d(TAG, "onBindViewHolder    " + mCategoryList.get(position_category).catergory1_name);

            List<Category2> a_sub_cate_list = mCategoryList.get(position_category).subCategoryList;

            mSubCategoryAdapter = new SubCategoryAdapter(mContext, a_sub_cate_list, position_category);
            holder.subCategoyryRecyclerView.setAdapter(mSubCategoryAdapter);
            holder.subCategoyryRecyclerView.setHasFixedSize(true);
            LinearLayoutManager mManager = new LinearLayoutManager(mContext);
            holder.subCategoyryRecyclerView.setLayoutManager(mManager);
            holder.itemView.setTag(position_category);
            mTVCateName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSubCategoryList = mCategoryList.get((int) holder.itemView.getTag()).subCategoryList;
                    for (int i = 0; mSubCategoryList.size() > i; i++) {
                        TextView tvSubmenu = new TextView(new ContextThemeWrapper(mContext, R.style.SubMenuTextView), null, 0);
                        LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(300, 50);
                        tvSubmenu.setLayoutParams(lllp);
                        tvSubmenu.setText(mSubCategoryList.get(i).catergory2_name);
                        tvSubmenu.setGravity(Gravity.CENTER);
                        tvSubmenu.setTag(i); //서브카테고리 position
                        tvSubmenu.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                int pos = (int)v.getTag();
                                mRecyclerCategory.smoothScrollToPosition(pos);
                                return false;
                            }
                        });
                        mLinearSubMenu.addView(tvSubmenu);

                    }
                }
            });

        }


        @Override
        public void onViewAttachedToWindow(@NonNull CategoryItemViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            //서브 카테고리 리스트 구현
            if (mCategoryList == null)
                return;                                                                                                                               //index0 수정할것

            //     mTVCateName.setText(mCategoryList.get((int) holder.itemView.getTag()).catergory1_name + "/" + mCategoryList.get((int) holder.itemView.getTag()).subCategoryList.get(0).catergory2_name);
            mTVCateName.setText(mCategoryList.get((int) holder.itemView.getTag()).catergory1_name);
        }

        @Override
        public void onViewDetachedFromWindow(@NonNull CategoryItemViewHolder holder) {
            super.onViewDetachedFromWindow(holder);
            if (mCategoryList == null)
                return;
            if ((int) holder.itemView.getTag() > 0)
                //수정할것
                mTVCateName.setText(mCategoryList.get((int) holder.itemView.getTag() - 1).catergory1_name);
            //      mTVCateName.setText(mCategoryList.get((int) holder.itemView.getTag()).catergory1_name + "/" + mCategoryList.get((int) holder.itemView.getTag()).subCategoryList.get(0).catergory2_name);
        }

        @Override
        public int getItemCount() {
            return mCategoryList.size();
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
            for (int i = 0; mCategoryList.size() > i; i++) {
                for (int k = 0; mCategoryList.size() > k; k++) {
                    ArrayList<Category2> tempList = mCategoryList.get(i).subCategoryList;
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
            int position_category1;
            MenuAdapter mMenuAdapter;

            public SubCategoryAdapter(Context context, List<Category2> subCategoryList, int position_category1) {
                this.mContext = context;
                this.subCategoryList = subCategoryList;
                this.position_category1 = position_category1;

            }


            @NonNull
            @Override
            public SubCategoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.restaurant_sub_cate_row, parent, false);
                return new SubCategoryItemViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull SubCategoryItemViewHolder holder, int position) {
                mMenuAdapter = new MenuAdapter(mContext, subCategoryList.get(position).Menus, position);
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


        }


        private class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuItemViewHolder> {
            Context mContext;
            List<RestaurantMenu> mMenuList;
            int mSubCategoryPosition;
            public MenuItemViewHolder preSelectedViewHolder;

            MenuAdapter(Context context, List<RestaurantMenu> menuList, int subCategoryPosition) {
                mContext = context;
                mMenuList = menuList;
                mSubCategoryPosition = subCategoryPosition;

            }


            @NonNull
            @Override
            // recyclerView 가 parent 임
            public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.restaurant_menu_row, parent, false);
                Log.d(TAG, "onCreateViewHolder    " + "MenuItemViewHolder");
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
                //     Log.d(TAG, "onBindViewHolder    " + mMenuList.get(idx).name + "MenuItemViewHolder");

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
                if(mMenuList.get(idx).price != null)
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
            }

            @Override
            public void onViewDetachedFromWindow(@NonNull MenuItemViewHolder holder) {
                super.onViewDetachedFromWindow(holder);
            }

            @Override
            public int getItemCount() {
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

    private void makeOrderItems() {

        OrderItemInvoice a_ItemInvoice = null;
        if (mOrderItemInvoiceArrayList.size() == 0) {
            a_ItemInvoice = new OrderItemInvoice();
            a_ItemInvoice.mMenunName = mOrderedMenuItem.name;
            a_ItemInvoice.mQty = 1;
            a_ItemInvoice.mNameOrders.add((new NameOrder(getGuestName(mOrderedGuestId), NUM_OF_SINGLE_ORDERED_ITEM)));
            mOrderItemInvoiceArrayList.add(a_ItemInvoice);


        } else {

            for (int j = 0; mOrderItemInvoiceArrayList.size() > j; j++) {
                a_ItemInvoice = mOrderItemInvoiceArrayList.get(j);

                if (a_ItemInvoice.mMenunName.equals(mOrderedMenuItem.name)) {
                    //메뉴이름이 같을때
                    a_ItemInvoice.mQty += NUM_OF_SINGLE_ORDERED_ITEM;
                    //여기에선  mOrderedMenuItem qty가이 항상 1이어야 한다..
                    putNameOrder(a_ItemInvoice, getGuestName(mOrderedGuestId), NUM_OF_SINGLE_ORDERED_ITEM);

                    return;
                }
            }
            //메뉴이름이 업스면 새인보이스생성
            a_ItemInvoice = new OrderItemInvoice();
            a_ItemInvoice.mMenunName = mOrderedMenuItem.name;
            a_ItemInvoice.mQty = 1;
            a_ItemInvoice.mNameOrders.add((new NameOrder(getGuestName(mOrderedGuestId), NUM_OF_SINGLE_ORDERED_ITEM)));

            mOrderItemInvoiceArrayList.add(a_ItemInvoice);
            return;


        }
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