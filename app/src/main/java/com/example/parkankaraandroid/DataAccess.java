package com.example.parkankaraandroid;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class DataAccess {
    //
    private Timer timer;
    private TimerTask timerTask;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private ArrayList<CarPark> carParks;

    public DataAccess() {
        carParks = new ArrayList<CarPark>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("CarPark");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                carParks.clear();
                for ( DataSnapshot ds: dataSnapshot.getChildren()) {
                    try {
                        HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                        CarPark carPark = new CarPark(hashMap);
                        carParks.add(carPark);
                        Log.d(TAG, "onDataChange: Entered the first listener!!!!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        checkDatabase();

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

    public void checkDatabase(){
        timer = new Timer(true);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                databaseReference.addValueEventListener(new ValueEventListener() {
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
        timer.schedule(timerTask,10000);
    }
}