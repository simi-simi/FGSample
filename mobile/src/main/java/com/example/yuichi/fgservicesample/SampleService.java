package com.example.yuichi.fgservicesample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SampleService extends Service {
    private final String TAG = "SampleService";

    @Override
    public void onCreate(){
        Log.d(TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d(TAG, "onCStartCommand()");
        return START_STICKY;
    }
    @Override
    public void onDestroy(){
        Log.d(TAG, "onDestroy()");
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
