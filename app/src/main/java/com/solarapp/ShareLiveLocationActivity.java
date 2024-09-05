package com.solarapp;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import com.solarapp.utils.Constants2;
import com.solarapp.utils.FetchAddressIntentServices;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ShareLiveLocationActivity extends AppCompatActivity {

    String uri ;
    private GpsTracker gpsTracker;
    private TextView tvLatitude,tvLongitude,locationAddress;

    ImageView imageView;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
//    ProgressBar progressBar;
//    TextView textLatLong, address, postcode, locaity, state, district, country;
    ResultReceiver resultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_live_location);

        uri = getIntent().getStringExtra("cool");

        resultReceiver = new AddressResultReceiver(new Handler());



        tvLatitude = (TextView)findViewById(R.id.latitude);
        tvLongitude = (TextView)findViewById(R.id.longitude);
        locationAddress = (TextView)findViewById(R.id.locationAddress);



        imageView = findViewById(R.id.imageView);

        Picasso.get().load(Uri.fromFile(new File(uri))).into(imageView);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        gpsTracker = new GpsTracker(ShareLiveLocationActivity.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
//            tvLatitude.setText(String.valueOf(latitude));
//            tvLongitude.setText(String.valueOf(longitude));
//            locationAddress.setText(gpsTracker.getLocation_address());
        }else{
            gpsTracker.showSettingsAlert();
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ShareLiveLocationActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLocation();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission is denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {
//        progressBar.setVisibility(View.VISIBLE);
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(ShareLiveLocationActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(getApplicationContext())
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestlocIndex = locationResult.getLocations().size() - 1;
                            double lati = locationResult.getLocations().get(latestlocIndex).getLatitude();
                            double longi = locationResult.getLocations().get(latestlocIndex).getLongitude();
//                            textLatLong.setText(String.format("Latitude : %s\n Longitude: %s", lati, longi));

                            tvLatitude.setText(String.valueOf(lati));
                            tvLongitude.setText(String.valueOf(longi));
                            Location location = new Location("providerNA");
                            location.setLongitude(longi);
                            location.setLatitude(lati);
                            fetchaddressfromlocation(location);

                        } else {
//                            progressBar.setVisibility(View.GONE);

                        }
                    }
                }, Looper.getMainLooper());

    }

    private class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants2.SUCCESS_RESULT) {
                locationAddress.setText(resultData.getString(Constants2.ADDRESS)+" "+resultData.getString(Constants2.LOCAITY)
                        +" "+resultData.getString(Constants2.STATE)
                        +" "+resultData.getString(Constants2.DISTRICT)
                        +" "+resultData.getString(Constants2.COUNTRY)
                        +" "+resultData.getString(Constants2.POST_CODE)
                );
//                address.setText(resultData.getString(Constants2.ADDRESS));
//                locaity.setText(resultData.getString(Constants2.LOCAITY));
//                state.setText(resultData.getString(Constants2.STATE));
//                district.setText(resultData.getString(Constants2.DISTRICT));
//                country.setText(resultData.getString(Constants2.COUNTRY));
//                postcode.setText(resultData.getString(Constants2.POST_CODE));
            } else {
                Toast.makeText(ShareLiveLocationActivity.this, resultData.getString(Constants2.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
//            progressBar.setVisibility(View.GONE);
        }


    }

    private void fetchaddressfromlocation(Location location) {
        Intent intent = new Intent(this, FetchAddressIntentServices.class);
        intent.putExtra(Constants2.RECEVIER, resultReceiver);
        intent.putExtra(Constants2.LOCATION_DATA_EXTRA, location);
        startService(intent);


    }
}