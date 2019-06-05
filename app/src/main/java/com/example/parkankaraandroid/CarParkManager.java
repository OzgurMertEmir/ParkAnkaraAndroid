package com.example.parkankaraandroid;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class CarParkManager {

    //properties
    private DataAccess dataAccess;
    static CarPark chosenPark;
    private ArrayList<CarPark> carParks;


    //constructors
    public CarParkManager(){
        dataAccess = new DataAccess();
        carParks = dataAccess.getCarParks();
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
        if(getChosenPark().getEmptySpace() <= 0){
            return false;
        }
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
            return chosenPark.getAdress();
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
        return carParks;
    }


}
