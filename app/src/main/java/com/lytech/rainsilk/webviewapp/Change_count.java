package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by rainsilk on 2016/2/22.
 */
public class Change_count extends Activity {

    private EditText edtxt_result, etMember;
    LocalDatabase localDatabase;
    String memberID = null, memberCount = null, memberGiftCount = null, jobcount = "20", servicecount = null, res = null, res1 = null;
    String mycount, mygiftcount, mycarcount;
    public static final int CONNECTION_TIMEOUT = 15000; //15 second
    public static final String SERVER_ADDRESS = "http://lytechly.comxa.com/";
    boolean forcusstate = true;
    private Spinner mSpinCountType1, mSpinCountType2;
    String CType1, CType2, chooseItem, chooseItem2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_count);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);//隱藏鍵盤

        Intent intent = getIntent();
        String id = intent.getStringExtra("ID");

        localDatabase = new LocalDatabase(this);

        etMember = (EditText) findViewById(R.id.etMemberNum);
        etMember.setText(id);
        // set all ui component.
        edtxt_result = (EditText) this
                .findViewById(R.id.editText_result);
        TextView title = (TextView) this.findViewById(R.id.tv22);
        title.setText(" 會員編號");

        mSpinCountType1 = (Spinner) findViewById(R.id.spinnerCounttype);
        mSpinCountType1.setOnItemSelectedListener(spnRegionTypeItemSelect);
        mSpinCountType2 = (Spinner) findViewById(R.id.spinnerCounttype2);
        mSpinCountType2.setOnItemSelectedListener(spnRegionTypeItemSelect2);
