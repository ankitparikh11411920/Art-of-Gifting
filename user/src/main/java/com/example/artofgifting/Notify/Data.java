package com.example.artofgifting.Notify;

public class Data {
    private String Prodname;
    private String Address;
    private String OrderKey;
    private String ProdKey;
    private String Amount;
    private String Date;
    private String UserKey;
    private String Image;
    private String Username;
    private String Time;
    private String Qty;

    public Data(String prodname, String address, String orderKey, String prodKey, String amount, String date, String userKey, String image, String username, String time, String qty) {
        Prodname = prodname;
        Address = address;
        OrderKey = orderKey;
        ProdKey = prodKey;
        Amount = amount;
        Date = date;
        UserKey = userKey;
        Image = image;
        Username = username;
        Time = time;
        Qty = qty;
    }

    public Data() {
    }

    public String getProdname() {
        return Prodname;
    }

    public void setProdname(String prodname) {
        Prodname = prodname;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getOrderKey() {
        return OrderKey;
    }

    public void setOrderKey(String orderKey) {
        OrderKey = orderKey;
    }

    public String getProdKey() {
        return ProdKey;
    }

    public void setProdKey(String prodKey) {
        ProdKey = prodKey;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getUserKey() {
        return UserKey;
    }

    public void setUserKey(String userKey) {
        UserKey = userKey;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }
}
