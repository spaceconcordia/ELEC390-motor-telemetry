package com.example.spaceconcordia.spacecadets.Data_Types;

public class Flow_Sensor {

    private short Value;
    private int Average;
    private int Max_Value;
    private int Min_Value;
    private boolean Alert;
    private String name;

    public Flow_Sensor(){
        Value = 0;
        Average = 0;
        Max_Value = 100; //Change these values to proper ones please
        Min_Value = 10;
        Alert = false;
    }

    public Flow_Sensor(String name){
        this.name = name;
        Value = 0;
        Average = 0;
        Max_Value = 100; //Change these values to proper ones please
        Min_Value = 10;
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

    public int GetAverage(){
        return Average;
    }

    public boolean Alert(){
        return Alert;
    }

    public String getName(){return name;}
}
