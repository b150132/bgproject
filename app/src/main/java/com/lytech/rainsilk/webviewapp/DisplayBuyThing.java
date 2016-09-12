package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by rainsilk on 2016/1/22.
 */
public class DisplayBuyThing extends Activity {
    LocalDatabase localDatabase;
    int productnum,sum;
    TextView txtsum;
    TableLayout user_list;
    String sharetext;
    EditText et_sharetext;
    String guestaddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_info);

        txtsum = (TextView)findViewById(R.id.txtSum);

        Intent intent = getIntent();
        productnum = intent.getIntExtra("productnum", 0);
        sum = intent.getIntExtra("sum", 0);
        String [] pname = new String[productnum];
        int [] pnum = new int[productnum];

//        id = intent.getStringExtra("ID");
        for (int j = 0; j < productnum; j++) {
            pname[j] = intent.getStringExtra("n"+j);
            pnum[j] = intent.getIntExtra("nu"+j,0);
        }

        user_list = (TableLayout)findViewById(R.id.user_list);
        user_list.setStretchAllColumns(true);
        TableLayout.LayoutParams row_layout = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams view_layout = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for(int i = 0; i < productnum; i++) {
            TableRow tr = new TableRow(DisplayBuyThing.this);
            tr.setLayoutParams(row_layout);
            tr.setGravity(Gravity.CENTER_HORIZONTAL);

            TextView buyname = new TextView(DisplayBuyThing.this);
            buyname.setText(pname[i]);
            buyname.setLayoutParams(view_layout);

            TextView buynum = new TextView(DisplayBuyThing.this);
            buynum.setText(pnum[i] + "個");
            buynum.setLayoutParams(view_layout);

            if(sharetext!=null)
                sharetext=sharetext+"\n"+pname[i]+" "+pnum[i]+"個\n";
            else
                sharetext=pname[i]+" "+pnum[i]+"個\n";

            tr.addView(buyname);
            tr.addView(buynum);
            user_list.addView(tr);
        }
        localDatabase = new LocalDatabase(this);
        boolean authenticate = localDatabase.getUserLoggedIn();

        if (authenticate) {

            final Contact contact = localDatabase.getLoggedInUser();
            txtsum.setText("總金額: " + sum + " 元");
            sharetext = sharetext + "\n" + "總金額: " + sum + " 元\n";
            //TODO:店家地址 電話 LINE + 自己地址 電話 LINE
            sharetext = sharetext + "\n" + "訂購會員ID: " + contact.getID() + "\n";
            sharetext = sharetext + "\n" + "訂購會員電話: " + contact.getPhone_num() + "\n";
            sharetext = sharetext + "\n" + "訂購會員LineID: " + contact.getlineid() + "\n";
        }


    }
    public void onButtonClick(View view){
        switch (view.getId()) {
            case R.id.btnbackhome:
                Intent i = new Intent(DisplayBuyThing.this, AFirstPage.class);
                startActivity(i);
                break;
            case R.id.btnsendtoshop:
                final View item = LayoutInflater.from(DisplayBuyThing.this).inflate(R.layout.enterpass, null);
                new AlertDialog.Builder(DisplayBuyThing.this)
                        .setTitle("請輸入送貨地址")
                        .setView(item)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = (EditText) item.findViewById(R.id.edittext);
                                Toast.makeText(getApplicationContext(), getString(R.string.hi) + editText.getText().toString(), Toast.LENGTH_SHORT).show();

                                //地址是否超過9個字元
                                if (editText.getText().toString().length()<9) {
                                    guestaddr = editText.getText().toString();
                                    openDialog();
                                } else {
                                    Toast.makeText(getApplicationContext(), "地址錯誤!請重新操作", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .show();

                break;
        }
    }

    void openDialog() {
        sharetext = sharetext + "\n" + "外送地址:"+guestaddr+"\n";
        String shareText = sharetext;//et_sharetext.getEditableText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(intent, "傳送給店家透過.."));
     }

}
