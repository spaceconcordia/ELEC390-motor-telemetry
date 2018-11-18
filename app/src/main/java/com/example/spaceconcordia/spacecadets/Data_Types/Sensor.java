package com.example.spaceconcordia.spacecadets.Data_Types;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Sensor implements Serializable {

    private String name;
    private short value;
    private int type;
    private BlockingQueue sensorDataQueue = new LinkedBlockingQueue(100);

    public Sensor(String name, int Type){
        this.name = name;
        this.type = type;
    }

    public String getName(){return name;}
    public short getValue(){return value;}

    public void UpdateValue(short value){
        this.value = value;
    }

}
