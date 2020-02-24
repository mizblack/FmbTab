package com.eye3.golfpay.fmb_tab.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderDetail implements Serializable {
 //   protected String TAG = getClass().getSimpleName();

    @SerializedName("reserve_guest_id")
    @Expose
    public String reserve_guest_id;
    @SerializedName("paid_total_amount")
    @Expose
    public String paid_total_amount;

    @SerializedName("items")
    @Expose
    public ArrayList<OrderedMenuItem> mOrderedMenuItemList = new ArrayList<OrderedMenuItem>();

    //    public OrderDetail(String reserve_guest_id , String paid_total_amount, OrderedMenuItem orederedMenuItem){
//        this.reserve_guest_id = reserve_guest_id;
//        this.paid_total_amount = paid_total_amount;
//        this.orderedMenuItemList.add(orederedMenuItem);
//    }
    public OrderDetail(){
        this.reserve_guest_id = "";
        this.paid_total_amount = "";
        this.mOrderedMenuItemList = new ArrayList<>();
    }

    public OrderDetail(String reserve_guest_id){
        this.reserve_guest_id = reserve_guest_id;
        this.paid_total_amount = "";
        this.mOrderedMenuItemList = new ArrayList<>();
    }

    public ArrayList<OrderedMenuItem> addOrPlusOrderedMenuItem(OrderedMenuItem orederedMenuItem) {
        if (isOrderedMenuItemExist(orederedMenuItem.id)) {
            //중복 메뉴 추가시 qty를 더한다.
            plusQty(orederedMenuItem.id);
        }else {  //중복 메뉴가 아니면 추가
            mOrderedMenuItemList.add(orederedMenuItem);
        }
        return mOrderedMenuItemList;
    }

    //
    public boolean isOrderedMenuItemExist(String id) {
        if (Integer.valueOf(id) < 0) {
          //  Log.d(TAG, "존재하지 않는 메뉴 아이디 입니다.");
        }
        for (int i = 0; mOrderedMenuItemList.size() > i; i++) {
            if (id.equals(mOrderedMenuItemList.get(i).id)) {
                return true;
            }
        }
        return false;
    }

    public void plusQty(String id) {
       // if (Integer.valueOf(id) < 0)
          //  Log.d(TAG, "존재하지 않는 메뉴 아이디 입니다.");

        for (int i = 0; mOrderedMenuItemList.size() > i; i++) {
            if (id.equals(mOrderedMenuItemList.get(i).id)) {
                mOrderedMenuItemList.get(i).qty = String.valueOf(Integer.valueOf(mOrderedMenuItemList.get(i).qty) + 1);
                return;
            }
        }

    }

    public int calculateTotalPaidAmount() {
        int tempTotal = 0;
        for (int i = 0; mOrderedMenuItemList.size() > i; i++) {

            tempTotal += Integer.valueOf(mOrderedMenuItemList.get(i).qty) * Integer.valueOf(mOrderedMenuItemList.get(i).price);
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
