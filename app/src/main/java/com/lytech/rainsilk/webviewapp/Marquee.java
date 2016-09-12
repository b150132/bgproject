package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Date;

/**
 * Created by rainsilk on 2016/3/4.
 */
public class Marquee extends Activity {
    LocalDatabase localDatabase;
    private Spinner mSpinRegionType;
    String RegionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marquee);

        localDatabase = new LocalDatabase(this);

        final TextView tv=(TextView)findViewById(R.id.tv_ouput);
        final EditText et = (EditText)findViewById(R.id.et_input);
        mSpinRegionType = (Spinner) findViewById(R.id.spinnerJobtype);
        mSpinRegionType.setOnItemSelectedListener(spnRegionTypeItemSelect);

        Button btn = (Button)findViewById(R.id.btn_submit);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO:寫入資料庫
                Cmarquee cmarquee;
                Date mDate = new Date();

                //印出的結果是 "Sat Apr 13 16:50:03 台北標準時間 2013"
                String time = mDate.toString();

                boolean authenticate= localDatabase.getUserLoggedIn();
                tv.setText(et.getText().toString());
                tv.setSelected(true);

                if(authenticate) {

                    Contact contact = localDatabase.getLoggedInUser();
                    cmarquee = new Cmarquee(contact.id, RegionName, et.getText().toString(), time);

                    Context mCtx;
                    mCtx = Marquee.this;
                    ServerRequests serverRequests = new ServerRequests(mCtx);
                    serverRequests.storeMarDetailInBackground(cmarquee, new GetUserCallback() {
                        @Override
                        public void done(Contact returnedContact) {
                            Log.e("rainsilk mar ", "done");
                            //finish();
                        }
                    });
                }

            }

        });
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.btncheck) {
            finish();
        }
    }

    private AdapterView.OnItemSelectedListener spnRegionTypeItemSelect = new AdapterView.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
            RegionName= parent.getSelectedItem().toString();
            Object item = parent.getItemAtPosition(position);
            RegionName=Integer.toString(position);

        }
        @Override
        public void onNothingSelected(AdapterView<?>  arg0){

        }
    };
}
