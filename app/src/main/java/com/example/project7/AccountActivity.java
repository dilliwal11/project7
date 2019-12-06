package com.example.project7;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.DetectedActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AccountActivity extends AppCompatActivity {
    private Intent mIntentService;
    private PendingIntent mPendingIntent;
    private ActivityRecognitionClient mActivityRecognitionClient;
    private String TAG = MainActivity.class.getSimpleName();
    BroadcastReceiver broadcastReceiver;
    int temp = -1;long start = 0;long lastStepsRedorded;
    DatabaseHelper databaseHelper;
    private TextView txtActivity, txtConfidence;
    private ImageView imgActivity;
    TextView textView;
    Context c;String formatDate,formatTime;
    SensorManager sensorManager;
    SimpleDateFormat date;
    SimpleDateFormat time;
    private long steps = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);


        databaseHelper = new DatabaseHelper(this);
        txtActivity = findViewById(R.id.txt_activity);
        txtConfidence = findViewById(R.id.txt_confidence);
        imgActivity = findViewById(R.id.img_activity);
        toastMessage(String.valueOf(lastStepsRedorded));
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        textView = (TextView)findViewById(R.id.textView);


        // Broadcast reciever will be invoked when service intent will get executed

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int temp = -1;
                if (intent.getAction().equals(Constants.BROADCAST_DETECTED_ACTIVITY)) {
                    int type = intent.getIntExtra("type", -1);
                    int confidence = intent.getIntExtra("confidence", 0);
                    temp = type;
                    if(temp!=-1){
                        long start = System.currentTimeMillis();
                        temp = -1;
                    }
                    handleUserActivity(type, confidence);
                }
            }
        };

       // startTracking();






    }


    void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }





    public void notificationGenerate(View view) {
        Intent intent = new Intent(AccountActivity.this,Notification.class);
        startActivity(intent);



    }

    private void handleUserActivity(int type, int confidence) {

        date = new SimpleDateFormat("dd:MM:yyyy");
        time = new SimpleDateFormat("hh:mm:ss");
        formatDate = date.format(new Date());
        formatTime = time.format(new Date());
        String label="";
        int icon = R.drawable.ic_still;
        int count = 0;
        Log.d(TAG, "handleUserActivity: type "+type);
        switch (type) {
            case DetectedActivity.STILL: {
                temp = DetectedActivity.STILL;
                label = "still";
                Log.d(TAG, "handleUserActivity: "+count);
                break;
            }
            case DetectedActivity.WALKING: {
                temp = DetectedActivity.WALKING;
                label = "walking";
                icon = R.drawable.ic_walking;
                break;
            }
        }
        if (confidence > 85) {

            if(temp == DetectedActivity.WALKING){           // walking time
                start = System.currentTimeMillis() - start;
            }
            else if(temp==DetectedActivity.STILL)  {
                Log.d(TAG, "handleUserActivity temp: "+temp);
                int seconds= (int) ((start/1000)%60);
                long distance =  (steps*2);         // Considered step size to be 2 ft
                textView.setText(("distance: "+String.valueOf(distance)+" time: "+ seconds +" sec"));
                if(databaseHelper.check(formatDate)==false){
                    if(distance>0) { // whenever the user is still, the database is updated locally.
                        boolean db = databaseHelper.addData(String.valueOf(distance),formatDate,formatTime);
                        if(db){ // distance walked at specific date and time will be stored in sqlite database
                            Log.d(TAG, "handleUserActivity: added to database" +distance);
                        }
                    }
                }
                else{
                    databaseHelper.updateTodaysValue(formatDate, String.valueOf(distance),formatTime);
                    // updation of distance for particular day.
                    Log.d(TAG, "handleUserActivity: updated"+distance);

                }
            }
            txtActivity.setText(label);
            txtConfidence.setText("Confidence: " + confidence);
            imgActivity.setImageResource(icon);
        }
    }
}
