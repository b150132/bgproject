package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by rainsilk on 2016/2/22.
 */
public class Type_count_server extends Activity {

    private EditText edtxt_result, etMember;
    LocalDatabase localDatabase;
    String memberID = null, memberCount = null, memberGiftCount = null, jobcount = "20", servicecount = null, res = null, res1 = null;
    String mycount, mygiftcount;
    public static final int CONNECTION_TIMEOUT = 15000; //15 second
    public static final String SERVER_ADDRESS = "http://lytechly.comxa.com/";
    boolean forcusstate = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.type_count);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);//隱藏鍵盤

        Intent intent = getIntent();
        String Lineid = intent.getStringExtra("LINEID");

        localDatabase = new LocalDatabase(this);

        etMember = (EditText) findViewById(R.id.etMemberNum);
        // set all ui component.
        edtxt_result = (EditText) this
                .findViewById(R.id.editText_result);
        TextView title = (TextView) this.findViewById(R.id.tv22);
        title.setText(" 輸入會員編號");
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
                    IBinder mIBinder = Type_count_server.this.getCurrentFocus().getWindowToken();
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
                    IBinder mIBinder = Type_count_server.this.getCurrentFocus().getWindowToken();
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
                    Log.e("rainsilk", "edtxt request");

                }

                //如果字數達到4，取消自己焦點，隱藏虛擬鍵盤
                if (edtxt_result.getText().toString().length() == 4) {
                    edtxt_result.clearFocus();
                    Log.e("rainsilk", "edtxt clear");
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
            Log.e("rainsilk", "servicecount=" + servicecount);
            //GUEST ID
            memberID = etMember.getText().toString();
            Log.e("rainsilk", "memberID=" + memberID);
//            //Self ID
            boolean authenticate = localDatabase.getUserLoggedIn();

            if (authenticate) {
                //交易成立: 現金交易 買1000 寫入顧客1000count and 200 giftcount 寫入服務員業績 performence 50 寫入所有資料進資料庫
                final Contact contact = localDatabase.getLoggedInUser();
                //get 服務員自己的點數
                mycount = contact.getCount();
                mygiftcount = contact.getGiftCount();

                ServerRequests serverRequests = new ServerRequests(this);
                memberCount = serverRequests.doInBackground(contact, memberID, new GetUserCallback() {
                    @Override
                    public void done(Contact returnedContact) {
                        //根據編號查別人點數
                        memberCount = contact.getCount();
                        memberGiftCount = contact.getGiftCount();
                        //加服務員performence
                        res = Integer.toString(Integer.parseInt(mycount) - Integer.parseInt(jobcount));
                        //加客人 membercount and giftcount
                        memberCount = Integer.toString((int) (Integer.parseInt(memberCount) + (Integer.parseInt(servicecount) + 0.5)));
                        res1 = Integer.toString((int) ((Integer.parseInt(memberGiftCount) + (Integer.parseInt(servicecount)) * 0.5) + 0.5));

                        //TODO:點數不可為null or 負!

//                        //寫入服務員自己
//                        new SetCount(contact.id,res,mygiftcount).execute();
                        //寫入客人
                        new SetCount(memberID, memberCount, res1).execute();

                        Count count;
                        Date mDate = new Date();

                        //印出的結果是 "Sat Apr 13 16:50:03 台北標準時間 2013"
                        String time = mDate.toString();
                        Log.e("rainsilk time", "time=" + time);
                        count = new Count("1", contact.id, memberID, "mycount", memberCount, "mygiftcount", res1, time);

                        Context mCtx;
                        mCtx = Type_count_server.this;
                        ServerRequests serverRequests = new ServerRequests(mCtx);
                        serverRequests.storeCountDetailInBackground(count, new GetUserCallback() {
                            @Override
                            public void done(Contact returnedContact) {
                                Log.e("rainsilk buy count ", "finish");
                                finish();
                            }
                        });


                    }
                });


            } else
                Log.e("rainsilk", "auth fail");
        }

    }


    private class SetCount extends AsyncTask<Void, Void, Void> {

        String id;
        String count;
        String giftcount;

        public SetCount(String id, String count, String giftcount) {
            this.id = id;
            this.count = count;
            this.giftcount = giftcount;
            Log.e("rainsilkinfo", "GetCount function id=" + id);
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> data_to_send = new ArrayList<>();

            data_to_send.add(new BasicNameValuePair("id", id));
            data_to_send.add(new BasicNameValuePair("count", count));
            data_to_send.add(new BasicNameValuePair("giftcount", giftcount));


            Log.e("rainsilkinfo", "set id =" + id);
            Log.e("rainsilkinfo", "set count =" + count);
            Log.e("rainsilkinfo", "set giftcount =" + giftcount);

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "BuyUserCount.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(data_to_send));
                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                Log.e("rainsilkinfo", "set count result =" + result);

                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.length() == 0) {
                    Log.e("rainsilkinfo", "retunedContact = null;");
                } else {
                    Log.e("rainsilkinfo", "retunedContact =new count;");
                }

            } catch (Exception e) {
                Log.e("rainsilkinfo", "SetCount failed");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "點數已輸入成功!", Toast.LENGTH_SHORT).show();
        }
    }
}
