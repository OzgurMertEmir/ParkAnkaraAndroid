package com.example.parkankaraandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FavoriteCarParks extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Set<String> favorites;
    ArrayList<String> cpCondition;
    ArrayList<String> cpName;
    ArrayList<String> cpAddress;
    ArrayList<String> latitude;
    ArrayList<String> longitude;
    ListView favoritesListView;
    FavoritesPostClass adapter;
    ControllerMaster controllerMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_car_parks);
        Toolbar toolbar = findViewById(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //find listView
        favoritesListView = findViewById(R.id.favoritesListView);

        //Constructors for ArrayLists
        cpName = new ArrayList<>();
        cpCondition = new ArrayList<>();
        cpAddress = new ArrayList<>();
        latitude = new ArrayList<>();
        longitude = new ArrayList<>();

        //ControllerMaster
        controllerMaster = new ControllerMaster();

        //Finds Shared Preferences
        sharedPreferences = getSharedPreferences("com.example.parkankara", Context.MODE_PRIVATE);
        favorites = new HashSet<>( sharedPreferences.getStringSet("CarParkName", new HashSet<String>()) );

        //Connecting to adapter
        adapter = new FavoritesPostClass(cpName, cpAddress, cpCondition, this);
        favoritesListView.setAdapter(adapter);

        getFavoritesFromMaster();

        favoritesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                controllerMaster.getCarParkManager().chooseCarPark(cpName.get(position));
                startService();
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getFavoritesFromMaster(){

        for(CarPark carPark : controllerMaster.getCarParks() ){
            if( favorites.contains(carPark.getName() ) )
            {
                cpName.add( carPark.getName() );
                cpAddress.add( carPark.getAddress() );
                cpCondition.add( String.valueOf( carPark.getEmptySpace() ) );
                latitude.add( carPark.getLatitude());
                longitude.add(carPark.getLongtitude());
                adapter.notifyDataSetChanged();
            }
        }

    }

    private void startService(){
        Intent serviceIntent = new Intent(this, AvailabilityChecker.class);
        startService(serviceIntent);
    }


}

