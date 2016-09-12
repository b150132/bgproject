package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by rainsilk on 2016/1/22.
 */
public class Service_sendcar extends Activity {
    private Spinner mSpinBuyType;
    private TextView mTxtB, mTxtLocation, mTitle;
    private ImageButton getjob, location;
    private Item item;
    LocalDatabase localDatabase;

    private static final int START_LOCATION = 1;
    String ServiceCode;
    boolean IsLocation = false, IsBroadcast = false;

    public static final String BROADCAST_ACTION =
            "net.macdidi.broadcast01.action.MYBROADCAST01";
    // 建立廣播接收元件物件
    MyBroadcastReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_sendcar);

        Intent intent = getIntent();
        ServiceCode = intent.getStringExtra("jobcode"); //"2"; //司機派車排班代碼, 4機車外送排班

        mTitle = (TextView) findViewById(R.id.txtTitle);
        mTxtB = (TextView) findViewById(R.id.textViewMinType);
        mTxtLocation = (TextView) findViewById(R.id.txtlocationname);
        mSpinBuyType = (Spinner) findViewById(R.id.spinnerMintype);
        getjob = (ImageButton) findViewById(R.id.imgGetService);
        location = (ImageButton) findViewById(R.id.imgbtnLocation);
        int jobcode = Integer.parseInt(ServiceCode);

        item = new Item();

        switch (jobcode) {
            case 2:
                mTitle.setText("計程車排班");
                break;
            case 4:
                mTitle.setText("機車外送排班");
                break;
            default:
                break;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String id = intent.getStringExtra("ID");
        String lineid = intent.getStringExtra("LINEID");
        String phone = intent.getStringExtra("PHONE");
        String locate1 = intent.getStringExtra("LOCAT1");
        String locate2 = intent.getStringExtra("LOCAT2");

        if (intent.getBooleanExtra("close_activity", false)) {

            //Toast.makeText(this,"有人叫車,結束....",Toast.LENGTH_LONG).show();
            finish();
            Intent i = new Intent(Service_sendcar.this, CheckLineFinish.class);
            i.putExtra("jobcode", ServiceCode);
            i.putExtra("ID", id);
            i.putExtra("LINEID", lineid);
            i.putExtra("PHONE", phone);
            i.putExtra("LOCAT1", locate1);
            i.putExtra("LOCAT2", locate2);
////                                i.putExtra("LINEID", Lineid);
            startActivity(i);
        }
    }

    @Override
    protected void onDestroy() {
        // 移除廣播接收元件
        if (IsBroadcast)
            unregisterReceiver(receiver);
        super.onDestroy();

    }


    private AdapterView.OnItemSelectedListener spnBuyTypeItemSelect = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            mTxtB.setText(getString(R.string.buytype_select) + parent.getSelectedItem().toString());
            Object item = parent.getItemAtPosition(position);
            String chooseItem = item.toString();

            switch (parent.getSelectedItem().toString()) {
                case "":
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }
    };

    public void onButtonClick(View v) {

        if (v.getId() == R.id.imgUndo) {
            finish();
        }

        if (v.getId() == R.id.imgbtnLocation) {
            //檢查是否開啟定位
            boolean isOpen = isOpenGps();

            if (isOpen) {
                AlertDialog.Builder ab = new AlertDialog.Builder(Service_sendcar.this);

                ab.setTitle("提醒")
                        .setMessage("請於進入地圖頁後點擊定位點,以儲存您現在的位置!")
                        .setCancelable(true);

                ab.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intentMap = new Intent(Service_sendcar.this, MapsActivity.class);

                        intentMap.putExtra("lat", item.getLatitude());
                        intentMap.putExtra("lng", item.getLongitude());
                        intentMap.putExtra("title", item.getTitle());
                        Toast.makeText(Service_sendcar.this, "點擊定位點以儲存位置", Toast.LENGTH_LONG).show();
                        Toast.makeText(Service_sendcar.this, "點擊定位點以儲存位置", Toast.LENGTH_LONG).show();
                        startActivityForResult(intentMap, START_LOCATION);
                        IsLocation = true;
                    }
                });
                ab.setNegativeButton(android.R.string.cancel, null);
                ab.show();
                    }

                    else
                            Toast.makeText(Service_sendcar.this,"請先開啟定位服務!!",Toast.LENGTH_SHORT).

                    show();
                }
                if (v.getId() == R.id.imgbtnCancelService) {
            finish();
        }

        if (v.getId() == R.id.imgGetService) {
            if (IsLocation == true) {
                AlertDialog.Builder ab = new AlertDialog.Builder(Service_sendcar.this);

                ab.setTitle("隱私權確認及資訊傳輸")
                        .setMessage("將進行配對並傳送會員資訊: LINE ID ?")
                        .setCancelable(true);

                ab.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent result = new Intent();
                        Contact contact = localDatabase.getLoggedInUser();

                        //register service - send car
                        contact.setService(ServiceCode);
                        SetService(contact);

                        setResult(Activity.RESULT_OK, result);

                        getjob.setEnabled(false);
                        location.setEnabled(false);
                        Toast.makeText(Service_sendcar.this, "排班搜尋中請稍候....或按取消排班", Toast.LENGTH_LONG).show();

                        receiver = new MyBroadcastReceiver();
                        IsBroadcast = true; // 避免destroy error

                        // 準備註冊與移除廣播接收元件的IntentFilter物件
                        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
                        //        filter.putExtra("LINEID", Lineid);
                        // 註冊廣播接收元件
                        registerReceiver(receiver, filter);

                    }
                });
                ab.setNegativeButton(android.R.string.cancel, null);
                ab.show();

                String placeName = "捷運劍潭站";//et.getText().toString().trim();
                if (placeName.length() > 0) {
                    Geocoder gc = new Geocoder(Service_sendcar.this);
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
                        List<Address> lstAddress = null;
                        try {
                            lstAddress = gc.getFromLocation(item.getLatitude(), item.getLongitude(), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        returnAddress = lstAddress.get(0).getAddressLine(0);
                        String distance = NumberFormat.getInstance().format(results[0]);
                        mTxtLocation.setText(returnAddress);
                    }
                }
            }//END OF IF ISLOCATION
            else
                Toast.makeText(Service_sendcar.this, "請先進行定位!!", Toast.LENGTH_SHORT).show();


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

                    localDatabase = new LocalDatabase(this);
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
