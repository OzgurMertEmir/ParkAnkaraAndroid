package com.example.parkankaraandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class FavoriteCarParks extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Set<String> favorites;
    ArrayList<String> cpCondition;
    ArrayList<String> cpName;
    ArrayList<String> cpAddress;
    ArrayList<String> latitude;
    ArrayList<String> longitude;
    ListView favoritesListView;
    static FavoritesPostClass adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_car_parks);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //find listView
        favoritesListView = findViewById(R.id.favoritesListView);

        //Constructors for ArrayLists
        cpName = new ArrayList<>();
        cpCondition = new ArrayList<>();
        cpAddress = new ArrayList<>();
        latitude = new ArrayList<>();
        longitude = new ArrayList<>();

        //Connect to Database
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("CarPark");

        //Finds Shared Preferences
        sharedPreferences = getSharedPreferences("com.example.parkankara", Context.MODE_PRIVATE);
        //sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        favorites = new HashSet<>( sharedPreferences.getStringSet("CarParkName", new HashSet<String>()) );
        getFavoritesFromDatabase();

        //Connecting to adapter
        adapter = new FavoritesPostClass(cpName, cpAddress, cpCondition, this);
        favoritesListView.setAdapter(adapter);

        favoritesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Locations.staticLat = Double.parseDouble(latitude.get(position));
                Locations.staticLng = Double.parseDouble(longitude.get(position));
                startService(new Intent(getApplicationContext(), AvailabilityChecker.class) );

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getFavoritesFromDatabase(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren() ){
                    HashMap<String, String> hashMap = ( HashMap<String, String> ) ds.getValue();
                    if( favorites.contains(hashMap.get("name") ) )
                    {
                        cpName.add( hashMap.get("name") );
                        cpAddress.add( hashMap.get("address") );
                        cpCondition.add( String.valueOf( Integer.parseInt(hashMap.get("fullCapacity") ) - Integer.parseInt(hashMap.get("currentCars") ) ) );
                        latitude.add( hashMap.get("latitude") );
                        longitude.add(hashMap.get("longitude"));
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
