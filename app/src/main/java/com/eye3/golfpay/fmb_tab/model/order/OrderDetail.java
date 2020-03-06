package com.eye3.golfpay.fmb_tab.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderDetail implements Serializable {

    @SerializedName("reserve_guest_id")
    @Expose
    public String reserve_guest_id;

    @SerializedName("paid_total_amount")
    @Expose
    public String paid_total_amount = "0";

    @SerializedName("items")
    @Expose
    public ArrayList<OrderedMenuItem> mOrderedMenuItemList = new ArrayList<>();

    public ArrayList<OrderedMenuItem> getOrderedMenuItemList() {
        return mOrderedMenuItemList;
    }
    public OrderDetail() {

    }
    public OrderDetail(String reserve_guest_id) {
        this.reserve_guest_id = reserve_guest_id;
        this.paid_total_amount = "0";
        //    this.mOrderedMenuItemList = new ArrayList<>();
    }

    public void addOrPlusOrderedMenuItem(OrderedMenuItem mOrderedMenuItem) {
        if (isOrderedMenuItemExist(mOrderedMenuItem.id)) {
            //중복 메뉴 추가시 qty 를 더한다.
            plusQty(mOrderedMenuItem.id);
        } else {  //중복 메뉴가 아니면 추가
            mOrderedMenuItemList.add(mOrderedMenuItem);
        }
    }

    public boolean isOrderedMenuItemExist(String id) {

        if (mOrderedMenuItemList == null || Integer.parseInt(id) < 0) {
            //  Log.d(TAG, "존재하지 않는 메뉴 아이디 입니다.");
            return false;
        }

        for (int i = 0; mOrderedMenuItemList.size() > i; i++) {
            if (id.equals(mOrderedMenuItemList.get(i).id)) {
                return true;
            }
        }
        return false;
    }

    private void plusQty(String id) {
        // if (Integer.valueOf(id) < 0)
        //  Log.d(TAG, "존재하지 않는 메뉴 아이디 입니다.");
        for (int i = 0; mOrderedMenuItemList.size() > i; i++) {
            if (id.equals(mOrderedMenuItemList.get(i).id)) {
                mOrderedMenuItemList.get(i).qty = String.valueOf(Integer.valueOf(mOrderedMenuItemList.get(i).qty) + 1);
                //중복 메뉴일경우 qty 추가후 total 을 다시계산
                mOrderedMenuItemList.get(i).setTotal();
                return;
            }
        }
    }

    private int calculateTotalPaidAmount() {
        int tempTotal = 0;
        for (int i = 0; mOrderedMenuItemList.size() > i; i++) {

            tempTotal += Integer.parseInt(mOrderedMenuItemList.get(i).qty) * Integer.parseInt(mOrderedMenuItemList.get(i).price);
        }
        return (tempTotal);
    }

    public String getPaid_total_amount() {
        return String.valueOf(calculateTotalPaidAmount());
    }

    public void setTotalPaidAmount(String total) {
        this.paid_total_amount = total;
    }

}
