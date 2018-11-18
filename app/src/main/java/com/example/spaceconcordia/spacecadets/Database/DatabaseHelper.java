package com.example.spaceconcordia.spacecadets.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.spaceconcordia.spacecadets.Data_Types.Flow_Sensor;
import com.example.spaceconcordia.spacecadets.Data_Types.Pressure_Sensor;
import com.example.spaceconcordia.spacecadets.Data_Types.Temperature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseHelper  extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "sensorManager";
    private Context context = null;

    //Sensors Table
    private static final String TABLE_SENSORS = "sensors";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TYPE = "type";

    //Readings Table
    private static final String TABLE_READINGS = "readings";
    private static final String KEY_IDs = "id"; //Not sure yet if we need this columnn
    private static final String KEY_SENSOR_ID = "sensor_id";
    private static final String KEY_VALUE = "value";

    private static final String CREATE_SENSOR_TABLE= "CREATE TABLE " + TABLE_SENSORS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_NAME + " TEXT, "
            + KEY_TYPE + " TEXT, "
            + KEY_VALUE + " TEXT "
            + ")";

    private static final String CREATE_READING_TABLE="CREATE TABLE " + TABLE_READINGS + "("
            + KEY_IDs + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_SENSOR_ID + " INTEGER NOT NULL, "
            + KEY_VALUE + " INTEGER NOT NULL "
            + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();

    }


    @Override
    public void onCreate(SQLiteDatabase db) {



        db.execSQL(CREATE_SENSOR_TABLE);
        db.execSQL(CREATE_READING_TABLE);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENSORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_READINGS);

        onCreate(db);

    }

    public boolean addTemperatureSensor(Temperature temperature){
        String type = "Temperature";
        String name = temperature.getName();
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(KEY_NAME,name);
        contentValues.put(KEY_TYPE,type);
        long result = db.insert(TABLE_SENSORS,null, contentValues);
        if( result == -1){
            db.close();
            return false;}
        else{
            db.close();
            return true;
        }
    }

    public boolean addFlowSensor(Flow_Sensor flow){
        String type = "Flow";
        String name = flow.getName();
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(KEY_NAME,name);
        contentValues.put(KEY_TYPE,type);
        long result = db.insert(TABLE_SENSORS,null, contentValues);
        if( result == -1)
            return false;
        else
            return true;
    }

    public boolean addPressureSensor(Pressure_Sensor pressure){
        String type = "Pressure";
        String name = pressure.getName();
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(KEY_NAME,name);
        contentValues.put(KEY_TYPE,type);
        long result = db.insert(TABLE_SENSORS,null, contentValues);
        if( result == -1)
            return false;
        else
            return true;
    }

    public boolean addReadings(int sensor_id,short value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_SENSOR_ID,sensor_id);
        contentValues.put(KEY_VALUE,value);
        long result = db.insert(TABLE_READINGS,null, contentValues);
        if( result == -1)
            return false;
        else
            return true;

    }

    public String getname(int row){

        String name="";
        String rownum =Integer.toString(row);

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;
        int id = -1;
        try {

            cursor = db.query(TABLE_SENSORS, null,
                    KEY_ID + " = ? ", new String[]{rownum},
                    null, null, null);


            if (cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME));

            }
        } catch (Exception e) {
            Log.d(TAG, "Exception: " + e.getMessage());
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return name;

    }



//This method returns the sensor Table as a String so it can be written in a text file
    public String printSensorTable(){
        String Table="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_SENSORS , null);
        if(cur.getCount() !=0){
            cur.moveToFirst();
            do{
                String row_values="";
                for(int i=0; i<cur.getColumnCount();i++){
                    row_values= row_values + "  " + cur.getString(i);
                }
                Table= Table + " \n " + row_values;
            }while (cur.moveToNext());
        }

        return Table;
    }

//This method returns the readings table as a string so it can be written in the text file
    public String printReadingTable(){
        String Table="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_READINGS , null);
        if(cur.getCount() !=0){
            cur.moveToFirst();
            do{
                String row_values="";
                for(int i=0; i<cur.getColumnCount();i++){
                    row_values= row_values + "  " + cur.getString(i);
                }
                Table= Table + " \n " + row_values;
            }while (cur.moveToNext());
        }

        return Table;
    }






    public int getSensorId(String name) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = null;
        int id = -1;
        try {

            cursor = sqLiteDatabase.query(TABLE_SENSORS, null,
                    KEY_NAME + " = ? ", new String[]{name},
                    null, null, null);


            if (cursor.moveToFirst()) {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID));

            }
        } catch (Exception e) {
            Log.d(TAG, "Exception: " + e.getMessage());
            //Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return id;
    }
}






















