package com.eye3.golfpay.fmb_tab.model.order;

import java.io.Serializable;

public class ApplyData implements Serializable {

    private ShadeMenuTemp shadeMenuTemp;
    private Menu menu;

    private int quantity;


    public void setShadeMenuTemp(ShadeMenuTemp shadeMenuTemp) {
        this.shadeMenuTemp = shadeMenuTemp;
    }

    public ShadeMenuTemp getShadeMenuTemp() {
        return shadeMenuTemp;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
