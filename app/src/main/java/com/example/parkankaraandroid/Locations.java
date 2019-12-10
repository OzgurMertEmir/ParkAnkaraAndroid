package com.example.parkankaraandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class Locations extends AppCompatActivity {
    //constants
    private static final String TAG = "Locations";

    //properties
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    ControllerMaster controllerMaster;
    ArrayList<String> cpName;
    ArrayList<String> cpAddress;
    ArrayList<String> cpCondition;
    ArrayList<CarPark> carParks;
    ListView listView;
    LocationsPostClass adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = findViewById(R.id.listView);
        controllerMaster = new ControllerMaster();
        carParks = controllerMaster.getCarParks();
        cpName = new ArrayList<>();
        cpAddress = new ArrayList<>();
        cpCondition = new ArrayList<>();
        for(CarPark carPark: carParks){
            cpName.add(carPark.getName());
            cpAddress.add(carPark.getAddress());
            cpCondition.add(String.valueOf(carPark.getEmptySpace()));
        }


        adapter = new LocationsPostClass(cpName, cpAddress, cpCondition, this);

        listView.setAdapter(adapter);

        controllerMaster.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                try {
                    cpName.clear();
                    cpAddress.clear();
                    cpCondition.clear();

                    Log.d(TAG, "propertyChange: CARPARKS UPDATED");

                    carParks = controllerMaster.getCarParks();

                    for(CarPark carPark : carParks){
                        Log.d(TAG, "propertyChange: " + carPark.getName() + ": " + carPark.getEmptySpace());
                        cpName.add(carPark.getName());
                        cpAddress.add(carPark.getAddress());
                        cpCondition.add(String.valueOf(carPark.getEmptySpace()));
                        adapter.notifyDataSetChanged();
                    }

                }catch( Exception e ){
                    e.printStackTrace();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                controllerMaster.getCarParkManager().chooseCarPark(cpName.get(position));
                //startService();
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startService() {
        Intent serviceIntent = new Intent(this, AvailabilityChecker.class);
        startService(serviceIntent);
    }

}


