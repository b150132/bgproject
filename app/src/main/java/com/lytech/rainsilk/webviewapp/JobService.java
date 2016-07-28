package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by rainsilk on 2016/1/7.
 */
public class JobService extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_service);
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.ibService) {
            Intent i = new Intent(JobService.this, Service_all.class);
            startActivity(i);
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }

        if (v.getId() == R.id.ibJob) {
            Intent i = new Intent(JobService.this, Job_sendcar.class);
            startActivity(i);
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }

    }
}
