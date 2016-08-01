package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.superbearman6.imagecachetatics.ImageCacheManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;


/**
 * Created by rainsilk on 2016/2/24.
 */
public class Shop_1_retrive extends Activity {
    private Spinner mSpinShopnameType;
    String ShopName;
    String commName, commPrice, commCount;
    public static final int CONNECTION_TIMEOUT = 30000; //30 second
    public static final String SERVER_ADDRESS = "http://lytechly.comxa.com/";
    private static final int RESULT_LOAD_IMAGE = 0, RESULT_LOAD_IMAGE1 = 1, RESULT_LOAD_IMAGE2 = 2, RESULT_LOAD_IMAGE3 = 3, RESULT_LOAD_IMAGE4 = 4, RESULT_LOAD_IMAGE5 = 5, RESULT_LOAD_IMAGE6 = 6;
    ImageView imgTitle;
    int commnumber = 6;
    LinearLayout All;
    ImageView[] imv = new ImageView[commnumber];
    TextView[] tv = new TextView[commnumber];
    TextView[] tvcost = new TextView[commnumber];
    TextView[] tvcount = new TextView[commnumber];
    Button gotocart;
    LocalDatabase localDatabase;
    int imgupcount = 0;
    private ImageCacheManager imageCacheManager;

    Thread thread;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e("rainsilk", "msg.what=" + msg.what);

            tv[msg.what - 1].setText(commName);
            tvcost[msg.what - 1].setText(commPrice);
            tvcount[msg.what - 1].setText(commCount);

            super.handleMessage(msg);
        }
    };

    int[] imvs = {R.id.imv1, R.id.imv2, R.id.imv3, R.id.imv4, R.id.imv5, R.id.imv6};
    int[] tvs = {R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4, R.id.tv5, R.id.tv6};
    int[] tvcosts = {R.id.tvcost1, R.id.tvcost2, R.id.tvcost3, R.id.tvcost4, R.id.tvcost5, R.id.tvcost6};
    int[] tvcounts = {R.id.tvcount1, R.id.tvcount2, R.id.tvcount3, R.id.tvcount4, R.id.tvcount5, R.id.tvcount6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_1_retrive);

        All= (LinearLayout)findViewById(R.id.shopretrieve);
        mSpinShopnameType = (Spinner) findViewById(R.id.spinnerJobtype);
        mSpinShopnameType.setOnItemSelectedListener(spnRegionTypeItemSelect);
        mSpinShopnameType.setEnabled(false);


         /*FMU*/
        imageCacheManager = ImageCacheManager.getImageCacheService(this,
                ImageCacheManager.MODE_FIXED_MEMORY_USED, "memory");
        imageCacheManager.setMax_Memory(1024 * 1024);

        localDatabase = new LocalDatabase(this);

        imgTitle = (ImageView) findViewById(R.id.imgTitle);

        for (int i = 0; i < commnumber; i++) {
            imv[i] = (ImageView) findViewById(imvs[i]);
            tv[i] = (TextView) findViewById(tvs[i]);
            tvcost[i] = (TextView) findViewById(tvcosts[i]);
            tvcount[i] = (TextView) findViewById(tvcounts[i]);
        }

        gotocart = (Button) findViewById(R.id.btnShopCart);
        gotocart.setEnabled(false);

        Contact contact = localDatabase.getLoggedInUser();
        String shopname = contact.getUname();

        thread = new Thread(mutiThread);

