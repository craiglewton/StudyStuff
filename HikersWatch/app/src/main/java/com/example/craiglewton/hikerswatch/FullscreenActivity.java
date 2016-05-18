package com.example.craiglewton.hikerswatch;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity implements LocationListener {

    private static final String LOG_TAG = "HikersWatch";

    private static final int REQUEST_LOCATION_CODE = 0;

    private LocationManager locationManager;
    private String provider;

    private TextView labelsText;
    private TextView detailsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        labelsText = (TextView) findViewById(R.id.locationLabels);
        detailsText = (TextView) findViewById(R.id.locationDetails);


    }

    @Override
    protected void onResume() {
        Log.i(LOG_TAG, "onResume...");
        super.onResume();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            Log.i(LOG_TAG, "onResume: missing permissions");
        } else {
            Log.i(LOG_TAG, "onResume: locationManager.requestLocationUpdates...");

            Location location = locationManager.getLastKnownLocation(provider);
            if(location!=null){
                displayLocationDetails(location);
            } else {
                Log.e(LOG_TAG, "onResume: THE LAST KNOWN LOCATION IS NULL");
            }
            locationManager.requestLocationUpdates(provider, 400, 1, this);
        }
    }

    private void displayLocationDetails(Location location) {
        double lng = location.getLongitude();
        double lat = location.getLatitude();
        Log.i(LOG_TAG, "displayLocationDetails: WOW IT WORKED :) " + lng + " : " + lat);

        ArrayList<String> titles = new ArrayList<String>();
        titles.add("Latitude");
        titles.add("Longitude");
        titles.add("Accuracy");
        titles.add("Altitude");
        titles.add("Speed");
        titles.add("Bearing");
        titles.add("Address");


        String address = "no address found";
        Geocoder geocoder = new Geocoder(getApplicationContext(),Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);

            StringBuilder addressBuilder = new StringBuilder();

            for(int i=0; i <= addresses.get(0).getMaxAddressLineIndex(); i++){
                addressBuilder.append( addresses.get(0).getAddressLine(i).replace(",","\n") + "\n" );
            }

            //address = addresses.get(0).toString();
            address = addressBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }


        ArrayList<String> values = new ArrayList();
        //values.add( String.format("%.2f", lat));
        values.add( String.valueOf(lat) +"\u00B0" );
        values.add( String.valueOf(lng) +"\u00B0" );
        values.add( String.valueOf(location.getAccuracy()) + " m");
        values.add( String.valueOf(location.getAltitude()) +" m");
        values.add( String.valueOf(location.getSpeed()) +" m/s");
        values.add( String.valueOf(location.getBearing()) );
        values.add( address );

        StringBuilder labelsBuilder = new StringBuilder();
        for( String title : titles ) {
            labelsBuilder.append(title + ":\n");
        }
        labelsText.setText(labelsBuilder.toString());

        StringBuilder detailsBuilder = new StringBuilder();
        for( String value : values ) {
            detailsBuilder.append(value + "\n");
        }
        detailsText.setText(detailsBuilder.toString());

    }


    @Override
    public void onLocationChanged(Location location) {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        Log.i(LOG_TAG, "onLocationChanged");

        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if(addresses!=null && addresses.size()>0){
                Log.i(LOG_TAG, addresses.get(0).toString());
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }

        displayLocationDetails(location);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    protected void onPause() {

        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(LOG_TAG, "onPause: missing permissions");
            return;
        }
        Log.i(LOG_TAG, "onPause: locationManager.removeUpdates");
        locationManager.removeUpdates(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.i(LOG_TAG, "AM I EVEN GETTING HERE?");

        if(requestCode==REQUEST_LOCATION_CODE) {

//            String result = grantResults[0]+" : " + grantResults[1];
//            Log.i(LOG_TAG, "onRequestPermissionsResult = "+result);
            for(int item : grantResults) {
                Log.i(LOG_TAG, "PERMISSION GRANTED = " + (item==PackageManager.PERMISSION_GRANTED) );
                if( item==PackageManager.PERMISSION_GRANTED ) {
                    Log.i(LOG_TAG, "onRequestPermissionsResult: locationManager.requestLocationUpdates...");
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    provider = locationManager.getBestProvider(new Criteria(), false);
                    //noinspection MissingPermission - duh, obviously don't need another permission check
                    Location location = locationManager.getLastKnownLocation(provider);
                    if(location!=null){
                        displayLocationDetails(location);
                    } else {
                        Log.e(LOG_TAG, "onRequestPermissionsResult: THE LAST KNOWN LOCATION IS NULL");
                    }
                    //noinspection MissingPermission - umm, obviously don't need another permission check
                    locationManager.requestLocationUpdates(provider, 400, 1, this);
                }
            }
        }
    }

}