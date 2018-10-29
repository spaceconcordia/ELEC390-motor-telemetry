package com.example.spaceconcordia.spacecadets.Data_Types;

import java.util.regex.Pattern;

public class BigData {
    private static int TempSen = 14;
    private static int FlowSen = 2;
    private static int PresSen = 13;

    private Temperature Temp_Sensor_List[];
    private Flow_Sensor Flow_Sensor_List[];
    private Pressure_Sensor Pressure_Sensor_List[];
    private String All_Sensor_List[];


    public BigData() {

        Temp_Sensor_List = new Temperature[TempSen];
        Flow_Sensor_List = new Flow_Sensor[FlowSen];
        Pressure_Sensor_List = new Pressure_Sensor[PresSen];

        for(int i = 0; i<TempSen; i++){

            // These are just temporary names for now
            Temp_Sensor_List[i] = new Temperature("Temperature Sensor " + (i+1));
        }        for(int i = 0; i<FlowSen;i++){
            Flow_Sensor_List[i] = new Flow_Sensor("Flow Sensor " + (i+1));
        }        for(int i = 0; i<PresSen; i++) {
            Pressure_Sensor_List[i] = new Pressure_Sensor("Pressure Sensor " + (i+1));
        }

        // if all the sensors use the same max and min values, then we need to change them once
        // in their own class. But if there are sensors that have their own min and max requirements
        // we can easily initiate them here, all we need to do is add another constructor.
        // for example, Temp_Sensor_List[2].ChangeMax(int max)
        // For Flow and Pressure the syntax is Flow_SensorList.ChangeMinMax(int min, int max)
        // This will make the initialisation way simpler anytime we want to do this!!
        // Special Initialisation Starts from HERE


        // To HERE
    }

    /// getter of the sensor list objects
    public String[] getAllSensorsByString(){return All_Sensor_List;}


    /**
     * Sample packet : 445‑A90‑21B2‑E15‑2281‑1147‑140E‑1550‑2023‑70B‑F45‑D71‑139A‑FA4‑14C7‑1F40‑E04‑15B7‑A84‑13E4‑15C7‑1FD0‑1A5F‑1FD2‑171‑2164‑2113‑5E1‑2233
     * The packet are in hexadecimals and values are separated by '-'
     * 29 sensors in order : Temperature sensors 0-13 - flow sensors 14-15 - Pressure sensors 16-28
     */
    public int parse(String Packet) {

        String[] Sensors = Packet.split(Pattern.quote("-"));

        if (Sensors.length == 29){ // Reject the packet if there is not 29 sensors count in it

            All_Sensor_List = new String[Sensors.length];

            for(int i = 0; i<TempSen; i++){
                Temp_Sensor_List[i].UpdateValue(Short.parseShort(Sensors[i],16));
                All_Sensor_List[i] = Temp_Sensor_List[i].getName() + "\n" + "Current Value: " + Temp_Sensor_List[i].GetValue();
            }        for(int i = 0; i<FlowSen;i++){
                Flow_Sensor_List[i].UpdateValue(Short.parseShort(Sensors[i+TempSen],16));
                All_Sensor_List[TempSen + i] = Flow_Sensor_List[i].getName() + "\n" + "Current Value: " + Flow_Sensor_List[i].GetValue();
            }        for(int i = 0; i<PresSen; i++) {
                Pressure_Sensor_List[i].UpdateValue(Short.parseShort(Sensors[i+TempSen+FlowSen],16));
                All_Sensor_List[TempSen + FlowSen + i] = Pressure_Sensor_List[i].getName() + "\n" + "Current Value: " + Pressure_Sensor_List[i].GetValue();
            }

            /**
             *
             * Temperature[0] = Short.parseShort(Sensors[0],16); // This convert the HEX string to short
             * ...
             * Temperature[13] =Short.parseShort(Sensors[13],16);
             * Flow_Sensor[0] = Short.parseShort(Sensors[14],16);
             * Flow_Sensor[1] = Short.parseShort(Sensors[15],16);
             * Pressure_Sensor[0]  = Short.parseShort(Sensors[16],16);
             * ...
             * Pressure_Sensor[12]  = Short.parseShort(Sensors[28],16);
             */

        }

        return Sensors.length;
    }

}



