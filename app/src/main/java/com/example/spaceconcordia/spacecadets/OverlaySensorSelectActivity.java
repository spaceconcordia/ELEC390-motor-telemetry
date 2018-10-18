package com.example.spaceconcordia.spacecadets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/// user selects from list several sensors to be displayed simultaneously on a single graph
public class OverlaySensorSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay_sensor_select);

        this.setupUI();
    }

    protected void setupUI(){
        setTitle(R.string.title_activity_overlay_sensor_select);
    }

}
