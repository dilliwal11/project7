package com.example.project7;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StatisticActivity extends AppCompatActivity {
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
