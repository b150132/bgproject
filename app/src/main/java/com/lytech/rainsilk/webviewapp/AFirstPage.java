package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rainsilk on 2016/1/1.
 */
public class AFirstPage extends Activity {
    private Spinner mSpinRegionType;
    String RegionName, Positon;//區域: 中文 , 數字
    String MarName, Marall;
    private ImageButton mBtnShowMenu, mBtnMore, mBtnMore2;
    private ImageView img6, img22, img23;
    private LinearLayout mLinLayMenu, mLinLayDisplay;
    private LinearLayout L1, L2, L3, LALL;

    //跑馬燈
    TextView tv;

    private View vSideMenu;
    private ViewGroup vGrpApp, vGrpRoot;
    private TranslateAnimation mAnimSideMenuOpen, mAnimSideMenuClose;
    private final int ANIM_DURATION_SIDE_NAVIGATION_MENU = 800;
    private boolean mBlnMenuIsOpen = false;
    private int statusBarHight = -1;

    private static final int START_LOCATION = 1;

    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    //掃描點數
    LocalDatabase localDatabase;
    String memberID = null, memberCount = null,memberGCount = null,memberCCount = null,
            counttype=null ,countnum = null, servicecount = null, res = null, res1 = null;
    //紀錄給點數會員(店家司機)的三種點數 countnum: QR code的附帶點數   counttype:三種點數中的哪一種
    String mycount,myGCount,myCCount, myid, mylevel;
    //紀錄本身的三種點數及權限等級
    public static final int CONNECTION_TIMEOUT = 15000; //15 second
    public static final String SERVER_ADDRESS = "http://lytechly.comxa.com/";
    String id;

    Thread thread1,thread2;
    Handler mHandler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (Marall != null)
                Marall = Marall + " | " + MarName;
            else
                Marall = MarName;

            if (Marall.length() > 100)
                Marall = Marall.substring(Marall.length() - MarName.length(), Marall.length());

            tv.setText(Marall);
            tv.setSelected(true);

            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.afirstpage);

        thread1 = new Thread(mutiThread1);

        Intent intent = getIntent();
        id = intent.getStringExtra("ID");

        localDatabase = new LocalDatabase(this);

        mSpinRegionType = (Spinner) findViewById(R.id.spinnerJobtype);
        mSpinRegionType.setOnItemSelectedListener(spnRegionTypeItemSelect);
        mBtnShowMenu = (ImageButton) findViewById(R.id.imageButtonMenu);
        mBtnMore = (ImageButton) findViewById(R.id.imageButtonMore);
        mLinLayDisplay = (LinearLayout) findViewById(R.id.linLayDisplay);
        mLinLayMenu = (LinearLayout) findViewById(R.id.linLayMenu);
        img6 = (ImageView) findViewById(R.id.imageView6);
        img22 = (ImageView) findViewById(R.id.imageView21);

        Bitmap mBitmapmenu;
        mBitmapmenu = getLocalBitmap(AFirstPage.this, R.drawable.menu);

        mBtnShowMenu.setImageBitmap(mBitmapmenu);

        LALL = (LinearLayout) findViewById(R.id.LayoutAll);
        L1 = (LinearLayout) findViewById(R.id.l1);
        L2 = (LinearLayout) findViewById(R.id.l2);

        //marquee 4個以上字元
        tv = (TextView) findViewById(R.id.tv_ouput);

