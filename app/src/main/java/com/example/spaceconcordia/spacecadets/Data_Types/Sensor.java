package com.example.spaceconcordia.spacecadets.Data_Types;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Sensor implements Serializable {

    private String name;
    private short value;
    private int type;

    public Sensor(String name, int type){
        this.name = name;
        this.type = type;
    }

    public int getType(){return type;}

    public String getName(){return name;}

    public short getRawValue(){return value;}

    public void UpdateValue(short value){
        this.value = value;
    }

    public String getDimensions(){
        String Dimensions = "";
        switch(type) {
            case 0:
                Dimensions = "(degC)";
                break;
            case 1:
                Dimensions = "(PSI)";
                break;
            case 2:
                Dimensions = "(L/sec)";
                break;
            case 3:
                Dimensions = "(%)";
                break;
            case 4:
                Dimensions = "(degC)";
                break;
        }
        return Dimensions;
    }
    public float GetTransferredValue(){
        //Todo Get proper Transfer function
        float Transferredvalue = 0;
        switch (type) {
            case 0: // Temperature Sensor in degC for ADC Board
                Transferredvalue = (float) (value/0.000040);   //Approx. 40uV/degC
            break;
            case 1: // Pressure Sensor in PSI
                Transferredvalue = (float) ((value*0.00488 - 0.5) * 58.015) - 146; //0.5V offset, 4V range, so ~0.283 PSI/V in the end
            break;
            case 2: // Flow Sensor -  Used as temporary pot sensor
                Transferredvalue =value*1;
            break;
            case 3: // Demonstration Test pot sensor (goes from 0% to 100%)
                Transferredvalue = (float) (value*0.067567-176.6892);
                break;
            case 4: // Temperature Sensor in degC for Built-in thermistor
                Transferredvalue = (float) (value*0.05-72);   //Very approximate function
                break;
        }
        return Transferredvalue;
    }
}
