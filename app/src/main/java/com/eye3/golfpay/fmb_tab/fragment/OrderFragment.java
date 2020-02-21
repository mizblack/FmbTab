package com.eye3.golfpay.fmb_tab.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.databinding.FrRestaurantOrderBinding;
import com.eye3.golfpay.fmb_tab.model.order.Category;
import com.eye3.golfpay.fmb_tab.model.order.OrderDetail;
import com.eye3.golfpay.fmb_tab.model.order.OrederedMenuItem;
import com.eye3.golfpay.fmb_tab.model.order.Restaurant;
import com.eye3.golfpay.fmb_tab.model.order.RestaurantMenu;
import com.eye3.golfpay.fmb_tab.model.order.ShadeOrder;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.net.ResponseData;

import java.util.ArrayList;
import java.util.Objects;

public class OrderFragment extends BaseFragment {
    static int NUM_OF_RESTAURANT;
    protected String TAG = getClass().getSimpleName();
    //   private View tabsLinearLayout, applyTabLinearLayout, arrow;
    private Button orderOrApplyBtn , resetBtn;
    private ArrayList<Restaurant> mRestaurantList = new ArrayList<>();
    RecyclerView mRecyclerCategory, mRecyclerMenu;
    CategoryAdapter mCateAdapter;
    // MenuAdapter mMenuAdapter;
    FrRestaurantOrderBinding binding;
    TextView[] RestaurantTabBar;
    ArrayList<TextView> mGuestViewList = new ArrayList<>();

    ImageView mFoodImage;
    int mSelectedRestaurantTabIdx = 0;
    //탭홀더
    private LinearLayout mTabLinear, mGuestContainer;

    ShadeOrder mShadeOrders;
    //  OrderDetail mOrderDetail = new OrderDetail();
    ArrayList<OrderDetail> mOrderDetailList = new ArrayList<OrderDetail>();

    OrederedMenuItem mOrederedMenuItem = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
        }
        getRestaurantMenu();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_restaurant_order, container, false);
        mTabLinear = v.findViewById(R.id.tabLinearLayout);
        mGuestContainer = v.findViewById(R.id.guest_container);
        createGuestList(mGuestContainer);
        mRecyclerCategory = v.findViewById(R.id.recycler_category);
        mRecyclerMenu = v.findViewById(R.id.recycler_menu);
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
        for (int i = 0; mRestaurantList.size() > i; i++) {
            final int idx = i;
            tvRestTabBar[i] = new TextView(getActivity());
            tvRestTabBar[i].setLayoutParams(new ViewGroup.LayoutParams(150, ViewGroup.LayoutParams.MATCH_PARENT));
            tvRestTabBar[i].setTextAppearance(R.style.MainTabTitleTextView);
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

    //레스토랑바 선택시 보여주는 함수
    private void selectRestaurant(int selectedTabIdx) {
        for (int i = 0; RestaurantTabBar.length > i; i++) {
            RestaurantTabBar[i].setTextColor(Color.GRAY);

        }
        RestaurantTabBar[selectedTabIdx].setTextColor(Color.BLACK);
        RestaurantTabBar[selectedTabIdx].setVisibility(View.VISIBLE);
        setSelectedRestarant(selectedTabIdx);
    }

    private void setSelectedRestarant(int mSelectedRestaurantIdx) {
        initRecyclerView(mRecyclerCategory, mCateAdapter, mRestaurantList.get(mSelectedRestaurantIdx).categoryList);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // tabsLinearLayout = Objects.requireNonNull(getView()).findViewById(R.id.tabsLinearLayout);
        //   applyTabLinearLayout = Objects.requireNonNull(getView()).findViewById(R.id.applyTabLinearLayout);
        orderOrApplyBtn = Objects.requireNonNull(getView()).findViewById(R.id.orderOrApplyTextView);
        resetBtn =  Objects.requireNonNull(getView()).findViewById(R.id.resetButton);
//        tabsLinearLayout.setVisibility(View.VISIBLE);

        orderOrApplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderOrApplyBtn.getText().toString().equals("주문하기")) {
                    //    tabsLinearLayout.setVisibility(View.GONE);
                    //   applyTabLinearLayout.setVisibility(View.VISIBLE);
                    //   orderOrApplyBtn.setText("적용하기");
                    sendShadeOrders();
                } else if (orderOrApplyBtn.getText().toString().equals("적용하기")) {
                    GoNativeScreenAdd(new ShadePaymentFragment(), null);
                }
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               init();
            }
        });



