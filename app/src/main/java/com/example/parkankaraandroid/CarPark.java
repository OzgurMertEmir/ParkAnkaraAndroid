package com.example.parkankaraandroid;

import com.google.android.gms.maps.model.LatLng;
import java.util.HashMap;

public class CarPark{

    //properties
    String name;
    String adress;
    int carNumber;
    int capacity;
    double latitude;
    double longtitude;
    LatLng location;
    int emptySpace;

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

    public String getCarNumber(){
        return String.valueOf(carNumber);
    }

    public int getEmptySpace(){
        return emptySpace;
    }

    public String getAdress(){
        return adress;
    }

    public int getCapacity(){
        return capacity;
    }
}
