package com.example.spaceconcordia.spacecadets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/// this is the activity that displays the data coming from the selected sensor
public class SingleSensorDisplayActivity extends AppCompatActivity {

    protected GraphView graphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_sensor_display);

        this.setupUI();
    }

    public void setupUI(){
        Intent intent = getIntent();
        setTitle(intent.getExtras().getString("sensorName"));

        graphView = findViewById(R.id.sensorDisplayGraph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graphView.addSeries(series);

        //TODO: get data points from database for selected sensor
    }


}
