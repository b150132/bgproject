package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by rainsilk on 2016/1/22.
 */
public class Display extends Activity {
    TextView tvname, tvemail, tvuname, tvpass1, tvphone, tvaddress, tvjob, tvintroperson, tvlineid;
    TextView tvID;
    TextView tvcount, tvshopcount, tvcarcount;
    LocalDatabase localDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displayinfo);

        tvname = (TextView) findViewById(R.id.TFName);
        tvemail = (TextView) findViewById(R.id.TFEmail);
        tvuname = (TextView) findViewById(R.id.TFUsername);
        tvpass1 = (TextView) findViewById(R.id.TFPassword);
        tvphone = (TextView) findViewById(R.id.TFPhone);

        tvaddress = (TextView) findViewById(R.id.TFAddress);
        tvjob = (TextView) findViewById(R.id.TFJob);
        tvintroperson = (TextView) findViewById(R.id.TFFounder);
        tvlineid = (TextView) findViewById(R.id.TFLine);
        tvID = (TextView) findViewById(R.id.TFId);

        tvcount = (TextView) findViewById(R.id.TFCount);
        tvshopcount = (TextView) findViewById(R.id.TFShopcount);
        tvcarcount = (TextView) findViewById(R.id.TFCarcount);

        localDatabase = new LocalDatabase(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (authenticate() == true) {
            displayContactDetails();
        } else {
            Intent intent = new Intent(Display.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private boolean authenticate() {
        return localDatabase.getUserLoggedIn();
    }

    private void displayContactDetails() {
        Contact contact = localDatabase.getLoggedInUser();
        tvname.setText(contact.name);
        tvemail.setText(contact.email);
        tvuname.setText(contact.uname);
        tvpass1.setText(contact.pass);

        tvphone.setText(contact.phone_num);
        tvaddress.setText(contact.address);
        tvjob.setText(contact.job);
        tvintroperson.setText(contact.introperson);
        tvlineid.setText(contact.lineid);

        tvID.setText(contact.id);
        tvcount.setText(contact.count);
        tvshopcount.setText(contact.giftcount);
        tvcarcount.setText(contact.carcount);

    }

    public void onLogoutClick(View view) {
        localDatabase.cleanData();
        localDatabase.setUserLoggedIn(false);
        Intent intent = new Intent(Display.this, MainActivity.class);
        startActivity(intent);
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.btngohome) {
            Intent i = new Intent(Display.this, AFirstPage.class);
            i.putExtra("ID", tvID.getText().toString());
//            i.putExtra("CountNum",);
//            i.putExtra("Shopcount",);
//            i.putExtra("Carcount",);
            startActivity(i);
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }

    }
}
