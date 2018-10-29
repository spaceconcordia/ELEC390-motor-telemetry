package com.example.spaceconcordia.spacecadets.Data_Types;

public class Pressure_Sensor {


    private short Value;
    private int Average;
    private boolean Alert;
    private int Max_Value; //I assumed for pressure there is a min and max value that are important
    private int Min_Value;
    private String name;


    public Pressure_Sensor(){
        Value = 0;
        Average = 0;
        Max_Value = 500; // change this value to actual max psi of system. if we forget to enter at least we have it
        Min_Value = 0;
        Alert = false;
    }

    public Pressure_Sensor(String name){
        this.name = name;
        Value = 0;
        Average = 0;
        Max_Value = 500; // change this value to actual max psi of system. if we forget to enter at least we have it
        Min_Value = 0;
        Alert = false;
    }

    public void ChangeMinMax(int min, int max){
        Max_Value = max;
        Min_Value = min;
    }

    public void UpdateValue(short value){
        Value = value;
        Average = (Average+value)/2;
        Alert = value > Max_Value || value < Min_Value;
    }

    public short GetValue(){
        return Value;
    }

    public int getAverage(){
        return Average;
    }

    public boolean Alert(){
        return Alert;
    }

    public String getName(){return name;}
}
