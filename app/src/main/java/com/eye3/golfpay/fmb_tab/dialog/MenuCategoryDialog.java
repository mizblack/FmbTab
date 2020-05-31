package com.eye3.golfpay.fmb_tab.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.adapter.ClubAdapter;
import com.eye3.golfpay.fmb_tab.adapter.RestaurantCategoryAdapter;
import com.eye3.golfpay.fmb_tab.model.order.Category;
import com.eye3.golfpay.fmb_tab.model.order.Category2;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-09-22.
 */

public class MenuCategoryDialog extends Dialog {

    private IListenerApplyCategory mListener;
    private ConstraintLayout category1;
    private ConstraintLayout category2;
    private ConstraintLayout category3;
    private TextView btnApply;
    private TextView btnClose;
    private RecyclerView rvCategory1;
    private RecyclerView rvCategory2;
    private RecyclerView rvCategory3;
    private ArrayList<Category> categories;

    private int currentCt1Index = -1;
    private String currentCt1Id = null;
    private String currentCt2Id = null;

    private static final int DURATION = 500;

    public interface IListenerApplyCategory {
        void onApplyCategory(String ct1Id, String ct2Id);
    }

    public MenuCategoryDialog(Context context) {
        super(context);
    }

    public MenuCategoryDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setListener(IListenerApplyCategory listener) {
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_menu_category);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        category1 = findViewById(R.id.view_category1);
        category2 = findViewById(R.id.view_category2);
        category3 = findViewById(R.id.view_category3);

        rvCategory1 = findViewById(R.id.rv_category1);
        rvCategory2 = findViewById(R.id.rv_category2);
        rvCategory3 = findViewById(R.id.rv_category3);
        initCategory1();
        initCategory2();

        btnApply = findViewById(R.id.btn_apply);
        btnClose = findViewById(R.id.btn_close);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentCt1Id == null || currentCt2Id == null) {
                    Toast.makeText(getContext(), "카테고리를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

//                String test = "category1: " + currentCt1Id + "  category2: " + currentCt2Id;
//                Toast.makeText(getContext(), test, Toast.LENGTH_SHORT).show();
                mListener.onApplyCategory(currentCt1Id, currentCt2Id);
                dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "닫기~", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showMenu();
            }
        }, 10);
    }

    public void setData(ArrayList<Category> categories) {
        this.categories = categories;
    }

    private void showMenu() {

        Animation RightSwipe = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        category1.startAnimation(RightSwipe);
        category1.setVisibility(View.VISIBLE);

        btnApply.animate()
                .alpha(1.0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        btnApply.setVisibility(View.VISIBLE);
                    }
                });

        btnClose.animate()
                .alpha(1.0f)
                .setDuration(DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        btnClose.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void initCategory1() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvCategory1.setHasFixedSize(true);
        rvCategory1.setLayoutManager(layoutManager);
        RestaurantCategoryAdapter adapter = new RestaurantCategoryAdapter(getContext(), new RestaurantCategoryAdapter.IOnClickAdapter() {
            @Override
            public void onAdapterItemClicked(int position) {

                if (currentCt1Index == position)
                    return;

                currentCt1Id = categories.get(position).catergory1_id;
                currentCt1Index = position;
                currentCt2Id = null;

                if (category2.getWidth() > 2) {
                    resetCategory2(position);
                    return;
                }

                ValueAnimator anim = ValueAnimator.ofInt(1, category1.getWidth());
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int val = (Integer) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = category2.getLayoutParams();
                        layoutParams.width = val;
                        category2.setLayoutParams(layoutParams);
                        category2.setVisibility(View.VISIBLE);
                        resetCategory2(position);
                    }
                });
                anim.setDuration(DURATION);
                anim.start();
            }
        });

        for (Category ct1 : categories) {
            adapter.addItem(ct1.catergory1_id, ct1.catergory1_name);
        }

        rvCategory1.setAdapter(adapter);
    }

    private void initCategory2() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvCategory2.setHasFixedSize(true);
        rvCategory2.setLayoutManager(layoutManager);
        RestaurantCategoryAdapter adapter = new RestaurantCategoryAdapter(getContext(), new RestaurantCategoryAdapter.IOnClickAdapter() {
            @Override
            public void onAdapterItemClicked(int position) {

                currentCt2Id = categories.get(currentCt1Index).subCategoryList.get(position).catergory2_id;

//                if (category3.getWidth() > 1)
//                    return;

//                ValueAnimator anim = ValueAnimator.ofInt(1, category1.getWidth());
//                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                        int val = (Integer) valueAnimator.getAnimatedValue();
//                        ViewGroup.LayoutParams layoutParams = category3.getLayoutParams();
//                        layoutParams.width = val;
//                        category3.setLayoutParams(layoutParams);
//                        category3.setVisibility(View.VISIBLE);
//                        initCategory3();
//                    }
//                });
//                anim.setDuration(DURATION);
//                anim.start();
            }
        });

        rvCategory2.setAdapter(adapter);
    }

    private void resetCategory2(int index) {
        RestaurantCategoryAdapter adapter = (RestaurantCategoryAdapter)rvCategory2.getAdapter();
        adapter.clearData();

        for (Category2 ct2: categories.get(index).subCategoryList) {
            adapter.addItem(ct2.catergory2_id, ct2.catergory2_name);
        }

        adapter.notifyDataSetChanged();
    }

    private void initCategory3() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvCategory3.setHasFixedSize(true);
        rvCategory3.setLayoutManager(layoutManager);
        RestaurantCategoryAdapter adapter = new RestaurantCategoryAdapter(getContext(), new RestaurantCategoryAdapter.IOnClickAdapter() {
            @Override
            public void onAdapterItemClicked(int count) {

            }
        });

        adapter.addItem("4", "볶음밥류");
        adapter.addItem("5", "볶음면류");

        rvCategory3.setAdapter(adapter);
    }
}
