package com.example.parkankaraandroid;


import android.util.Log;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class ControllerMaster{
//
    //properties
    static CarParkManager carParkManager = new CarParkManager();

    public ControllerMaster(){ }

    public CarParkManager getCarParkManager(){
        return carParkManager;
    }

    public ArrayList<CarPark> getCarParks(){
        Log.d(TAG, "getCarParks: ENTERED AND SENT CARPARKS TO LOCATIONS");
        return carParkManager.getCarParks();
    }
}
