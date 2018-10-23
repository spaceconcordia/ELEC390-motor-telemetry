package com.example.spaceconcordia.spacecadets.Data_Types;

public class Temperature {

    private short Value;
    private short Old_Value;
    private int Average; //if we ever want to display the average, at least we have it here
    private int Max_Value;
    private Boolean Alert;

    //this is a first constructor so if we forget to input a max value, it assigns one
    private Temperature(){
        Value = 0;
        Old_Value = 0;
        Average = 0;
        Alert = false;
        Max_Value = 200; //if no max value is entered at least we have one
    }

    //this is a second constructor so that we input a max value
    public void ChangeMax(int max){
        Max_Value = max;
    }

    public void UpdateValue(short value){
        Old_Value = Value;
        Value = value;
        Average = (Old_Value+Value)/2;
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

}
