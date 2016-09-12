package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

/**
 * Created by rainsilk on 2016/3/4.
 */
public class ShopMenu extends Activity {
    private Button uploadimg, usecount, searchmap;
    private List<String> cancellist;
    private static final int START_LOCATION = 1;

    LocalDatabase localDatabase;
    Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_menu);
        uploadimg = (Button) findViewById(R.id.btnShopUpload);
        usecount = (Button) findViewById(R.id.btnShopUseCount);
        searchmap = (Button) findViewById(R.id.btnShopGuestMap);

        localDatabase = new LocalDatabase(this);
        boolean authenticate = localDatabase.getUserLoggedIn();
        //交易成立:
        contact = localDatabase.getLoggedInUser();
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.btnShopUpload) { //上傳商鋪產品
            Toast.makeText(ShopMenu.this, "上傳商鋪產品", Toast.LENGTH_LONG).show();
            Intent i = new Intent(ShopMenu.this, Shop_1.class);
            startActivity(i);
            overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
        }
        if (v.getId() == R.id.btnShopUseCount) { //小額消費點數扣除

            //密碼再次驗證
            final View item = LayoutInflater.from(ShopMenu.this).inflate(R.layout.enterpass, null);
            new AlertDialog.Builder(ShopMenu.this)
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

                                //  Toast.makeText(ShopMenu.this, "功能未開放 ", Toast.LENGTH_LONG).show();
                                //  Toast.makeText(ChooseCountType.this, "賣點數 " , Toast.LENGTH_LONG).show();
                                Intent i = new Intent(ShopMenu.this, Type_count_use.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
                            } else {
                                Toast.makeText(getApplicationContext(), "密碼不符!請重新操作", Toast.LENGTH_SHORT).show();
                            }

                        }
                    })
                    .show();
        }
        if (v.getId() == R.id.btnShopShowSale) { //顯示商品銷售狀況
            Toast.makeText(ShopMenu.this, "商品銷售狀況", Toast.LENGTH_LONG).show();
            Intent i = new Intent(ShopMenu.this, DisplayShopSale.class);
            //TODO:帶Shopname過去 查資料庫後show頁然後返回
            i.putExtra("shopname",contact.getUname());
            startActivity(i);
            overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
        }
        if (v.getId() == R.id.btnShopGuestMap) { //搜尋附近客人
            //Toast.makeText(ShopMenu.this, "功能未開放 " , Toast.LENGTH_LONG).show();
            Intent intentMap = new Intent(this, MapsShowGuestActivity.class);

            //根據下拉選單的位置=>查詢地址=>查詢座標=>Intnent傳值
            //TODO:店家地址為中心?
            String placeName = "台北車站";//"台北車站";//et.getText().toString().trim();
            if (placeName.length() > 0) {
                Geocoder gc = new Geocoder(ShopMenu.this);
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
    }
}
