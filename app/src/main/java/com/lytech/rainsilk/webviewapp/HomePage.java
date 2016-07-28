package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by rainsilk on 2015/12/24.
 */
public class HomePage extends Activity {
    private ImageButton mImageButton1;
    private ImageButton mImageButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        mImageButton1 =(ImageButton) findViewById(R.id.imageButton);
        mImageButton2 =(ImageButton) findViewById(R.id.imageButton2);
        //mImageButton1.setImageResource(R.drawable.videophoto);

        getWindow().setFormat(PixelFormat.UNKNOWN);//??

//        //display a video file
//        VideoView mVideoView= (VideoView)findViewById(R.id.videoView1);
//
//        String uriPath2 = "android.resource://com.lytech.rainsilk.webviewapp/"+R.raw.img1768;
//        Uri uri2 = Uri.parse(uriPath2);
//        mVideoView.setVideoURI(uri2);
//        mVideoView.requestFocus();
 //       mVideoView.start();

//        buttonPlayVideo.setOnClickListener(new Button.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                VideoView mVideoView= (VideoView)findViewById(R.id.videoView1);
//
//                String uriPath2 = "android.resource://com.lytech.rainsilk.webviewapp/"+R.raw.img1768;
//                Uri uri2 = Uri.parse(uriPath2);
//                mVideoView.setVideoURI(uri2);
//                mVideoView.requestFocus();
//                mVideoView.start();
//            }
//        });

    }
    public void onButtonClick(View v)
    {
        if(v.getId()==R.id.buttonServiceList)
        {
            Intent i = new Intent(HomePage.this,JobService.class);
            startActivity(i);
            overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
        }

        if(v.getId()==R.id.imageButton)
        {
           //play video;
            //mImageButton1.setVisibility(ImageButton.INVISIBLE);

            Intent i = new Intent(HomePage.this,Buy.class);
            startActivity(i);
            overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);

        }

        if(v.getId()==R.id.imageButton2)
        {
            Intent i = new Intent(HomePage.this,Buy.class);
            startActivity(i);
            overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);

        }

        //webview Video list
//        if(v.getId()==R.id.buttonVideoList)
//        {
//            Intent i = new Intent(HomePage.this,Display.class);
//            startActivity(i);
//            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
//        }

    }
}
