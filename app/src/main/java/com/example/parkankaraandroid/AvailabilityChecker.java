package com.example.parkankaraandroid;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Timer;

public class AvailabilityChecker extends Service {

    //constants

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    //properties
    Timer timer;
    CarParkManager carParkManager;

    //methods
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
