package com.example.parkankaraandroid;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class AvailabilityChecker extends Service {

    private static final String TAG = "AvailabilityChecker";
    private IBinder binder = new MyBinder();
    private Handler handler;
    private boolean isPaused;

    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        isPaused = true;
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

    public  void onTaskRemoved(Intent rootIntent){
        super.onTaskRemoved(rootIntent);
        rootIntent = new Intent("com.android.ServiceStopped");
        sendBroadcast(rootIntent);
    }
}