//        Context mCtx;
//        mCtx = Shop_1_retrive.this;
//        DownloadImage serverRequests = new DownloadImage(mCtx);
    }

    private Runnable mutiThread = new Runnable() {
        public void run() {
            // 運行網路連線的程式
            Showcomminfo(ShopName);
            Log.e("riansilk", "run!!!!!!!!!");

        }
    };

    private class DownloadImage extends AsyncTask<Void, Void, Bitmap> {
        String name;
        ProgressDialog progressDialog;

        public DownloadImage(String name) {
            this.name = name;
        }

        public DownloadImage(Context context) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("商城產品下載中");
            progressDialog.setMessage("請稍後...");
        }

        protected Bitmap doInBackground(Void... params) {
//            progressDialog.show();
            String url = SERVER_ADDRESS + "pictures/" + name + ".JPG";
            try {
                URLConnection connection = new URL(url).openConnection();
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.setReadTimeout(CONNECTION_TIMEOUT);

                return imageCacheManager.downlaodImage(new URL(url));
                // return BitmapFactory.decodeStream((InputStream)connection.getContent(),null,getBitmapOptions(2));

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public BitmapFactory.Options getBitmapOptions(int scale) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inSampleSize = scale;
            return options;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                switch (imgupcount) {
                    case 0:
                        imgTitle.setImageBitmap(bitmap);
                        imgupcount++;
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                        imv[imgupcount - 1].setImageBitmap(bitmap);
                        imgupcount++;
                        break;
                    case 6:
                        imv[imgupcount - 1].setImageBitmap(bitmap);
                        imgupcount = 0;
                        mSpinShopnameType.setEnabled(true);
                        gotocart.setEnabled(true);
                        break;
                    default:
                        Log.e("rainsilk", "ERROR: Download exception!");
                        break;
                }


//                progressDialog.dismiss();
            }
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btnback) {
            finish();
        }
        if (v.getId() == R.id.imv1) {
            Intent i = new Intent(Shop_1_retrive.this, Shoplevel2.class);
            startActivity(i);
        }
        if (v.getId() == R.id.imv2) {
            Intent i = new Intent(Shop_1_retrive.this, Shoplevel2.class);
            startActivity(i);
        }
        if (v.getId() == R.id.imv3) {
            Intent i = new Intent(Shop_1_retrive.this, Shoplevel2.class);
            startActivity(i);
        }
        if (v.getId() == R.id.imv4) {
            Intent i = new Intent(Shop_1_retrive.this, Shoplevel2.class);
            startActivity(i);
        }
        if (v.getId() == R.id.btnShopCart) {
            //顯示購物車進行選購
            String[] s = new String[commnumber];
            String[] cost = new String[commnumber];
            for (int j = 0; j < commnumber; j++) {
                s[j] = tv[j].getText().toString();
                cost[j] = tvcost[j].getText().toString();
            }

            Intent i = new Intent(Shop_1_retrive.this, ShopCart.class);
            i.putExtra("shopname",ShopName);

            i.putExtra("s1", s[0]);
            i.putExtra("s2", s[1]);
            i.putExtra("s3", s[2]);
            i.putExtra("s4", s[3]);
            i.putExtra("s5", s[4]);
            i.putExtra("s6", s[5]);

            i.putExtra("cost1", cost[0]);
            i.putExtra("cost2", cost[1]);
            i.putExtra("cost3", cost[2]);
            i.putExtra("cost4", cost[3]);
            i.putExtra("cost5", cost[4]);
            i.putExtra("cost6", cost[5]);

            startActivity(i);
        }
        if (v.getId() == R.id.btnShopEvaluation) {
            //顯示評價頁面
            Intent i = new Intent(Shop_1_retrive.this, Rating_JobCar.class);
            i.putExtra("ID", ShopName);
            startActivity(i);
        }

    }

    private AdapterView.OnItemSelectedListener spnRegionTypeItemSelect = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            ShopName = parent.getSelectedItem().toString();
            Log.e("rainsilk", "Region name:" + ShopName);

            //mTxtB.setText(getString(R.string.buytype_select)+parent.getSelectedItem().toString());
            Object item = parent.getItemAtPosition(position);
            String chooseItem = item.toString();
            Log.e("rainsilk", "ITEM=" + chooseItem);

            String str1 = "e-圓排骨";
            String str2 = "momo購物";
            String str3 = "cc食品";

            //Backgroud
            Drawable drawable;
            Resources res = Shop_1_retrive.this.getResources();
