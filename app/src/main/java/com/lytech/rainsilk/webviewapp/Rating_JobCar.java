package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by rainsilk on 2016/3/4.
 */
public class Rating_JobCar extends Activity {
    private RatingBar rb_normal;
    private RatingBar rb_service;
    private TextView txtRatingID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating);
        Intent intent = getIntent();
        String id = intent.getStringExtra("ID");

        txtRatingID =(TextView)findViewById(R.id.txtRatingid);
        txtRatingID.setText("所評價的會員ID: "+id);

        rb_normal =  (RatingBar) findViewById(R.id.ratingBar);
        rb_normal.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(Rating_JobCar.this, "rating:" + String.valueOf(rating), Toast.LENGTH_LONG).show();

            }
        });

        rb_service =  (RatingBar) findViewById(R.id.ratingBar2);
        rb_service.setOnRatingBarChangeListener(new  RatingBar.OnRatingBarChangeListener()  {
            @Override
            public  void onRatingChanged(RatingBar ratingBar,  float rating,  boolean fromUser)
            {
                Toast.makeText(Rating_JobCar.this, "rating:" + String.valueOf(rating), Toast.LENGTH_LONG).show();

                Toast.makeText(Rating_JobCar.this, "評價完成!", Toast.LENGTH_LONG).show();

                Intent i = new Intent(Rating_JobCar.this, AFirstPage.class);
                startActivity(i);
                overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
            }
        });
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.btncheck) {
            finish();
        }
    }
}
