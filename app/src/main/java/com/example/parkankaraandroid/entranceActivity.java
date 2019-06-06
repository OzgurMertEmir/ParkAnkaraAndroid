package com.example.parkankaraandroid;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class entranceActivity extends AppCompatActivity {

    private AvailabilityChecker checker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);


    }



    protected void seeTheMap(View view)
    {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);
    }

    protected void chooseLocation(View view){
        Intent intent = new Intent(getApplicationContext(), Locations.class);
        startActivity(intent);
    }

    protected void favoriteCarParks(View view){
        Intent intent = new Intent(getApplicationContext(), FavoriteCarParks.class);
        startActivity(intent);
    }
}
