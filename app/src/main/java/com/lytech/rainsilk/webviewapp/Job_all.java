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
public class Job_all extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_job_all);
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.bt1) {
            AlertDialog.Builder ab = new AlertDialog.Builder(Job_all.this);

            ab.setTitle("說明")
                    .setMessage("車輛排班提供您載客的管道, 責任由合作車隊承擔, 本APP僅提供平台")
                    .setCancelable(true);

            ab.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(Job_all.this, Job_all_car.class);
                    startActivity(i);
                }
            });
            ab.setNegativeButton(android.R.string.cancel, null);
            ab.show();

        }

        if (v.getId() == R.id.bt2) {
            Intent i = new Intent(Job_all.this, Service_all_people.class);
            startActivity(i);
            overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
        }

        if (v.getId() == R.id.bt3) {
            Intent i = new Intent(Job_all.this, Service_all_purchase.class);
            startActivity(i);
            overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
        }

        if (v.getId() == R.id.bt4) {
            Intent i = new Intent(Job_all.this, Service_all_other.class);
            startActivity(i);
            overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
        }

    }
}
