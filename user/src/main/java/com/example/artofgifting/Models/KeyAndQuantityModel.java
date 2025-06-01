package com.example.artofgifting.Models;

import java.io.Serializable;

public class KeyAndQuantityModel implements Serializable {
    String prod_key,order_qty;

    public String getProd_key() {
        return prod_key;
    }

    public void setProd_key(String prod_key) {
        this.prod_key = prod_key;
    }

    public String getOrder_qty() {
        return order_qty;
    }

    public void setOrder_qty(String order_qty) {
        this.order_qty = order_qty;
    }
}
