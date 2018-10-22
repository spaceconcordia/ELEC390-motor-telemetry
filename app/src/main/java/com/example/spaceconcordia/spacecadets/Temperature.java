package com.example.spaceconcordia.spacecadets;

public class Temperature {


    private int ID;
    private short Value;
    private short Old_Value;
    private int Average; //if we ever want to display the average, at least we have it here

    private Temperature(int id, short value){
        ID = id;
        Value = value;
        Old_Value = 0;
        Average = 0;
    }

    public void UpdateValue(short value){
        Old_Value = Value;
        Value = value;
        Average = (Old_Value+Value)/2;
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

}
