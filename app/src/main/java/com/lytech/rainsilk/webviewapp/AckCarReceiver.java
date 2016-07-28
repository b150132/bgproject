package com.lytech.rainsilk.webviewapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AckCarReceiver extends BroadcastReceiver {
    public AckCarReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("HelloAckReciever", "COMPLETED!!!!!!!!!!!!!!!!!!!!!!!!!");
        Log.e("HelloAckReciever", "" + intent.getAction());
        // 讀取記事標題
        String title = "有客人正在叫您的車";//intent.getStringExtra("title");
        // 顯示訊息框
        Toast.makeText(context, title, Toast.LENGTH_LONG).show();
    }
}
