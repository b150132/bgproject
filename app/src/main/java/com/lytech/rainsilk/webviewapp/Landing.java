package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;


/**
 * Created by rainsilk on 2015/12/22.
 */
public class Landing extends Activity {
    FasterAnimationsContainer mFasterAnimationsContainer;
    private static final int[] IMAGE_RESOURCES = { R.drawable.bx00,
            R.drawable.bx01, R.drawable.bx02, R.drawable.bx03,
            R.drawable.bx04, R.drawable.bx05, R.drawable.bx06,
            R.drawable.bx07, R.drawable.bx08, R.drawable.bx09,
            R.drawable.bx10, R.drawable.bx11, R.drawable.bx12,
            R.drawable.bx13, R.drawable.bx14, R.drawable.bx15,
            R.drawable.bx16, R.drawable.bx17, R.drawable.bx18,
            R.drawable.bx19, R.drawable.bx20, R.drawable.bx21,
            R.drawable.bx22, R.drawable.bx23, R.drawable.bx24,
            R.drawable.bx25, R.drawable.bx26, R.drawable.bx27,
            R.drawable.bx28, R.drawable.bx29, R.drawable.bx30,
            R.drawable.bx31, R.drawable.bx32};

    private static final int ANIMATION_INTERVAL = 120;// 200ms

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_gif);
        ImageView imageView = (ImageView) findViewById(R.id.imageView1);
        mFasterAnimationsContainer = FasterAnimationsContainer
                .getInstance(imageView);
        mFasterAnimationsContainer.addAllFrames(IMAGE_RESOURCES,
                ANIMATION_INTERVAL);
        mFasterAnimationsContainer.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(Landing.this, MainActivity.class);
                Landing.this.startActivity(mainIntent);
                Landing.this.finish();

                overridePendingTransition(R.anim.abc_fade_in,
                        R.anim.abc_fade_out);
            }
        }, 8000);
    }
}
