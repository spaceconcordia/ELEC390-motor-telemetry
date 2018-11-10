package com.example.spaceconcordia.spacecadets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.spaceconcordia.spacecadets.Data_Types.BigData;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/// this is the activity that displays the data coming from the selected sensor
public class SingleSensorDisplayActivity extends AppCompatActivity {

    //protected GraphView graph;
    protected TextView dataTextView;
    protected GraphView graph;

    private static boolean isInFront;
    private int position;
    private int xValue = 0;
    private LineGraphSeries<DataPoint> series;

    // true if current activity is SingleSensorDislayActivity
    public static boolean isActivityInFront(){
        return isInFront;
    }

    public int getPosition(){return position;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_sensor_display);

        this.setupUI();

    }

    @Override
    public void onResume() {
        super.onResume();
        isInFront = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isInFront = false;
    }

    public void setupUI(){

        Intent intent = getIntent();
        setTitle(intent.getExtras().getString("sensorName"));
        position = intent.getExtras().getInt("sensorPosition");
        BigData PresentData = (BigData) intent.getSerializableExtra("serialized_data");
        PresentData.setContext(this);

        dataTextView = findViewById(R.id.newDataTextView);

        graph = findViewById(R.id.sensorDisplayGraph);
        series = new LineGraphSeries<>();
        graph.addSeries(series);
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(1000);
        viewport.setMaxX(100);
        viewport.setScalable(true);
    }

    public void updateDataPoint(String value){
        dataTextView.setText(String.format("Current Value: %s", value));
        int point = Integer.parseInt(value);

        //append a new data point to the graph every time the sensor's value get updated
        series.appendData(new DataPoint(xValue++, point), true, 100);
    }

}
