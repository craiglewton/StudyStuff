package com.example.craiglewton.memorableplaces;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String LOG_TAG = "MEMORABLE.MapsActivity";

    private GoogleMap mMap;

    //private ArrayList<Place> placesList;
    private ArrayList<String> places;

    private Intent resultIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //ActionBar actionBar = getSupportA
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int loc = getIntent().getIntExtra(MainActivity.EXTRA_LOCATION, -1);
        ArrayList<String> locs = getIntent().getStringArrayListExtra(MainActivity.EXTRA_LOCATION_ARRAY);

        //places = locs!=null ? locs : new ArrayList<String>();
        places = locs;

        resultIntent = new Intent();
        resultIntent.putExtra(MainActivity.EXTRA_LOCATION_ARRAY, places);
        setResult(MainActivity.LOCATIONS_UPDATE, resultIntent);

        Log.i(LOG_TAG, loc+" : "+locs.toString()+" @ "+places.toString());
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker( new MarkerOptions().position(sydney).title("Marker in Sydney") );
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setOnMapLongClickListener(
                new GoogleMap.OnMapLongClickListener() {

                    @Override
                    public void onMapLongClick(LatLng latLng) {

                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                        String label = latLng.toString();
                        try {
                            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                            if(addresses!=null && addresses.size()>0){
                                label = addresses.get(0).getAddressLine(0).toString();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        mMap.addMarker( new MarkerOptions().position(latLng).title(label) );
                        places.add(label);

                        Log.i(LOG_TAG, "places update to... "+places.toString());
                        //Log.i(LOG_TAG, "intent update to... "+resultIntent.getStringArrayListExtra(MainActivity.EXTRA_LOCATION_ARRAY));
                    }

                }
        );
    }
}
