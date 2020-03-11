package com.eye3.golfpay.fmb_tab.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.AppDef;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.order.OrderDetail;
import com.eye3.golfpay.fmb_tab.model.order.OrderedMenuItem;

import java.util.ArrayList;

import static com.eye3.golfpay.fmb_tab.fragment.OrderFragment.mTabsRootLinear;

public class ApplyFragment extends BaseFragment {

    protected String TAG = getClass().getSimpleName();
    LinearLayout mDutchPayLInear, mOneOverNLinear, mLinearPersonalBillContainer;
    int TOTAL_NUM_OF_MEMBER = 0;
    int mTotalAmount;
    String mPayType = "dutch";
    private ImageView mArrow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mPayType = bundle.get("payType").toString();
        }
        TOTAL_NUM_OF_MEMBER = Global.orderDetailList.size();
        mTotalAmount = getTheTotalInvoice(Global.orderDetailList);
    }

    private int getTheTotalInvoice(ArrayList<OrderDetail> orderDetailList) {
        int theTotal = 0;
        for (int i = 0; orderDetailList.size() > i; i++)
            theTotal += Integer.parseInt(orderDetailList.get(i).getPaid_total_amount());
        return theTotal;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_apply, container, false);
        mDutchPayLInear = v.findViewById(R.id.dutchPay);
        mDutchPayLInear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("payType", "dutch");
                GoOrderLeftBoard(new ApplyFragment(), bundle);
            }
        });
        mOneOverNLinear = v.findViewById(R.id.oneOverN);
        mOneOverNLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("payType", "one_over_n");
                GoOrderLeftBoard(new ApplyFragment(), bundle);
            }
        });
        mLinearPersonalBillContainer = v.findViewById(R.id.linear_personal_bill_container);

        if (mPayType.equals("dutch"))
            createPersonalDutchPayBillViewList(Global.orderDetailList);
        else
            createOneOverNBillViewList(Global.orderDetailList);
        mArrow = v.findViewById(R.id.arrow);
        mArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mParentActivity.removeFragment(ApplyFragment.this);
                mTabsRootLinear.setVisibility(View.VISIBLE);
            }
        });
        return v;
    }

    private View createPersonalDutchPayBillView(OrderDetail orderDetail) {
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.personal_bill, null, false);

        TextView tvName = v.findViewById(R.id.memberNameTextView);
        tvName.setText(OrderFragment.getGuestName(orderDetail.reserve_guest_id));
        TextView tvPrice = v.findViewById(R.id.memberPriceTextView);

        String strPrice = AppDef.priceMapper(Integer.valueOf(orderDetail.paid_total_amount));
        tvPrice.setText(strPrice + "원");
        LinearLayout memberOrderLinearLayout = v.findViewById(R.id.memberOrderLinearLayout);

        for (int i = 0; orderDetail.mOrderedMenuItemList.size() > i; i++) {
            OrderedMenuItem a_item = orderDetail.mOrderedMenuItemList.get(i);
            TextView tvOrderItem = new TextView(getActivity(), null, 0, R.style.ItemOrderBillTextStyle);
            LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(250, 50);
            lllp.gravity = Gravity.CENTER_VERTICAL;
            lllp.leftMargin = 10;
            tvOrderItem.setLayoutParams(lllp);
            tvOrderItem.setPadding(8, 0, 0, 0);
            String str = a_item.name + "x" + a_item.qty;
            tvOrderItem.setText(str);
            memberOrderLinearLayout.addView(tvOrderItem);
        }
        return v;

    }

    private View createOneOverNBillView(OrderDetail orderDetail) {
        String paidPortion = "1";

        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.personal_bill, null, false);

        TextView tvName = v.findViewById(R.id.memberNameTextView);
        tvName.setText(OrderFragment.getGuestName(orderDetail.reserve_guest_id));
        TextView tvPrice = v.findViewById(R.id.memberPriceTextView);
        String strAmount = String.valueOf(mTotalAmount / TOTAL_NUM_OF_MEMBER);
        tvPrice.setText(strAmount + "원");
        LinearLayout memberOrderLinearLayout = v.findViewById(R.id.memberOrderLinearLayout);

        TextView TvOneOverN = new TextView(getActivity(), null, 0, R.style.ItemOrderBillTextStyle);
        LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(250, 50);
        lllp.gravity = Gravity.CENTER_VERTICAL;
        lllp.leftMargin = 10;
        TvOneOverN.setLayoutParams(lllp);
        TvOneOverN.setPadding(8, 0, 0, 0);
        String str = paidPortion + "/" + String.valueOf(TOTAL_NUM_OF_MEMBER);
        TvOneOverN.setText(str);
        memberOrderLinearLayout.addView(TvOneOverN);
        return v;

    }

    private void createPersonalDutchPayBillViewList(ArrayList<OrderDetail> orderDetailArrayList) {

        for (int i = 0; orderDetailArrayList.size() > i; i++) {
            View orderbillView = createPersonalDutchPayBillView(orderDetailArrayList.get(i));
            mLinearPersonalBillContainer.addView(orderbillView);
        }
    }

    private void createOneOverNBillViewList(ArrayList<OrderDetail> orderDetailArrayList) {

        for (int i = 0; orderDetailArrayList.size() > i; i++) {
            View orderbillView = createOneOverNBillView(orderDetailArrayList.get(i));
            mLinearPersonalBillContainer.addView(orderbillView);
        }
    }


}
