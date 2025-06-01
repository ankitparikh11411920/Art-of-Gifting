package com.example.artofgifting.Models;

public class OrderModel {
    private String order_key,user_key,prod_key,orderqty,order_address,finalamount, Prodname,orderdate,ordertime,orderimageurl,orderedby;

    public OrderModel(){

    }

    public OrderModel(String order_key, String user_key, String prod_key, String orderqty, String order_address, String finalamount, String prodname, String orderdate, String ordertime, String orderimageurl, String orderedby) {
        this.order_key = order_key;
        this.user_key = user_key;
        this.prod_key = prod_key;
        this.orderqty = orderqty;
        this.order_address = order_address;
        this.finalamount = finalamount;
        this.Prodname = prodname;
        this.orderdate = orderdate;
        this.ordertime = ordertime;
        this.orderimageurl = orderimageurl;
        this.orderedby = orderedby;
    }

    public String getOrder_key() {
        return order_key;
    }

    public void setOrder_key(String order_key) {
        this.order_key = order_key;
    }

    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public String getProd_key() {
        return prod_key;
    }

    public void setProd_key(String prod_key) {
        this.prod_key = prod_key;
    }

    public String getOrderqty() {
        return orderqty;
    }

    public void setOrderqty(String orderqty) {
        this.orderqty = orderqty;
    }

    public String getOrder_address() {
        return order_address;
    }

    public void setOrder_address(String order_address) {
        this.order_address = order_address;
    }

    public String getFinalamount() {
        return finalamount;
    }

    public void setFinalamount(String finalamount) {
        this.finalamount = finalamount;
    }

    public String getProdname() {
        return Prodname;
    }

    public void setProdname(String prodname) {
        this.Prodname = prodname;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public String getOrderimageurl() {
        return orderimageurl;
    }

    public void setOrderimageurl(String orderimageurl) {
        this.orderimageurl = orderimageurl;
    }

    public String getOrderedby() {
        return orderedby;
    }

    public void setOrderedby(String orderedby) {
        this.orderedby = orderedby;
    }
}
