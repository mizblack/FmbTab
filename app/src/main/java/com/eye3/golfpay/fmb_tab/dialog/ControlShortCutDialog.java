package com.eye3.golfpay.fmb_tab.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.adapter.RestaurantCategoryAdapter;
import com.eye3.golfpay.fmb_tab.model.order.Category;
import com.eye3.golfpay.fmb_tab.model.order.Category2;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-09-22.
 */

public class ControlShortCutDialog extends Dialog {

    public interface IListenerApplyShortcut {
        void onApplyShortcut(String shortcut);
    }

    private IListenerApplyShortcut mListener;
    private ConstraintLayout layout_container;
    private ConstraintLayout category1;
    private ConstraintLayout category2;
    private ConstraintLayout category3;
    private ConstraintLayout category4;
    private TextView btnApply;
    private TextView btnClose;
    private RecyclerView rvCategory1;
    private RecyclerView rvCategory2;
    private RecyclerView rvCategory3;
    private RecyclerView rvCategory4;

    private int currentCt1Index = -1;

    private String[] shortcut = new String[5];

    private static final int DURATION = 500;

    public ControlShortCutDialog(Context context) {
        super(context);
    }

    public ControlShortCutDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setListener(IListenerApplyShortcut listener) {
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_control_shortcut);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        layout_container = findViewById(R.id.layout_container);
        category1 = findViewById(R.id.view_category1);
        category2 = findViewById(R.id.view_category2);
        category3 = findViewById(R.id.view_category3);
        category4 = findViewById(R.id.view_category4);

        rvCategory1 = findViewById(R.id.rv_category1);
        rvCategory2 = findViewById(R.id.rv_category2);
        rvCategory3 = findViewById(R.id.rv_category3);
        rvCategory4 = findViewById(R.id.rv_category4);

        initCategory1();
        initCategory2();
        initCategory3();
        initCategory4();

        btnApply = findViewById(R.id.btn_apply);
        btnClose = findViewById(R.id.btn_close);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = String.format("%s %s %s에서 %s 분실했습니다.", shortcut[0], shortcut[1], shortcut[2], shortcut[3]);
                mListener.onApplyShortcut(msg);
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

    }

    private void showMenu() {

        Animation RightSwipe = AnimationUtils.loadAnimation(getContext(), R.anim.left_to_right);
        layout_container.startAnimation(RightSwipe);
        layout_container.setVisibility(View.VISIBLE);

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
        final RestaurantCategoryAdapter adapter = new RestaurantCategoryAdapter(getContext(), new RestaurantCategoryAdapter.IOnClickAdapter() {
            @Override
            public void onAdapterItemClicked(int position) {
                RestaurantCategoryAdapter adapter = (RestaurantCategoryAdapter)rvCategory1.getAdapter();
                shortcut[0] = adapter.getItem(position);
            }
        });

        adapter.addItem("4", "IN");
        adapter.addItem("5", "OUT");
        adapter.addItem("5", "기타");

        rvCategory1.setAdapter(adapter);
    }

    private void initCategory2() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvCategory2.setHasFixedSize(true);
        rvCategory2.setLayoutManager(layoutManager);
        RestaurantCategoryAdapter adapter = new RestaurantCategoryAdapter(getContext(), new RestaurantCategoryAdapter.IOnClickAdapter() {
            @Override
            public void onAdapterItemClicked(int position) {
                RestaurantCategoryAdapter adapter = (RestaurantCategoryAdapter)rvCategory2.getAdapter();
                shortcut[1] = adapter.getItem(position);
            }
        });

        for (int i = 1; i < 10; i++)
            adapter.addItem("4", String.format("%d번홀", i));


        rvCategory2.setAdapter(adapter);
    }

    private void initCategory3() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvCategory3.setHasFixedSize(true);
        rvCategory3.setLayoutManager(layoutManager);
        RestaurantCategoryAdapter adapter = new RestaurantCategoryAdapter(getContext(), new RestaurantCategoryAdapter.IOnClickAdapter() {
            @Override
            public void onAdapterItemClicked(int position) {
                RestaurantCategoryAdapter adapter = (RestaurantCategoryAdapter)rvCategory3.getAdapter();
                shortcut[2] = adapter.getItem(position);
            }
        });

        adapter.addItem("4", "티박스");
        adapter.addItem("5", "페어웨이");
        adapter.addItem("5", "페어웨이벙커");
        adapter.addItem("5", "페어웨이러프");
        adapter.addItem("5", "그린주변");
        adapter.addItem("5", "그린위");
        adapter.addItem("5", "그린벙커");
        adapter.addItem("5", "카트도로");
        adapter.addItem("5", "상봉역");
        adapter.addItem("5", "상봉역주변");
        adapter.addItem("5", "상봉역1번출구");
        adapter.addItem("5", "상봉역2번출구");
        adapter.addItem("5", "상봉역벙커");
        adapter.addItem("5", "상봉역위");

        rvCategory3.setAdapter(adapter);
    }

    private void initCategory4() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvCategory4.setHasFixedSize(true);
        rvCategory4.setLayoutManager(layoutManager);
        RestaurantCategoryAdapter adapter = new RestaurantCategoryAdapter(getContext(), new RestaurantCategoryAdapter.IOnClickAdapter() {
            @Override
            public void onAdapterItemClicked(int position) {
                RestaurantCategoryAdapter adapter = (RestaurantCategoryAdapter)rvCategory4.getAdapter();
                shortcut[3] = adapter.getItem(position);
            }
        });

        adapter.addItem("4", "검");
        adapter.addItem("4", "방패");
        adapter.addItem("4", "반지");
        adapter.addItem("4", "목걸이");
        adapter.addItem("4", "도끼");
        adapter.addItem("4", "지팡이");
        adapter.addItem("4", "투구");
        adapter.addItem("4", "장갑");
        adapter.addItem("4", "물약");
        adapter.addItem("4", "고기");
        adapter.addItem("4", "약초");


        rvCategory4.setAdapter(adapter);
    }
}
