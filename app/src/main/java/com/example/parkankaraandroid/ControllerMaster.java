package com.example.parkankaraandroid;


import android.util.Log;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class ControllerMaster{
    //constants
    private final static String TAG = "ControllerMaster";

    //properties
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    static CarParkManager carParkManager = new CarParkManager();
    private ArrayList<CarPark> carParks;

    public ControllerMaster(){
        carParks = carParkManager.getCarParks();
        carParkManager.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                ArrayList<CarPark> oldCarParks = carParks;
                carParks = carParkManager.getCarParks();
                Log.d(TAG, "propertyChange: " + evt.getPropertyName() + " Old Value: " +evt.getOldValue() + "New Value: " + evt.getNewValue());
                pcs.firePropertyChange(evt);
            }
        });
    }

    public CarParkManager getCarParkManager(){
        return carParkManager;
    }

    public ArrayList<CarPark> getCarParks(){
        Log.d(TAG, "getCarParks: ENTERED AND SENT CARPARKS TO LOCATIONS");
        return carParks;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener){
        this.pcs.addPropertyChangeListener(listener);
    }
}
