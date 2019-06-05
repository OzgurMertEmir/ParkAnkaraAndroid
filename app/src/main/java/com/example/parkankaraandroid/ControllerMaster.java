package com.example.parkankaraandroid;

import java.util.ArrayList;

public class ControllerMaster {

    //properties
    CarParkManager carParkManager;

    public ControllerMaster(){
        carParkManager = new CarParkManager();
    }

    public CarParkManager getCarParkManager(){
        return carParkManager;
    }

    public ArrayList<CarPark> getCarParks(){
        return carParkManager.getCarParks();
    }
}
