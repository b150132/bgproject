package com.lytech.rainsilk.webviewapp;

/**
 * Created by rainsilk on 2015/12/18.
 */
public class Commodity {
    //variables
    String id;

    String name;
    String num, shop, price, goodcount, photoname, sale;
    String real, type;

    //another constructor
    public Commodity(String name, String num, String shop, String price, String goodcount, String photoname) {
        this.name = name;
        this.num = num;
        this.shop = shop;
        this.price = price;
        this.goodcount = goodcount;
        this.photoname = photoname;

    }
    public Commodity(String num, String shop, String sale) {
        this.num = num;
        this.shop = shop;
        this.sale = sale;
    }

    public String getID() {
        return this.id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return this.num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getShop() {
        return this.shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhotoname() {
        return this.photoname;
    }

    public void setPhotoname(String photoname) {
        this.photoname = photoname;
    }

    public String getCount() {
        return this.goodcount;
    }

    public void setCount(String goodcount) {
        this.goodcount = goodcount;
    }

    public String getSale() {
        return this.sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getReal() {
        return this.real;
    }

    public void setReal(String real) {
        this.real = real;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
