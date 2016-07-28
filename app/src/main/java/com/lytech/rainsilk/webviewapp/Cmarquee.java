package com.lytech.rainsilk.webviewapp;

/**
 * Created by rainsilk on 2015/12/18.
 */
public class Cmarquee {
    //variables
    String id;
    String sendby;
    String area, text;
    String time;

    //another constructor
    public Cmarquee(String sendby, String area, String text, String time) {
        this.sendby = sendby;
        this.area = area;
        this.text = text;
        this.time = time;
    }

    public String getID() {
        return this.id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getSendby() {
        return this.sendby;
    }

    public void setSendby(String sendby) {
        this.sendby = sendby;
    }

    public String getArea() {
        return this.area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
