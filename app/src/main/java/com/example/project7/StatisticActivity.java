package com.example.project7;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

 public  class StatisticActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;

    TextView statTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        statTv = findViewById(R.id.statisticTV);
        databaseHelper = new DatabaseHelper(this);

        statTv.setText(databaseHelper.allStats());

        //user stats are fetched from local database.

    }
}
