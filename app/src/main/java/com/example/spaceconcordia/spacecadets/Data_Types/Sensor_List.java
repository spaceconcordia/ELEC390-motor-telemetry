package com.example.spaceconcordia.spacecadets.Data_Types;

import java.io.Serializable;
import java.util.ArrayList;

public class Sensor_List implements Serializable {
    private int NbSensors = 29;
    private ArrayList<String> SensorName;
    private ArrayList<Integer> SensorType;


    public Sensor_List(){
        SensorName  = new ArrayList<>();
        SensorType = new ArrayList<>();

        SensorName.add("Temperature 1");
        SensorName.add("Temperature 2");
        SensorName.add("Temperature 3");
        SensorName.add("Temperature 4");
        SensorName.add("Temperature 5");
        SensorName.add("Temperature 6");
        SensorName.add("Temperature 7");
        SensorName.add("Temperature 8");
        SensorName.add("Temperature 9");
        SensorName.add("Temperature 10");
        SensorName.add("Temperature 11");
        SensorName.add("Temperature 12");
        SensorName.add("Temperature 13");
        SensorName.add("Temperature 14");
        SensorName.add("Potentiometer 1");
        SensorName.add("Flow 1");
        SensorName.add("Pressure 1");
        SensorName.add("Pressure 2");
        SensorName.add("Pressure 3");
        SensorName.add("Pressure 4");
        SensorName.add("Pressure 5");
        SensorName.add("Pressure 6");
        SensorName.add("Pressure 7");
        SensorName.add("Pressure 8");
        SensorName.add("Pressure 9");
        SensorName.add("Pressure 10");
        SensorName.add("Pressure 11");
        SensorName.add("Pressure 12");
        SensorName.add("Pressure 13");

        /*TYPE:
        * 0 : Temperature
        * 1 : Pressure
        * 2 : Flow
        * */
        SensorType.add(0);
        SensorType.add(0);
        SensorType.add(0);
        SensorType.add(0);
        SensorType.add(0);
        SensorType.add(0);
        SensorType.add(0);
        SensorType.add(0);
        SensorType.add(0);
        SensorType.add(0);
        SensorType.add(0);
        SensorType.add(0);
        SensorType.add(0);
        SensorType.add(0);
        SensorType.add(3);
        SensorType.add(2);
        SensorType.add(1);
        SensorType.add(1);
        SensorType.add(1);
        SensorType.add(1);
        SensorType.add(1);
        SensorType.add(1);
        SensorType.add(1);
        SensorType.add(1);
        SensorType.add(1);
        SensorType.add(1);
        SensorType.add(1);
        SensorType.add(1);
        SensorType.add(1);
    }
    public int getNbSensors(){return NbSensors;}
    public String getname(int i){return SensorName.get(i);}
    public int gettype(int i){return SensorType.get(i);}

}
