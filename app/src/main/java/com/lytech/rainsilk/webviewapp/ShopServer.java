package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by rainsilk on 2016/1/7.
 */
public class ShopServer extends Activity {
    String myid, mylevel;
    LocalDatabase localDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_server);

        Intent intent = getIntent();
        myid = intent.getStringExtra("ID");
        mylevel = intent.getStringExtra("Level");

        localDatabase = new LocalDatabase(this);
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.ibShop) {

            AlertDialog.Builder ab = new AlertDialog.Builder(ShopServer.this);

            ab.setTitle("權限檢查")
                    .setMessage("本區為公司合作之 店家 才能進入 非此身分會員請按\"取消\"退出")
                    .setCancelable(true);

            ab.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //權限檢查
                    //Self ID

                    //get 自己的id

                    if (mylevel.equals("333") || mylevel.equals("777")) { //店家:333, 服務員:111

                        Intent i = new Intent(ShopServer.this, ShopMenu.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);

                    } else {
                        Toast.makeText(ShopServer.this, "您不具備店家身分!", Toast.LENGTH_LONG).show();
                    }

                }
            });
            ab.setNegativeButton(android.R.string.cancel, null);
            ab.show();


        }

        if (v.getId() == R.id.ibServer) {
            AlertDialog.Builder ab = new AlertDialog.Builder(ShopServer.this);

            ab.setTitle("權限檢查")
                    .setMessage("本區為公司授權之 服務員 才能進入 非此身分會員請按\"取消\"退出")
                    .setCancelable(true);

            ab.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //權限檢查
                    //Self ID

                    //get 自己的id

                    if (mylevel.equals("111") || mylevel.equals("777")) { //店家:333, 服務員:111
                        boolean authenticate = localDatabase.getUserLoggedIn();
//交易成立:
                        final Contact contact = localDatabase.getLoggedInUser();

//密碼再次驗證
                        final View item = LayoutInflater.from(ShopServer.this).inflate(R.layout.enterpass, null);
                        new AlertDialog.Builder(ShopServer.this)
                                .setTitle("請輸入會員密碼")
                                .setView(item)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        EditText editText = (EditText) item.findViewById(R.id.edittext);
                                        Toast.makeText(getApplicationContext(), getString(R.string.hi) + editText.getText().toString(), Toast.LENGTH_SHORT).show();

                                        //比較是否符合
                                        //如果符合
                                        if (contact.getPass().equals(editText.getText().toString())) {

                                            Intent i = new Intent(ShopServer.this, ChooseCountType.class);
                                            startActivity(i);
                                            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "密碼不符!請重新操作", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                })
                                .show();
                    } else {
                        Toast.makeText(ShopServer.this, "您不具備服務員資格!", Toast.LENGTH_LONG).show();
                    }

                }
            });
            ab.setNegativeButton(android.R.string.cancel, null);
            ab.show();


        }

    }
}
