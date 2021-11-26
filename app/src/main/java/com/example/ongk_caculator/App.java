package com.example.ongk_caculator;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String Chanel_1_ID="chanel1";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChanel1();
    }
    private void createNotificationChanel1(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1= new NotificationChannel(
                    Chanel_1_ID,
                    "chanel1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("HI HELLO");
            NotificationManager manager= getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
}
