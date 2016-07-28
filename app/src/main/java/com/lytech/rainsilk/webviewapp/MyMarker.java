package com.lytech.rainsilk.webviewapp;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by rainsilk on 2015/12/18.
 */
public class MyMarker {
    //variables
    //    int id;
    LatLng position;
    String title;

    //another constructor
    public MyMarker(LatLng position, String title) {
        this.position = position;
        this.title = title;
    }

    public LatLng getPosition() {
        return this.position;
    }

    public void setPosition(String id) {
        this.position = position;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String count) {
        this.title = title;
    }


}
