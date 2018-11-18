package com.example.spaceconcordia.spacecadets.Data_Types;

import android.content.Context;

import com.example.spaceconcordia.spacecadets.Database.DBhelper;
import com.example.spaceconcordia.spacecadets.SingleSensorDisplayActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class BigData implements Serializable {
    private static int TempSen = 14;
    private static int FlowSen = 2;
    private static int PresSen = 13;
    private int NbSensors = TempSen + FlowSen + PresSen;
    private boolean ActiveSens[];

    private Temperature Temp_Sensor_List[];
    private Flow_Sensor Flow_Sensor_List[];
    private Pressure_Sensor Pressure_Sensor_List[];
    private Sensor Sensor_List[];
    private ArrayList<String> All_Sensor_List;

    private char EngineStatus;

    private static final String TAG = "BigData";


    Context context;
    private static Context currentContext;

    public BigData() {

        Temp_Sensor_List = new Temperature[TempSen];
        Flow_Sensor_List = new Flow_Sensor[FlowSen];
        Pressure_Sensor_List = new Pressure_Sensor[PresSen];
        Sensor_List = new Sensor[TempSen+FlowSen+PresSen];
        ActiveSens = new boolean[NbSensors];
        EngineStatus = 'D';

        for(int i = 0; i<TempSen; i++){
            // These are just temporary names for now
            Temp_Sensor_List[i] = new Temperature("Temperature Sensor " + (i+1));
            Sensor_List[i] = Temp_Sensor_List[i];
        }
        for(int i = 0; i<FlowSen;i++){
            Flow_Sensor_List[i] = new Flow_Sensor("Flow Sensor " + (i+1));
            Sensor_List[i+TempSen] = Flow_Sensor_List[i];
        }
        for(int i = 0; i<PresSen; i++) {
            Pressure_Sensor_List[i] = new Pressure_Sensor("Pressure Sensor " + (i+1));
            Sensor_List[i+TempSen+FlowSen] = Pressure_Sensor_List[i];
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

    public String getSensorValueByListPosition(int position){

        if(position < TempSen){
            return String.valueOf(Temp_Sensor_List[position].GetValue());
        }
        else if(position < TempSen + FlowSen){
            return String.valueOf(Flow_Sensor_List[position-TempSen].GetValue());
        }
        else{
            return String.valueOf(Pressure_Sensor_List[position-(TempSen+FlowSen)].GetValue());
        }
    }

    /**
     * Sample packet : 445‑A90‑21B2‑E15‑2281‑1147‑140E‑1550‑2023‑70B‑F45‑D71‑139A‑FA4‑14C7‑1F40‑E04‑15B7‑A84‑13E4‑15C7‑1FD0‑1A5F‑1FD2‑171‑2164‑2113‑5E1‑2233
     * The packet are in hexadecimals and values are separated by '-'
     * 29 sensors in order : Temperature sensors 0-13 - flow sensors 14-15 - Pressure sensors 16-28
     */
    public char parse(String Packet) {

        String[] PacketParts = Packet.split(Pattern.quote("-"));

         if (PacketParts.length == NbSensors + 1) { // Reject the packet if there is not 29+1 sensors count in it

                //First PacketPart is controller status
                if (!PacketParts[0].isEmpty()) {
                    EngineStatus = PacketParts[0].charAt(0);
                }

                All_Sensor_List = new ArrayList<>();

                for (int i = 0; i < TempSen; i++) {
                    if(!PacketParts[i + 1].equals("X")) {
                        Temp_Sensor_List[i].UpdateValue(Short.parseShort(PacketParts[i + 1], 16));
                        All_Sensor_List.add(i, Temp_Sensor_List[i].getName() + "\n" + "Current value: " + Temp_Sensor_List[i].GetValue());
                        ActiveSens[i] = true;
                    }else{
                        All_Sensor_List.add(i, Temp_Sensor_List[i].getName() + "\n" + "Disconnected");
                        Temp_Sensor_List[i].UpdateValue((short)-1);
                    }
                }
                for (int i = 0; i < FlowSen; i++) {
                    if(!PacketParts[i + TempSen + 1].equals("X")) {
                    Flow_Sensor_List[i].UpdateValue(Short.parseShort(PacketParts[i + TempSen + 1], 16));
                    All_Sensor_List.add(TempSen + i, Flow_Sensor_List[i].getName() + "\n" + "Current value: " + Flow_Sensor_List[i].GetValue());
                    }
                    else{
                        All_Sensor_List.add(TempSen + i, Flow_Sensor_List[i].getName() + "Disconnected");
                        Flow_Sensor_List[i].UpdateValue((short)-1);


                    }
                }
                for (int i = 0; i < PresSen; i++) {
                    if(!PacketParts[i + TempSen + FlowSen + 1].equals("X")) {
                        Pressure_Sensor_List[i].UpdateValue(Short.parseShort(PacketParts[i + TempSen + FlowSen + 1], 16));
                        All_Sensor_List.add(TempSen + FlowSen + i, Pressure_Sensor_List[i].getName() + "\n" + "Current value: " + Pressure_Sensor_List[i].GetValue());
                    }else{
                        All_Sensor_List.add(TempSen + FlowSen + i, Pressure_Sensor_List[i].getName() + "Disconnected");
                        Temp_Sensor_List[i].UpdateValue((short)-1);
                    }
                }



                // send the selected sensor's data to the graphing activity when it is created/resumed
                if(SingleSensorDisplayActivity.isActivityInFront()){
                    SingleSensorDisplayActivity currentActivity = (SingleSensorDisplayActivity) getContext();

                    //pass the new data point associated with the selected sensor for the graphing activity
                    currentActivity.updateDataPoint(getSensorValueByListPosition(currentActivity.getPosition()));
                }

            } else if (PacketParts.length == 1) {
             EngineStatus = PacketParts[0].charAt(0);
             } else{
             EngineStatus = 'B';
         }
        return EngineStatus; // Something Went wrong! Bad packet
    }


    public char GetEngineStatus(){return EngineStatus;}


    public short GetSensorValue(int Index) {
        if (Index < TempSen) {
            return Temp_Sensor_List[Index].GetValue();
        } else if (Index < TempSen+FlowSen) {
            return Flow_Sensor_List[Index-TempSen].GetValue();
        } else if (Index < NbSensors) {
            return Pressure_Sensor_List[Index-TempSen-FlowSen].GetValue();
        }
        return 0; // Return 0 for error
    }
}
