package com.example.yuichi.fgservicesample;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class SampleService extends Service {
    private final String TAG = "SampleService";

    private final String CHANNEL_ID = "idSampleServiceNotification";


    private final int TIMES_SERVICE_LOOP = 15000;
    private final int DURATION_PROCESS = 5 * 1000;

    private Thread serviceThread;


    /*
    通知チャンネルの対応
     */
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "SampleServiceNotification", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(getString(R.string.ServiceNotificationDescription));

            getSystemService(NotificationManager.class).createNotificationChannel(notificationChannel);
        }
    }

    private void destroyNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getSystemService(NotificationManager.class).deleteNotificationChannel(CHANNEL_ID);
        }
    }

    private Notification getNotificationForStartForeground(){
        PendingIntent pending = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentIntent(pending)
                .setSmallIcon(R.drawable.icon_sample_service_notification)
                .setTicker(getString(R.string.ServiceNotificationTickerText))
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.ServiceNotificationTitle))
                .setContentText(getString(R.string.ServiceNotificationContext))
                .build();
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        return notification;
    }

    @Override
    public void onCreate(){
        Log.d(TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d(TAG, "onCStartCommand()");
        if(serviceThread == null) {

            createNotificationChannel();

            startForeground(startId, getNotificationForStartForeground());

            serviceThread = new Thread(new SampleServiceThread(intent));
            serviceThread.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        Log.d(TAG, "onDestroy()");
        serviceThread.interrupt();
        serviceThread = null;

        stopForeground(true);
        destroyNotificationChannel();
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }


    class SampleServiceThread implements Runnable{
        private Intent targetIntent;

        private SampleServiceThread(Intent intent){
            targetIntent = intent;
        }

        @Override
        public void run(){
            Log.d(TAG, "Runnuble started.");
            try {
                for (int i = 0; i < TIMES_SERVICE_LOOP && !Thread.interrupted(); i++) {
                    Log.d(TAG, "running index = " + i);
                    Thread.sleep(DURATION_PROCESS);
                }
            }
            catch(InterruptedException e){
                Log.d(TAG, "interuppted");
            }
            stopService(targetIntent);
            Log.d(TAG, "Runnuble exit.");
        }
    }
}
