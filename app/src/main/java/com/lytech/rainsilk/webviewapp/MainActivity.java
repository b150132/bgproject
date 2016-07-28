package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

    EditText etusername, etpassword;
    LocalDatabase localDatabase;
    private CheckBox login_check1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etusername = (EditText) findViewById(R.id.editTextUsername);
        etpassword = (EditText) findViewById(R.id.editTextPassword);
        login_check1 = (CheckBox) findViewById(R.id.login_check1);

        SharedPreferences remdname = getPreferences(Activity.MODE_PRIVATE);
        String name_str = remdname.getString("name", "");
        String pass_str = remdname.getString("pass", "");
        etusername.setText(name_str);
        etpassword.setText(pass_str);

        //CheckBox控制是否記住密碼
        login_check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences remdname = getPreferences(Activity.MODE_PRIVATE);
                    SharedPreferences.Editor edit = remdname.edit();
                    edit.putString("name", etusername.getText().toString());
                    edit.putString("pass", etpassword.getText().toString());
                    edit.commit();
                }
                if (!isChecked) {

                    SharedPreferences remdname = getPreferences(Activity.MODE_PRIVATE);
                    SharedPreferences.Editor edit = remdname.edit();
                    edit.putString("name", "");
                    edit.putString("pass", "");
                    edit.commit();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar Item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.checkedTextView) {
            Intent i = new Intent(MainActivity.this, Policy.class);
            startActivity(i);
        }
        if (v.getId() == R.id.btnLogin) {

//            //auto loging
//            EditText a =(EditText)findViewById(R.id.editTextUsername);
//            EditText b = (EditText)findViewById(R.id.editTextPassword);
//
//           //For Demo
//            Intent i = new Intent(MainActivity.this, AFirstPage.class);
//            startActivity(i);
//            overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);

            String username = etusername.getText().toString();
            String password = etpassword.getText().toString();
            localDatabase = new LocalDatabase(this);

            Contact contact = new Contact(username, password);
            authenticate(contact);
//            String password= helper.searchPass(str);//
//            Log.e("WeiWei","password = " + password);

            //Intent i = new Intent(MainActivity.this,Display.class);
            //i.putExtra("Username",str);
            //startActivity(i);
            //overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);

//                Intent i = new Intent(MainActivity.this,HomePage.class);
//                startActivity(i);
//                overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);

//                //Pop Message
//                Toast temp =Toast.makeText(MainActivity.this , "Username and Password don't match!",Toast.LENGTH_SHORT);
//                temp.show();

        }

        if (v.getId() == R.id.Bsignup) {
            Intent i = new Intent(MainActivity.this, SignUp0.class);
            startActivity(i);
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }

    }

    public void authenticate(Contact contact) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchDataInBackground(contact, new GetUserCallback() {
            @Override
            public void done(Contact returnedContact) {
                if (returnedContact == null) {
                    //show error message
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("網路未開啟或帳密錯誤");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                } else {
                    Log.e("rainsilkinfo", "retunedContact !=null; go to display");
                    //
                    localDatabase.storeData(returnedContact);
                    localDatabase.setUserLoggedIn(true);

                    //在這寫登錄後的事件內容
                    if (login_check1.isChecked())//檢測使用者名密碼
                    {
                        SharedPreferences remdname = getPreferences(Activity.MODE_PRIVATE);
                        SharedPreferences.Editor edit = remdname.edit();
                        edit.putString("name", etusername.getText().toString());
                        edit.putString("pass", etpassword.getText().toString());
                        edit.commit();
                    }

                    Intent i = new Intent(MainActivity.this, Display.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }

            }
        });
    }

}
