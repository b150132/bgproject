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
public class Job_all_car extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_job_car);
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.bt1) {
            AlertDialog.Builder ab = new AlertDialog.Builder(Job_all_car.this);

            ab.setTitle("說明")
                    .setMessage("如於排班成功後,無故不到未經溝通者,將賠償100點個人總現金給消費者,請謹慎使用")
                    .setCancelable(true);

            ab.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(Job_all_car.this, Service_sendcar.class);
                    i.putExtra("jobcode", "2");
                    startActivity(i);
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
            });
            ab.setNegativeButton(android.R.string.cancel, null);
            ab.show();
        }
        if (v.getId() == R.id.bt6) {
            AlertDialog.Builder ab = new AlertDialog.Builder(Job_all_car.this);

            ab.setTitle("說明")
                    .setMessage("如於排班成功後,無故不到未經溝通者,將賠償100點個人總現金給店家會員,請謹慎使用")
                    .setCancelable(true);

            ab.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(Job_all_car.this, Service_sendcar.class);
                    i.putExtra("jobcode", "4");
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
