package com.example.spaceconcordia.spacecadets.Data_Types;




public class BigData {
    private static int TempSen = 14;
    private static int FlowSen = 2;
    private static int PresSen = 13;

    public Temperature Temp_Sensor_List[];
    public Flow_Sensor Flow_Sensor_List[];
    public Pressure_Sensor Pressure_Sensor_List[];


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



}
