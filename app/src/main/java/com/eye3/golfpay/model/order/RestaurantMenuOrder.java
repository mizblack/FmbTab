package com.eye3.golfpay.model.order;

import android.content.Context;
import android.widget.Toast;

import com.eye3.golfpay.common.Global;

import java.util.ArrayList;
import java.util.List;

public class RestaurantMenuOrder {
    private static String STRING_CADDY = "(캐디)";

    private List<OrderDetail> mOrderDetailList;
    private List<OrderItemInvoice> mOrderItemInvoiceArrayList;
    public OrderedMenuItem mCurrentOrderedMenuItem;
    //주문자 아이디
    public String mOrderedGuestId;
    public String mOrderedGuestName = getGuestName(mOrderedGuestId);
    //   public OrderDetail mCurrentOrderDetail;
    //   public OrderItemInvoice mCurrentOrderItemInvoice;
    Context context;
    private int mSelectedRestaurantIdx = 0;

    public RestaurantMenuOrder(Context context, List<OrderDetail> orderDetailList) {
        this.mOrderDetailList = orderDetailList;
        this.mOrderItemInvoiceArrayList = new ArrayList<>();
        this.context = context;
    }

    public void setOrderedGuestId(String id) {
        mOrderedGuestId = id;
    }

    public List<OrderItemInvoice> getOrderItemInvoiceArrayList(){
        return mOrderItemInvoiceArrayList;
    }

    public void setOrderItemInvoiceArrayList(List<OrderItemInvoice> list){
        mOrderItemInvoiceArrayList = list;
    }

    public void clearOrderItemInvoiceArrayList() {
        mOrderItemInvoiceArrayList.clear();
    }

    public void setCurrentOrderedMenuItem(OrderedMenuItem orderedMenuItem) {
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
    public void addRestaurantMenuOrder(int selectedRestaurantIdx, OrderedMenuItem orderedMenuItem, String guestId) {
        mSelectedRestaurantIdx = selectedRestaurantIdx;
        addOrderMenuItemIntoOrderDetailList(orderedMenuItem, guestId);
        // orderedMenuItem.qty 가 양수냐 음수냐에 따라 가감됨
        addOrderedMenuItemIntoInvoiceArrayList(new OrderedMenuItem(orderedMenuItem.id, orderedMenuItem.qty, orderedMenuItem.price, orderedMenuItem.menuName, orderedMenuItem.caddy_id), guestId);
    }

    private void addOrderMenuItemIntoOrderDetailList(OrderedMenuItem orderedMenuItem, String guestId) {
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

    public int getOrderQty(RestaurantMenu menu, String guestId) {
        for (int i = 0; mOrderItemInvoiceArrayList.size() > i; i++) {
            OrderItemInvoice itemInvoice = mOrderItemInvoiceArrayList.get(i);
            if (itemInvoice.mMenuId.equals(menu.id)) {
                for (GuestNameOrder guestNameOrder : itemInvoice.mGuestNameOrders) {
                    if (guestNameOrder.mGuestId.equals(guestId)) {
                        return guestNameOrder.qty;
                    }
                }
            }

        }
        return 0;
    }

    private void addOrderedMenuItemIntoInvoiceArrayList(OrderedMenuItem currentOrderedMenuItem, String guestId) {

        OrderItemInvoice itemInvoice = null;
        if (mOrderItemInvoiceArrayList.size() == 0) {
            itemInvoice = new OrderItemInvoice();
            itemInvoice.restaurantId = mSelectedRestaurantIdx;
            itemInvoice.mMenuId = currentOrderedMenuItem.id;
            itemInvoice.mMenuName = currentOrderedMenuItem.menuName;
            itemInvoice.mMenuPrice = currentOrderedMenuItem.price;
            itemInvoice.mQty = Integer.parseInt(currentOrderedMenuItem.qty);
            itemInvoice.mGuestNameOrders.add((new GuestNameOrder(currentOrderedMenuItem.id, guestId, getGuestName(mOrderedGuestId), Integer.parseInt(currentOrderedMenuItem.qty), currentOrderedMenuItem.menuName, currentOrderedMenuItem.caddy_id)));
            mOrderItemInvoiceArrayList.add(itemInvoice);

        } else {
            for (int j = 0; mOrderItemInvoiceArrayList.size() > j; j++) {
                itemInvoice = mOrderItemInvoiceArrayList.get(j);

                if (itemInvoice.mMenuName.equals(currentOrderedMenuItem.menuName)) {
                    //메뉴이름이 같을때
                    itemInvoice.mQty += Integer.parseInt(currentOrderedMenuItem.qty);
                    //여기에선  mOrderedMenuItem qty가 항상 1이어야 한다..

                    putGuestNameOrder(itemInvoice, guestId, getGuestName(guestId), currentOrderedMenuItem);

                    return;
                }
            }
            //메뉴이름이 업스면 새인보이스생성
            itemInvoice = new OrderItemInvoice();
            itemInvoice.restaurantId = mSelectedRestaurantIdx;
            itemInvoice.mMenuName = currentOrderedMenuItem.menuName;
            itemInvoice.mMenuId = currentOrderedMenuItem.id;
            itemInvoice.mMenuPrice = currentOrderedMenuItem.price;
            itemInvoice.mQty = 1;
            //      if (currentOrderedMenuItem.caddy_id == "")
            itemInvoice.mGuestNameOrders.add((new GuestNameOrder(currentOrderedMenuItem.id, guestId, getGuestName(guestId), currentOrderedMenuItem)));
            //      else
            //           a_ItemInvoice.mGuestNameOrders.add((new GuestNameOrder(getGuestName(guestId) + STRING_CADDY, currentOrderedMenuItem)));

            mOrderItemInvoiceArrayList.add(itemInvoice);
        }
    }

    private void putGuestNameOrder(OrderItemInvoice itemInvoice, String guestId, String guestName, OrderedMenuItem orderedMenuItem) {
        if (itemInvoice.mGuestNameOrders.isEmpty())
            return;

        for (int i = 0; itemInvoice.mGuestNameOrders.size() > i; i++) {
            if (itemInvoice.mGuestNameOrders.get(i).mGuestName.equals(guestName)) {
                if (itemInvoice.mGuestNameOrders.get(i).caddy_id.equals(orderedMenuItem.caddy_id)) { //게스트 명이 같고 caddy 주문이 같으면 수량을 더한다..
                    int newQty = itemInvoice.mGuestNameOrders.get(i).qty + Integer.parseInt(orderedMenuItem.qty);
                    itemInvoice.mGuestNameOrders.set(i, new GuestNameOrder(orderedMenuItem.id, guestId, guestName, newQty, itemInvoice.mMenuName, orderedMenuItem.caddy_id));
                    return;
                }
            }
        }

        //게스트명이 같은것이 없으면 게스트명으로 새로 생성한다.
        itemInvoice.mGuestNameOrders.add(new GuestNameOrder(orderedMenuItem.id, guestId, guestName, Integer.parseInt(orderedMenuItem.qty), itemInvoice.mMenuName, orderedMenuItem.caddy_id));
    }

    public void clearRestaurantMenuOrder() {
        mOrderDetailList.clear();
        mOrderItemInvoiceArrayList.clear();
    }

    public void clearCurrentOrderedMenuItem() {
        this.mCurrentOrderedMenuItem = null;
    }


}
