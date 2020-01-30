package com.example.parkankaraandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class entranceActivity extends AppCompatActivity {
    //

    private AvailabilityChecker checker;
    ControllerMaster controllerMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
        controllerMaster = new ControllerMaster();


    }



    public void seeTheMap(View view)
    {
        controllerMaster.getCarParkManager().removeChosenPark();
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);
    }

    public void chooseLocation(View view){
        Intent intent = new Intent(getApplicationContext(), Locations.class);
        startActivity(intent);
    }

    public void favoriteCarParks(View view){
        Intent intent = new Intent(getApplicationContext(), FavoriteCarParks.class);
        startActivity(intent);
    }
}
