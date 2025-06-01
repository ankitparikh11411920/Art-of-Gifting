package com.example.artofgifting.Models;

import java.io.Serializable;

public class CartModel implements Serializable {
    String cart_qty;
    String prod_key;

    public String getCart_qty() {
        return cart_qty;
    }

    public void setCart_qty(String cart_qty) {
        this.cart_qty = cart_qty;
    }

    public String getProd_key() {
        return prod_key;
    }

    public void setProd_key(String prod_key) {
        this.prod_key = prod_key;
    }

}

