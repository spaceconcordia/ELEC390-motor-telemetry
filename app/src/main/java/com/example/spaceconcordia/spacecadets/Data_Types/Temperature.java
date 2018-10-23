package com.example.spaceconcordia.spacecadets.Data_Types;

public class Temperature {


    private int ID;
    private short Value;
    private short Old_Value;
    private int Average; //if we ever want to display the average, at least we have it here
    private int Max_Value;
    private Boolean Alert;


    //this is a first constructor so if we forget to input a max value, it assigns one
    private Temperature(int id, short value){
        ID = id;
        Value = value;
        Old_Value = 0;
        Average = 0;
        Alert = false;
        Max_Value = 200; //if no max value is entered at least we have one
    }


    //this is a second constructor so that we input a max value
    private Temperature(int id, short value, int max){
        ID = id;
        Value = value;
        Old_Value = 0;
        Average = 0;
        Max_Value = max;
        Alert = value > Max_Value;

    }

    public void UpdateValue(short value){
        Old_Value = Value;
        Value = value;
        Average = (Old_Value+Value)/2;
        Alert = value > Max_Value;
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
