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
        if(type == 0){
            return "(degC)";
        }
        else if(type == 1){
            return "(PSI)";
        }
        else{
            return "(L/sec)";
        }
    }
    public float GetTransferredValue(){
        //Todo Get proper Transfer function
        float Transferredvalue = 0;
        switch (type) {
            case 0: // Temperature Sensor in degC
                Transferredvalue = (float) (value/0.000040);   //Approx. 40uV/degC
            break;
            case 1: // Pressure Sensor in PSI
                Transferredvalue = (float) ((value*0.00488 - 0.5) * 58.015); //0.5V offset, 4V range, so ~0.283 PSI/V in the end
            break;
            case 2: // Flow Sensor
                Transferredvalue =  value*1;
            break;
        }
        return Transferredvalue;
    }
}