//        int children_on_screen = 2;
//        int size = getheightOfTheScreen();
//
//        for(int i=0;i<mLinLayDisplay.getChildCount()-1;i++) {
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLinLayDisplay.getChildAt(i).getLayoutParams();
//            params.height = size *  2 ;
//            if(i==0)
//                params.height = size;
//
//        }
//        mLinLayDisplay.requestLayout();
        //get screen view
        try {
            vGrpApp = (LinearLayout) findViewById(android.R.id.content).getParent();
        } catch (ClassCastException e) {
            vGrpApp = (LinearLayout) findViewById(android.R.id.content);
        }

        //side menu in root viewGroup
        vGrpRoot = (ViewGroup) vGrpApp.getParent();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vSideMenu = inflater.inflate(R.layout.sidemenu, null);

        vGrpRoot.addView(vSideMenu);
        vGrpApp.bringToFront();

        //set menu region
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int iMenuAreaWidth = (int) (0.5 * dm.widthPixels);

        mAnimSideMenuOpen = new TranslateAnimation(0, iMenuAreaWidth, 0, 0);
        mAnimSideMenuOpen.setInterpolator(new DecelerateInterpolator());

        mAnimSideMenuOpen.setDuration(ANIM_DURATION_SIDE_NAVIGATION_MENU);
        mAnimSideMenuOpen.setFillAfter(true);
        mAnimSideMenuOpen.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                enableDisableViewGroup(vGrpApp, false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                vSideMenu.bringToFront();

                vSideMenu.findViewById(R.id.frmLayOverlay).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        vGrpApp.startAnimation(mAnimSideMenuClose);
                        mBlnMenuIsOpen = false;
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mAnimSideMenuClose = new TranslateAnimation(iMenuAreaWidth, 0, 0, 0);
        mAnimSideMenuClose.setInterpolator(new DecelerateInterpolator());
        mAnimSideMenuClose.setDuration(ANIM_DURATION_SIDE_NAVIGATION_MENU);
        mAnimSideMenuClose.setFillAfter(true);
        mAnimSideMenuClose.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                vSideMenu.findViewById(R.id.frmLayOverlay).setOnClickListener(null);

                enableDisableViewGroup(vGrpApp, true);
                vGrpApp.bringToFront();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        ViewGroup.LayoutParams params = mLinLayMenu.getLayoutParams();
        params.width = (iMenuAreaWidth);
        mLinLayMenu.setLayoutParams(params);

        //跑馬燈
        Positon = "0";

        thread1.start();


    }//END OF ONCREATE

    private Runnable mutiThread1 = new Runnable() {
        public void run() {
            // 運行網路連線的程式
            ShowMar(Positon,1);
        }
    };

    private Runnable mutiThread2 = new Runnable() {
        public void run() {
            // 運行網路連線的程式
            ShowMar(Positon,2);
        }
    };

    //處理查詢跑馬燈文字
    private void ShowMar(String area,int num) {

        try {
            String result = DBConnector.executeQueryMar(area);
            Log.e("rainsilk", "mar info result=" + result);
            JSONArray jsonArray = new JSONArray(result);
            Log.e("rainsilk", "jsonArray.length()=" + jsonArray.length());
            //處理取得之text資訊
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);

                MarName = jsonData.getString("Text");
                Log.e("rainsilk", "mar i=" + i + 1);
                Message msg = mHandler1.obtainMessage();
                msg.what = i + 1;
                msg.sendToTarget();

                if(num==1)
                    thread1.sleep(500);
                else
                    thread2.sleep(500);
            }//END OF FOR LOOP
        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }
    }

    private int getheightOfTheScreen() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        return height;
    }

    @Override
    protected void onResume() {
        super.onResume();

        mBtnShowMenu.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Method getSupportActionBar = AFirstPage.this.getClass().getMethod("get SupportActionBar", (Class[]) null);
                    Object sab = getSupportActionBar.invoke(AFirstPage.this, (Object[]) null);
                    sab.toString();

                    if (Build.VERSION.SDK_INT >= 11)
                        getStatusBarHight();
                } catch (Exception es) {
                    getStatusBarHight();
                }


                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                params.setMargins(0, statusBarHight, 0, 0);
                vSideMenu.setLayoutParams(params);

            }
        });
    }

    private void getStatusBarHight() {
        if (statusBarHight == -1) {
            Rect r = new Rect();
            Window window = AFirstPage.this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(r);
            statusBarHight = r.top;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return super.onOptionsItemSelected(item);
    }

    public Bitmap getLocalBitmap(Context con, int resourceId) {
        InputStream inputStream = con.getResources().openRawResource(resourceId);
        return BitmapFactory.decodeStream(inputStream, null, getBitmapOptions(2));
    }

    public BitmapFactory.Options getBitmapOptions(int scale) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inSampleSize = scale;
        return options;
    }

    private void enableDisableViewGroup(ViewGroup viewGroup, boolean enable) {
        int childcount = viewGroup.getChildCount();
        for (int i = 0; i < childcount; i++) {
            View view = viewGroup.getChildAt(i);
            if (view.isFocusable())
                view.setEnabled(enable);
            if (view instanceof ViewGroup)
                enableDisableViewGroup((ViewGroup) view, enable);
            else if (view instanceof ListView) {
                if (view.isFocusable())
                    view.setEnabled(enable);
                ListView listView = (ListView) view;
                int listChildCount = listView.getChildCount();
                for (int j = 0; j < listChildCount; j++)
                    if (view.isFocusable())
                        listView.getChildAt(j).setEnabled(false);

            }

        }

    }


    public void onButtonClick(View v) {
        if (v.getId() == R.id.imageButtonMenu) {
            vGrpApp.startAnimation(mAnimSideMenuOpen);
            mBlnMenuIsOpen = true;
        }


        if (v.getId() == R.id.imgbtnHome) { //Homepage
            Intent i = new Intent(AFirstPage.this, AFirstPage.class);
            startActivity(i);
            overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
        }


        if (v.getId() == R.id.imgbtnBuy) { //購物商城
//            Intent i = new Intent(AFirstPage.this, Buy.class);
            Intent i = new Intent(AFirstPage.this, Shop_1_retrive.class);
            startActivity(i);
            overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
        }

        if (v.getId() == R.id.imgbtnJob) { //工作列表_計程車排班
            Intent i = new Intent(AFirstPage.this, Job_all.class);
            startActivity(i);
            overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
        }

        if (v.getId() == R.id.imgbtnService) { //服務列表_叫計程車
            Intent i = new Intent(AFirstPage.this, Service_all.class);
            startActivity(i);
            overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
        }

        if (v.getId() == R.id.btnMarquee) { //跑馬燈
            Intent i = new Intent(AFirstPage.this, Marquee.class);
            startActivity(i);
            overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
        }

        if (v.getId() == R.id.btnscan) {     //獲得免費點數
            // Scan 程式
            String vScanClass = "com.google.zxing.client.android.SCAN";

            // Scan App
            String vScanApp = "com.google.zxing.client.android";

            // 指定要開啟的 Class
            Intent intent = new Intent(vScanClass);

            // 支援的掃描類型
            intent.putExtra("SCAN_MODE", "SCAN_MODE");

            try {
                // 開啟掃描 App
                startActivityForResult(intent, 0);
            } catch (Exception e) {
                // 沒有此 App, 則開啟 Google Play 進行 App 安裝
                Intent installIntent = new Intent(Intent.ACTION_VIEW
                        , Uri.parse("market://details?id=" + vScanApp)
                );
                startActivity(installIntent);
            }
        }

        if (v.getId() == R.id.buttonServiceList) {
            Intent i = new Intent(AFirstPage.this, JobService.class);
            startActivity(i);
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }

        if (v.getId() == R.id.imgbtnCount) {
            AlertDialog.Builder ab = new AlertDialog.Builder(AFirstPage.this);

            ab.setTitle("說明")
                    .setMessage("點數轉換皆須涵蓋服務費,請謹慎使用")
                    .setCancelable(true);

            ab.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(AFirstPage.this, Change_count.class);
                    i.putExtra("ID", id);
                    startActivity(i);
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
            });
            ab.setNegativeButton(android.R.string.cancel, null);
            ab.show();

        }

        if (v.getId() == R.id.btnMovecount) {
            AlertDialog.Builder ab = new AlertDialog.Builder(AFirstPage.this);

            ab.setTitle("說明")
                    .setMessage("點數移轉皆須涵蓋服務費,請謹慎使用")
                    .setCancelable(true);

            ab.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(AFirstPage.this, Change_count.class);
                    i.putExtra("ID", id);
                    startActivity(i);
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
            });
            ab.setNegativeButton(android.R.string.cancel, null);
            ab.show();

        }

        if (v.getId() == R.id.imageButton) //精選商城1
        {
            Intent intentMap = new Intent(this, MapsShowActivity.class);

            //根據下拉選單的位置=>查詢地址=>查詢座標=>Intnent傳值
            String placeName = RegionName;//"台北車站";//et.getText().toString().trim();
            if (placeName.length() > 0) {
                Geocoder gc = new Geocoder(AFirstPage.this);
                List<Address> addressList = null;
                Address address = null;
                String returnAddress = "";
                try {
                    addressList = gc.getFromLocationName(placeName, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addressList == null || addressList.isEmpty()) {
//                    mTxtLocation.setText("找不到地址");
                } else {
                    address = addressList.get(0);
//                    float results[] = new float[1];
                    intentMap.putExtra("lat", address.getLatitude());
                    intentMap.putExtra("lng", address.getLongitude());
                    intentMap.putExtra("title", "title");

                    startActivityForResult(intentMap, START_LOCATION);
//                    Log.e("rainsilk", "mTxtLocation=" + distance +"m");
                }
            }

        }

        if (v.getId() == R.id.imageButton2) //精選商城2
        {
            Intent intentMap = new Intent(this, MapsShowActivity.class);

            //根據下拉選單的位置=>查詢地址=>查詢座標=>Intnent傳值
            String placeName = RegionName;//"台北車站";//et.getText().toString().trim();
            if (placeName.length() > 0) {
                Geocoder gc = new Geocoder(AFirstPage.this);
                List<Address> addressList = null;
                Address address = null;
                String returnAddress = "";
                try {
                    addressList = gc.getFromLocationName(placeName, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addressList == null || addressList.isEmpty()) {
                    //mTxtLocation.setText("找不到地址");
                } else {
                    address = addressList.get(0);
//                    float results[] = new float[1];
                    intentMap.putExtra("lat", address.getLatitude());
                    intentMap.putExtra("lng", address.getLongitude());
                    intentMap.putExtra("title", "title");

                    startActivityForResult(intentMap, START_LOCATION);
                }
            }
        }
        if (v.getId() == R.id.imageButton3) //實體市集3
        {
            Intent intentMap = new Intent(this, MapsShowActivity.class);

            //根據下拉選單的位置=>查詢地址=>查詢座標=>Intnent傳值
            String placeName = RegionName;//"台北車站";//et.getText().toString().trim();
            if (placeName.length() > 0) {
                Geocoder gc = new Geocoder(AFirstPage.this);
                List<Address> addressList = null;
                Address address = null;
                String returnAddress = "";
                try {
                    addressList = gc.getFromLocationName(placeName, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addressList == null || addressList.isEmpty()) {
//                    mTxtLocation.setText("找不到地址");

                } else {
                    address = addressList.get(0);
//                    float results[] = new float[1];
                    intentMap.putExtra("lat", address.getLatitude());
                    intentMap.putExtra("lng", address.getLongitude());
                    intentMap.putExtra("title", "title");

                    startActivityForResult(intentMap, START_LOCATION);
//                    Log.e("rainsilk", "mTxtLocation=" + distance +"m");
                }
            }
        }
        if (v.getId() == R.id.imageButton4) //區域賣場百貨4
        {
            Intent intentMap = new Intent(this, MapsShowActivity.class);

            //根據下拉選單的位置=>查詢地址=>查詢座標=>Intnent傳值
            String placeName = RegionName;//"台北車站";//et.getText().toString().trim();
            if (placeName.length() > 0) {
                Geocoder gc = new Geocoder(AFirstPage.this);
                List<Address> addressList = null;
                Address address = null;
                String returnAddress = "";
                try {
                    addressList = gc.getFromLocationName(placeName, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addressList == null || addressList.isEmpty()) {
//                    mTxtLocation.setText("找不到地址");

                } else {
                    address = addressList.get(0);
//                    float results[] = new float[1];
                    intentMap.putExtra("lat", address.getLatitude());
                    intentMap.putExtra("lng", address.getLongitude());
                    intentMap.putExtra("title", "title");

                    startActivityForResult(intentMap, START_LOCATION);
//                    Log.e("rainsilk", "mTxtLocation=" + distance +"m");
                }
            }
        }
        if (v.getId() == R.id.imgbtnSystem) //系統管理 => 店家 服務員之操作
        {
            AlertDialog.Builder ab = new AlertDialog.Builder(AFirstPage.this);

            ab.setTitle("權限檢查")
                    .setMessage("本區為公司合作之 店家 或是 服務員 才能進入 非此兩種身分會員請按\"取消\"退出")
                    .setCancelable(true);

            ab.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //TODO: 權限檢查
                    //Self ID
                    boolean authenticate = localDatabase.getUserLoggedIn();



                        final Contact contact = localDatabase.getLoggedInUser();
                        //get 自己的id
                        myid = contact.getID();

                        ServerRequests serverRequests = new ServerRequests(AFirstPage.this);
                        serverRequests.checkCompetDataInBackground(contact, "level", myid, new GetUserCallback() {
                            @Override
                            public void done(Contact returnedContact) {
                                //根據id查詢使用者的level
                                mylevel = contact.getLevel();

                                if (mylevel.equals("333") || mylevel.equals("111") || mylevel.equals("777")) { //店家:333, 服務員:111
                                    Intent i = new Intent(AFirstPage.this, ShopServer.class);
                                    i.putExtra("ID", myid);
                                    i.putExtra("Level", mylevel);
                                    startActivity(i);
                                    overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
                                } else {
                                    Toast.makeText(AFirstPage.this, "您的權限不符!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });




                }
            });
            ab.setNegativeButton(android.R.string.cancel, null);
            ab.show();


        }
        if (v.getId() == R.id.imgbtnCService) //客訴
        {
//            //呼叫mail程序
//            Uri uri = Uri.parse("mailto:xxx@abc.com");
//            Intent it = new Intent(Intent.ACTION_SENDTO, uri);
//            startActivity(it);

//                //簡易寄送E-mail
//                Intent it = new Intent(Intent.ACTION_SEND);
//                it.putExtra(Intent.EXTRA_EMAIL, "me@abc.com");
//                it.putExtra(Intent.EXTRA_TEXT, "The email body text");
//                it.setType("text/plain");
//                startActivity(Intent.createChooser(it, "Choose Email Client"));


//一般寄送E-mail
            Intent it = new Intent(Intent.ACTION_SEND);
            String[] tos = {"b150132@yahoo.com.tw"}; //收件者
            String[] ccs = {"ccmail@abc.com"}; //副本
//主要寄送對象
            it.putExtra(Intent.EXTRA_EMAIL, tos);
//cc對象
            it.putExtra(Intent.EXTRA_CC, ccs);
//Email內容
            it.putExtra(Intent.EXTRA_TEXT, "您好,我有以下的問題想尋求客服的協助......");
//Email主題
            it.putExtra(Intent.EXTRA_SUBJECT, "致宅商王客服: 客服信件");
            it.setType("message/rfc822");
            startActivity(Intent.createChooser(it, "選擇電子郵件客戶端"));

//
////寄送附件
//                Intent it = new Intent(Intent.ACTION_SEND);
////Email主題
//                it.putExtra(Intent.EXTRA_SUBJECT, "The email subject text");
////附件
//                it.putExtra(Intent.EXTRA_STREAM, "file:///sdcard/mysong.mp3");
////檔案格式
//                sendIntent.setType("audio/mp3");
//                startActivity(Intent.createChooser(it, "選擇電子郵件客戶端"));

        }


    }//END OF ONBUTTONCLICK


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // Scan App 回傳內容
            String result = data.getStringExtra("SCAN_RESULT");

//            result = "shoporcar id:counttype:countnumber"; // 22: 2 :5 22號會員所發出的折扣點數5點
            String[] tokens = result.split(":");
            String[] shopinfo = new String[]{null, null, null};
            int tokencouter = 0;
            for (String token : tokens) {
                System.out.println(token);
                shopinfo[tokencouter] = token;
                tokencouter++;
            }

            countnum = shopinfo[2]; //edtxt_result.getText().toString();
            //點數種類
            counttype = shopinfo[1];
            //Shop or Car ID
            memberID = shopinfo[0];//etMember.getText().toString();
            //Self ID
            boolean authenticate = localDatabase.getUserLoggedIn();


                //掃描成立: 扣商店或司機???點數 哪一種 加自己???點數?, ???=100
                final Contact contact = localDatabase.getLoggedInUser();
                //get 客人自己的點數
                mycount = contact.getCount();
                myGCount = contact.getGiftCount();
                myCCount = contact.getCarCount();

                ServerRequests serverRequests = new ServerRequests(this);
                memberCount = serverRequests.doInBackground(contact, memberID, new GetUserCallback() {
                    @Override
                    public void done(Contact returnedContact) {
                        //根據編號查別人(商店)點數
                        memberCount = contact.getCount();
                        memberGCount = contact.getGiftCount();
                        memberCCount = contact.getCarCount();

                        int icounttype=Integer.parseInt(counttype);
                        switch (icounttype)
                        {
                            case 1: //現金點數
                                if(Integer.parseInt(memberCount)<Integer.parseInt(countnum))  //點數餘額不足
                                {
                                    Toast.makeText(AFirstPage.this,"本現金已發完",Toast.LENGTH_LONG).show();
                                    finish();
                                }
                                //加自己
                                mycount = Integer.toString(Integer.parseInt(mycount) + Integer.parseInt(countnum));
                                //扣店家或司機
                                memberCount = Integer.toString(Integer.parseInt(memberCount) - Integer.parseInt(countnum));//(int) ((Integer.parseInt(servicecount)) * 0.9 + 0.5));
                                break;
                            case 2: //店家點數
                                if(Integer.parseInt(memberGCount)<Integer.parseInt(countnum))  //點數餘額不足
                                {
                                    Toast.makeText(AFirstPage.this,"本折扣現金已發完",Toast.LENGTH_LONG).show();
                                    finish();
                                }
                                //加自己
                                myGCount = Integer.toString(Integer.parseInt(myGCount) + Integer.parseInt(countnum));
                                //扣店家或司機
                                memberGCount = Integer.toString(Integer.parseInt(memberGCount) - Integer.parseInt(countnum));//(int) ((Integer.parseInt(servicecount)) * 0.9 + 0.5));
                                break;
                            case 3: //運輸點數 未使用
                                break;
                        }
                        //SetCount Function 指定會員ID 寫入三種點數 若不變請填原值
                        //寫入自己
                        new SetCount(contact.id, mycount,myGCount,myCCount).execute();
                        //寫入店家或司機
                        new SetCount(memberID, memberCount,memberGCount,memberCCount).execute();

                        //TODO:寫入來客量（掃描量）
                        new SetCover(memberID).execute();

                        //寫入點數異動資料庫
                        Count count,mcount;
                        Date mDate = new Date();

                        //印出的結果是 "Sat Apr 13 16:50:03 台北標準時間 2013"
                        String time = mDate.toString();

                        //7 點數掃描贈點
                        count = new Count("7", contact.id, mycount, myGCount, myCCount, "null", "null", time);
                        mcount = new Count("7", memberID, memberCount, memberGCount, memberCCount, "null", "null", time);

                        Context mCtx;
                        mCtx = AFirstPage.this;
                        ServerRequests serverRequests = new ServerRequests(mCtx);
                        serverRequests.storeCountDetailInBackground(count, new GetUserCallback() {
                            @Override
                            public void done(Contact returnedContact) {
                              finish();
                            }
                        });

//                        ServerRequests serverRequests2 = new ServerRequests(mCtx);
//                        serverRequests2.storeCountDetailInBackground(mcount, new GetUserCallback() {
//                            @Override
//                            public void done(Contact returnedContact) {
//                                Log.e("rainsilk count ", "掃描贈點 店家司機點數異動紀錄finish");
//                                finish();
//                            }
//                        });
                    }
                });



//            text.setText( result );
        }

        super.onActivityResult(requestCode, resultCode, data);
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
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> data_to_send = new ArrayList<>();

            data_to_send.add(new BasicNameValuePair("id", id));
            data_to_send.add(new BasicNameValuePair("countnum",cashcount));
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
            Toast.makeText(getApplicationContext(), "點數已輸入成功!", Toast.LENGTH_SHORT).show();
        }
    }

    private class SetCover extends AsyncTask<Void, Void, Void> {

        String shoporcarid;

        public SetCover(String shoporcarid) {
            this.shoporcarid = shoporcarid;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> data_to_send = new ArrayList<>();

            data_to_send.add(new BasicNameValuePair("page", "shoplist"));
            data_to_send.add(new BasicNameValuePair("column", "Coverrate"));
            data_to_send.add(new BasicNameValuePair("value", "1"));
            data_to_send.add(new BasicNameValuePair("keycolumn", "Memberid"));
            data_to_send.add(new BasicNameValuePair("keyvalue", shoporcarid));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "UpdateAnything.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(data_to_send));
                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity = httpResponse.getEntity();

            } catch (Exception e) {
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

    private AdapterView.OnItemSelectedListener spnRegionTypeItemSelect = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            RegionName = parent.getSelectedItem().toString();

            //mTxtB.setText(getString(R.string.buytype_select)+parent.getSelectedItem().toString());
            Object item = parent.getItemAtPosition(position);
            Positon = Integer.toString(position);

            String chooseItem = item.toString();

            //再次呼叫跑馬燈
            thread2 = new Thread(mutiThread2);
            thread2.start();

            String str1 = "輔食";
            String str2 = "配方奶粉";
            String str3 = "營養品";

//TODO:變換首頁精選商品
//            if(str1.equals(chooseItem)==true)
//                imgBuy.setImageResource(R.drawable.shop);
//            else
//                Log.e("rainsilk","not fus");
//
//            if(str2.equals(chooseItem)==true)
//                imgBuy.setImageResource(R.drawable.buy_2);
//            else
//                Log.e("rainsilk","not nifan");
//
//            if(str3.equals(chooseItem)==true)
//                imgBuy.setImageResource(R.drawable.buy_3);
//            else
//                Log.e("rainsilk","not inyunping");

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
