package com.eye3.golfpay.fmb_tab.model.order;

import android.content.Context;
import android.widget.Toast;

import com.eye3.golfpay.fmb_tab.common.Global;

import java.util.ArrayList;
import java.util.List;

public class RestaurantMenuOrder {
    private static String STRING_CADDY = "(캐디)";

    private List<OrderDetail> mOrderDetailList = new ArrayList<>();//먼저 생성해야 아래리스트에 renew됨.
    private List<OrderItemInvoice> mOrderItemInvoiceArrayList = new ArrayList<>();
    public OrderedMenuItem mCurrentOrderedMenuItem;
    //주문자 아이디
    public String mOrderedGuestId;
    public String mOrderedGuestName = getGuestName(mOrderedGuestId);
    //   public OrderDetail mCurrentOrderDetail;
    //   public OrderItemInvoice mCurrentOrderItemInvoice;
    Context context;

    public RestaurantMenuOrder(Context context, List<OrderDetail> mOrderDetailList, List<OrderItemInvoice> mOrderItemInvoiceArrayList) {
        this.mOrderDetailList = mOrderDetailList;
        this.mOrderItemInvoiceArrayList = mOrderItemInvoiceArrayList;
        this.context = context;
    }

    public void setOrderedGuestId(String id) {
        mOrderedGuestId = id;
    }

    public List<OrderItemInvoice> getmOrderItemInvoiceArrayList(){
        return mOrderItemInvoiceArrayList;
    }

    public void setmCurrentOrderedMenuItem(OrderedMenuItem orderedMenuItem) {
        this.mCurrentOrderedMenuItem = orderedMenuItem;
    }

    void initOrderDetailList() {
        mOrderDetailList.clear();
        int Size = Global.selectedReservation.getGuestData().size();
        //최초주문시 사이즈가 0이면
        if (mOrderDetailList.size() == 0) {
            for (int i = 0; Size > i; i++) {
                mOrderDetailList.add(i, new OrderDetail(Global.selectedReservation.getGuestData().get(i).getId()));
            }
        }
    }

    //이 method를 통해 메뉴추가할것
    public void addRestaurantMenuOrder(OrderedMenuItem orderedMenuItem, String guestId) {
        addOderedMenuItemintoOrderDetailList(orderedMenuItem, guestId);
        // orderedMenuItem.qty 가 양수냐 음수냐에 따라 가감됨
        addOrderedMenuItemIntoInvoiceArrayList(new OrderedMenuItem(orderedMenuItem.id, orderedMenuItem.qty, orderedMenuItem.price, orderedMenuItem.menuName, orderedMenuItem.caddy_id), guestId);
    }

