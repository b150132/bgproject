package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by rainsilk on 2016/2/16.
 */
public class Service_all_car extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_car);
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.bt1) {
            AlertDialog.Builder ab = new AlertDialog.Builder(Service_all_car.this);

            ab.setTitle("說明")
                    .setMessage("如於叫車成功後,無故不到未經溝通者,將賠償100點個人總現金給服務提供者,請謹慎使用")
                    .setCancelable(true);

            ab.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(Service_all_car.this, Job_sendcar.class);
                    i.putExtra("servicecode", "1");
                    startActivity(i);
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
            });
            ab.setNegativeButton(android.R.string.cancel, null);
            ab.show();
        }

        if (v.getId() == R.id.bt6) {
            AlertDialog.Builder ab = new AlertDialog.Builder(Service_all_car.this);

            ab.setTitle("說明")
                    .setMessage("此功能提供店家送貨,無故不到未經溝通者,將賠償100點個人總現金給服務提供者,請謹慎使用")
                    .setCancelable(true);

            ab.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(Service_all_car.this, Job_sendcar.class);
                    i.putExtra("servicecode", "3");
                    startActivity(i);
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
            });
            ab.setNegativeButton(android.R.string.cancel, null);
            ab.show();
        }
//
//        if(v.getId()==R.id.bt2)
//        {
//            Intent i = new Intent(Service_all_car.this,Buy.class);
//            startActivity(i);
//            overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
//        }
//
//        if(v.getId()==R.id.bt3)
//        {
//            Intent i = new Intent(Service_all_car.this,Buy.class);
//            startActivity(i);
//            overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
//        }
//
//        if(v.getId()==R.id.bt4)
//        {
//            Intent i = new Intent(Service_all_car.this,Buy.class);
//            startActivity(i);
//            overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
//        }

    }
}
