package com.example.spaceconcordia.spacecadets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.spaceconcordia.spacecadets.Data_Types.Temperature;
import com.example.spaceconcordia.spacecadets.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/// user selects from list a single sensor to be displayed
public class SingleSensorSelectActivity extends AppCompatActivity {

    protected ListView sensorListView;
    List<Temperature> temperatureList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_sensor_select);

        this.setupUI();
        this.loadListView();

        sensorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSensor = (String) (sensorListView.getItemAtPosition(position));

                Intent intent = new Intent(SingleSensorSelectActivity.this, SingleSensorDisplayActivity.class);
                intent.putExtra("sensorName", selectedSensor);
                startActivity(intent);
            }
        });

    }

    protected void setupUI(){
        setTitle(R.string.title_activity_single_sensor_select);
        sensorListView = findViewById(R.id.singleSensorSelectListView);
    }

    protected void loadListView()
    {

        ArrayList<String> listTempSensors = new ArrayList<>();

        for(int i = 0; i < 10; i++){
            listTempSensors.add("Sensor" + i);
        }

        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listTempSensors);
        sensorListView.setAdapter(adapter);

        //TODO: get list of sensor names from database to select from
//        DatabaseHelper dbhelper = new DatabaseHelper(this);
//        temperatureList = dbhelper.getAllTemperatureSensors();
//
//        ArrayList<String> listTempSensors = new ArrayList<>();
//
//        for(int i=0; i< temperatureList.size(); i++){
//            String temp = "";
//            listTempSensors.add(temperatureList.get(i).getName() + "\n"+ temperatureList.get(i).GetValue());
//        }
//        ArrayAdapter adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listTempSensors);
//        sensorListView.setAdapter(adapter);
    }
}
