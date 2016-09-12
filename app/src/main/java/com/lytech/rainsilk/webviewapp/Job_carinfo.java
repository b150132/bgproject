package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rainsilk on 2016/2/17.
 */
public class Job_carinfo extends Activity {
    private String choosecarid;
    private Button GoRating, ServiceCancel;
    private TextView mTitle;
    private List<String> cancellist;
    LocalDatabase localDatabase;
    String ServiceCode;
    int servicecode;
    double Olat, Olng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_info);

        Intent intent = getIntent();
        ServiceCode = intent.getStringExtra("servicecode");//"1"; //派車服務 6:外送服務
        servicecode = Integer.parseInt(ServiceCode);
        Olat = intent.getDoubleExtra("lat", 0.0);
        Olng = intent.getDoubleExtra("lng", 0.0);

        localDatabase = new LocalDatabase(this);

        GoRating = (Button) findViewById(R.id.btnRateCar);
        ServiceCancel = (Button) findViewById(R.id.btnCancelService);
        mTitle = (TextView) findViewById(R.id.txtTitle);

        switch (servicecode) {
            case 1:
                mTitle.setText("空閒計程車資料");
                break;
            case 3:
                mTitle.setText("空閒機車外送資料");
                break;
            default:
                break;
        }

        cancellist = new ArrayList<>();

        cancellist.add("不要向司機收費");
        cancellist.add("司機沒有出現");
        cancellist.add("司機取消派車");
        cancellist.add("司機line錯誤");
        cancellist.add("其他");

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());

        Showinfo();
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.btnRateCar) {
            //顯示評價頁面
            Intent i = new Intent(Job_carinfo.this, Rating_JobCar.class);
            i.putExtra("ID", choosecarid);
            startActivity(i);
            overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
        }
        if (v.getId() == R.id.btnCancelService) {

            new AlertDialog.Builder(Job_carinfo.this)
                    .setItems(cancellist.toArray(new String[cancellist.size()]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String name = cancellist.get(which);
                            Toast.makeText(getApplicationContext(), "取消原因: " + name, Toast.LENGTH_SHORT).show();

                            //回首頁
                            Intent i = new Intent(Job_carinfo.this, AFirstPage.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
                        }
                    })
                    .show();

        }

    }

    private void Showinfo() {
        TableLayout user_list = (TableLayout) findViewById(R.id.user_list);
        user_list.setStretchAllColumns(true);
        TableLayout.LayoutParams row_layout = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams view_layout = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        try {
            String result = null;
            String jobcode = String.valueOf(servicecode + 1);
            result = DBConnector.executeQuery(jobcode);

               /*
                             SQL 結果有多筆資料時使用JSONArray
                            只有一筆資料時直接建立JSONObject物件
                            JSONObject jsonData = new JSONObject(result);
                            */

            JSONArray jsonArray = new JSONArray(result);

            for (int i = 0; i < jsonArray.length(); i++) {
                final JSONObject jsonData = jsonArray.getJSONObject(i);
                TableRow tr = new TableRow(Job_carinfo.this);
                tr.setLayoutParams(row_layout);
                tr.setGravity(Gravity.CENTER_HORIZONTAL);

                final TextView user_id = new TextView(Job_carinfo.this);
                final String textid = jsonData.getString("ID");
                final Button btnChoose = new Button(Job_carinfo.this);
                switch (servicecode) {
                    case 1:
                        user_id.setText("計程車會員編號" + textid);
                        btnChoose.setText("選擇此計程車");
                        break;
                    case 3:
                        user_id.setText("外送車會員編號" + textid);
                        btnChoose.setText("選擇此外送機車");
                        break;
                    default:
                        break;
                }
                user_id.setLayoutParams(view_layout);

                final String Lineid = jsonData.getString("Lineid");

//                    ///////////////////////////////////////////////////////
                TextView mTxtLocation = new TextView(Job_carinfo.this);
                float results[] = new float[1];

                Location.distanceBetween(Olat, Olng,
                        Double.parseDouble(jsonData.getString("Locationone")), Double.parseDouble(jsonData.getString("Locationtwo")), results);

                final String distance = NumberFormat.getInstance().format(results[0]);

                float min = results[0] / 700;
                min = min + 0.5f;
                int intmin = (int) min;

                mTxtLocation.setText("約" + intmin + "分鐘路程");
                mTxtLocation.setLayoutParams(view_layout);

                btnChoose.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        //TODO:客戶選中+改資料庫狀態
                        GoRating.setEnabled(true);
                        ServiceCancel.setEnabled(true);
                        btnChoose.setEnabled(false);

                        Contact contact = localDatabase.getLoggedInUser();
                        //於服務攔註記選定司機之會員ID
                        String ServiceCode = textid;
                        contact.setService(ServiceCode);
                        SetService(contact);

                        choosecarid = Lineid;
                        Toast.makeText(Job_carinfo.this, "請加入計程車LINE ID: " + Lineid, Toast.LENGTH_LONG).show();

//                            try {
//                                Thread.sleep(2000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
                        Intent intent = getPackageManager().getLaunchIntentForPackage("jp.naver.line.android");
                        startActivity(intent);
                        //TODO:幫客戶打開LINE
                        //TODO:排班按鍵:1.檢查是否多次排班?2.檢查是否有人正在排班?


                    }
                });
                ///////////////////////////////////////////////////////
                tr.addView(user_id);
                tr.addView(mTxtLocation);
                tr.addView(btnChoose);
                user_list.addView(tr);
            }
        } catch (Exception e) {

        }
    }

    public void SetService(Contact contact) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.setServiceDataInBackground(contact, new GetUserCallback() {
            @Override
            public void done(Contact returnedContact) {

            }
        });
    }

}
