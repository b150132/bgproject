package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Created by rainsilk on 2016/3/4.
 */
public class Topshop1_1 extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topshop_level2);
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.imgbtnBuynow) {
            Toast.makeText(Topshop1_1.this,"聯繫貨運中",Toast.LENGTH_SHORT).show();;
        }
    }
}
