package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by rainsilk on 2016/1/22.
 */
public class DisplayShopSale extends Activity {
    LocalDatabase localDatabase;
    String shopname;
    Thread thread;

    TextView user_id;
    String textname, textnum, textprice, textcount, textsale;
    TableRow tr;
    TableLayout user_list;
    TableLayout.LayoutParams row_layout;
    TableRow.LayoutParams view_layout;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            user_id = new TextView(DisplayShopSale.this);
            tr = new TableRow(DisplayShopSale.this);
            user_list = (TableLayout) findViewById(R.id.user_list);
            user_list.setStretchAllColumns(true);
            row_layout = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view_layout = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tr.setLayoutParams(row_layout);
            tr.setGravity(Gravity.CENTER_HORIZONTAL);

            user_id = new TextView(DisplayShopSale.this);
            user_id.setText("編號:" + textnum + " 品名:" + textname + " 售價:" + textprice + " 點數:" + textcount + " 銷售量" + textsale + "\n");
            user_id.setLayoutParams(view_layout);
            ///////////////////////////////////////////////////////
            tr.removeView(user_id);//先切斷連結
            tr.addView(user_id);

            user_list.removeView(tr);//先切斷連結
            user_list.addView(tr);

            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale_info);

        Intent intent = getIntent();
        shopname = intent.getStringExtra("shopname");
        localDatabase = new LocalDatabase(this);

        //顯示銷售量資訊
        thread = new Thread(mutiThread);
        thread.start();
        //Showinfo();
    }
    private Runnable mutiThread = new Runnable() {
        public void run() {
            // 運行網路連線的程式
            Showinfo();
        }
    };
    public void onButtonClick(View view){
        switch (view.getId()) {
            case R.id.btnbackhome:
                Intent i = new Intent(DisplayShopSale.this, AFirstPage.class);
                startActivity(i);
                break;
        }
    }

    private void Showinfo() {
//        TableLayout user_list = (TableLayout) findViewById(R.id.user_list);
//      user_list.setStretchAllColumns(true);
//        TableLayout.LayoutParams row_layout = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        TableRow.LayoutParams view_layout = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        try {
            String result = null;
            result = DBConnector.executeQueryComm(shopname);
            Log.e("rainsilk", "sale result=" + result);
               /*
                             SQL 結果有多筆資料時使用JSONArray
                            只有一筆資料時直接建立JSONObject物件
                            JSONObject jsonData = new JSONObject(result);
                            */
            JSONArray jsonArray = new JSONArray(result);
            Log.e("runnable", "jsonArray.length()="+jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
//                TableRow tr = new TableRow(DisplayShopSale.this);


                //user_id = new TextView(DisplayShopSale.this);
                textname= jsonData.getString("Name");
                textnum= jsonData.getString("Num");
                textprice= jsonData.getString("Price");
                textcount= jsonData.getString("Count");
                textsale= jsonData.getString("Sale");

                //不能直接控制UI, 所以執行Handler, 宣告於全域
                Message msg = mHandler.obtainMessage();
                msg.what = i;
                msg.sendToTarget();

                thread.sleep(400);

//                user_id.setText("編號:" + textnum + " 品名:" + textname + " 售價:" + textprice + " 點數:" + textcount + " 銷售量" + textsale);
//                user_id.setLayoutParams(view_layout);
//
//                ///////////////////////////////////////////////////////
//                tr.addView(user_id);
//                user_list.addView(tr);
            }
        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }
    }



}
