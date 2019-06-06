package com.example.parkankaraandroid;


import java.util.ArrayList;

public class ControllerMaster{
//
    //properties
    static CarParkManager carParkManager = new CarParkManager();

    public ControllerMaster(){ }

    public CarParkManager getCarParkManager(){
        return carParkManager;
    }

    public ArrayList<CarPark> getCarParks(){
        return carParkManager.getCarParks();
    }

}
