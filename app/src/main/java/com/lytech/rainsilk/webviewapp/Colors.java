package com.lytech.rainsilk.webviewapp;

import android.graphics.Color;

/**
 * Created by rainsilk on 2016/1/20.
 */
public class Colors {

    private String code;

    private Colors(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public int parseColor() {
        return Color.parseColor(code);
    }
}
