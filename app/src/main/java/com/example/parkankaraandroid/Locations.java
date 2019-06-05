package com.example.parkankaraandroid;

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
    FirebaseDatabase fbDatabase;
    DatabaseReference myRef;
    ArrayList<String> cpCondition;
    ArrayList<String> cpName;
    ArrayList<String> cpAddress;
    ListView listView;
    LocationsPostClass adapter;
    ArrayList<String> latitude;
    ArrayList<String> longitude;
    public static Double staticLat;
    public static Double staticLng;
    public static String staticName;
    public static boolean isFull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = findViewById(R.id.listView);
        cpCondition = new ArrayList();
        cpAddress = new ArrayList();
        cpName = new ArrayList<>();
        latitude = new ArrayList<>();
        longitude = new ArrayList<>();
        staticLat = null;
        staticLng = null;
        staticName = null;

        fbDatabase = FirebaseDatabase.getInstance();
        myRef = fbDatabase.getReference();

        getDataFromFirebase();

        adapter = new LocationsPostClass(cpName, cpAddress, cpCondition, this);

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                staticLat = Double.parseDouble(latitude.get(position));
                staticLng = Double.parseDouble(longitude.get(position));
                if(Integer.parseInt(cpCondition.get(position) ) > 0){
                    isFull = false;
                }
                else{
                    isFull = true;
                }
                AvailabilityChecker.checkLatitude = Double.parseDouble(latitude.get(position));

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getDataFromFirebase(){
        DatabaseReference newReference = fbDatabase.getReference("CarPark");
        newReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cpName.clear();
                cpCondition.clear();
                latitude.clear();
                longitude.clear();
                cpAddress.clear();
                for( DataSnapshot ds : dataSnapshot.getChildren() )
                {
                    HashMap<String, String> hashMap = ( HashMap<String, String> ) ds.getValue();
                    cpName.add(hashMap.get("name"));

                    cpCondition.add( String.valueOf ( Integer.parseInt(hashMap.get("fullCapacity") ) - Integer.parseInt(hashMap.get("currentCars") ) ) );

                    latitude.add( hashMap.get("latitude") );
                    longitude.add( hashMap.get("longitude") );

                    cpAddress.add(hashMap.get("address"));

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