//        arrow = Objects.requireNonNull(getView()).findViewById(R.id.arrow);
//        arrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tabsLinearLayout.setVisibility(View.VISIBLE);
//                applyTabLinearLayout.setVisibility(View.GONE);
//                orderOrApplyBtn.setText("주문하기");
//            }
//        });

    }

    private void init() {
        mOrederedMenuItem = null;
        mShadeOrders = null;
        mOrderDetailList.clear();
        refreshGuestList();
    }

    private void refreshGuestList() {
        mGuestViewList.clear();
        createGuestList(mGuestContainer);
    }

    private void initRecyclerView(RecyclerView recycler, CategoryAdapter cateAdapter, ArrayList<Category> categoryList) {
        cateAdapter = new CategoryAdapter(mContext, categoryList);
        recycler.setAdapter(cateAdapter);
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
                if (response.getResultCode().equals("ok")) {
                    response.getData();
                    mRestaurantList = (ArrayList<Restaurant>) response.getList();
                    NUM_OF_RESTAURANT = mRestaurantList.size();
                    RestaurantTabBar = new TextView[NUM_OF_RESTAURANT];
                    createTabBar(RestaurantTabBar, mRestaurantList);
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
                response.getError();
            }

            @Override
            public void onFailure(Throwable t) {
                hideProgress();
            }
        });

    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryItemViewHolder> {
        Context mContext;
        ArrayList<Category> mCategoryList;

        public CategoryAdapter(Context context, ArrayList<Category> categoryList) {
            mContext = context;
            mCategoryList = categoryList;
        }

        @NonNull
        @Override
        //recyclerview가 parent임
        public CategoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.restaurant_cate_row, parent, false);

            CategoryItemViewHolder viewHolder = new CategoryItemViewHolder(view);


            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final CategoryItemViewHolder holder, int position) {
            ArrayList<RestaurantMenu> menuList = mCategoryList.get(position).Menus;

            holder.mRecyclerMenu.setAdapter(new MenuAdapter(mContext, menuList));
            holder.mRecyclerMenu.setHasFixedSize(true);
            LinearLayoutManager mManager = new LinearLayoutManager(mContext);
            holder.mRecyclerMenu.setLayoutManager(mManager);

        }


        @Override
        public int getItemCount() {
            return mCategoryList.size();
        }

        public class CategoryItemViewHolder extends RecyclerView.ViewHolder {
            RecyclerView mRecyclerMenu;

            //onCreateViewHolder의 view임()
            public CategoryItemViewHolder(@NonNull final View itemView) {
                super(itemView);
                mRecyclerMenu = itemView.findViewById(R.id.recycler_menu);

            }
        }
    }

    private class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuItemViewHolder> {
        Context mContext;
        ArrayList<RestaurantMenu> mMenuList;

        public MenuAdapter(Context context, ArrayList<RestaurantMenu> menuList) {
            mContext = context;
            mMenuList = menuList;
        }

        @NonNull
        @Override
        //recyclerview가 parent임
        public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.restaurant_menu_row, parent, false);

            MenuAdapter.MenuItemViewHolder viewHolder = new MenuAdapter.MenuItemViewHolder(view);


            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final MenuAdapter.MenuItemViewHolder holder, int position) {
            final int idx = position;
            holder.tvMenuName.setText(mMenuList.get(idx).name);
            holder.tvPrice.setText(mMenuList.get(idx).price);
            holder.tvMenuId.setText(mMenuList.get(idx).id);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setFoodImage(mFoodImage, mMenuList.get(idx).image);
                    mOrederedMenuItem = new OrederedMenuItem(holder.tvMenuId.getText().toString().trim(), "1", holder.tvPrice.getText().toString().trim());
                }
            });


        }

        @Override
        public int getItemCount() {
            return mMenuList.size();
        }

        public class MenuItemViewHolder extends RecyclerView.ViewHolder {
            public TextView tvMenuName, tvPrice, tvMenuId;

            //onCreateViewHolder의 view임()
            public MenuItemViewHolder(@NonNull final View itemView) {
                super(itemView);
                tvMenuName = itemView.findViewById(R.id.tv_menu_name);
                tvPrice = itemView.findViewById(R.id.tv_menu_price);
                tvMenuId = itemView.findViewById(R.id.tv_menu_id);
//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mOrederedMenuItem = new OrederedMenuItem( tvMenuId.getText().toString().trim(),"1", tvPrice.getText().toString().trim());
//
//                    }
//                });

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

    void createGuestList(LinearLayout container) {

        int Size = Global.selectedReservation.getGuestData().size();
        for (int i = 0; Size > i; i++) {
            final int idx = i;
            TextView tv = new TextView(mContext);
            tv.setLayoutParams(new ViewGroup.LayoutParams(150, 100));
            tv.setTextAppearance(R.style.RankColumnTextView);
            tv.setText(Global.selectedReservation.getGuestData().get(i).getGuestName());
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //주문된 음식이 있다면
                    ((TextView) v).setBackgroundColor(getResources().getColor(R.color.black, getActivity().getTheme()));
                    ((TextView) v).setTextColor(getResources().getColor(R.color.white, getActivity().getTheme()));
                    if (mOrederedMenuItem != null) {
                        OrderDetail orderDetail = new OrderDetail();
                        orderDetail.reserve_guest_id = Global.selectedReservation.getGuestData().get(idx).getId();

                        orderDetail.addOrderedMenuItem(mOrederedMenuItem);
                        orderDetail.paid_total_amount = orderDetail.getTotalPaidAmount();
                        mOrderDetailList.add(orderDetail);
                    } else {
                        Toast.makeText(mContext, "주문한 음식이 없습니다. 먼저 음식을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            mGuestViewList.add(tv);
            container.addView(tv);

        }
    }

    private void sendShadeOrders() {
        mShadeOrders = new ShadeOrder(mRestaurantList.get(mSelectedRestaurantTabIdx).id, Global.reserveId, mOrderDetailList);
        DataInterface.getInstance().sendShadeOrders(mContext, mShadeOrders, new DataInterface.ResponseCallback<ResponseData<Object>>() {
            @Override
            public void onSuccess(ResponseData<Object> response) {
                if (response.getResultCode().equals("ok")) {
                    Toast.makeText(getActivity(), "주문이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    orderOrApplyBtn.setText("적용하기");
                    init();
                }
            }

            @Override
            public void onError(ResponseData<Object> response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }


}




