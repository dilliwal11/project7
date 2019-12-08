package com.example.project7;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AccountActivity extends AppCompatActivity implements SensorEventListener {



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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.account_activity);
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

        startTracking();
    }


    // ActivityRecognitionApi used for calculating the time of user walking distance


    // Android Sensor Service used to calculate the steps walked. Android provided TYPE_STEP_COUNTER implemented.


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

    void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }



    @Override
    protected void onResume() {
        super.onResume();
        Sensor countsensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if(countsensor!=null){

            sensorManager.registerListener(this,countsensor,SensorManager.SENSOR_DELAY_NORMAL);
        }

        else {
            Log.d("TAG", "onResume: sensor not found");
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(com.example.project7.Constants.BROADCAST_DETECTED_ACTIVITY));
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    private void startTracking() {
        mActivityRecognitionClient = new ActivityRecognitionClient(this);  // Service Intent used to know the Activity of user using ActivityRecognitionApi
        mIntentService = new Intent(this, DetectedActivitiesIntentService.class);
        mPendingIntent = PendingIntent.getService(this, 1, mIntentService, PendingIntent.FLAG_UPDATE_CURRENT);
        requestActivityUpdates();

    }

    public void requestActivityUpdates() { // At every interval of time this callback is called to know user activity
        Task<Void> task = mActivityRecognitionClient.requestActivityUpdates(
                com.example.project7.Constants.DETECTION_INTERVAL_IN_MILLISECONDS,
                mPendingIntent);


        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void result) {

                Toast.makeText(getApplicationContext(),
                        "Successfully requested activity updates",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Requesting activity updates failed to start",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }


    //long start = 0;
    private long steps = 0;
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        steps++;
        //textView.setText(String.valueOf(steps));
        lastStepsRedorded = (long) sensorEvent.values[0];

        Log.d("TAG", "onSensorChanged: "+steps);



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(AccountActivity.this, MainActivity.class);
        startActivity(intent);



    }

    public void notificationGenerate(View view) {
        Intent intent = new Intent(AccountActivity.this, Notification.class);
        startActivity(intent);



    }

    public void stats(View view) {

        Intent intent = new Intent(AccountActivity.this,StatisticActivity.class);
        startActivity(intent);

    }
}
