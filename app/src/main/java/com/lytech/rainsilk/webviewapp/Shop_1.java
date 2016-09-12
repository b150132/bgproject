package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by rainsilk on 2016/2/24.
 */
public class Shop_1 extends Activity {
    public static final int CONNECTION_TIMEOUT = 30000; //30 second
    public static final String SERVER_ADDRESS = "http://lytechly.comxa.com/";
    int shopCommodityNum = 6; //店家商品數量
    private static final int RESULT_LOAD_IMAGE = 0, RESULT_LOAD_IMAGE1 = 1, RESULT_LOAD_IMAGE2 = 2, RESULT_LOAD_IMAGE3 = 3, RESULT_LOAD_IMAGE4 = 4, RESULT_LOAD_IMAGE5 = 5, RESULT_LOAD_IMAGE6 = 6;
    ImageView imgTitle;
    ImageView[] imv = new ImageView[shopCommodityNum];
    Button upload;
    LocalDatabase localDatabase;
    int imgupcount = 0;


    String[] goods = new String[shopCommodityNum], goodsprice = new String[shopCommodityNum], goodscount = new String[shopCommodityNum];
    private EditText[] etxs = new EditText[shopCommodityNum];
    private EditText[] etxprices = new EditText[shopCommodityNum];
    private EditText[] etxcounts = new EditText[shopCommodityNum];
    int[] imvs = {R.id.imv1, R.id.imv2, R.id.imv3, R.id.imv4, R.id.imv5, R.id.imv6};
    int[] tvs = {R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4, R.id.tv5, R.id.tv6};
    int[] tvcosts = {R.id.tvcost1, R.id.tvcost2, R.id.tvcost3, R.id.tvcost4, R.id.tvcost5, R.id.tvcost6};
    int[] tvcounts = {R.id.tvcount1, R.id.tvcount2, R.id.tvcount3, R.id.tvcount4, R.id.tvcount5, R.id.tvcount6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_1);
        localDatabase = new LocalDatabase(this);

        upload = (Button) findViewById(R.id.BUpbutton);

        imgTitle = (ImageView) findViewById(R.id.imgTitle);
        for (int i = 0; i < shopCommodityNum; i++)
            imv[i] = (ImageView) findViewById(imvs[i]);
//        imv2 = (ImageView)findViewById(R.id.imv2);
//        imv3 = (ImageView)findViewById(R.id.imv3);
//        imv4 = (ImageView)findViewById(R.id.imv4);
//        imv5 = (ImageView)findViewById(R.id.imv5);
//        imv6 = (ImageView)findViewById(R.id.imv6);

