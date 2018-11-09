package com.example.spaceconcordia.spacecadets.Data_Types;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Temperature extends Sensor {

    private short Value;
    private int Average; //if we ever want to display the average, at least we have it here
    private int Max_Value;
    private Boolean Alert;
    private String name; //name of the sensor. can be its location on rocket also

    //this is a first constructor so if we forget to input a max value, it assigns one
    public Temperature(){
        Value = 0;
        Average = 0;
        Alert = false;
        Max_Value = 200; //if no max value is entered at least we have one
    }

    //Constructor that creates a sensor in regards to its given name
    public Temperature(String name){
        this.name = name;
        Value = 0;
        Average = 0;
        Alert = false;
        Max_Value = 200; //if no max value is entered at least we have one
    }

    //this is a second constructor so that we input a max value
    public void ChangeMax(int max){
        Max_Value = max;
    }

    public void UpdateValue(short value){
        Value = value;
        Average = (Average+value)/2;
        Alert = value > Max_Value;
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

    public String getName(){ return name;}

}
