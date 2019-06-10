package com.example.parkankaraandroid;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class AvailabilityChecker extends Service {
    //
    private static final String TAG = "AvailabilityChecker";
    private CarParkManager manager = new CarParkManager();
    private Timer timer;
    private TimerTask timerTask;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                try{
                    if( !manager.isEmpty() ){
                        Log.d(TAG, "onStartCommand: Service is going to stop");
                        manager.removeChosenPark();
                        stopSelf();
                    }
                }catch(Exception e){
                    System.out.println(e);
                }
            }


        };
        timer.schedule(timerTask, 0, 10000);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onDestroy(){
        try{
            timer.cancel();
            Intent intent = new Intent(getApplicationContext(), Locations.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Park yeri doldu!!!", Toast.LENGTH_LONG).show();
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
