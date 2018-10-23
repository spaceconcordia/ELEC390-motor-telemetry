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

    void addTemperatureSensor(Temperature temperature) {
        String type = "Temperature";

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, temperature.getName());
        values.put(KEY_TYPE, type);

        db.insert(TABLE_SENSORS, null, values);
        db.close();

    }

    void addPressureSensor(Pressure_Sensor pressure) {
        String type = "Pressure";

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, pressure.getName());
        values.put(KEY_TYPE, type);

        db.insert(TABLE_SENSORS, null, values);
        db.close();

    }

    void addFlowSensor(Flow_Sensor flow) {
        String type = "Flow";

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, flow.getName());
        values.put(KEY_TYPE, type);

        db.insert(TABLE_SENSORS, null, values);
        db.close();

    }

    //Todo method that returns list of temperature sensors
    public List<Temperature> getAllTemperatureSensors() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(TABLE_SENSORS,null,KEY_TYPE + "Temperature", null,null,null,null);


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

}


    //Todo method that returns list of pressure sensors

    //Todo method that returns list of flow sensors




