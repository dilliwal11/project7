package com.example.project7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AccountActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);











    }
    public void notificationGenerate(View view) {
        Intent intent = new Intent(AccountActivity.this,Notification.class);
        startActivity(intent);



    }


}
