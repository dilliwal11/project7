package com.example.project7;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationReciever extends BroadcastReceiver {


    String channelID = "channel-011";
    String channelName = "channel";
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {


        Intent repeatingIntent = new Intent(context,MainActivity.class);

        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationChannel mChannel = null;

       /* if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
             mChannel = new NotificationChannel(
                    channelID, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }*/




       NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setContentIntent(pendingIntent)
                                                .setSmallIcon(R.drawable.ic_walking).setContentTitle("Notification titile")
                .setContentText("get up and walk").setChannelId(channelID).setPriority(NotificationCompat.PRIORITY_DEFAULT).setAutoCancel(true);
       // Log.d(TAG, "onReceive: re");



       NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(100,builder.build());


    }
}
