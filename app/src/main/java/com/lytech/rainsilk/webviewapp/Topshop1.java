package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by rainsilk on 2016/3/4.
 */
public class Topshop1 extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topshop_level1);
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.imv3) {
           Intent i = new Intent(Topshop1.this,Topshop1_1.class);
            startActivity(i);
        }
    }
}
