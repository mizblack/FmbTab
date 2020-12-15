package com.eye3.golfpay.model.order;

public class GuestNameOrder {
    public String mMenuId;
    public String mGuestId;
    public String mGuestName;
    public int  qty = 0;
    public String caddy_id;
    public String mMenuName;

    public GuestNameOrder(String id, String guestId, String name, int qty , String menuName, String caddyId){
        this.mMenuId = id;
        this.mGuestId = guestId;
        this.mGuestName = name;
        this.qty = qty;
        this.caddy_id = caddyId;
        this.mMenuName = menuName;
    }

    public GuestNameOrder(String id, String guestId, String guestName ,OrderedMenuItem orderedMenuItem) {
        this.mMenuId = id;
        this.mGuestId = guestId;
        this.mGuestName = guestName;
        this.qty = Integer.parseInt(orderedMenuItem.qty);
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
