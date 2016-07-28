package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by rainsilk on 2016/3/4.
 */
public class Topshop2 extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topshop2_level1);
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.imv3) {
           Intent i = new Intent(Topshop2.this,Topshop1_1.class);
            startActivity(i);
        }
    }
}
