package com.example.spaceconcordia.spacecadets.Data_Types;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Sensor implements Serializable {

    private String name;
    private short value;

    public Sensor(){}

    public String getName(){return name;}
    public short getValue(){return value;}



}
