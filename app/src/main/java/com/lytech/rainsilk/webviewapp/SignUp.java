package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by rainsilk on 2015/12/18.
 */
public class SignUp extends Activity implements View.OnClickListener {

    SQLite helper = new SQLite(this);
    ImageView headphoto,TexiPhoto;
    TextView TexiPhotoText;
    private static final int RESULT_LOAD_IMAGE = 1,RESULT_LOAD_IMAGE2 = 2;
    public static final String SERVER_ADDRESS = "http://lytechly.comxa.com/";
    String assoiation = "";
    String giftshopcount = "1000";
    private CheckBox ShopCheckBox, CarCheckBox;
    private Spinner spinner;
    private EditText tfjob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        Intent intent = getIntent();
        assoiation = intent.getStringExtra("ass");

        headphoto = (ImageView) findViewById(R.id.imgviewHead);
        headphoto.setOnClickListener(this);
        TexiPhoto = (ImageView) findViewById(R.id.TexiPhoto);
        TexiPhoto.setOnClickListener(this);
        TexiPhotoText = (TextView)findViewById(R.id.textViewTexiPhoto);

        ShopCheckBox = (CheckBox) findViewById(R.id.Shopcheck);
        CarCheckBox = (CheckBox) findViewById(R.id.Carcheck);
        ShopCheckBox.setOnCheckedChangeListener(chklistener);
        CarCheckBox.setOnCheckedChangeListener(chklistener);

