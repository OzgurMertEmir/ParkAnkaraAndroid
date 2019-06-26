package com.example.parkankaraandroid;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;

public class CarPark{
    //constants
    private final static String TAG = "CarPark";

    //properties
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private String name;
    private String address;
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
        address = properties.get("address");
    }

    //methods of carparks
    public String getName(){
        return name;
    }

    public void setName(String name){ this.name = name; }

    public LatLng getLocation(){
        return location;
    }

    public void setLocation(LatLng latLng){ location = latLng; }

    public int getCarNumber(){ return carNumber; }

    public void setCarNumber(int carNumber){ this.carNumber = carNumber; }

    public int getEmptySpace(){
        return emptySpace;
    }

    public void setEmptySpace(int emptySpace){
        int oldValue = this.emptySpace;
        this.emptySpace = emptySpace;
        Log.d(TAG, "setEmptySpace: SET NEW EMPTY SPACE FOR CARPARK");
        propertyChangeSupport.firePropertyChange("emptySpace", oldValue, emptySpace);
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String adress){ this.address = adress; }

    public int getCapacity(){
        return capacity;
    }

    public void setCapacity(int capacity){ this.capacity = capacity; }

    public String getLatitude(){return String.valueOf(latitude);}

    public void setLatitude(double latitude){ this.latitude = latitude; }

    public String getLongtitude(){return String.valueOf(longtitude);}

    public void setLongtitude(double longtitude){ this.longtitude = longtitude; }

    public void addPropertyChangeListener(PropertyChangeListener listener){
        this.propertyChangeSupport.addPropertyChangeListener("emptySpace", listener);
    }
}
