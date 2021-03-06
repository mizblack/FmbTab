package com.eye3.golfpay.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eye3.golfpay.R;
import com.eye3.golfpay.common.AppDef;
import com.eye3.golfpay.common.BasicSpinnerAdapter;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.model.order.OrderDetail;
import com.eye3.golfpay.model.order.OrderedMenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderApplyFragment extends BaseFragment {

    protected String TAG = getClass().getSimpleName();
    LinearLayout mDutchPayLInear, mOneOverNLinear, mLinearPersonalBillContainer;
    int TOTAL_PAY_MEMBER = 0;
    int mTotalAmount;
    String mPayType = "dutch";
    private ImageView mArrow;
    dreammaker.android.widget.Spinner mSpinn_Pay_Person;
    BasicSpinnerAdapter mSpinnAdapter;
    String[] mPayPersonList;
    public static LinearLayout mTabsRootLinear;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mPayType = bundle.get("payType").toString();
        }
        TOTAL_PAY_MEMBER = AppDef.orderDetailList.size();
        mPayPersonList = new String[TOTAL_PAY_MEMBER];
        mTotalAmount = getTheTotalInvoice(AppDef.orderDetailList);
    }

    private int getTheTotalInvoice(List<OrderDetail> orderDetailList) {
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
                GoOrderLeftBoard(new OrderApplyFragment(), bundle);
            }
        });
        mOneOverNLinear = v.findViewById(R.id.oneOverN);
        mOneOverNLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("payType", "one_over_n");
                GoOrderLeftBoard(new OrderApplyFragment(), bundle);
            }
        });
        mLinearPersonalBillContainer = v.findViewById(R.id.linear_personal_bill_container);

        if (mPayType.equals("dutch"))
            createPersonalDutchPayBillViewList(AppDef.orderDetailList);
        else
            createOneOverNBillViewList(AppDef.orderDetailList);
        mArrow = v.findViewById(R.id.arrow);
        mArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mParentActivity.removeFragment(OrderApplyFragment.this);
                mTabsRootLinear.setVisibility(View.VISIBLE);
            }
        });
        mSpinn_Pay_Person = v.findViewById(R.id.spinn_pay_person);
        //    mSpinnAdapter = new ArrayAdapter<>(mContext, android.R.layout.select_dialog_multichoice, getResources().getStringArray(R.array.spinn_array));
//        mSpinnAdapter = new BasicSpinnerAdapter(getActivity(),
//                R.layout.simple_multiple_choice_single_line_item, AdapterDataProvider.getGuestList());
          mSpinnAdapter.setDropDownViewResource( R.layout.simple_multiple_choice_single_line_item);
        mSpinn_Pay_Person.setAdapter(mSpinnAdapter);
        mSpinn_Pay_Person.setSelection(0);
        mSpinn_Pay_Person.setText("???????????? ??????");
        mSpinn_Pay_Person.setGravity(Gravity.CENTER);
        mSpinn_Pay_Person.setTextColor(Color.WHITE);
        mSpinn_Pay_Person.setBackgroundColor(Color.BLACK);
