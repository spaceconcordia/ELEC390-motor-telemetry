package com.example.spaceconcordia.spacecadets.Data_Types;

public class Flow_Sensor {

    private int ID;
    private short Value;
    private short Old_Value;
    private int Average;
    private int Max_Value;
    private int Min_Value;
    private boolean Alert;

    private Flow_Sensor(int id, short value){
        ID = id;
        Value = value;
        Old_Value = 0;
        Average = 0;
        Max_Value = 100; //Change these values to proper ones please
        Min_Value = 10;
        Alert = false;
    }

    private Flow_Sensor(int id, short value, int max, int min){
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

        if(value>Max_Value || value<Min_Value){
            Alert = true;
        }
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
