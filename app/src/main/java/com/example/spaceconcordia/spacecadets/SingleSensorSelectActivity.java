package com.example.spaceconcordia.spacecadets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/// user selects from list a single sensor to be displayed
public class SingleSensorSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_sensor_select);

        this.setupUI();
    }

    protected void setupUI(){
        setTitle(R.string.title_activity_single_sensor_select);
    }
}