//TODO:變換首頁精選商品
            if (str1.equals(chooseItem) == true) {

                String shopname = "Easter1230";
                ShopName = "Easter1230";
                imgupcount = 0;
                //prepare image name
                String titlestr = shopname + "title";
                String onestr = shopname + "one";
                String twostr = shopname + "two";
                String thrstr = shopname + "three";
                String fourstr = shopname + "four";
                String fivestr = shopname + "five";
                String sixstr = shopname + "six";

                Thread thread = new Thread(mutiThread);
                thread.start();

//                Context mCtx;
//                mCtx = Shop_1_retrive.this;
//                DownloadImage serverRequests = new DownloadImage(mCtx);
                mSpinShopnameType.setEnabled(false);

                new DownloadImage(titlestr).execute();
                new DownloadImage(onestr).execute();
                new DownloadImage(twostr).execute();
                new DownloadImage(thrstr).execute();
                new DownloadImage(fourstr).execute();
                new DownloadImage(fivestr).execute();
                new DownloadImage(sixstr).execute();
            } else
                Log.e("rainsilk", "not 1");

            if (str2.equals(chooseItem) == true) {
                drawable =  res.getDrawable(R.drawable.bg_dark);
                All.setBackground(drawable);
                for(int i=0;i<commnumber;i++) {
                    tv[i].setTextColor(Color.WHITE);
                    tvcount[i].setTextColor(Color.WHITE);
                    tvcost[i].setTextColor(Color.WHITE);
                }
                imgupcount = 0;
                String shopname = "gmail2";
                ShopName = "gmail2";
                //prepare image name
                String titlestr = shopname + "title";
                String onestr = shopname + "one";
                String twostr = shopname + "two";
                String thrstr = shopname + "three";
                String fourstr = shopname + "four";
                String fivestr = shopname + "five";
                String sixstr = shopname + "six";


                thread.start();
//                Context mCtx;
//                mCtx = Shop_1_retrive.this;
//                DownloadImage serverRequests = new DownloadImage(mCtx);
                mSpinShopnameType.setEnabled(false);


                new DownloadImage(titlestr).execute();
                new DownloadImage(onestr).execute();
                new DownloadImage(twostr).execute();
                new DownloadImage(thrstr).execute();
                new DownloadImage(fourstr).execute();
                new DownloadImage(fivestr).execute();
                new DownloadImage(sixstr).execute();
            } else
                Log.e("rainsilk", "not 2");

            if (str3.equals(chooseItem) == true) {
                drawable =  res.getDrawable(R.drawable.bg_color);
                All.setBackground(drawable);
                imgupcount = 0;
                String shopname = "cc";
                ShopName = "cc";
                //prepare image name
                String titlestr = shopname + "title";
                String onestr = shopname + "one";
                String twostr = shopname + "two";
                String thrstr = shopname + "three";
                String fourstr = shopname + "four";
                String fivestr = shopname + "five";
                String sixstr = shopname + "six";

                thread.start();
                mSpinShopnameType.setEnabled(false);

                new DownloadImage(titlestr).execute();
                new DownloadImage(onestr).execute();
                new DownloadImage(twostr).execute();
                new DownloadImage(thrstr).execute();
                new DownloadImage(fourstr).execute();
                new DownloadImage(fivestr).execute();
                new DownloadImage(sixstr).execute();
            } else
                Log.e("rainsilk", "not 3");


            switch (parent.getSelectedItem().toString()) {
                case "":
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }
    };

    //處理查詢商品名稱及價格事件
    private void Showcomminfo(String localshop) {

        try {
            String result = DBConnector.executeQueryComm(localshop);
            Log.e("rainsilk", "comm info result=" + result);
            JSONArray jsonArray = new JSONArray(result);
            Log.e("rainsilk", "jsonArray.length()=" + jsonArray.length());
            //處理取得之商品資訊
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);

                commName = jsonData.getString("Name");
                String commNum = jsonData.getString("Num");
                commPrice = jsonData.getString("Price");
                commCount = jsonData.getString("Count");
                Log.e("rainsilk", "comm i=" + i + 1);
                Message msg = mHandler.obtainMessage();
                msg.what = i + 1;
                msg.sendToTarget();

                thread.sleep(500);
            }//END OF FOR LOOP
        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }
    }


}
