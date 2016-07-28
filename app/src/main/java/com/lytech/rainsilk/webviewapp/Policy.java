package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by rainsilk on 2016/3/4.
 */
public class Policy extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.policy);
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.btncheck) {
            finish();
        }
    }
}
