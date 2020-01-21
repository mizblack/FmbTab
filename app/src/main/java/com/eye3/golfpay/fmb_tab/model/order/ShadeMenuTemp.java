package com.eye3.golfpay.fmb_tab.model.order;

import java.io.Serializable;

public class ShadeMenuTemp implements Serializable {
    private String name;
    private int price;

    public ShadeMenuTemp(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
