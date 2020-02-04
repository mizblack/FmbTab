package com.eye3.golfpay.fmb_tab.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.eye3.golfpay.fmb_tab.R;

import java.util.Objects;

public class OrderFragment extends BaseFragment {

    private View tabsLinearLayout, applyTabLinearLayout, arrow;
    private TextView orderOrApplyTextView;
    protected String TAG = getClass().getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_order, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tabsLinearLayout = Objects.requireNonNull(getView()).findViewById(R.id.tabsLinearLayout);
        applyTabLinearLayout = Objects.requireNonNull(getView()).findViewById(R.id.applyTabLinearLayout);
        orderOrApplyTextView = Objects.requireNonNull(getView()).findViewById(R.id.orderOrApplyTextView);

        tabsLinearLayout.setVisibility(View.VISIBLE);

        orderOrApplyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderOrApplyTextView.getText().toString().equals("주문하기")) {
                    tabsLinearLayout.setVisibility(View.GONE);
                    applyTabLinearLayout.setVisibility(View.VISIBLE);
                    orderOrApplyTextView.setText("적용하기");
                } else if (orderOrApplyTextView.getText().toString().equals("적용하기")) {
                    GoNativeScreenAdd(new ShadePaymentFragment(), null);
                }
            }
        });

        arrow = Objects.requireNonNull(getView()).findViewById(R.id.arrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabsLinearLayout.setVisibility(View.VISIBLE);
                applyTabLinearLayout.setVisibility(View.GONE);
                orderOrApplyTextView.setText("주문하기");
            }
        });

    }

}


