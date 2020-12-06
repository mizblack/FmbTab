package com.eye3.golfpay.model.order;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

//각 guest당 주문내역 (기본 4개 리스트로)
public class OrderDetail implements Serializable {
    public String TAG = getClass().getSimpleName();
    @SerializedName("reserve_guest_id")
    @Expose
    public String reserve_guest_id;
    //자동 계산됨
    @SerializedName("paid_total_amount")
    @Expose
    public String paid_total_amount = "0";

    @SerializedName("items")
    @Expose
    public ArrayList<OrderedMenuItem> mOrderedMenuItemList = new ArrayList<>();

    public ArrayList<OrderedMenuItem> getOrderedMenuItemList() {
        return mOrderedMenuItemList;
    }

    public OrderDetail(String reserve_guest_id, String paid_total_amount, ArrayList<OrderedMenuItem> mOrderedMenuItemList) {
        this.reserve_guest_id = reserve_guest_id;
        this.paid_total_amount = paid_total_amount;
        this.mOrderedMenuItemList = mOrderedMenuItemList;
    }

    public OrderDetail(String reserve_guest_id) {
        this.reserve_guest_id = reserve_guest_id;
        this.paid_total_amount = getPaid_total_amount();

        //    this.mOrderedMenuItemList = new ArrayList<>();
    }

    public void addOrPlusSelectedOrderedMenuItem(OrderedMenuItem orderedMenuItem, String guestId){
        if(reserve_guest_id.equals(guestId)){
            if (isOrderedMenuItemExist(orderedMenuItem.id)) {
                //중복 메뉴 추가시 qty 를 더한다.
                plusQty(orderedMenuItem);
            } else {  //중복 메뉴가 아니면 추가
                mOrderedMenuItemList.add(orderedMenuItem);
            }
        }else
            Log.d(TAG, "게스트가 주문한 메뉴가 아닙니다.");

        this.paid_total_amount=  getPaid_total_amount();
    }


    public boolean isOrderedMenuItemExist(String id) {

        if (mOrderedMenuItemList == null || id == "" || Integer.parseInt(id) < 0) {
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

    private void plusQty(OrderedMenuItem item) {
      //  int mAddQty = Integer.valueOf(item.qty);
        // if (Integer.valueOf(id) < 0)
        //  Log.d(TAG, "존재하지 않는 메뉴 아이디 입니다.");
        for (int i = 0; mOrderedMenuItemList.size() > i; i++) {
            if (item.id.equals(mOrderedMenuItemList.get(i).id)) {
                if (Integer.valueOf(mOrderedMenuItemList.get(i).qty) + Integer.valueOf(item.qty) < 0) {
                    Log.i(TAG, "orderMenuItem 수량 한도 미만");
                    return;
                }
               int addQty = Integer.valueOf(mOrderedMenuItemList.get(i).qty) + Integer.valueOf(item.qty);
                mOrderedMenuItemList.get(i).qty = String.valueOf(addQty);

                //중복 메뉴일경우 qty 추가후 total 을 다시계산
                mOrderedMenuItemList.get(i).setTotal();
                return;
            }
        }
    }

    private void minusQty(String id) {
        // if (Integer.valueOf(id) < 0)
        //  Log.d(TAG, "존재하지 않는 메뉴 아이디 입니다.");
        for (int i = 0; mOrderedMenuItemList.size() > i; i++) {
            if (id.equals(mOrderedMenuItemList.get(i).id)) {
                mOrderedMenuItemList.get(i).qty = String.valueOf(Integer.valueOf(mOrderedMenuItemList.get(i).qty) - 1);
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

    public OrderedMenuItem findOrderMenu(String id) {
        for (OrderedMenuItem item :mOrderedMenuItemList) {
            if (item.getId() == id)
                return item;
        }

        return null;
    }

    public Integer getTotalQty() {
        int qty = 0;
        for (OrderedMenuItem item :mOrderedMenuItemList) {
            qty += Integer.parseInt(item.qty);
        }

        return qty;
    }
}
