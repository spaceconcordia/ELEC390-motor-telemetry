package com.example.spaceconcordia.spacecadets.Data_Types;

public class BigData {
    private static int TempSen = 14;
    private static int FlowSen = 2;
    private static int PresSen = 13;

    private Temperature Temp_Sensor_List[];
    private Flow_Sensor Flow_Sensor_List[];
    private Pressure_Sensor Pressure_Sensor_List[];


    public BigData(){

        Temp_Sensor_List = new Temperature[TempSen];
        Flow_Sensor_List = new Flow_Sensor[FlowSen];
        Pressure_Sensor_List = new Pressure_Sensor[PresSen];

        // if all the sensors use the same max and min values, then we need to change them once
        // in their own class. But if there are sensors that have their own min and max requirements
        // we can easily initiate them here, all we need to do is add another constructor.
        // for example, Temp_Sensor_List[2].ChangeMax(int max)
        // For Flow and Pressure the syntax is Flow_SensorList.ChangeMinMax(int min, int max)
        // This will make the initialisation way simpler anytime we want to do this!!
        // Special Initialisation Starts from HERE





        // To HERE
    }
    private short sensors[]; // TEMPORARY ARRAY, replace with the proper one

        public void parse (String Packet) {


            int i; // Sensors index
            int inx;
            String PacketBuffer = Packet;

            for (i = 0; i < 29; i++) {

                inx = PacketBuffer.indexOf("-");

                // If there is none, exit
                if (inx == -1)
                    return;

                // Get the complete message
                String s = PacketBuffer.substring(0, inx);
                if (i < 14) {

                    sensors[i] = Short.parseShort(s);
                    /** 14 Temp sensors
                     *
                     * i = 0-13
                     */

                } else if (i < 16) {
                    sensors[i] = Short.parseShort(s);
                    ///
                    /** 2 flow sensors
                     *
                     * i = 14-15
                     */

                } else {
                    sensors[i] = Short.parseShort(s);

                    /** 13 Pressure sensors
                     * i = 16-28
                     */

                }
                // Remove the message from the buffer
                PacketBuffer = PacketBuffer.substring(inx + 1);
            }
        }

    }



