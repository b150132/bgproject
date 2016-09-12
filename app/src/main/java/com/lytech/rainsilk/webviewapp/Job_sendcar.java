package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by rainsilk on 2016/1/22.
 */
public class Job_sendcar extends Activity {
    private Spinner mSpinBuyType;
    private TextView mTxtB, mTxtLocation, mTitle;
    private Item item;
    LocalDatabase localDatabase;
    String ServiceCode;
    boolean IsLocation = false;

    private static final int START_LOCATION = 1;
    private static final int START_LOCATION2 = 2;
    final int MY_ID = 100;
    int servicecode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_sendcar);
        Intent intent = getIntent();
        ServiceCode = intent.getStringExtra("servicecode");//"1"; //派車服務 6:外送服務
        servicecode = Integer.parseInt(ServiceCode);

        localDatabase = new LocalDatabase(this);

        mTitle = (TextView) findViewById(R.id.txtTitle);
        switch (servicecode) {
            case 1:
                mTitle.setText("呼叫計程車");
                break;
            case 3:
                mTitle.setText("呼叫外送服務");
                break;
            default:
                break;
        }
        mTxtB = (TextView) findViewById(R.id.textViewMinType);
        mTxtLocation = (TextView) findViewById(R.id.txtlocationname);
        mSpinBuyType = (Spinner) findViewById(R.id.spinnerMintype);

        item = new Item();

    }

    private AdapterView.OnItemSelectedListener spnBuyTypeItemSelect = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            mTxtB.setText(getString(R.string.buytype_select) + parent.getSelectedItem().toString());
            Object item = parent.getItemAtPosition(position);
            String chooseItem = item.toString();
//            String str1= "美食";
//            String str2= "咖啡廳";
//            String str3= "貨運";
//
//
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

    public void onButtonClick(View v) throws IOException {

        if (v.getId() == R.id.imgUndo) {
            finish();
        }

        if (v.getId() == R.id.imgbtnLocation) {

            //檢查是否開啟定位
            boolean isOpen = isOpenGps();

            if (isOpen) {
                AlertDialog.Builder ab = new AlertDialog.Builder(Job_sendcar.this);

                ab.setTitle("提醒")
                        .setMessage("請於進入地圖頁後點擊定位點,以儲存您現在的位置!")
                        .setCancelable(true);

                ab.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intentMap = new Intent(Job_sendcar.this, MapsActivity.class);
                        intentMap.putExtra("lat", item.getLatitude());
                        intentMap.putExtra("lng", item.getLongitude());
                        intentMap.putExtra("title", item.getTitle());
                        Toast.makeText(Job_sendcar.this, "點擊定位點以儲存位置", Toast.LENGTH_LONG).show();
                        Toast.makeText(Job_sendcar.this, "點擊定位點以儲存位置", Toast.LENGTH_LONG).show();
                        startActivityForResult(intentMap, START_LOCATION);
                        IsLocation = true;
                    }
                });
                ab.setNegativeButton(android.R.string.cancel, null);
                ab.show();

            } else
                Toast.makeText(Job_sendcar.this, "請先開啟定位服務!!", Toast.LENGTH_SHORT).show();
        }
        if (v.getId() == R.id.imgbtnCancelJob) {
            finish();
        }

        if (v.getId() == R.id.imgGetJob) {
            if (IsLocation == true) {
                AlertDialog.Builder ab = new AlertDialog.Builder(Job_sendcar.this);

                ab.setTitle("注意")
                        .setMessage("1.將扣除點數且無法復原\n2.將進行配對並傳送會員資訊: LINE ID ?")
                        .setCancelable(true);

                ab.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent result = new Intent();
                        Contact contact = localDatabase.getLoggedInUser();

                        //register job
                        contact.setService(ServiceCode);
                        SetService(contact);


                        Intent i = new Intent(Job_sendcar.this, Job_carinfo.class);
                        i.putExtra("servicecode", ServiceCode); //項目
                        i.putExtra("lat", item.getLatitude()); //定位
                        i.putExtra("lng", item.getLongitude());
                        startActivityForResult(i, START_LOCATION2);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);

                        //TODO: GET ASK CAR 'S MEMBER'S LOCATION FROM DATA BASE
                        //TODO: SEARCH 700M = 3 MIN,1400M 6MIN,2100M 9 MIN
                        //TODO: SHOW THE LINE ID AND CHECK!
                        //                            result.putExtra("lat", currentLocation.getLatitude());
                        //                            result.putExtra("lng", currentLocation.getLongitude());
                        //                            Log.e("rainsilk", "lat=" + currentLocation.getLatitude());
                        //                            Log.e("rainsilk", "lng=" + currentLocation.getLongitude());
                        setResult(Activity.RESULT_OK, result);
                        //                            finish();
                    }
                });
                ab.setNegativeButton(android.R.string.cancel, null);
                ab.show();

                String placeName = "捷運劍潭站";//et.getText().toString().trim();
                if (placeName.length() > 0) {
                    Geocoder gc = new Geocoder(Job_sendcar.this);
                    List<Address> addressList = null;
                    Address address = null;
                    String returnAddress = "";
                    try {
                        addressList = gc.getFromLocationName(placeName, 1);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (addressList == null || addressList.isEmpty()) {
                        mTxtLocation.setText("找不到地址");
                    } else {
                        address = addressList.get(0);

                        float results[] = new float[1];

                        Location.distanceBetween(item.getLatitude(), item.getLongitude(), address.getLatitude(), address.getLongitude(), results);
                        //自經緯度取得地址
                        List<Address> lstAddress = gc.getFromLocation(item.getLatitude(), item.getLongitude(), 1);
                        returnAddress = lstAddress.get(0).getAddressLine(0);
                        String distance = NumberFormat.getInstance().format(results[0]);
                        mTxtLocation.setText(returnAddress);
                    }
                }
            }//END OF IF ISLOCATION
            else
                Toast.makeText(Job_sendcar.this, "請先進行定位!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

                case START_LOCATION:
                    double lat = data.getDoubleExtra("lat", 0.0);
                    double lng = data.getDoubleExtra("lng", 0.0);
                    item.setLatitude(lat);
                    item.setLongitude(lng);
                    String vOut = Double.toString(lat);
                    String vOut2 = Double.toString(lng);

                    //de bug用
                    //mTxtLocation.setText(vOut + "," + vOut2);
                    mTxtLocation.setText("定位完成,請點確認");

                    Contact contact = localDatabase.getLoggedInUser();
                    contact = new Contact(contact.getID(), vOut, vOut2);
                    authenticate(contact);

                    break;

            }
        }
    }

    public void authenticate(Contact contact) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.setLocatDataInBackground(contact, new GetUserCallback() {
            @Override
            public void done(Contact returnedContact) {

            }
        });
    }

    public void SetService(Contact contact) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.setServiceDataInBackground(contact, new GetUserCallback() {
            @Override
            public void done(Contact returnedContact) {

            }
        });
    }

    /**
     * 判斷GPS是否開啟，GPS或者AGPS開啟一個就認為是開啟的
     */
    private boolean isOpenGps() {

        LocationManager locationManager
                = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // 通過GPS衛星定位，定位級別可以精確到街（通過24顆衛星定位，在室外和空曠的地方定位準確、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通過WLAN或移動網路(3G/2G)確定的位置（也稱作AGPS，輔助GPS定位。主要用於在室內或遮蓋物（建築群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }


}
