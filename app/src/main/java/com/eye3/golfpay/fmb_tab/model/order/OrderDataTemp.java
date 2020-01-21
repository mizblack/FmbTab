package com.eye3.golfpay.fmb_tab.model.order;

import java.io.Serializable;

public class OrderDataTemp implements Serializable {

    private ShadeMenuTemp shadeMenuTemp;
    private Menu menu;

    private int member00Quantity = 0;
    private int member01Quantity = 0;
    private int member02Quantity = 0;
    private int member03Quantity = 0;

    private int shadeTabIndex = 0;
    private int shadeMenuIndex = 0;

    public ShadeMenuTemp getShadeMenu() {
        return shadeMenuTemp;
    }

    public int getPrice() {
        return shadeMenuTemp.getPrice()
                * (member00Quantity
                + member01Quantity
                + member02Quantity
                + member03Quantity);
    }

    public int getQuantity() {
        return member00Quantity
                + member01Quantity
                + member02Quantity
                + member03Quantity;
    }

    public void setShadeTabIndex(int shadeTabIndex) {
        this.shadeTabIndex = shadeTabIndex;
    }

    public int getShadeTabIndex() {
        return shadeTabIndex;
    }

    public void setShadeMenuIndex(int shadeMenuIndex) {
        this.shadeMenuIndex = shadeMenuIndex;
    }

    public int getShadeMenuIndex() {
        return shadeMenuIndex;
    }

    public void setShadeMenu(ShadeMenuTemp shadeMenuTemp) {
        this.shadeMenuTemp = shadeMenuTemp;
    }

    public int getMember00Quantity() {
        return member00Quantity;
    }

    public void setMember00Quantity(int member00Quantity) {
        this.member00Quantity = member00Quantity;
    }

    public int getMember01Quantity() {
        return member01Quantity;
    }

    public void setMember01Quantity(int member01Quantity) {
        this.member01Quantity = member01Quantity;
    }

    public int getMember02Quantity() {
        return member02Quantity;
    }

    public void setMember02Quantity(int member02Quantity) {
        this.member02Quantity = member02Quantity;
    }

    public int getMember03Quantity() {
        return member03Quantity;
    }

    public void setMember03Quantity(int member03Quantity) {
        this.member03Quantity = member03Quantity;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
