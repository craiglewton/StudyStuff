package com.example.craiglewton.memorableplaces;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MEMORABLE.MainActivity";

    public static final int LOCATIONS_UPDATE = 0;
    public static final String EXTRA_LOCATION = "location";
    public static final String EXTRA_LOCATION_ARRAY = "locationarray";

    //private ArrayList<String> places; // java tip: can declare here or as final var in constructor for reference of inner classes within the constructor

    private ArrayList<String> places;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);

        places = new ArrayList<String>();
        //final ArrayList<String> places = new ArrayList<String>(); // java tip: can make final here or have as instance variable so inner classes declared inside the constructor can reference
        places.add("Add new place...");

        // note to self: ListAdapter can only take a single string type item layout
        // you can set to a single text view (not inside any layout) using:
        //      ArrayAdapter adapter = new ArrayAdapter(this, R.id.listTextView, places);
        // but I wanted to wrap in a layout for fun so use the adapter below.
        // For more interesting list item layouts then create a custom adapter by extending BaseAdapter and using layou inflate etc.


        // REMEMBER THE CONTEXT IS "this" NOT "getApplicationContext()" - "this" refers to the Activity
        //ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.list_item, R.id.listTextView, places); // WRONG!
        adapter = new ArrayAdapter(this, R.layout.list_item, R.id.listTextView, places); // works

        listView.setAdapter(adapter);

        // LEARN THIS BECAUSE YOUR FIRST GUESS WAS WRONG!
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Log.i(LOG_TAG, places.get(position) );
                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        intent.putExtra(EXTRA_LOCATION, position);
                        intent.putExtra(EXTRA_LOCATION_ARRAY, places);
                        //startActivity(intent);
                        startActivityForResult(intent, LOCATIONS_UPDATE);
                    }
                }
        );


        /* WRONG WRONG WRONG!
        listView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView tv = (TextView) v.findViewById(R.id.listTextView);
                        Log.i(LOG_TAG, tv.getText().toString() );
                    }
                }
        );
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ArrayList<String> placesResult = data.getStringArrayListExtra(EXTRA_LOCATION_ARRAY);

        places.clear();
        for(String item : placesResult) {
            places.add(item);
        }
        adapter.notifyDataSetChanged();

        Log.i(LOG_TAG, "RESULT places = " + placesResult.toString());
    }
}
