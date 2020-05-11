package com.eye3.golfpay.fmb_tab.model.order;

public class GuestNameOrder {
    public String mGuestName;
    public int  qty = 0;
    public String caddy_id;
    public String mMenuName;

    public GuestNameOrder(String name, int qty , String menuName, String caddyId){
        this.mGuestName = name;
        this.qty = qty;
        this.caddy_id = caddyId;
        this.mMenuName = menuName;
    }

    public GuestNameOrder(String guestName ,OrderedMenuItem orderedMenuItem){
        this.mGuestName = guestName;
        this.qty = Integer.valueOf(orderedMenuItem.qty);
        this.caddy_id = orderedMenuItem.caddy_id;
        this.mMenuName = orderedMenuItem.menuName;
    }

    public void increaseQtyByOne(){
        qty++;
    }

    public void decreaseQtyByOne(){
         qty--;
    }

    public void increase(int addQty){
        qty += addQty;
    }

    public void decrease(int addQty){
        qty -= addQty;
    }


}
