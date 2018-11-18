package com.example.spaceconcordia.spacecadets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.spaceconcordia.spacecadets.Bluetooth.packetanalysis;
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
    protected MenuItem zoomInButton;
    protected MenuItem zoomOutButton;

    private static boolean isInFront;
    private int position;
    private int xValue = 0;
    private LineGraphSeries<DataPoint> series;
    private Button emergencyStopButton;
    private TextView BTstatusText;
    private packetanalysis PacketAnalysis;
    private BigData PresentData;
    private boolean BTconnected;
    private int yMaxValue = 100;
    private Viewport viewport;
    private Menu graphMenu;

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
        PresentData = (BigData) intent.getSerializableExtra("serialized_data");
        PacketAnalysis = (packetanalysis) intent.getSerializableExtra("PacketAnalysis");
        BTconnected = (boolean) intent.getSerializableExtra("BTconnected");
        PresentData.setContext(this);

        dataTextView = findViewById(R.id.newDataTextView);

        graph = findViewById(R.id.sensorDisplayGraph);
        series = new LineGraphSeries<>();
        graph.addSeries(series);
        viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(yMaxValue);
        viewport.setMaxX(100);
        viewport.setScalable(true);

        this.emergencyStopButton = findViewById(R.id.emergencyStopSensorButton);
        this.BTstatusText = findViewById(R.id.BTStatusSensorTextview);
        emergencyStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //Action of Emergency Stop Button
                    Intent intent = new Intent();
                    intent.putExtra("DisconnectStatus", true);
                    setResult(RESULT_OK, intent);
                    finish();

            }
        });


    }

    public void updateDataPoint(String value){
        int point = Integer.parseInt(value);
        if (value.equals("-1")) {
            dataTextView.setText(String.format("Disconnected"));
        }else {
            dataTextView.setText(String.format("Current Value: %s", value));
            if (point > yMaxValue) {
                yMaxValue = point;
                viewport.setMaxY(yMaxValue);
            }
        }
            //append a new data point to the graph every time the sensor's value get updated
            series.appendData(new DataPoint(xValue++, point), true, 100);

            // update the Connection Status bar at the same time
            PacketAnalysis.GenerateStatusBarText(PresentData.GetEngineStatus(), BTstatusText); // This function update the status bar

    }

}
