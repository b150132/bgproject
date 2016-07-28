package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

/**
 * Created by rainsilk on 2016/3/4.
 */
public class ChooseCountType extends Activity {
    private Button buyc, usec, movec;
    private List<String> cancellist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_count_type);
        buyc = (Button) findViewById(R.id.btnbuycount);
        usec = (Button) findViewById(R.id.btnusecount);
        movec = (Button) findViewById(R.id.btnMovecount);
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.btnbuycount) { //買點數
            Toast.makeText(ChooseCountType.this, "購買點數", Toast.LENGTH_LONG).show();
            Intent i = new Intent(ChooseCountType.this, Type_count_server.class);
            startActivity(i);
            overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
        }
        if (v.getId() == R.id.btnusecount) { //賣點數
            Toast.makeText(ChooseCountType.this, "點數換錢請向公司聯繫！ ", Toast.LENGTH_LONG).show();
            //  Toast.makeText(ChooseCountType.this, "賣點數 " , Toast.LENGTH_LONG).show();
            //            Intent i = new Intent(ChooseCountType.this, Type_count_car.class);
//            startActivity(i);
//            overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
        }
        if (v.getId() == R.id.btnsrcmember) { //查詢點數用盡會員
//            Toast.makeText(ChooseCountType.this, "功能未開放 ", Toast.LENGTH_LONG).show();
            //查欄位 count <1000
            //  Toast.makeText(ChooseCountType.this, "注意: 移轉將扣手續費" , Toast.LENGTH_LONG).show();
            Intent i = new Intent(ChooseCountType.this, DisplayCountOutGuest.class);
            startActivity(i);
            overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
        }
    }
}