    private void addOderedMenuItemintoOrderDetailList(OrderedMenuItem orderedMenuItem, String guestId) {
        if (guestId != null) {
            OrderDetail orderDetail = mOrderDetailList.get(getIndexById(guestId));
            orderDetail.addOrPlusSelectedOrderedMenuItem(orderedMenuItem, guestId);
        } else
            Toast.makeText(context, "게스트 아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
    }


    private int getIndexByName(String guestName) {
        for (int i = 0; mOrderDetailList.size() > i; i++) {
            if (getGuestName(mOrderDetailList.get(i).reserve_guest_id).equals(guestName))
                return i;
        }
        return -1;

    }

    private int getIndexById(String guestId) {
        for (int i = 0; mOrderDetailList.size() > i; i++) {
            if (mOrderDetailList.get(i).reserve_guest_id.equals(guestId))
                return i;
        }
        return -1;

    }


    public String getGuestName(String reserveId) {
        for (int i = 0; Global.selectedReservation.getGuestData().size() > i; i++) {
            if (Global.selectedReservation.getGuestData().get(i).getId().equals(reserveId)) {
                return Global.selectedReservation.getGuestData().get(i).getGuestName();
            }
        }
        return "";
    }

    private void addOrderedMenuItemIntoInvoiceArrayList(OrderedMenuItem currentOrderedMenuItem, String guestId) {

        OrderItemInvoice a_ItemInvoice = null;
        if (mOrderItemInvoiceArrayList.size() == 0) {
            a_ItemInvoice = new OrderItemInvoice();
            a_ItemInvoice.mMenunName = currentOrderedMenuItem.menuName;
            a_ItemInvoice.mQty = Integer.valueOf(currentOrderedMenuItem.qty);
            a_ItemInvoice.mGuestNameOrders.add((new GuestNameOrder(getGuestName(mOrderedGuestId), Integer.valueOf(currentOrderedMenuItem.qty), currentOrderedMenuItem.menuName, currentOrderedMenuItem.caddy_id)));
            mOrderItemInvoiceArrayList.add(a_ItemInvoice);

        } else {
            for (int j = 0; mOrderItemInvoiceArrayList.size() > j; j++) {
                a_ItemInvoice = mOrderItemInvoiceArrayList.get(j);

                if (a_ItemInvoice.mMenunName.equals(currentOrderedMenuItem.menuName)) {
                    //메뉴이름이 같을때
                    a_ItemInvoice.mQty += Integer.valueOf(currentOrderedMenuItem.qty);
                    //여기에선  mOrderedMenuItem qty가 항상 1이어야 한다..

                    putGuestNameOrder(a_ItemInvoice, getGuestName(guestId), currentOrderedMenuItem);

                    return;
                }
            }
            //메뉴이름이 업스면 새인보이스생성
            a_ItemInvoice = new OrderItemInvoice();
            a_ItemInvoice.mMenunName = currentOrderedMenuItem.menuName;
            a_ItemInvoice.mQty = 1;
            //      if (currentOrderedMenuItem.caddy_id == "")
            a_ItemInvoice.mGuestNameOrders.add((new GuestNameOrder(getGuestName(guestId), currentOrderedMenuItem)));
            //      else
            //           a_ItemInvoice.mGuestNameOrders.add((new GuestNameOrder(getGuestName(guestId) + STRING_CADDY, currentOrderedMenuItem)));

            mOrderItemInvoiceArrayList.add(a_ItemInvoice);
            return;


        }
    }

    private void putGuestNameOrder(OrderItemInvoice a_iteminvoice, String guestName, OrderedMenuItem orderedMenuItem) {
        if (a_iteminvoice.mGuestNameOrders.size() < 0)
            return;
        for (int i = 0; a_iteminvoice.mGuestNameOrders.size() > i; i++) {
            if (a_iteminvoice.mGuestNameOrders.get(i).mGuestName.equals(guestName)) {
                if (a_iteminvoice.mGuestNameOrders.get(i).caddy_id.equals(orderedMenuItem.caddy_id)) { //게스트 명이 같고 caddy 주문이 같으면 수량을 더한다..
                    int newQty = a_iteminvoice.mGuestNameOrders.get(i).qty + Integer.valueOf(orderedMenuItem.qty);
                    a_iteminvoice.mGuestNameOrders.set(i, new GuestNameOrder(guestName, newQty, a_iteminvoice.mMenunName, orderedMenuItem.caddy_id));
//                } else //게스트 명이 같고 caddy 주문이 다르면 새로 생성하여 추가한다.
//                    a_iteminvoice.mGuestNameOrders.add(new GuestNameOrder(guestName, Integer.valueOf(orderedMenuItem.qty), a_iteminvoice.mMenunName, orderedMenuItem.caddy_id));
                    return;
                }
            }
        }
        //게스트명이 같은것이 없으면 게스트명으로 새로 생성한다.
        a_iteminvoice.mGuestNameOrders.add(new GuestNameOrder(guestName, Integer.valueOf(orderedMenuItem.qty), a_iteminvoice.mMenunName, orderedMenuItem.caddy_id));
    }

    public void clearRestaurantMenuOrder() {
        mOrderDetailList.clear();
        mOrderItemInvoiceArrayList.clear();
    }

    public void clearCurrentOrderedMenuItem() {
        this.mCurrentOrderedMenuItem = null;
    }


}
