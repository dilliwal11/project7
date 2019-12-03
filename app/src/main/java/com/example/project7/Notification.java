package com.example.project7;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Notification extends AppCompatActivity {



TimePicker timePicker;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        timePicker = findViewById(R.id.timepicker1);
        button = findViewById(R.id.notiButton);


// At an interval of every hour the user will be notified to walk.

    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY,timePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE,timePicker.getCurrentMinute());
            calendar.set(Calendar.SECOND,0);
            Intent intent = new Intent(getApplicationContext(), NotificationReciever.class);
            intent.setAction("MY_NOTIFICATION_MESSAGEs");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), AlarmManager.INTERVAL_HOUR,pendingIntent);


            Log.d("kkkk", "onClick: "+calendar.getTimeInMillis());


        }
    });


    }
}
