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
 * 叫車點數系統
 * =====================
 * 司機方扣 20 count
 * 客人方扣 10 shopcount , 加 50 giftcount
 *
 * 例外條件: 任一方無故不到 扣 100 count 給對方會員
 *
 * 更新: 完成後雙方SERVICE = NULL
 * 紀錄評價
 * 提供更多資訊如車照片,車牌號碼
 */
public class Type_count_car extends Activity {

    private EditText edtxt_result, etMember; //輸入列, 會員編號列
    LocalDatabase localDatabase; //自身資訊
    String memberID = null, memberCount= null, memberCarCount = null, memberGiftCount = null, jobcount = "20",getgiftcount = "50", servicecount = "10", res = null, res1 = null;
    //memberID客人會員ID, memberCount客人總現金,memberCarCount客人運輸點數, memberGiftcount客人店家點數, jobcount排班扣除之count, servicecount服務扣除之運輸點數, getgiftcount服務獲得之店家點數
    //res,res1暫存
    String mycount, mygiftcount, mycarcount; //司機方點數資訊
    public static final int CONNECTION_TIMEOUT = 15000; //15 second
    public static final String SERVER_ADDRESS = "http://lytechly.comxa.com/";
    boolean forcusstate = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.type_count);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);//隱藏鍵盤

        Intent intent = getIntent();
        memberID = intent.getStringExtra("ID");

        localDatabase = new LocalDatabase(this);

        etMember = (EditText) findViewById(R.id.etMemberNum);
        //自動帶入會員編號
        etMember.setText(memberID);
        // set all ui component.
        edtxt_result = (EditText) this.findViewById(R.id.editText_result);
        //自動帶入扣除之運輸點數
        edtxt_result.setText(servicecount);
        TextView title = (TextView) this.findViewById(R.id.tv22);
        title.setText(" 輸入會員編號");

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
                    IBinder mIBinder = Type_count_car.this.getCurrentFocus().getWindowToken();
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
                    IBinder mIBinder = Type_count_car.this.getCurrentFocus().getWindowToken();
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

        Toast.makeText(Type_count_car.this,"請出示此頁給您的乘客,確認無誤後按\"確認\"鍵",Toast.LENGTH_LONG).show();



    }//End of onCreate

    public void onButtonClick(View v) {

        if (v.getId() == R.id.button_c) {
            if (forcusstate)
                edtxt_result.setText("");
            else
                etMember.setText("");
        }
        if (v.getId() == R.id.btOK) {
            //GUEST ID
            memberID = etMember.getText().toString();
            Log.e("rainsilk", "客人ID=" + memberID);
            //service count 客人付出之運輸點數
            servicecount = edtxt_result.getText().toString();
            Log.e("rainsilk", "客人付出之運輸點數=" + servicecount);
            //取得登入驗證跟資料
            boolean authenticate = localDatabase.getUserLoggedIn();

            if (authenticate) {
                //交易成立
                final Contact contact = localDatabase.getLoggedInUser();
                //取得司機自己的點數資料
                mycount = contact.getCount();
                mygiftcount = contact.getGiftCount();
                mycarcount = contact.getCarCount();

                ServerRequests serverRequests = new ServerRequests(this);
                //memberCarCount 客人運輸點數
                String returnvalue = serverRequests.doInBackground(contact, memberID, new GetUserCallback() {
                    @Override
                    public void done(Contact returnedContact) {
                        //根據會員ID查別人點數
                        memberCount = contact.getCount();
                        memberGiftCount = contact.getGiftCount();
                        memberCarCount = contact.getCarCount();

                        //TODO:需CHECK 但不是在這邊?
                        if (Integer.parseInt(memberCarCount)<=10)
                        {
                            Toast.makeText(Type_count_car.this,"您的點數餘額不足,請加值!",Toast.LENGTH_LONG).show();
                            //不做動作直接出去
                            finish();
                        }
                        //扣司機count20 並四捨五入
                        res = Integer.toString((int)(Integer.parseInt(mycount) - Integer.parseInt(jobcount)));
                        //加客人 giftcount 50 並四捨五入
                        res1 = Integer.toString((int) (Integer.parseInt(memberGiftCount) + (Integer.parseInt(getgiftcount))));
                        //扣客人 carcount 10 並四捨五入
                        memberCarCount = Integer.toString((int) (Integer.parseInt(memberCarCount) - (Integer.parseInt(servicecount) )));

                        //SetCount Function 指定會員ID 寫入三種點數 若不變請填原值
                        //寫入司機自己
                        new SetCount(contact.id, res, mygiftcount,mycarcount).execute();
                        //寫入客人
                        new SetCount(memberID, memberCount,res1,memberCarCount).execute();

                        //寫入點數異動資料庫
                        //TODO:須修正成三種點數紀錄模式
                        Count count;
                        Date mDate = new Date();

                        //印出的結果是 "Sat Apr 13 16:50:03 台北標準時間 2013"
                        String time = mDate.toString();
                        Log.e("rainsilk time", "time=" + time);
                        //3 司機無打折
                        count = new Count("3", contact.id, memberID, res, memberCount, mygiftcount, res1, time);

                        Context mCtx;
                        mCtx = Type_count_car.this;
                        ServerRequests serverRequests = new ServerRequests(mCtx);
                        serverRequests.storeCountDetailInBackground(count, new GetUserCallback() {
                            @Override
                            public void done(Contact returnedContact) {
                                Log.e("rainsilk car count ", "finish");
                                finish();
                            }
                        });

                        //顯示評價頁面
                        Intent i = new Intent(Type_count_car.this, Rating_JobCar.class);
                        i.putExtra("ID", memberID);
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);

                    }
                });


            } else
                Log.e("rainsilk", "auth fail");
        }

    }


    private class SetCount extends AsyncTask<Void, Void, Void> {

        String id;
        String cashcount;
        String giftcount;
        String carcount;

        public SetCount(String id, String cashcount, String giftcount, String carcount) {
            this.id = id;
            this.cashcount=cashcount;
            this.giftcount = giftcount;
            this.carcount = carcount;
            Log.e("rainsilkinfo", "點數異動Setcount");
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> data_to_send = new ArrayList<>();

            data_to_send.add(new BasicNameValuePair("id", id));
            data_to_send.add(new BasicNameValuePair("countnum",cashcount));
            data_to_send.add(new BasicNameValuePair("giftcount", giftcount));
            data_to_send.add(new BasicNameValuePair("carcount", carcount));

            Log.e("rainsilkinfo", "set id =" + id);
            Log.e("rainsilkinfo", "set cashcount =" + cashcount);
            Log.e("rainsilkinfo", "set giftcount =" + giftcount);
            Log.e("rainsilkinfo", "set carcount =" + carcount);

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
