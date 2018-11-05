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
    private static final String TABLE_SENSORS = "sensors";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TYPE = "type";
    private static final String KEY_VALUE = "value";
    private Context context = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SENSOR_TABLE = "CREATE TABLE" + TABLE_SENSORS + "("
                + KEY_ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + "TEXT NOT NULL,"
                + KEY_TYPE + "TEXT NOT NULL"
                + KEY_VALUE + "TEXT NOT FULL"
                + ")";

        Log.d(TAG, "Table create SQL :" + CREATE_SENSOR_TABLE);
        db.execSQL(CREATE_SENSOR_TABLE);

        Log.d(TAG, "DB created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENSORS);

        onCreate(db);

    }

    public void addTemperatureSensor(Temperature temperature, int index) {
        String type = "Temperature";

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(temperature.getName() == null) {
            values.put(KEY_NAME, index);  }
        else{
            values.put(KEY_NAME, temperature.getName());
        }
        values.put(KEY_TYPE, type);
        values.put(KEY_VALUE, temperature.GetValue());

        db.insert(TABLE_SENSORS, null, values);
        db.close();

    }

    public void addPressureSensor(Pressure_Sensor pressure, int index) {
        String type = "Pressure";

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(pressure.getName() == null){
            values.put(KEY_NAME, index);
        }
        else{
            values.put(KEY_NAME, pressure.getName());
        }
        values.put(KEY_TYPE, type);
        values.put(KEY_VALUE, pressure.GetValue());

        db.insert(TABLE_SENSORS, null, values);
        db.close();

    }

    public void addFlowSensor(Flow_Sensor flow, int index) {
        String type = "Flow";

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(flow.getName() == null){
            values.put(KEY_NAME, index);
        }
        else{
            values.put(KEY_NAME, flow.getName());
        }
        values.put(KEY_TYPE, type);
        values.put(KEY_VALUE, flow.GetValue());

        db.insert(TABLE_SENSORS, null, values);
        db.close();

    }

    // method that returns list of temperature sensors

    public List<Temperature> getAllTemperatureSensors() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(TABLE_SENSORS, null, KEY_TYPE + " = ?", new String[]{"Temperature"}, null, null, null, null);


            if (cursor != null)
                if (cursor.moveToFirst()) {
                    List<Temperature> temperature_sensor_list = new ArrayList<>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                        String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));

                        temperature_sensor_list.add(new Temperature(name));
                    } while (cursor.moveToNext());

                    return temperature_sensor_list;
                }

        } catch (Exception e) {

            Log.d(TAG, "Exception" + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }
        return
                Collections.emptyList();

    }


    // method that returns list of pressure sensors

    public List<Pressure_Sensor> getAllPressureSensors() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(TABLE_SENSORS, null, KEY_TYPE + " = ?", new String[]{"Pressure"}, null, null, null, null);


            if (cursor != null)
                if (cursor.moveToFirst()) {
                    List<Pressure_Sensor> pressure_sensor_list = new ArrayList<>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                        String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));

                        pressure_sensor_list.add(new Pressure_Sensor(name));
                    } while (cursor.moveToNext());

                    return pressure_sensor_list;
                }

        } catch (Exception e) {

            Log.d(TAG, "Exception" + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }
        return
                Collections.emptyList();

    }



    // method that returns list of flow sensors

    public List<Flow_Sensor> getAllFlowSensors() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(TABLE_SENSORS,null,KEY_TYPE + " = ?", new String[]{"Flow"}, null,null,null,null);


            if (cursor != null)
                if (cursor.moveToFirst()) {
                    List<Flow_Sensor> flow_sensor_list = new ArrayList<>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                        String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));

                        flow_sensor_list.add(new Flow_Sensor(name));
                    } while (cursor.moveToNext());

                    return flow_sensor_list;
                }

        } catch (Exception e) {

            Log.d(TAG, "Exception" + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }
        return
                Collections.emptyList();

    }

}















