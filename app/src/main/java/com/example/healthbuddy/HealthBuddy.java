package com.example.healthbuddy;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class HealthBuddy extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        startForeground();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startForeground() {
        Intent notificationIntent = new Intent(HealthBuddy.this, SignInActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(HealthBuddy.this, 0,notificationIntent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(Globals.BACKGROUND_ID, new NotificationCompat.Builder(HealthBuddy.this,Globals.channel.getId())
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.icon_success)
                    .setContentTitle("Health Buddy is Active")
                    .setContentText("Health Buddy is Running in Background")
                    .setContentIntent(pendingIntent)
                    .build());
        }
    }
}
