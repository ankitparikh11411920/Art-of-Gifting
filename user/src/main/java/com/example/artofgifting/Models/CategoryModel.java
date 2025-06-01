package com.example.artofgifting.Models;

public class CategoryModel {
    private String catname,catimageurl,catkey;

    public String getCatkey() {
        return catkey;
    }

    public void setCatkey(String catkey) {
        this.catkey = catkey;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getCatimageurl() {
        return catimageurl;
    }

    public void setCatimageurl(String catimageurl) {
        this.catimageurl = catimageurl;
    }
}