//        mSpinn_Pay_Person.getDialogBuilder().setPositiveButton("??????", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                mSpinn_Pay_Person.getSelectedPositions();
//                disablePersonalBill();
//            }
//        });
        mSpinn_Pay_Person.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    mPayPersonList = s.toString().split(",");
                    TOTAL_PAY_MEMBER = mPayPersonList.length;
                    switch (mPayType) {
                        case "dutch":
                            createPersonalDutchPayBillViewList(AppDef.orderDetailList);
                            disablePersonalBillofSelectedGuest(mPayPersonList);
                            break;
                        case "one_over_n":
                            createOneOverNBillViewList(AppDef.orderDetailList);
                            disablePersonalBillofSelectedGuest(mPayPersonList);
                            break;

                    }

                }
            }
        });
        //?????? ?????? ??????
        mSpinn_Pay_Person.setOnSpinnerItemClickListener(new dreammaker.android.widget.Spinner.OnSpinnerItemClickListener() {
            @Override
            public boolean onClickSpinnerItem(dreammaker.android.widget.Spinner spinner, int adapterPosition) {
                return false;
            }

        });

        return v;

    }

    public  String getGuestName(String reserveId) {
        for (int i = 0; Global.selectedReservation.getGuestData().size() > i; i++) {
            if (Global.selectedReservation.getGuestData().get(i).getId().equals(reserveId)) {
                return Global.selectedReservation.getGuestData().get(i).getGuestName();
            }
        }
        return "";
    }

    ArrayList<String> getGuestList() {
        ArrayList<String> ArrList = new ArrayList<>();
        for (int i = 0; AppDef.orderDetailList.size() > i; i++) {
            ArrList.add(getGuestName(AppDef.orderDetailList.get(i).reserve_guest_id));
        }
        return ArrList;
    }

    private View createPersonalDutchPayBillView(OrderDetail orderDetail) {
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.personal_bill, null, false);

        TextView tvName = v.findViewById(R.id.memberNameTextView);
        tvName.setText(getGuestName(orderDetail.reserve_guest_id));
        TextView tvPrice = v.findViewById(R.id.memberPriceTextView);

        String strPrice = AppDef.priceMapper(Integer.valueOf(orderDetail.paid_total_amount));
        tvPrice.setText(strPrice + "???");
        LinearLayout memberOrderLinearLayout = v.findViewById(R.id.personalOrderLinearLayout);

        for (int i = 0; orderDetail.mOrderedMenuItemList.size() > i; i++) {
            OrderedMenuItem a_item = orderDetail.mOrderedMenuItemList.get(i);
            TextView tvOrderItem = new TextView(getActivity(), null, 0, R.style.ItemOrderBillTextStyle);
            LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(250, 50);
            lllp.gravity = Gravity.CENTER_VERTICAL;
            lllp.leftMargin = 10;
            tvOrderItem.setLayoutParams(lllp);
            tvOrderItem.setPadding(8, 0, 0, 0);
            String str = a_item.menuName + "x" + a_item.qty;
            tvOrderItem.setText(str);
            memberOrderLinearLayout.addView(tvOrderItem);
        }
        return v;

    }

    private View createOneOverNBillView(OrderDetail orderDetail) {
        String paidPortion = "1";
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.personal_bill, null, false);

        TextView tvName = v.findViewById(R.id.memberNameTextView);
        tvName.setText(getGuestName(orderDetail.reserve_guest_id));
        TextView tvPrice = v.findViewById(R.id.memberPriceTextView);
        String strAmount = String.valueOf(mTotalAmount / TOTAL_PAY_MEMBER);
        tvPrice.setText(strAmount + "???");
        LinearLayout memberOrderLinearLayout = v.findViewById(R.id.personalOrderLinearLayout);

        TextView TvOneOverN = new TextView(getActivity(), null, 0, R.style.ItemOrderBillTextStyle);
        LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(250, 50);
        lllp.gravity = Gravity.CENTER_VERTICAL;
        lllp.leftMargin = 10;
        TvOneOverN.setLayoutParams(lllp);
        TvOneOverN.setPadding(8, 0, 0, 0);
        String str = paidPortion + "/" + TOTAL_PAY_MEMBER;
        TvOneOverN.setText(str);
        memberOrderLinearLayout.addView(TvOneOverN);
        return v;

    }

    private void createPersonalDutchPayBillViewList(List<OrderDetail> orderDetailArrayList) {

        for (int i = 0; orderDetailArrayList.size() > i; i++) {
            View orderbillView = createPersonalDutchPayBillView(orderDetailArrayList.get(i));
            orderbillView.setTag(orderDetailArrayList.get(i));
            mLinearPersonalBillContainer.addView(orderbillView);
        }
    }

    private void createOneOverNBillViewList(List<OrderDetail> orderDetailArrayList) {
        mLinearPersonalBillContainer.removeAllViewsInLayout();
        for (int i = 0; orderDetailArrayList.size() > i; i++) {
            View orderbillView = createOneOverNBillView(orderDetailArrayList.get(i));
            orderbillView.setTag(orderDetailArrayList.get(i));
            mLinearPersonalBillContainer.addView(orderbillView);
        }
    }

    private void resetPersonalBill() {
        View v;
        TextView tvTextName = null, tvMemberPrice = null;
        LinearLayout memberOrderLinearLayout = null;
        for (int i = 0; mLinearPersonalBillContainer.getChildCount() > i; i++) {
            v = mLinearPersonalBillContainer.getChildAt(i);
            tvTextName = v.findViewById(R.id.memberNameTextView);
            tvMemberPrice = v.findViewById(R.id.memberPriceTextView);
            memberOrderLinearLayout = v.findViewById(R.id.personalOrderLinearLayout);
            Objects.requireNonNull(tvTextName).setTextColor(Color.BLACK);
            Objects.requireNonNull(tvMemberPrice).setTextColor(Color.BLACK);
            Objects.requireNonNull(tvMemberPrice).setText(((OrderDetail) (v.getTag())).paid_total_amount);

            Objects.requireNonNull(tvMemberPrice).setVisibility(View.VISIBLE);
            if (memberOrderLinearLayout != null)
                Objects.requireNonNull(memberOrderLinearLayout).setVisibility(View.VISIBLE);

        }
    }

    private boolean isExist(String name, String[] selectedGuestArr) {
        for (int i = 0; selectedGuestArr.length > i; i++) {
            if (name.equals(selectedGuestArr[i].trim())) {
                return true;
            }
        }
        return false;
    }

    private void disablePersonalBillofSelectedGuest(String[] selectedGuestArr) {
        resetPersonalBill();
        View v;
        TextView tvTextName = null, tvMemberPrice = null, tvOneOverN = null;
        LinearLayout memberOrderLinearLayout = null;
        for (int i = 0; mLinearPersonalBillContainer.getChildCount() > i; i++) {
            v = mLinearPersonalBillContainer.getChildAt(i);
            OrderDetail a_order = (OrderDetail) v.getTag();
            switch (mPayType) {
                case "dutch":
                    if (!isExist(getGuestName(a_order.reserve_guest_id), selectedGuestArr)) {
                        tvTextName = v.findViewById(R.id.memberNameTextView);
                        tvMemberPrice = v.findViewById(R.id.memberPriceTextView);
                        memberOrderLinearLayout = v.findViewById(R.id.personalOrderLinearLayout);
                        tvTextName.setTextColor(Color.parseColor("#88cccccc"));
                        tvMemberPrice.setVisibility(View.VISIBLE);
                        tvMemberPrice.setTextColor(Color.parseColor("#88cccccc"));
                        if (memberOrderLinearLayout != null)
                            memberOrderLinearLayout.setVisibility(View.INVISIBLE);

                    } else {
                        Objects.requireNonNull(tvMemberPrice).setVisibility(View.VISIBLE);
                    }

                    break;
                case "one_over_n":
                    if (!isExist(getGuestName(a_order.reserve_guest_id), selectedGuestArr)) {
                        tvTextName = v.findViewById(R.id.memberNameTextView);
                        tvMemberPrice = v.findViewById(R.id.memberPriceTextView);
                        memberOrderLinearLayout = v.findViewById(R.id.personalOrderLinearLayout);
                        tvOneOverN = v.findViewById(R.id.member01OneOverNTextView);
                        Objects.requireNonNull(tvTextName).setTextColor(Color.parseColor("#88cccccc"));
                        Objects.requireNonNull(tvMemberPrice).setVisibility(View.INVISIBLE);
                        Objects.requireNonNull(tvMemberPrice).setTextColor(Color.parseColor("#88cccccc"));
                        Objects.requireNonNull(tvOneOverN).setVisibility(View.INVISIBLE);
                        if (memberOrderLinearLayout != null)
                            Objects.requireNonNull(memberOrderLinearLayout).setVisibility(View.INVISIBLE);
                    }
                    break;
            }
        }
    }
}


