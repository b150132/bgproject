package com.lytech.rainsilk.webviewapp;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by rainsilk on 2016/3/21.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final int CUSTOM_EFFECT_ID = 1;
    // 接收廣播後執行這個方法
    // 第一個參數Context物件，用來顯示訊息框、啟動服務
    // 第二個參數是發出廣播事件的Intent物件，可以包含資料
    LocalDatabase localDatabase;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        localDatabase = new LocalDatabase(context);

        final Contact contact = localDatabase.getLoggedInUser();
        // 讀取包含在Intent物件中的資料
        String service;// = intent.getStringExtra("service");
        service = contact.id;//"22";//查詢是否有用戶的服務代碼為我自己

        int age = intent.getIntExtra("age", -1);

        //sign up member data
        //contact = new Contact("namestr","emailstr","unamestr","pass1str","phonestr","addressstr","assoiation","jobstr","intropersonstr","lineidstr","0","0","0","0","0","2000");

//        ...
        // 因為這不是Activity元件，需要使用Context物件的時候，
        // 不可以使用「this」，要使用參數提供的Context物件
        //MediaPlayer.create(context, R.raw.babayetu).start();
        Toast.makeText(context, "開始排班", Toast.LENGTH_SHORT).show();
        ServerRequests serverRequests = new ServerRequests(context);
        final String finalService = service;
        serverRequests.storeUserDetailInBackground(contact, service, new GetUserCallback() {
            @Override
            public void done(Contact returnedContact) {

                String guestid = contact.getID();
                String guestline = contact.getlineid();
                String guestphone = contact.getPhone_num();
                String locat1 = contact.getLocation1();
                String locat2 = contact.getLocation2();

                if (guestid.equals(finalService) == true) {
                    Toast.makeText(context, "搜尋客人中,請再稍候一分鐘....", Toast.LENGTH_LONG).show();
                    Toast.makeText(context, "搜尋客人請稍候....或按取消或回上頁取消排班", Toast.LENGTH_LONG).show();
                    Toast.makeText(context, "搜尋客人請稍候....或按取消或回上頁取消排班", Toast.LENGTH_LONG).show();
                } else {
                    //finish();
                    // 取得NotificationManager物件
                    NotificationManager nm = (NotificationManager)
                            context.getSystemService(Context.NOTIFICATION_SERVICE);

                    // 建立NotificationCompat.Builder物件
                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(context);
                    // 設定圖示、時間、內容標題和內容訊息
                    builder.setSmallIcon(android.R.drawable.star_big_on)
                            .setWhen(System.currentTimeMillis())
                            .setContentTitle(context.getString(R.string.app_name))
                            .setContentText("您已接獲客人!!請確認!!");
                    // 發出通知
                    nm.notify(CUSTOM_EFFECT_ID, builder.build());

                    Toast.makeText(context, "您已接獲客人!!!!!請主動加Line!", Toast.LENGTH_LONG).show();
                    Toast.makeText(context, "您已接獲客人!!!!!請主動加Line!", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(context, Service_sendcar.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    i.putExtra("ID", guestid);
                    i.putExtra("LINEID", guestline);
                    i.putExtra("PHONE", guestphone);
                    i.putExtra("LOCAT1", locat1);
                    i.putExtra("LOCAT2", locat2);
                    i.putExtra("close_activity", true);
                    context.startActivity(i);

//                    //Intent ii = new Intent(context, CheckLineFinish.class);
//                    Intent ii = new Intent();
//                    ii.setClass(context, CheckLineFinish.class);
//                    ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); //注意本行的FLAG設置
//                    ii.putExtra("ID", guestid);
//                    ii.putExtra("LINEID", guestline);
//                    ii.putExtra("PHONE", guestphone);
//                    context.startActivity(ii);
                    //finish();
                }
            }
        });
    }
}
