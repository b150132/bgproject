package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

public class MapsShowActivity extends FragmentActivity implements ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    // Google API
    private GoogleApiClient googleApiClient;

    // Location
    private LocationRequest locationRequest;

    //
    private Location currentLocation;

    //
    private Marker currentMarker, itemMarker;

    Thread thread;
    double[] shoplat = new double[3];
    double[] shoplng = new double[3];
    String temp1, temp2;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            shoplat[msg.what - 1] = Double.parseDouble(temp1);
            shoplng[msg.what - 1] = Double.parseDouble(temp2);

            MarkerOptions markerOptthread = new MarkerOptions();
            markerOptthread.position(new LatLng(shoplat[msg.what - 1], shoplng[msg.what - 1]));
            markerOptthread.title("店家" + msg.what + "號");
            markerOptthread.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            mMap.addMarker(markerOptthread);

            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Thread thread = new Thread(mutiThread);
        thread.start();
        setUpMapIfNeeded();

        // Google API
        configGoogleApiClient();
        // �إ�Location�ШD����
        configLocationRequest();

        //
        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("lat", 0.0);
        double lng = intent.getDoubleExtra("lng", 0.0);

        //
        if (lat != 0.0 && lng != 0.0) {
            // �إ߮y�Ъ���
            LatLng itemPlace = new LatLng(lat, lng);
            // �[�J�a�ϼаO
            addMarker(itemPlace, intent.getStringExtra("title"),
                    intent.getStringExtra("datetime"));
            // ���ʦa��
            moveMap(itemPlace);
        } else {
            // �s�u��Google API�Τ��
            if (!googleApiClient.isConnected()) {
                googleApiClient.connect();
            }
        }

        // thread = new Thread(mutiThread);


    }

    private Runnable mutiThread = new Runnable() {
        public void run() {
            // 運行網路連線的程式
            Showshopinfo();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

        // �s�u��Google API�Τ��
        if (!googleApiClient.isConnected() && currentMarker != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // ������m�ШD�A��
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, MapsShowActivity.this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // ����Google API�Τ�ݳs�u
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    private void processController() {
        // ��ܮث��s�ƥ�
        final DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            // ��s��m��T
                            case DialogInterface.BUTTON_POSITIVE:
                                // �s�u��Google API�Τ��
                                if (!googleApiClient.isConnected()) {
                                    googleApiClient.connect();
                                }
                                break;
                            // �M����m��T
                            case DialogInterface.BUTTON_NEUTRAL:
                                Intent result = new Intent();
                                result.putExtra("lat", 0);
                                result.putExtra("lng", 0);
                                setResult(Activity.RESULT_OK, result);
                                finish();
                                break;
                            // ����
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

        // �аO�T�����I���ƥ�
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                // �p�G�O�O���x�s���аO
                if (marker.equals(itemMarker)) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(MapsShowActivity.this);

                    ab.setTitle(R.string.title_update_location)
                            .setMessage(R.string.message_update_location)
                            .setCancelable(true);

                    ab.setPositiveButton(R.string.update, listener);
                    ab.setNeutralButton(R.string.clear, listener);
                    ab.setNegativeButton(android.R.string.cancel, listener);

                    ab.show();
                }
            }
        });

        //
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //
                if (marker.equals(currentMarker)) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(MapsShowActivity.this);

                    ab.setTitle(R.string.title_current_location)
                            .setMessage(R.string.message_current_location)
                            .setCancelable(true);

                    ab.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent result = new Intent();
                            result.putExtra("lat", currentLocation.getLatitude());
                            result.putExtra("lng", currentLocation.getLongitude());
                            setResult(Activity.RESULT_OK, result);
                            finish();
                        }
                    });
                    ab.setNegativeButton(android.R.string.cancel, null);

                    ab.show();

                    return true;
                }

                return false;
            }
        });
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().
                    findFragmentById(R.id.map)).getMap();

            if (mMap != null) {
                // �����a�ϳ]�w
                //setUpMap();
                processController();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        LatLng place = new LatLng(25.033408, 121.564099);
        moveMap(place);
        addMarker(place, "Hello!", " Google Maps v2!");
    }

    // ���ʦa�Ϩ�Ѽƫ��w����m
    private void moveMap(LatLng place) {
        // �إߦa����v������m����
        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(place)
                        .zoom(17)
                        .build();

        // �ϥΰʵe���ĪG���ʦa��
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    // �b�a�ϥ[�J���w��m�P���D���аO
    private void addMarker(LatLng place, String title, String snippet) {
        BitmapDescriptor icon =
                BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(place)
                .title("所選區域")
                .snippet(snippet)
                .icon(icon);


        itemMarker = mMap.addMarker(markerOptions);

        //Marker1
        MarkerOptions markerOpt = new MarkerOptions();
        //TODO : 從資料庫抓資料顯示

        markerOpt.position(new LatLng(25.033611, 121.565000));
        markerOpt.title("台北101");
        markerOpt.snippet("於1999年動工，2004年12月31日完工啟用，樓高509.2公尺。");
//        markerOpt.draggable(false);
//        markerOpt.visible(true);
////        markerOpt.anchor(0.5f, 0.5f);//設為圖片中心
//        markerOpt.icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_mylocation));
//
        mMap.addMarker(markerOpt);
        //Marker2
        MarkerOptions markerOpt2 = new MarkerOptions();
        markerOpt2.position(new LatLng(25.047924, 121.517081));
        markerOpt2.title("台北火車站");

        mMap.addMarker(markerOpt2);

        //Marker3
        MarkerOptions markerOpt3 = new MarkerOptions();
        markerOpt3.position(new LatLng(25.042902, 121.515030));
        markerOpt3.title("國立台灣博物館");
        markerOpt3.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        mMap.addMarker(markerOpt3);

    }

    // �إ�Google API�Τ�ݪ���
    private synchronized void configGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    // �إ�Location�ШD����
    private void configLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(Bundle bundle) {

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, MapsShowActivity.this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        int errorCode = connectionResult.getErrorCode();


        if (errorCode == ConnectionResult.SERVICE_MISSING) {
            Toast.makeText(this, R.string.google_play_service_missing,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        currentLocation = location;
        LatLng latLng = new LatLng(
                location.getLatitude(), location.getLongitude());

        if (currentMarker == null) {
            currentMarker = mMap.addMarker(new MarkerOptions().position(latLng));

            //更改地圖縮放
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
            // Zoom in, animating the camera.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(4), 2000, null);
        } else {
            currentMarker.setPosition(latLng);
        }

        moveMap(latLng);
    }

    //處理查詢商店地點及資訊
    private void Showshopinfo() {
        String area = "0"; //台北市

        try {
            String result = DBConnector.executeQueryShop(area);
            Log.e("rainsilk", "shop locate result=" + result);
            JSONArray jsonArray = new JSONArray(result);
            Log.e("rainsilk", "jsonArray.length()=" + jsonArray.length());
            //處理取得之商品資訊
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);

                temp1 = jsonData.getString("Locationone");
                temp2 = jsonData.getString("Locationtwo");

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
