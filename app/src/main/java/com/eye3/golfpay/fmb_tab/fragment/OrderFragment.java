package com.eye3.golfpay.fmb_tab.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.databinding.FrRestaurantOrderBinding;
import com.eye3.golfpay.fmb_tab.model.order.Restaurant;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.net.ResponseData;

import java.util.ArrayList;
import java.util.Objects;

public class OrderFragment extends BaseFragment {

    private View tabsLinearLayout, applyTabLinearLayout, arrow;
    private TextView orderOrApplyTextView;
    protected String TAG = getClass().getSimpleName();
    FrRestaurantOrderBinding binding;
    ArrayList<TextView> TabList = new ArrayList<TextView>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
        }
      //  getRestaurantMenu();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_restaurant_order, container, false);
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


//    private void getRestaurantMenu() {
//        showProgress("식당 메뉴 정보를 가져오는 중입니다.");
//        DataInterface.getInstance().getRestaurantMenu(getActivity(), Global.CaddyNo, Global.selectedReservation.getReserveNo(), new DataInterface.ResponseCallback<ResponseData<Restaurant>>() {
//
//            //  DataInterface.getInstance().getRestaurantMenu(getActivity(), Global.CaddyNo, Global.selectedReservation.getReserveNo(), new DataInterface.ResponseCallback<ResponseData<Restaurant>>() {
//           DataInterface.getInstance(Global.HOST_ADDRESS_DEV).getRestaurantMenu(getActivity(), "29","95147541",new DataInterface.ResponseCallback<ResponseData<Restaurant>>(){
//                @Override
//                public void onSuccess (ResponseData< Restaurant > response) {
//                hideProgress();
//                if (response.getResultCode().equals("ok")) {
//                    response.getData();
//                } else if (response.getResultCode().equals("fail")) {
//                    Toast.makeText(getActivity(), response.getResultMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//                @Override
//                public void onError (ResponseData < Restaurant > response) {
//                hideProgress();
//                response.getError();
//            }
//
//                @Override
//                public void onFailure (Throwable t){
//                hideProgress();
//            }
//            });
//
//        }}

}


