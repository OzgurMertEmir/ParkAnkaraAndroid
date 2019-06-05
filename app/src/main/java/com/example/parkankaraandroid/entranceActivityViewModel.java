package com.example.parkankaraandroid;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class entranceActivityViewModel extends ViewModel {

    private static final String TAG = "entranceActivityViewMod";

    private MutableLiveData<AvailabilityChecker.MyBinder> binder = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: connected to service");
            AvailabilityChecker.MyBinder myBinder  = (AvailabilityChecker.MyBinder) service ;
            binder.postValue(myBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binder.postValue(null);
        }
    };


    public LiveData<AvailabilityChecker.MyBinder> getBinder(){
          return binder;
    }

    public LiveData<Boolean> isUpdating(){
        return isUpdating;
    }
}
