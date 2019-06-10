package com.example.parkankaraandroid;

import com.google.android.gms.maps.model.LatLng;
import java.util.HashMap;

public class CarPark{
    //
    //properties
    private String name;
    private String adress;
    private int carNumber;
    private int capacity;
    private double latitude;
    private double longtitude;
    private LatLng location;
    private int emptySpace;

    //constructor
    public CarPark(HashMap<String, String > properties){
        name = properties.get("name");
        carNumber = Integer.parseInt(properties.get("currentCars") );
        capacity = Integer.parseInt(properties.get("fullCapacity") );
        emptySpace = capacity - carNumber;
        latitude = Double.valueOf(properties.get("latitude"));
        longtitude = Double.valueOf(properties.get("longitude"));
        location = new LatLng(latitude, longtitude );
        adress = properties.get("address");
    }

    //methods of carparks
    public String getName(){
        return name;
    }

    public LatLng getLocation(){
        return location;
    }

    public int getCarNumber(){ return carNumber; }

    public int getEmptySpace(){
        return emptySpace;
    }

    public String getAdress(){
        return adress;
    }

    public int getCapacity(){
        return capacity;
    }

    public String getLatitude(){return String.valueOf(latitude);}

    public String getLongtitude(){return String.valueOf(longtitude);}
}
