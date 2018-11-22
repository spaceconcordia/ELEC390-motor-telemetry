package com.example.spaceconcordia.spacecadets.Data_Types;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Sensor implements Serializable {

    private String name;
    private short value;
    private int type;
    private BlockingQueue sensorDataQueue = new LinkedBlockingQueue(100);
    int Transferedvalue;

    public Sensor(String name, int type){
        this.name = name;
        this.type = type;
    }

    public String getName(){return name;}
    public short getRawValue(){return value;}

    public void UpdateValue(short value){
        this.value = value;
    }
    public int GetTransferedValue(){
        //Todo Get proper Transfer function
        Transferedvalue = 0;
        switch (type) {
            case 0: // Temperature Sensor
                Transferedvalue = value*100;
            break;
            case 1: // Pressure Sensor
                Transferedvalue = value*1;
            break;
            case 2: // Flow Sensor
                Transferedvalue =  value*1;
            break;
        }
        return Transferedvalue;
    }
}
