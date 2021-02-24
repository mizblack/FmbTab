package com.eye3.golfpay.dialog;

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

import com.eye3.golfpay.R;
import com.eye3.golfpay.adapter.HotkeyCategoryAdapter;
import com.eye3.golfpay.adapter.RestaurantCategoryAdapter;
import com.eye3.golfpay.model.control.ChatHotKeyOption;
import com.eye3.golfpay.model.order.Category;
import com.eye3.golfpay.model.order.Category2;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-09-22.
 */

public class ControlShortCutDialog extends Dialog {

    public interface IListenerApplyShortcut {
        void onApplyShortcut(String shortcut);
        void onClose();
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
    private ArrayList<ChatHotKeyOption> options;
    private final int currentCt1Index = -1;

    private final String[] shortcut = new String[5];

    private static final int DURATION = 500;

    public ControlShortCutDialog(Context context) {
        super(context);
    }

    public ControlShortCutDialog(Context context, ArrayList<ChatHotKeyOption> options, int themeResId) {
        super(context, themeResId);
        this.options = options;
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

        if (options.size() == 2) {
            initCategory1();
            initCategory2();
            category1.setVisibility(View.VISIBLE);
            category2.setVisibility(View.VISIBLE);
        } else if (options.size() == 3) {
            initCategory1();
            initCategory2();
            initCategory3();
            category1.setVisibility(View.VISIBLE);
            category2.setVisibility(View.VISIBLE);
            category3.setVisibility(View.VISIBLE);
        } else if (options.size() == 4) {
            initCategory1();
            initCategory2();
            initCategory3();
            initCategory4();
            category1.setVisibility(View.VISIBLE);
            category2.setVisibility(View.VISIBLE);
            category3.setVisibility(View.VISIBLE);
            category4.setVisibility(View.VISIBLE);
        }

        btnApply = findViewById(R.id.btn_apply);
        btnClose = findViewById(R.id.btn_close);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isValid()) {
                    Toast.makeText(getContext(), "모두 선택해 주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                String msg = getShortcutMessage();
                mListener.onApplyShortcut(msg);
                dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClose();
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

    private boolean isValid() {
        if (options.size() == 2) {
            return shortcut[0] != null && shortcut[1] != null;
        } else if (options.size() == 3) {
            return shortcut[0] != null && shortcut[1] != null && shortcut[2] != null;
        } else if (options.size() == 4) {
            return shortcut[0] != null && shortcut[1] != null && shortcut[2] != null && shortcut[3] != null;
        }

        return true;
    }

    private String getShortcutMessage() {
        if (options.size() == 2) {
            return String.format("%s에서 %s 분실했습니다.", shortcut[0], shortcut[1]);
        } else if (options.size() == 3) {
            return String.format("%s %s에서 %s 분실했습니다.", shortcut[0], shortcut[1], shortcut[2]);
        } else if (options.size() == 4) {
            return String.format("%s %s %s에서 %s 분실했습니다.", shortcut[0], shortcut[1], shortcut[2], shortcut[3]);
        }
        return "";
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

    private void addItem(HotkeyCategoryAdapter adapter, ChatHotKeyOption option) {
        for (ChatHotKeyOption.Detail detail : option.getDetail()) {
            adapter.addItem("", detail.text);
        }
    }

    private void initCategory1() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvCategory1.setHasFixedSize(true);
        rvCategory1.setLayoutManager(layoutManager);
        final HotkeyCategoryAdapter adapter = new HotkeyCategoryAdapter(getContext(), new HotkeyCategoryAdapter.IOnClickAdapter() {
            @Override
            public void onAdapterItemClicked(int position) {
                HotkeyCategoryAdapter adapter = (HotkeyCategoryAdapter)rvCategory1.getAdapter();
                shortcut[0] = adapter.getItem(position);
            }
        });

        addItem(adapter, options.get(0));
        rvCategory1.setAdapter(adapter);
    }

    private void initCategory2() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvCategory2.setHasFixedSize(true);
        rvCategory2.setLayoutManager(layoutManager);
        HotkeyCategoryAdapter adapter = new HotkeyCategoryAdapter(getContext(), new HotkeyCategoryAdapter.IOnClickAdapter() {
            @Override
            public void onAdapterItemClicked(int position) {
                HotkeyCategoryAdapter adapter = (HotkeyCategoryAdapter)rvCategory2.getAdapter();
                shortcut[1] = adapter.getItem(position);
            }
        });

        addItem(adapter, options.get(1));
        rvCategory2.setAdapter(adapter);
    }

    private void initCategory3() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvCategory3.setHasFixedSize(true);
        rvCategory3.setLayoutManager(layoutManager);
        HotkeyCategoryAdapter adapter = new HotkeyCategoryAdapter(getContext(), new HotkeyCategoryAdapter.IOnClickAdapter() {
            @Override
            public void onAdapterItemClicked(int position) {
                HotkeyCategoryAdapter adapter = (HotkeyCategoryAdapter)rvCategory3.getAdapter();
                shortcut[2] = adapter.getItem(position);
            }
        });

        addItem(adapter, options.get(2));
        rvCategory3.setAdapter(adapter);
    }

    private void initCategory4() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvCategory4.setHasFixedSize(true);
        rvCategory4.setLayoutManager(layoutManager);
        HotkeyCategoryAdapter adapter = new HotkeyCategoryAdapter(getContext(), new HotkeyCategoryAdapter.IOnClickAdapter() {
            @Override
            public void onAdapterItemClicked(int position) {
                HotkeyCategoryAdapter adapter = (HotkeyCategoryAdapter)rvCategory4.getAdapter();
                shortcut[3] = adapter.getItem(position);
            }
        });

        addItem(adapter, options.get(3));
        rvCategory4.setAdapter(adapter);
    }
}
