package com.example.spaceconcordia.spacecadets.Bluetooth;

import android.graphics.Color;
import android.widget.TextView;

import java.io.Serializable;

//This class determine the packet frequency and the transmission health
public class packetanalysis implements Serializable {


    //Bluetooth Packet Frequency Counter
    private int PacketFrequency;
    private long PastTime;
    private long currentTime;
    private int packetCounter;
    private short sampletime = 1000; //Count packet for 5 seconds

    //Bluetooth Connection Bad packet and quality counters
    private int BadPacket;
    private int BadPacketTotal;
    private int QualityPacketCounter;
    private int ConnectionQuality;
    private String StatusBarText;

    public packetanalysis(){
        PacketFrequency = 0;
        PastTime = 0;
        currentTime = 0;
        packetCounter = 0;

        BadPacket = 0;
        BadPacketTotal = 0;
        QualityPacketCounter = 0;
        ConnectionQuality = 0;
    }


    public int FrequencyCalc(){
        //Frequency counter: count the number of packet in a sample time then find the number of packet per second.
        currentTime = System.currentTimeMillis();
        packetCounter++;

        //If sampletime elapsed
        if (currentTime > PastTime + sampletime){
            PacketFrequency = packetCounter/(sampletime/1000);
            PastTime = currentTime;
            packetCounter= 0; // Reset count to 0;
        }
        return PacketFrequency;
    }


    public int PacketHealth(char Status){

        QualityPacketCounter++;
        if(Status == 'B'){
            BadPacket++;
            BadPacketTotal++;
        }
        if (QualityPacketCounter >= 100){
            ConnectionQuality = 100-BadPacket;
            BadPacket = 0;
            QualityPacketCounter= 0;
        }
        return ConnectionQuality;
    }

    public void GenerateStatusBarText(char Status, TextView BTstatusText){
        StatusBarText = "";
        switch (Status){
            case 'B':
                StatusBarText = "Bad Packet";
                BTstatusText.setBackgroundColor(Color.rgb(255,140,0)); // Orange
                break;
            case 'I':
                StatusBarText = "Idle";
                BTstatusText.setBackgroundColor(Color.GREEN);
                break;
            case 'F':
                StatusBarText ="Fired";
                BTstatusText.setBackgroundColor(Color.YELLOW);

                break;
            case 'X':
                StatusBarText = "E. Stop"; // Emergency Stop
                BTstatusText.setBackgroundColor(Color.RED);

                break;
            case 'x':
                StatusBarText = "D. Stop"; // Disconnect Stop
                BTstatusText.setBackgroundColor(Color.RED);
                break;
            case 'S':
                StatusBarText ="Simulated";
                BTstatusText.setBackgroundColor(Color.LTGRAY);
                break;
            case 'C':
                StatusBarText ="Completed";
                BTstatusText.setBackgroundColor(Color.BLUE);
                break;
            case 'D':
                StatusBarText ="Bad Connection";
                BTstatusText.setBackgroundColor(Color.rgb(255,140,0)); // Orange
                break;
            case 'T':
                StatusBarText ="Time out!";
                BTstatusText.setBackgroundColor(Color.rgb(255,140,0)); // Orange
                break;
            case 'Q':
                StatusBarText ="Disconnected";
                BTstatusText.setBackgroundColor(Color.rgb(255,140,0)); // Orange
                break;
        }
        if (Status != 'T' && Status != 'Q') {
            // Calculate Frequency
            int freq = FrequencyCalc();
            // Calculate Health
            int health = PacketHealth(Status);
            // get total bad packet
            //Add the frequency to the Status text
            if (freq != 0) {
                StatusBarText += "-" + freq + "Hz";
            }
            //Add the Health to the Status text
            if (health != 0) {
                StatusBarText += "-QL:" + health + "%";
            }
            StatusBarText += "-Bad:" + BadPacketTotal;
            }
        BTstatusText.setText(StatusBarText);
    }

    public int getPacketFrequency(){return PacketFrequency;};
}
