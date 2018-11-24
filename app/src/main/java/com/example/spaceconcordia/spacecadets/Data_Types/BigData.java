package com.example.spaceconcordia.spacecadets.Data_Types;

import android.content.Context;

import com.example.spaceconcordia.spacecadets.SingleSensorDisplayActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class BigData implements Serializable {

    private Sensor_List SensList;
    private boolean ActiveSens[];
    private Sensor Sensor_List[];
    private ArrayList<String> All_Sensor_List;
    private char EngineStatus;
    private static final String TAG = "BigData";
    private static Context currentContext;

    public BigData() {

        SensList = new Sensor_List();
        Sensor_List = new Sensor[SensList.getNbSensors()];
        ActiveSens = new boolean[SensList.getNbSensors()];
        EngineStatus = 'D';

        for(int i = 0; i<SensList.getNbSensors(); i++){
            Sensor_List[i] = new Sensor(SensList.getname(i),SensList.gettype(i));
        }
    }

    private Context getContext(){
        return currentContext;
    }

    public void setContext(Context context){
        this.currentContext = context;
    }

    /// getter of the sensor list objects
    public ArrayList<String> getAllSensorsByString(){return All_Sensor_List;}

    public float getSensorValueByListPosition(int position){

        if(position < SensList.getNbSensors()){
            if (Sensor_List[position].getRawValue()!= -1) {
                return Sensor_List[position].GetTransferredValue();
            }
        }
        return -1; // return -1 if error
    }

    public Sensor getSensorByListPosition(int position){
        if(position < SensList.getNbSensors()){
            return Sensor_List[position];
        }
        return null;
    }

    /**
     * Sample packet : S-445‑A90‑21B2‑E15‑2281‑1147‑140E‑1550‑2023‑70B‑F45‑D71‑139A‑FA4‑14C7‑1F40‑E04‑15B7‑A84‑13E4‑15C7‑1FD0‑1A5F‑1FD2‑171‑2164‑2113‑5E1‑2233
     * The packet are in hexadecimals and values are separated by '-'
     * 29 sensors in order : Status - Temperature sensors 0-13 - flow sensors 14-15 - Pressure sensors 16-28
     */
    public char parse(String Packet) {

        String[] PacketParts = Packet.split(Pattern.quote("-"));

         if (PacketParts.length == SensList.getNbSensors() + 1) { // Reject the packet if there is not 29+1 sensors count in it

                //First PacketPart is controller status
                if (!PacketParts[0].isEmpty()) {
                    EngineStatus = PacketParts[0].charAt(0);
                }

                All_Sensor_List = new ArrayList<>();

                for (int i = 0; i < SensList.getNbSensors(); i++) {
                    if(!PacketParts[i + 1].equals("X")) {
                        Sensor_List[i].UpdateValue(Short.parseShort(PacketParts[i + 1], 16));
                        All_Sensor_List.add(i, Sensor_List[i].getName() + "\n" +
                                        "Current value " +
                                        Sensor_List[i].getDimensions() +
                                        ": " +
                                        Sensor_List[i].GetTransferredValue());
                        ActiveSens[i] = true;
                    }else{
                        All_Sensor_List.add(i, Sensor_List[i].getName() + "\n" + "Disconnected");
                        Sensor_List[i].UpdateValue((short)-1);
                    }
                }
            } else if (PacketParts.length == 1) {
             EngineStatus = PacketParts[0].charAt(0);
             } else{
             EngineStatus = 'B';
         }

        // send the selected sensor's data to the graphing activity when it is created/resumed
        if(SingleSensorDisplayActivity.isActivityInFront()){
            SingleSensorDisplayActivity currentActivity = (SingleSensorDisplayActivity) getContext();

            //pass the new data point associated with the selected sensor for the graphing activity
            currentActivity.updateDataPoint(getSensorValueByListPosition(currentActivity.getPosition()), EngineStatus);
        }

        return EngineStatus; // Something Went wrong! Bad packet
    }


    public char GetEngineStatus(){return EngineStatus;}


//    public int GetTransferedSensorValue(int Index) {
//        if (Index < SensList.getNbSensors()) {
//            return Sensor_List[Index].GetTransferredValue();
//        }
//        return 0; // Return 0 for error
//    }

    public short GetRawSensorValue(int Index) {
        if (Index < SensList.getNbSensors()) {
            return Sensor_List[Index].getRawValue();
        }
        return 0; // Return 0 for error
    }
}
