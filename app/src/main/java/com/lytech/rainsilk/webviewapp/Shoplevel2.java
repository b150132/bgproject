package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by rainsilk on 2016/3/4.
 */
public class Shoplevel2 extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_level2);
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.imgbtnBuynow) {
           finish();
        }
        if(v.getId() ==R.id.btnback) {
            finish();
        }
    }
}