        tfjob = (EditText) findViewById(R.id.TFJob);
        //職業下拉選單
        //取值
        spinner = (Spinner) findViewById(R.id.spinnerJobtype);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView parent, View v, int position, long id) {
                // parent = 事件發生的母體 spinner_items
                // position = 被選擇的項目index = parent.getSelectedItemPosition()
                // id = row id，通常給資料庫使用
                String stfjob = spinner.getSelectedItem().toString();
                tfjob.setText(stfjob);
            }

            @Override
            public void onNothingSelected(AdapterView parent) {
            }
        });


    }

    private CheckBox.OnCheckedChangeListener chklistener = new CheckBox.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //判斷CheckBox是否有勾選，同mCheckBox.isChecked()
            if (ShopCheckBox.isChecked()) {
                //CheckBox狀態 : 已勾選，顯示TextView
                Toast.makeText(getApplicationContext(), "申請成為店家!", Toast.LENGTH_SHORT).show();
                CarCheckBox.setEnabled(false);
            } else {
                CarCheckBox.setEnabled(true);
            }
            if (CarCheckBox.isChecked()) {
                //CheckBox狀態 : 已勾選，顯示TextView
                Toast.makeText(getApplicationContext(), "申請成為司機!", Toast.LENGTH_SHORT).show();
                ShopCheckBox.setEnabled(false);
                TexiPhotoText.setVisibility(View.VISIBLE);
                TexiPhoto.setVisibility(View.VISIBLE);
            } else {
                ShopCheckBox.setEnabled(true);
                TexiPhotoText.setVisibility(View.GONE);
                TexiPhoto.setVisibility(View.GONE);
            }
        }
    };

    public void onSignUpClick(View v) {
        if (v.getId() == R.id.BSignbutton) {

            EditText name = (EditText) findViewById(R.id.TFName);
            EditText email = (EditText) findViewById(R.id.TFEmail);
            EditText uname = (EditText) findViewById(R.id.TFUsername);
            EditText pass1 = (EditText) findViewById(R.id.TFPassword);
            EditText pass2 = (EditText) findViewById(R.id.TFPassword2);
            EditText address = (EditText) findViewById(R.id.TFAddress);

            EditText phone_num = (EditText) findViewById(R.id.TFPhone);
            EditText job = (EditText) findViewById(R.id.TFJob);
            EditText introperson = (EditText) findViewById(R.id.TFFounder);
            EditText lineid = (EditText) findViewById(R.id.TFLine);

            String namestr = name.getText().toString();
            String emailstr = email.getText().toString();
            String unamestr = uname.getText().toString();
            String texiunamestr = "texi_"+uname.getText().toString();
            String pass1str = pass1.getText().toString();
            String pass2str = pass2.getText().toString();
            String phonestr = phone_num.getText().toString();

            String addressstr = address.getText().toString();
            String jobstr = job.getText().toString();
            String intropersonstr = introperson.getText().toString();
            String lineidstr = lineid.getText().toString();

            Contact contact;

            //進行欄位不為空檢查
            if (namestr.matches("") || unamestr.matches("") || phonestr.matches("")
                    || pass1str.matches("") || pass2str.matches("") || addressstr.matches("")
                    || intropersonstr.matches("") || lineidstr.matches("") || addressstr.matches("")) {
                Toast toast = Toast.makeText(SignUp.this, "欄位不能為空白!!", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                //密碼二次輸入驗證
                if (!pass1str.equals(pass2str)) {
                    //Pop Message
                    Toast pass = Toast.makeText(SignUp.this, "密碼驗證不相同,請再次確認!", Toast.LENGTH_SHORT);
                    pass.show();
                } else //都ok後進行寫入資料庫註冊
                {
                    //uploadImage data
                    Bitmap image = ((BitmapDrawable) headphoto.getDrawable()).getBitmap();
                    new UploadImage(image, unamestr).execute();
                    Bitmap texiimage = ((BitmapDrawable) TexiPhoto.getDrawable()).getBitmap();
                    new UploadImage(texiimage, texiunamestr).execute();

                    //sign up member data
                    contact = new Contact(namestr, emailstr, unamestr, pass1str, phonestr, addressstr, assoiation, jobstr, intropersonstr, lineidstr, "0", "0", giftshopcount, "0", "0", "0", "2000");
                    if (ShopCheckBox.isChecked() == true)
                        contact = new Contact(namestr, emailstr, unamestr, pass1str, phonestr, addressstr, assoiation, jobstr, intropersonstr, lineidstr, "0", "0", giftshopcount, "0", "0", "0", "333");

                    if (CarCheckBox.isChecked() == true)
                        contact = new Contact(namestr, emailstr, unamestr, pass1str, phonestr, addressstr, assoiation, jobstr, intropersonstr, lineidstr, "0", "0",giftshopcount, "0", "0", "0", "123");


                    ServerRequests serverRequests = new ServerRequests(this);
                    serverRequests.storeDataInBackground(contact, new GetUserCallback() {
                        @Override
                        public void done(Contact returnedContact) {
                            Intent intent = new Intent(SignUp.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }


        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imgviewHead) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
        }
        if (v.getId() == R.id.TexiPhoto) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE2);
        }
//        if(v.getId() ==R.id.Shopcheck)
//        {
//            //設定CheckBox勾選狀態
//            if(ShopCheckBox.isChecked()==true)
//                ShopCheckBox.setChecked(false);
//            else {
//                ShopCheckBox.setChecked(true);
//                Toast.makeText(SignUp.this, "申請成為店家!", Toast.LENGTH_SHORT);
//            }
//        }
//        if(v.getId() ==R.id.Carcheck)
//        {
//            //設定CheckBox勾選狀態
//            if(CarCheckBox.isChecked()==true)
//                CarCheckBox.setChecked(false);
//            else {
//                CarCheckBox.setChecked(true);
//                Toast.makeText(SignUp.this, "申請成為司機!", Toast.LENGTH_SHORT);
//            }
//        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectImage = data.getData();
            headphoto.setBackgroundColor(Color.TRANSPARENT); //@android:color/transparent
            headphoto.setImageURI(selectImage);
        }

        if (requestCode == RESULT_LOAD_IMAGE2 && resultCode == RESULT_OK && data != null) {
            Uri selectImage2 = data.getData();
            TexiPhoto.setBackgroundColor(Color.TRANSPARENT); //@android:color/transparent
            TexiPhoto.setImageURI(selectImage2);
        }
    }

    private class UploadImage extends AsyncTask<Void, Void, Void> {

        Bitmap image;
        String uname;

        public UploadImage(Bitmap image, String uname) {
            this.image = image;
            this.uname = uname;
            Log.e("rainsilkinfo", "UploadImage function");
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
            Toast.makeText(getApplicationContext(), "Image Uploaded!", Toast.LENGTH_SHORT).show();
        }
    }

    private HttpParams getHttpRequestParams() {
        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000 * 30);
        HttpConnectionParams.setSoTimeout(httpRequestParams, 1000 * 30);
        return httpRequestParams;
    }

}
