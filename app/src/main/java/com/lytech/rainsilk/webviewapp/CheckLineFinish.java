package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rainsilk on 2016/3/4.
 */
public class CheckLineFinish extends Activity {
    private Button openline, start, finish, cancel;
    private List<String> cancellist;
    LocalDatabase localDatabase;
    private TextView txtID, txtLine, txtPhone, txtAddress;
    String Jobcode;
    int jobcode;
    String id; //乘客會員 id, 會帶往點數頁

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checklinefinish);

        Intent intent = getIntent();
        Jobcode = intent.getStringExtra("jobcode");
        jobcode = Integer.parseInt(Jobcode);
        id = intent.getStringExtra("ID");
        String lineid = intent.getStringExtra("LINEID");
        String phone = intent.getStringExtra("PHONE");
        String locat1 = intent.getStringExtra("LOCAT1");
        String locat2 = intent.getStringExtra("LOCAT2");
        Log.e("rainsilk", "checklinefinish locat1=" + locat1);
        Log.e("rainsilk", "checklinefinish locat2=" + locat2);
        //自經緯度取得地址
        String returnAddress = " ";
        Geocoder gc = new Geocoder(CheckLineFinish.this);
        List<Address> lstAddress = null;
        try {
            lstAddress = gc.getFromLocation(Double.parseDouble(locat1), Double.parseDouble(locat2), 1);
            returnAddress = lstAddress.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }


        txtID = (TextView) findViewById(R.id.TFId);
        switch (jobcode) {
            case 2:
                txtID.setText("客人會員ID: " + id);
                break;
            case 4:
                txtID.setText("店家會員ID: " + id);
                break;
            default:
                break;
        }

        txtLine = (TextView) findViewById(R.id.TFLine);
        txtLine.setText(lineid);
        txtPhone = (TextView) findViewById(R.id.TFPhone);
        txtPhone.setText(phone);
        txtAddress = (TextView) findViewById(R.id.TFAddress);
        txtAddress.setText(returnAddress);


        openline = (Button) findViewById(R.id.btnOpenLine);
        start = (Button) findViewById(R.id.btnStart);
        finish = (Button) findViewById(R.id.btnFinish);
        cancel = (Button) findViewById(R.id.btnCancel);

        cancellist = new ArrayList<>();
        switch (jobcode) {
            case 2:
                cancellist.add("不要向客戶收費");
                cancellist.add("客戶沒有出現");
                cancellist.add("客戶取消叫車");
                cancellist.add("乘車地址錯誤");
                cancellist.add("其他");
                break;
            case 4:
                cancellist.add("不要向店家收費");
                cancellist.add("店家沒有出現");
                cancellist.add("店家取消外送");
                cancellist.add("店家地址錯誤");
                cancellist.add("其他");
                break;
            default:
                break;
        }


        localDatabase = new LocalDatabase(this);
        Contact contact = localDatabase.getLoggedInUser();

    }

    protected void onNewIntent(Intent intent) {
        Log.e("rainsilkinfo", "新intent");
        super.onNewIntent(intent);
        //退出
        if ((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0) {
            Log.e("rainsilkinfo", "符合條件");
            //finish();

        }
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.btnOpenLine) {
            start.setEnabled(true);
            Toast.makeText(CheckLineFinish.this, "請確認line好友並載客(貨)地址: ", Toast.LENGTH_LONG).show();
            Intent intent = getPackageManager().getLaunchIntentForPackage("jp.naver.line.android");
            startActivity(intent);
        }
        if (v.getId() == R.id.btnStart) {
            openline.setEnabled(false);
            start.setEnabled(false);
            finish.setEnabled(true);
            Toast.makeText(CheckLineFinish.this, "請於抵達後點擊: 結束載客(貨) ", Toast.LENGTH_LONG).show();
        }
        if (v.getId() == R.id.btnFinish) {
            openline.setEnabled(false);
            start.setEnabled(false);
            Toast.makeText(CheckLineFinish.this, "已結束載客(貨): 請進行點數確認 ", Toast.LENGTH_LONG).show();
            Intent i = new Intent(CheckLineFinish.this, Type_count_car.class);
            i.putExtra("ID",id);
            startActivity(i);
            overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
        }
        if (v.getId() == R.id.btnCancel) {
            openline.setEnabled(false);
            start.setEnabled(false);
            finish.setEnabled(false);

            new AlertDialog.Builder(CheckLineFinish.this)
                    .setItems(cancellist.toArray(new String[cancellist.size()]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String name = cancellist.get(which);
                            Toast.makeText(getApplicationContext(), "取消原因: " + name, Toast.LENGTH_SHORT).show();
                            //TODO:取消原因寫回資料庫做紀錄
                            //TODO:無故取消扣點

                            //回首頁
                            Intent i = new Intent(CheckLineFinish.this, AFirstPage.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
                        }
                    })
                    .show();


        }
    }
}
