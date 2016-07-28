package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
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

public class MapsActivity extends FragmentActivity implements ConnectionCallbacks,
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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
    }

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
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, MapsActivity.this);
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
                    AlertDialog.Builder ab = new AlertDialog.Builder(MapsActivity.this);

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
                    AlertDialog.Builder ab = new AlertDialog.Builder(MapsActivity.this);

                    ab.setTitle(R.string.title_current_location)
                            .setMessage(R.string.message_current_location)
                            .setCancelable(true);

                    ab.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent result = new Intent();
                            result.putExtra("lat", currentLocation.getLatitude());
                            result.putExtra("lng", currentLocation.getLongitude());
                            Log.e("rainsilk", "lat=" + currentLocation.getLatitude());
                            Log.e("rainsilk", "lng=" + currentLocation.getLongitude());
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
                .title(title)
                .snippet(snippet)
                .icon(icon);

        // �[�J�ó]�w�O���x�s����m�аO
        itemMarker = mMap.addMarker(markerOptions);
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
        // �]�wŪ����m��T�����j�ɶ����@��]1000ms�^
        locationRequest.setInterval(1000);
        // �]�wŪ����m��T�̧֪����j�ɶ����@��]1000ms�^
        locationRequest.setFastestInterval(1000);
        // �]�w�u��Ū������T�ת���m��T�]GPS�^
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(Bundle bundle) {
        // �w�g�s�u��Google Services
        // �Ұʦ�m��s�A��
        // ��m��T��s���ɭԡA���ε{���|�۰ʩI�sLocationListener.onLocationChanged
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, MapsActivity.this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Google Services�s�u���_
        // int�ѼƬO�s�u���_���N��
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Google Services�s�u����
        // ConnectionResult�ѼƬO�s�u���Ѫ���T
        int errorCode = connectionResult.getErrorCode();

        // �˸m�S���w��Google Play�A��
        if (errorCode == ConnectionResult.SERVICE_MISSING) {
            Toast.makeText(this, R.string.google_play_service_missing,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // ��m����
        // Location�ѼƬO�ثe����m
        currentLocation = location;
        LatLng latLng = new LatLng(
                location.getLatitude(), location.getLongitude());

        // �]�w�ثe��m���аO
        if (currentMarker == null) {
            currentMarker = mMap.addMarker(new MarkerOptions().position(latLng));
        } else {
            currentMarker.setPosition(latLng);
        }

        // ���ʦa�Ϩ�ثe����m
        moveMap(latLng);
    }

}
