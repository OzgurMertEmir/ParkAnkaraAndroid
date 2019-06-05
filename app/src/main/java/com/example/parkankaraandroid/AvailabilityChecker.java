package com.example.parkankaraandroid;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class AvailabilityChecker extends Service {

    private static final String TAG = "AvailabilityChecker";
    private IBinder binder = new MyBinder();
    private Handler handler;
    static double checkLatitude;
    CarParkManager manager = new CarParkManager();

    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class MyBinder extends Binder{
        AvailabilityChecker getService(){
            return AvailabilityChecker.this;
        }
    }

    public void notEndingTask() {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {

                handler.postDelayed(this, 100);
            }
        };
    }

    @Override
    public void onDestroy(){
        if(! manager.isEmpty()){
            Toast.makeText(getApplicationContext(), "Park yeri doldu", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), Locations.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    }


    public  void onTaskRemoved(Intent rootIntent){
        super.onTaskRemoved(rootIntent);
        rootIntent = new Intent("com.android.ServiceStopped");
        sendBroadcast(rootIntent);
    }
}
