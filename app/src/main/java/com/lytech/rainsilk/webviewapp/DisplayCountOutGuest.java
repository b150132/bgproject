package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by rainsilk on 2016/2/17.
 */
public class DisplayCountOutGuest extends Activity {
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
        setContentView(R.layout.display_count_out_guest);



        localDatabase = new LocalDatabase(this);

        mTitle = (TextView) findViewById(R.id.txtTitle);


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
           finish();
        }

    }

    private void Showinfo() {
        TableLayout user_list = (TableLayout) findViewById(R.id.user_list);
        user_list.setStretchAllColumns(true);
        TableLayout.LayoutParams row_layout = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams view_layout = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        try {
            String result = null;
            String outcount = String.valueOf(1000);
            result = DBConnector.executeCountOutQuery("contacts","count",outcount);

               /*
                             SQL 結果有多筆資料時使用JSONArray
                            只有一筆資料時直接建立JSONObject物件
                            JSONObject jsonData = new JSONObject(result);
                            */

            JSONArray jsonArray = new JSONArray(result);

            for (int i = 0; i < jsonArray.length(); i++) {
                final JSONObject jsonData = jsonArray.getJSONObject(i);
                TableRow tr = new TableRow(DisplayCountOutGuest.this);
                tr.setLayoutParams(row_layout);
                tr.setGravity(Gravity.CENTER_HORIZONTAL);

                final TextView user_id = new TextView(DisplayCountOutGuest.this);
                final String textid = jsonData.getString("ID");


                user_id.setText("客戶會員編號" + textid);

                user_id.setLayoutParams(view_layout);

                final String Lineid = jsonData.getString("Lineid");

//                    ///////////////////////////////////////////////////////
                TextView mTxtLocation = new TextView(DisplayCountOutGuest.this);


                float results[] = new float[1];

                Location.distanceBetween(Olat, Olng,
                        Double.parseDouble(jsonData.getString("Locationone")), Double.parseDouble(jsonData.getString("Locationtwo")), results);


                float min = results[0] / 700;
                min = min + 0.5f;


                mTxtLocation.setText("點數低於標準,請聯繫.");
                mTxtLocation.setLayoutParams(view_layout);
                ///////////////////////////////////////////////////////
                tr.addView(user_id);
                tr.addView(mTxtLocation);
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
