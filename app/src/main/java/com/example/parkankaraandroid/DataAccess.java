package com.example.parkankaraandroid;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class DataAccess {
    Timer timer;
    TimerTask timerTask;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    ArrayList<CarPark> carParks;

    public DataAccess() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("CarPark");
        timer = new Timer();
        carParks = new ArrayList<CarPark>();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        carParks.clear();
                        for ( DataSnapshot ds: dataSnapshot.getChildren()) {
                            try {
                                HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                                CarPark carPark = new CarPark(hashMap);
                                carParks.add(carPark);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                printArrayList();
            }
        };
        timer.schedule(timerTask, 0, 10000);
    }

    public void printArrayList()
    {
        for( CarPark cp : carParks){
            System.out.println(cp.getName());
        }
    }

    public ArrayList getCarParks(){
        return carParks;
    }
}