//        Button btn_amount = (Button) this.findViewById(R.id.button_amount);

        //to hide the virtual keyboard.
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtxt_result.getWindowToken(), 0);

        // get all button then set a listener for general.
        Button[] btns = {(Button) this.findViewById(R.id.button_num1),
                (Button) this.findViewById(R.id.button_num2),
                (Button) this.findViewById(R.id.button_num3),
                (Button) this.findViewById(R.id.button_num4),
                (Button) this.findViewById(R.id.button_num5),
                (Button) this.findViewById(R.id.button_num6),
                (Button) this.findViewById(R.id.button_num7),
                (Button) this.findViewById(R.id.button_num8),
                (Button) this.findViewById(R.id.button_num9),
//                (Button) this.findViewById(R.id.button_plus),
//                (Button) this.findViewById(R.id.button_sub),

                (Button) this.findViewById(R.id.button_num0)};

        OnClickListener generalOnClick = new OnClickListener() {
            public void onClick(View v) {
                // set the txt of editText plus the text of button.
                if (forcusstate == true)
                    edtxt_result.setText(edtxt_result.getText().toString() + ((Button) v).getText());
                else
                    etMember.setText(etMember.getText().toString() + ((Button) v).getText());
            }
        };

        for (Button btn : btns)
            btn.setOnClickListener(generalOnClick);


        View.OnFocusChangeListener mFocusChangedListener, mFocusChangedListener2;
        mFocusChangedListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    forcusstate = true;
                    //Toast.makeText(getApplicationContext(), "num got focus: " + v.toString(), Toast.LENGTH_LONG).show();
                    //隱藏鍵盤
                    edtxt_result.setInputType(InputType.TYPE_NULL); // 關閉軟鍵盤
                    IBinder mIBinder = Change_count.this.getCurrentFocus().getWindowToken();
                    InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mInputMethodManager.hideSoftInputFromWindow(mIBinder, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    forcusstate = false;

                }
            }
        };

        mFocusChangedListener2 = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    forcusstate = false;
                    //Toast.makeText(getApplicationContext(), "mem got focus: " + v.toString(), Toast.LENGTH_LONG).show();
                    //隱藏鍵盤
                    etMember.setInputType(InputType.TYPE_NULL); // 關閉軟鍵盤
                    IBinder mIBinder = Change_count.this.getCurrentFocus().getWindowToken();
                    InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mInputMethodManager.hideSoftInputFromWindow(mIBinder, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    forcusstate = true;
                }
            }
        };
        edtxt_result.setOnFocusChangeListener(mFocusChangedListener);
        etMember.setOnFocusChangeListener(mFocusChangedListener2);

        //建立文字監聽
        TextWatcher mTextWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //如果字數達到2，取消自己焦點，下一個EditText取得焦點
                if (etMember.getText().toString().length() == 2) {
                    etMember.clearFocus();

                    edtxt_result.setFocusable(true);
                    edtxt_result.setFocusableInTouchMode(true);
                    edtxt_result.requestFocus();
                    edtxt_result.requestFocusFromTouch();
                }

                //如果字數達到4，取消自己焦點，隱藏虛擬鍵盤
                if (edtxt_result.getText().toString().length() == 4) {
                    edtxt_result.clearFocus();
                }
            }
        };

        //加入文字監聽
        etMember.addTextChangedListener(mTextWatcher);
        edtxt_result.addTextChangedListener(mTextWatcher);


    }

    public void onButtonClick(View v) {

        if (v.getId() == R.id.button_c) {
            if (forcusstate)
                edtxt_result.setText("");
            else
                etMember.setText("");
        }
        if (v.getId() == R.id.btOK) {

            //service count
            servicecount = edtxt_result.getText().toString();

            //GUEST ID
            memberID = etMember.getText().toString();

            //Self ID
            boolean authenticate = localDatabase.getUserLoggedIn();



                //交易成立:
                final Contact contact = localDatabase.getLoggedInUser();

                //密碼再次驗證
                final View item = LayoutInflater.from(Change_count.this).inflate(R.layout.enterpass, null);
                new AlertDialog.Builder(Change_count.this)
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

                                    //進行寫入自己資料的動作
                                    ServerRequests serverRequests = new ServerRequests(Change_count.this);
                                    memberCount = serverRequests.doInBackground(contact, memberID, new GetUserCallback() {
                                        @Override
                                        public void done(Contact returnedContact) {
                                            String str1 = "個人總現金";
                                            String str2 = "折扣現金";
                                            String str3 = "消費現金";
                                            //get 店家自己的點數
                                            mycount = contact.getCount(); //消費點數
                                            mygiftcount = contact.getGiftCount(); //店家點數
                                            mycarcount = contact.getCarCount();//運輸點數

                                            if (chooseItem2.equals(chooseItem) == true) {
                                                //選擇相同,錯誤:點數型態不得相同
                                                Toast.makeText(Change_count.this, "選擇錯誤,請選不同點數進行兌換", Toast.LENGTH_LONG).show();
                                            } else {
                                                //計算出三點數互換的數值 存起來
                                                if (chooseItem.equals(str1) == true && chooseItem2.equals(str2)) {
                                                    //消費to店家
                                                    mycount = Integer.toString(Integer.parseInt(mycount) - Integer.parseInt(servicecount));
                                                    mygiftcount = Integer.toString(Integer.parseInt(mygiftcount) + (int) (Double.parseDouble(servicecount) * 1.2)); //買1000送200

                                                } else if (chooseItem.equals(str1) == true && chooseItem2.equals(str3)) {
                                                    //消費to運輸
                                                    mycount = Integer.toString(Integer.parseInt(mycount) - Integer.parseInt(servicecount));
                                                    mygiftcount = Integer.toString(Integer.parseInt(mygiftcount) + (int) (Double.parseDouble(servicecount) * 0.2)); //買1000送200
                                                    mycarcount = Integer.toString(Integer.parseInt(mycarcount) + (int) (Double.parseDouble(servicecount) * 1.0)); //買1000送200

                                                } else if (chooseItem.equals(str2) == true && chooseItem2.equals(str3)) {
                                                    //店家to運輸
                                                    mygiftcount = Integer.toString(Integer.parseInt(mygiftcount) - Integer.parseInt(servicecount) - 200); //扣手續點200
                                                    mycarcount = Integer.toString(Integer.parseInt(mycarcount) + Integer.parseInt(servicecount));


                                                } else {
                                                    //運輸to店家
                                                    mygiftcount = Integer.toString(Integer.parseInt(mygiftcount) + Integer.parseInt(servicecount));
                                                    mycarcount = Integer.toString(Integer.parseInt(mycarcount) - Integer.parseInt(servicecount) - 200);//扣手續點200
                                                }
                                            }//END OF 點數選項不相同

                                            //TODO:點數不可為負!

                                            //寫入店家自己
                                            new SetCount(contact.id, mycount, mygiftcount, mycarcount).execute();

                                            Count count;
                                            Date mDate = new Date();

                                            //印出的結果是 "Sat Apr 13 16:50:03 台北標準時間 2013"
                                            String time = mDate.toString();

                                            //轉換作業 7
                                            count = new Count("7", contact.id, "change", mycount, "memberCount", mygiftcount, mycarcount, time);

                                            Context mCtx;
                                            mCtx = Change_count.this;
                                            ServerRequests serverRequests = new ServerRequests(mCtx);
                                            serverRequests.storeCountDetailInBackground(count, new GetUserCallback() {
                                                @Override
                                                public void done(Contact returnedContact) {
                                                   finish();
                                                }
                                            });

                                        }
                                    }); //END OF SERVER REQUEST

                                } else {
                                    Toast.makeText(getApplicationContext(), "密碼不符!請重新操作", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .show();

        }

    }


    private class SetCount extends AsyncTask<Void, Void, Void> {

        String id;
        String count;
        String giftcount;
        String carcount;

        public SetCount(String id, String count, String giftcount, String carcount) {
            this.id = id;
            this.count = count;
            this.giftcount = giftcount;
            this.carcount = carcount;
            Log.e("rainsilkinfo", "GetCount function id=" + id);
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> data_to_send = new ArrayList<>();

            data_to_send.add(new BasicNameValuePair("id", id));
            data_to_send.add(new BasicNameValuePair("countnum", count));
            data_to_send.add(new BasicNameValuePair("giftcount", giftcount));
            data_to_send.add(new BasicNameValuePair("carcount", carcount));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "UpdateUserCount.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(data_to_send));
                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "點數轉換成功!", Toast.LENGTH_SHORT).show();
        }

    }//END OF MYSQL

    //處理spinner事件
    private AdapterView.OnItemSelectedListener spnRegionTypeItemSelect = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            CType1 = parent.getSelectedItem().toString();

            //mTxtB.setText(getString(R.string.buytype_select)+parent.getSelectedItem().toString());
            Object item = parent.getItemAtPosition(position);
            chooseItem = item.toString();

            switch (parent.getSelectedItem().toString()) {
                case "":
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };
    private AdapterView.OnItemSelectedListener spnRegionTypeItemSelect2 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            CType2 = parent.getSelectedItem().toString();

            Object item = parent.getItemAtPosition(position);
            chooseItem2 = item.toString();

            switch (parent.getSelectedItem().toString()) {
                case "":
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

}
