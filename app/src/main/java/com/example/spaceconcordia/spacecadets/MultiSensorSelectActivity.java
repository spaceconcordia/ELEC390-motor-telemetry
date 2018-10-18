package com.example.spaceconcordia.spacecadets;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

// user selects from list several sensors to display simultaneously on separate graphs
public class MultiSensorSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_sensor_select);

        this.setupUI();
    }

    protected void setupUI(){
        setTitle(R.string.title_activity_multi_sensor_select);
    }

}