        for (int i = 0; i < shopCommodityNum; i++)
            etxs[i] = (EditText) findViewById(tvs[i]);
//        etxs[0] =(EditText)findViewById(R.id.tv1);
//        etxs[1] =(EditText)findViewById(R.id.tv2);
//        etxs[2] =(EditText)findViewById(R.id.tv3);
//        etxs[3] =(EditText)findViewById(R.id.tv4);
//        etxs[4] =(EditText)findViewById(R.id.tv5);
//        etxs[5] =(EditText)findViewById(R.id.tv6);
        for (int i = 0; i < shopCommodityNum; i++) {
            etxprices[i] = (EditText) findViewById(tvcosts[i]);
            etxcounts[i] = (EditText) findViewById(tvcounts[i]);
        }
//        etxprices[0] = (EditText)findViewById(R.id.tvcost1);
//        etxprices[1] = (EditText)findViewById(R.id.tvcost2);
//        etxprices[2] = (EditText)findViewById(R.id.tvcost3);
//        etxprices[3] = (EditText)findViewById(R.id.tvcost4);
//        etxprices[4] = (EditText)findViewById(R.id.tvcost5);
//        etxprices[5] = (EditText)findViewById(R.id.tvcost6);
    }


    public void onClick(View v) {
        if (v.getId() == R.id.btnback) {
            finish();
        }

        if (v.getId() == R.id.BUpbutton) {
            upload.setEnabled(false);
            Toast.makeText(Shop_1.this, "於提示上傳成功時前請勿移開畫面!!", Toast.LENGTH_LONG).show();

            int count1 = 0;
            for (EditText etx : etxs) {
                goods[count1] = etx.getText().toString();
                count1++;
            }
            int count2 = 0;
            for (EditText etxprice : etxprices) {
                goodsprice[count2] = etxprice.getText().toString();
                count2++;
            }
            int count3 = 0;
            for (EditText etxcount : etxcounts) {
                goodscount[count3] = etxcount.getText().toString();
                count3++;
            }

            Contact contact = localDatabase.getLoggedInUser();
            String shopname = contact.getUname();

            //prepare image name
            String titlestr = shopname + "title";
            String onestr = shopname + "one";
            String twostr = shopname + "two";
            String thrstr = shopname + "three";
            String fourstr = shopname + "four";
            String fivestr = shopname + "five";
            String sixstr = shopname + "six";

            Bitmap image = ((BitmapDrawable) imgTitle.getDrawable()).getBitmap();
            Bitmap[] bimage = new Bitmap[shopCommodityNum];
            for (int j = 0; j < shopCommodityNum; j++)
                bimage[j] = ((BitmapDrawable) imv[j].getDrawable()).getBitmap();
//            Bitmap image2= ((BitmapDrawable)imv2.getDrawable()).getBitmap();
//            Bitmap image3= ((BitmapDrawable)imv3.getDrawable()).getBitmap();
//            Bitmap image4= ((BitmapDrawable)imv4.getDrawable()).getBitmap();
//            Bitmap image5= ((BitmapDrawable)imv5.getDrawable()).getBitmap();
//            Bitmap image6= ((BitmapDrawable)imv6.getDrawable()).getBitmap();

            //先記錄商品資料
            Commodity commodity;
            Context mCtx;
            mCtx = Shop_1.this;
            String pnamelist[] = {onestr, twostr, thrstr, fourstr, fivestr, sixstr};

            for (int k = 0; k < shopCommodityNum; k++) {
                commodity = new Commodity(goods[k], Integer.toString(k + 1), shopname, goodsprice[k], goodscount[k], pnamelist[k]);
                ServerRequests serverRequests = new ServerRequests(mCtx);
                serverRequests.storeCommDetailInBackground(commodity, new GetUserCallback() {
                    @Override
                    public void done(Contact returnedContact) {

                    }
                });
            }

            //上傳圖片
            new UploadImage(image, titlestr).execute();

            new UploadImage(bimage[0], onestr).execute();
            new UploadImage(bimage[1], twostr).execute();
            new UploadImage(bimage[2], thrstr).execute();
            new UploadImage(bimage[3], fourstr).execute();
            new UploadImage(bimage[4], fivestr).execute();
            new UploadImage(bimage[5], sixstr).execute();
        }

        if (v.getId() == R.id.imgTitle) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
        }
        if (v.getId() == R.id.imv1) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE1);
        }
        if (v.getId() == R.id.imv2) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE2);
        }
        if (v.getId() == R.id.imv3) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE3);
        }
        if (v.getId() == R.id.imv4) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE4);
        }
        if (v.getId() == R.id.imv5) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE5);
        }
        if (v.getId() == R.id.imv6) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE6);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectImage = data.getData();
            imgTitle.setBackgroundColor(Color.TRANSPARENT); //@android:color/transparent
            // imgTitle.setImageURI(selectImage);
            String selectedImagePath2 = getPath(selectImage);
            imgTitle.setImageBitmap(lessenUriImage(selectedImagePath2));

        }
        if (requestCode == RESULT_LOAD_IMAGE1 && resultCode == RESULT_OK && data != null) {
            Uri selectImage = data.getData();
            imv[0].setBackgroundColor(Color.TRANSPARENT); //@android:color/transparent
            String selectedImagePath2 = getPath(selectImage);
            imv[0].setImageBitmap(lessenUriImage(selectedImagePath2));
        }
        if (requestCode == RESULT_LOAD_IMAGE2 && resultCode == RESULT_OK && data != null) {
            Uri selectImage = data.getData();
            imv[1].setBackgroundColor(Color.TRANSPARENT); //@android:color/transparent
            String selectedImagePath2 = getPath(selectImage);
            imv[1].setImageBitmap(lessenUriImage(selectedImagePath2));
        }
        if (requestCode == RESULT_LOAD_IMAGE3 && resultCode == RESULT_OK && data != null) {
            Uri selectImage = data.getData();
            imv[2].setBackgroundColor(Color.TRANSPARENT); //@android:color/transparent
            // imv3.setImageURI(selectImage);
            String selectedImagePath2 = getPath(selectImage);
            imv[2].setImageBitmap(lessenUriImage(selectedImagePath2));
        }
        if (requestCode == RESULT_LOAD_IMAGE4 && resultCode == RESULT_OK && data != null) {
            Uri selectImage = data.getData();
            imv[3].setBackgroundColor(Color.TRANSPARENT); //@android:color/transparent
            //imv4.setImageURI(selectImage);
            String selectedImagePath2 = getPath(selectImage);
            imv[3].setImageBitmap(lessenUriImage(selectedImagePath2));
        }
        if (requestCode == RESULT_LOAD_IMAGE5 && resultCode == RESULT_OK && data != null) {
            Uri selectImage = data.getData();
            imv[4].setBackgroundColor(Color.TRANSPARENT); //@android:color/transparent
            //imv5.setImageURI(selectImage);
            String selectedImagePath2 = getPath(selectImage);
            imv[4].setImageBitmap(lessenUriImage(selectedImagePath2));
        }
        if (requestCode == RESULT_LOAD_IMAGE6 && resultCode == RESULT_OK && data != null) {
            Uri selectImage = data.getData();
            imv[5].setBackgroundColor(Color.TRANSPARENT); //@android:color/transparent
            //imv6.setImageURI(selectImage);
            String selectedImagePath2 = getPath(selectImage);
            imv[5].setImageBitmap(lessenUriImage(selectedImagePath2));
        }
    }

    private class UploadImage extends AsyncTask<Void, Void, Void> {

        Bitmap image;
        String uname;

        public UploadImage(Bitmap image, String uname) {
            this.image = image;
            this.uname = uname;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("uname", uname));
            dataToSend.add(new BasicNameValuePair("image", encodedImage));

            HttpParams httpRequestParams = getHttpRequestParams();

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "SavePicture.php");


            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            imgupcount++;
            if (imgupcount == 7) {
                Toast.makeText(getApplicationContext(), "圖片全數上傳成功,離開", Toast.LENGTH_LONG).show();
                finish();
            } else
                Toast.makeText(getApplicationContext(), "圖片上傳中......請稍候", Toast.LENGTH_SHORT).show();
        }
    }

    private HttpParams getHttpRequestParams() {
        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000 * 30);
        HttpConnectionParams.setSoTimeout(httpRequestParams, 1000 * 30);
        return httpRequestParams;
    }

    //圖片Outmemory處理
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public final static Bitmap lessenUriImage(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); //此时返回 bm 为空
        options.inJustDecodeBounds = false; //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = (int) (options.outHeight / (float) 320);
        if (be <= 0)
            be = 1;
        options.inSampleSize = be; //重新读入图片，注意此时已经把 options.inJustDecodeBounds 设回 false 了
        bitmap = BitmapFactory.decodeFile(path, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        System.out.println(w + " " + h); //after zoom
        return bitmap;
    }

}
