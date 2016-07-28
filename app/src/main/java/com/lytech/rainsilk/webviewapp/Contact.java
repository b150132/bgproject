package com.lytech.rainsilk.webviewapp;

import android.util.Log;

/**
 * Created by rainsilk on 2015/12/18.
 */
public class Contact {
    //variables
    //    int id;
    String name, email, uname, pass;
    String address, assoiation, job, introperson, lineid;
    String phone_num;

    String id;
    String location1, location2;
    String service;
    String count;
    String giftcount;
    String carcount;
    String evaluaion;
    String performance;
    String level;


    //another constructor
    public Contact(String name, String email, String uname, String pass, String phone_num,
                   String address, String assoiation, String job, String introperson, String lineid, String id, String count, String giftcount, String carcount, String evaluaion, String performance, String level) {
        this.name = name;
        this.email = email;
        this.uname = uname;
        this.pass = pass;
        this.phone_num = phone_num;

        this.address = address;
        this.assoiation = assoiation;
        this.job = job;
        this.introperson = introperson;
        this.lineid = lineid;

        this.id = id;
        this.count = count;
        this.giftcount = giftcount;
        this.carcount = carcount;
        this.evaluaion = evaluaion;
        this.performance = performance;
        this.level = level;
    }

    public Contact(String uname, String pass) {
        this.uname = uname;
        this.pass = pass;
        Log.e("rainsilkinfo", "uname=" + uname);
        Log.e("rainsilkinfo", "pass=" + pass);
    }

    public Contact(String id, String location1, String location2) {
        this.id = id;
        this.location1 = location1;
        this.location2 = location2;
        Log.e("rainsilkinfo", "id=" + id);
        Log.e("rainsilkinfo", "location1=" + location1);
        Log.e("rainsilkinfo", "location2=" + location2);

    }

    public String getService() {
        return this.service;
    }

    public void setService(String service) {
        Log.e("rainsilkinfo", "setservice=" + service);
        this.service = service;
    }


    public String getID() {
        return this.id;
    }

    public void setID(String id) {
        this.id = id;
    }

    //String address,String job,String introperson,String lineid
    public String getaddress() {
        return this.address;
    }

    public void setaddress(String address) {
        this.address = address;
    }

    public String getjob() {
        return this.job;
    }

    public void setjob(String job) {
        this.job = job;
    }

    public String getintroperson() {
        return this.introperson;
    }

    public void setintroperson(String introperson) {
        this.introperson = introperson;
    }

    public String getlineid() {
        return this.lineid;
    }

    public void setlineid(String lineid) {
        this.lineid = lineid;
    }

    public String getPhone_num() {
        return this.phone_num;
    }

    public void setPhone_num(String PhoneNum) {
        this.phone_num = PhoneNum;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUname() {
        return this.uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPass() {
        return this.pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return this.count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getGiftCount() {
        return this.giftcount;
    }

    public void setGiftCount(String giftcount) {
        this.giftcount = giftcount;
    }

    public String getCarCount() {
        return this.carcount;
    }

    public void setCarCount(String carcount) {
        this.carcount = carcount;
    }

    public String getEvalu() {
        return this.evaluaion;
    }

    public void setEvalu(String evaluaion) {
        this.evaluaion = evaluaion;
    }

    public String getPerfor() {
        return this.performance;
    }

    public void setPerfor(String performance) {
        this.performance = performance;
    }

    public String getLocation1() {
        return this.location1;
    }

    public void setLocation1(String location1) {
        this.location1 = location1;
    }

    public String getLocation2() {
        return this.location2;
    }

    public void setLocation2(String location2) {
        this.location2 = location2;
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

}
