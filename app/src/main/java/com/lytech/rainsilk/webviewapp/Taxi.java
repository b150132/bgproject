package com.lytech.rainsilk.webviewapp;

/**
 * Created by rainsilk on 2015/12/18.
 */
public class Taxi {
    //variables
    String id;

    String time, locatone, locattwo, carid, guestid, ccount, gcount;
    String valuation, cevalu, gevalu;

    //another constructor
    public Taxi(String time, String locatone, String locattwo, String carid, String guestid, String ccount, String gcount, String valuation, String cevalu, String gevalu) {
        this.time = time;
        this.locatone = locatone;
        this.locattwo = locattwo;
        this.carid = carid;
        this.guestid = guestid;

        this.ccount = ccount;
        this.gcount = gcount;
        this.valuation = valuation;
        this.cevalu = cevalu;
        this.gevalu = gevalu;

    }

    public String getID() {
        return this.id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocatone() {
        return this.locatone;
    }

    public void setLocatone(String locatone) {
        this.locatone = locattwo;
    }

    public String getLocattwo() {
        return this.locattwo;
    }

    public void setLocattwo(String locattwo) {
        this.locattwo = locattwo;
    }

    public String getCarid() {
        return this.carid;
    }

    public void setCarid(String carid) {
        this.carid = carid;
    }

    public String getGuestid() {
        return this.guestid;
    }

    public void setGuestid(String guestid) {
        this.guestid = guestid;
    }

    public String getCcount() {
        return this.ccount;
    }

    public void setCcount(String ccount) {
        this.ccount = ccount;
    }

    public String getGcount() {
        return this.gcount;
    }

    public void setGcount(String gcount) {
        this.gcount = gcount;
    }

    public String getValuation() {
        return this.valuation;
    }

    public void setValuation(String valuation) {
        this.valuation = valuation;
    }

    public String getCevalu() {
        return this.cevalu;
    }

    public void setCevalu(String cevalu) {
        this.cevalu = cevalu;
    }

    public String getGevalu() {
        return this.gevalu;
    }

    public void setGevalu(String gevalu) {
        this.gevalu = gevalu;
    }

}
