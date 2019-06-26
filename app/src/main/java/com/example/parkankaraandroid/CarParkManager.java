package com.example.parkankaraandroid;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;


public class CarParkManager {
    //constants
    private final static String TAG = "CarParkManager";

    //properties
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private DataAccess dataAccess;
    private static CarPark chosenPark;
    private ArrayList<CarPark> carParks;


    //constructors
    public CarParkManager(){
        dataAccess = new DataAccess();
        carParks = (ArrayList<CarPark>) dataAccess.getCarParks();
        dataAccess.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                ArrayList<CarPark> oldCarParks = carParks;
                Log.d(TAG, "propertyChange: "+ evt.getPropertyName() + " Old Value: " +evt.getOldValue() + "New Value: " + evt.getNewValue());
                carParks = dataAccess.getCarParks();
                try {
                    pcs.firePropertyChange(evt);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        chosenPark = null;
    }

    //methods
    public CarPark chooseCarPark(String s){
        for(CarPark carPark : carParks){
            if(carPark.getName().equalsIgnoreCase(s)){
                chosenPark = carPark;
            }
        }
        return chosenPark;
    }

    public boolean isEmpty(){
        if(chosenPark != null) {
            if (getChosenParkEmptySpace() <= 0) {
                return false;
            }
            return true;
        }else
            return false;
    }

    public CarPark getChosenPark(){
        return chosenPark;
    }

    public String getChosenParkName(){
        if(chosenPark != null){
            return chosenPark.getName();
        }else {
            return "No Car Park chosen";
        }
    }

    public String getChosenParkAddress(){
        if (chosenPark != null){
            return chosenPark.getAddress();
        }else{
            return "No Car Park chosen";
        }
    }

    public LatLng getChosenParkLocation(){
        if (chosenPark != null){
            return chosenPark.getLocation();
        }else{
            return new LatLng(0,0);
        }
    }

    public int getChosenParkCapacity(){
        if (chosenPark != null){
            return chosenPark.getCapacity();
        }else{
            return -1;
        }
    }

    public int getChosenParkEmptySpace(){
        if (chosenPark != null){
            return chosenPark.getEmptySpace();
        }else{
            return -1;
        }
    }

    public int getChosenParkCarCount(){
        if(chosenPark != null){
            return chosenPark.getCarNumber();
        }else{
            return -1;
        }
    }

    public ArrayList<CarPark> getCarParks(){
        Log.d(TAG, "getCarParks: Method entered!!!!!");
        for(CarPark carPark : carParks){
            System.out.println( "---------------------------------------------------------------------" + carPark.getName() + "--------------------------------------------------------------------------------");
        }
        return carParks;
    }

    public void removeChosenPark(){
        chosenPark = null;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener){
        this.pcs.addPropertyChangeListener(listener);
    }

}
