package com.example.spaceconcordia.spacecadets.Data_Types;

public class Pressure_Sensor {

    private int ID;
    private short Value;
    private short Old_Value;
    private int Average;
    private boolean Alert;
    private int Max_Value; //I assumed for pressure there is a min and max value that are important
    private int Min_Value;


    private Pressure_Sensor(int id, short value){
        ID = id;
        Value = value;
        Old_Value = 0;
        Average = 0;
        Max_Value = 500; // change this value to actual max psi of system. if we forget to enter at least we have it
        Min_Value = 0;
        Alert = false;
    }

    private Pressure_Sensor(int id, short value, int max, int min){
        ID = id;
        Value = value;
        Old_Value = 0;
        Average = 0;
        Max_Value = max;
        Min_Value = min;

        Alert = value > max || value < min;
    }

    public void UpdateValue(short value){
        Old_Value = Value;
        Value = value;
        Average = (Old_Value+Value)/2;

        Alert = value > Max_Value || value < Min_Value;
    }

    public short getValue(){
        return Value;
    }

    public int getAverage(){
        return Average;
    }

    public int getID(){
        return ID;
    }

    public boolean Alert(){
        return Alert;
    }
}
