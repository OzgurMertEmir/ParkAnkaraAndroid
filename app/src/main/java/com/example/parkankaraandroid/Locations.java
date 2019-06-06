package com.example.parkankaraandroid;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Locations extends AppCompatActivity {
    //
    ControllerMaster controllerMaster;
    ArrayList<String> cpCondition;
    ArrayList<String> cpName;
    ArrayList<String> cpAddress;
    ListView listView;
    LocationsPostClass adapter;
    ArrayList<String> latitude;
    ArrayList<String> longitude;
    private entranceActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = findViewById(R.id.listView);
        controllerMaster = new ControllerMaster();
        cpCondition = new ArrayList<>();
        cpAddress = new ArrayList<>();
        cpName = new ArrayList<>();
        latitude = new ArrayList<>();
        longitude = new ArrayList<>();
        viewModel = ViewModelProviders.of(this).get(entranceActivityViewModel.class);


        adapter = new LocationsPostClass(cpName, cpAddress, cpCondition, this);

        getDataFromMaster();

        listView.setAdapter(adapter);




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                controllerMaster.getCarParkManager().chooseCarPark(cpName.get(position));
                startService();
                bindService();

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getDataFromMaster(){
        cpName.clear();
        cpCondition.clear();
        latitude.clear();
        longitude.clear();
        cpAddress.clear();

        for( CarPark carPark : controllerMaster.getCarParks() )
        {
            cpName.add(carPark.getName());

            cpCondition.add( String.valueOf ( carPark.getEmptySpace() ) );

            latitude.add( carPark.getLatitude() );
            longitude.add( carPark.getLongtitude() );

            cpAddress.add(carPark.getAdress());


        }
        adapter.notifyDataSetChanged();
    }

    private void startService(){
        Intent serviceIntent = new Intent(this, AvailabilityChecker.class);
        startService(serviceIntent);
        bindService();
    }

    private void bindService(){
        Intent serviceIntent = new Intent(this, AvailabilityChecker.class);
        bindService(serviceIntent, viewModel.getServiceConnection(), Context.BIND_AUTO_CREATE);
    }
}

